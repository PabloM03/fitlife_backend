// File: src/main/java/com/fitlife/servlets/AnalyzeEtiquetaApiServlet.java
package com.fitlife.servlets;

import com.fitlife.api.AnalizarEtiquetaRequest;
import com.fitlife.api.AnalisisNutricionalResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet("/api/analyze")
public class AnalyzeEtiquetaApiServlet extends HttpServlet {
  private static final Gson gson = new Gson();
  private String usdaKey;

  @Override
  public void init() throws ServletException {
    try {
      // Leemos tu USDA API Key de /etc/usda.key
      usdaKey = new String(
        Files.readAllBytes(Paths.get("/etc/usda.key"))
      ).trim();
    } catch (IOException e) {
      throw new ServletException("No pude leer USDA key", e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json;charset=UTF-8");

    // 1) Parsear el body JSON a tu DTO AnalizarEtiquetaRequest
    AnalizarEtiquetaRequest request = gson.fromJson(
      new InputStreamReader(req.getInputStream()),
      AnalizarEtiquetaRequest.class
    );
    if (request.etiqueta == null || request.etiqueta.isBlank()) {
      resp.getWriter().print(gson.toJson(
        new AnalisisNutricionalResponse(
          false,
          "Debes enviar el campo 'etiqueta'",
          null, 0,0,0,0
        )
      ));
      return;
    }

    // 2) Llamar a USDA
    Nutrientes n = fetchNutrientsFromUSDA(request.etiqueta);
    if (n == null) {
      resp.getWriter().print(gson.toJson(
        new AnalisisNutricionalResponse(
          false,
          "No encontré datos nutricionales para '" + request.etiqueta + "'",
          request.etiqueta, 0,0,0,0
        )
      ));
      return;
    }

    // 3) Devolver respuesta con macros
    resp.getWriter().print(gson.toJson(
      new AnalisisNutricionalResponse(
        true,
        null,
        request.etiqueta,
        n.calorias, n.proteinas, n.grasas, n.carbohidratos
      )
    ));
  }

  /**
   * Consulta la API de USDA FoodData Central y retorna
   * un objeto Nutrientes con calorías, proteínas, grasas y carbos.
   * Devuelve null si no hay resultados.
   */
  private Nutrientes fetchNutrientsFromUSDA(String query) throws IOException {
    String url = "https://api.nal.usda.gov/fdc/v1/foods/search"
      + "?api_key=" + URLEncoder.encode(usdaKey, "UTF-8")
      + "&query="   + URLEncoder.encode(query,  "UTF-8")
      + "&pageSize=1";

    HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
    conn.setRequestMethod("GET");

    try (InputStream is = conn.getInputStream();
         InputStreamReader isr = new InputStreamReader(is)) {
      JsonObject root = JsonParser.parseReader(isr).getAsJsonObject();
      if (!root.has("foods") || root.getAsJsonArray("foods").size() == 0) {
        return null;
      }
      // Tomamos el primer alimento de la lista
      JsonObject food = root
        .getAsJsonArray("foods")
        .get(0).getAsJsonObject()
        .getAsJsonArray("foodNutrients")
        .get(0).getAsJsonObject()
        .getAsJsonObject(); // <-- pequeña corrección: foodNutrients es un array de objetos

      // Iteramos para extraer cada nutriente
      double cal=0, prot=0, fat=0, carb=0;
      for (var el : root.getAsJsonArray("foods")
                        .get(0).getAsJsonObject()
                        .getAsJsonArray("foodNutrients")) {
        JsonObject nut = el.getAsJsonObject();
        String name = nut.get("nutrientName").getAsString();
        double val  = nut.get("value").getAsDouble();
        switch (name) {
          case "Energy":                     cal  = val; break;
          case "Protein":                    prot = val; break;
          case "Total lipid (fat)":          fat  = val; break;
          case "Carbohydrate, by difference":carb = val; break;
        }
      }
      return new Nutrientes(cal, prot, fat, carb);
    }
  }

  /**
   * Clase interna para agrupar macros
   */
  private static class Nutrientes {
    double calorias, proteinas, grasas, carbohidratos;
    Nutrientes(double c, double p, double f, double cb) {
      this.calorias      = c;
      this.proteinas     = p;
      this.grasas        = f;
      this.carbohidratos = cb;
    }
  }
}
