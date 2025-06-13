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

@WebServlet("/api/comidas")
@MultipartConfig
public class PublicarComidaApiServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "/var/www/fitlife/uploads/comidas";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        GenericResponse response;

        HttpSession session = req.getSession(false);
        Usuario usuario = session != null
            ? (Usuario) session.getAttribute("usuario")
            : null;
        if (usuario == null) {
            writeJson(resp, new GenericResponse(false, "No autorizado"));
            return;
        }
        int usuarioId = usuario.getId();

        try {
            Part imagenPart = req.getPart("imagen");
            String nombre       = req.getParameter("nombre");
            int    calorias     = Integer.parseInt(req.getParameter("calorias"));
            double carbohidratos= Double.parseDouble(req.getParameter("carbohidratos"));
            double proteinas    = Double.parseDouble(req.getParameter("proteinas"));
            double grasas       = Double.parseDouble(req.getParameter("grasas"));
            String descripcion  = req.getParameter("descripcion");

            // Guardar imagen
            String uuid      = UUID.randomUUID().toString();
            String ext       = getFileExtension(imagenPart);
            String fileName  = uuid + ext;
            File dir         = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();
            String path      = UPLOAD_DIR + File.separator + fileName;
            imagenPart.write(path);

            String fotoPath = "comidas/" + fileName;
            boolean ok = new ComidaPublicadaDAO()
                .insertarPublicacion(
                    usuarioId, nombre, descripcion,
                    calorias, carbohidratos, proteinas, grasas,
                    fotoPath
                );

            response = ok
                ? new GenericResponse(true, "Publicado correctamente")
                : new GenericResponse(false, "Error al guardar en la base de datos");
        } catch (Exception e) {
            e.printStackTrace();
            response = new GenericResponse(false, "Error interno del servidor");
        }

        writeJson(resp, response);
    }

    private void writeJson(HttpServletResponse resp, GenericResponse response)
            throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.print(new Gson().toJson(response));
        }
    }

    private String getFileExtension(Part part) {
        String name = part.getSubmittedFileName();
        int idx = (name != null ? name.lastIndexOf('.') : -1);
        return (idx > 0 ? name.substring(idx) : "");
    }
}
