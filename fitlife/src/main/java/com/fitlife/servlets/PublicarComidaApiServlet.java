// src/main/java/com/fitlife/servlets/PublicarComidaApiServlet.java
package com.fitlife.servlets;

import com.fitlife.api.GenericResponse;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ComidaPublicadaDAO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/comidas")
@MultipartConfig
public class PublicarComidaApiServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "/var/www/fitlife/uploads/comidas";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        // — Autenticación (igual que antes) —
        HttpSession session = req.getSession(false);
        Usuario usuario = session != null
            ? (Usuario) session.getAttribute("usuario")
            : null;
        if (usuario == null) {
            sendWrapper(resp, false, "No autorizado");
            return;
        }
        int usuarioId = usuario.getId();

        try {
            Part img = req.getPart("imagen");
            String nombre       = req.getParameter("nombre");
            int    calorias     = Integer.parseInt(req.getParameter("calorias"));
            double carbohidratos= Double.parseDouble(req.getParameter("carbohidratos"));
            double proteinas    = Double.parseDouble(req.getParameter("proteinas"));
            double grasas       = Double.parseDouble(req.getParameter("grasas"));
            String descripcion  = req.getParameter("descripcion");

            // — guardado de imagen y BD igual que antes —
            String uuid     = UUID.randomUUID().toString();
            String ext      = getFileExtension(img);
            String fileName = uuid + ext;
            File dir        = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();
            img.write(UPLOAD_DIR + File.separator + fileName);

            boolean ok = new ComidaPublicadaDAO().insertarPublicacion(
                usuarioId, nombre, descripcion,
                calorias, carbohidratos, proteinas, grasas,
                "comidas/" + fileName
            );

            sendWrapper(resp,
                ok,
                ok ? "Publicado correctamente" : "Error al guardar en la base de datos"
            );
        } catch (Exception e) {
            e.printStackTrace();
            sendWrapper(resp, false, "Error interno del servidor");
        }
    }

    private void sendWrapper(HttpServletResponse resp, boolean exito, String mensaje)
            throws IOException {
        Map<String,Object> wrapper = new HashMap<>();
        wrapper.put("exito", exito);
        wrapper.put("mensaje", mensaje);
        String json = new Gson().toJson(wrapper);
        try (PrintWriter out = resp.getWriter()) {
            out.print(json);
        }
    }

    private String getFileExtension(Part part) {
        String name = part.getSubmittedFileName();
        int idx = (name != null ? name.lastIndexOf('.') : -1);
        return (idx > 0 ? name.substring(idx) : "");
    }
}

