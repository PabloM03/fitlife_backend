package com.fitlife.servlets;

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
import java.util.Map;

@WebServlet("/api/analyze")
public class AnalyzeEtiquetaApiServlet extends HttpServlet {
  private static final Gson gson = new Gson();
  private String usdaKey;

  @Override
  public void init() throws ServletException {
    try {
      usdaKey = new String(
        java.nio.file.Files.readAllBytes(
          java.nio.file.Paths.get("/etc/usda.key")
        )
      ).trim();
    } catch (IOException e) {
      throw new ServletException("No pude leer USDA key", e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json;charset=UTF-8");

    // 1) Leer JSON { "etiqueta":"pizza" }
    JsonObject body = JsonParser
      .parseReader(new InputStreamReader(req.getInputStream()))
      .getAsJsonObject();
    if (!body.has("etiqueta")) {
      write(resp, Map.of("exito", false, "mensaje", "Falta campo 'etiqueta'"));
      return;
    }
    String etiqueta = body.get("etiqueta").getAsString();

    // 2) Llamar a USDA
    Nutrientes n = fetchNutrientsFromUSDA(etiqueta);
    if (n == null) {
      write(resp, Map.of("exito", false, "mensaje", "No hallé datos nutricionales"));
      return;
    }

    // 3) Responder JSON
    write(resp, Map.of(
      "exito", true,
      "etiqueta", etiqueta,
      "calorias", n.calorias,
      "proteinas", n.proteinas,
      "grasas", n.grasas,
      "carbohidratos", n.carbohidratos
    ));
  }

  private Nutrientes fetchNutrientsFromUSDA(String q) throws IOException {
    String url = "https://api.nal.usda.gov/fdc/v1/foods/search"
      + "?api_key=" + URLEncoder.encode(usdaKey,"UTF-8")
      + "&query="   + URLEncoder.encode(q,     "UTF-8")
      + "&pageSize=1";
    HttpURLConnection c = (HttpURLConnection)new URL(url).openConnection();
    try (InputStream in=c.getInputStream();
         Reader  r=new InputStreamReader(in)) {
      var root = JsonParser.parseReader(r).getAsJsonObject();
      var arr  = root.getAsJsonArray("foods");
      if (arr.size()==0) return null;
      var food = arr.get(0).getAsJsonObject()
                    .getAsJsonArray("foodNutrients");
      double cal=0, prot=0, fat=0, carb=0;
      for (var el: food) {
        var nut = el.getAsJsonObject();
        String name = nut.get("nutrientName").getAsString();
        double val  = nut.get("value").getAsDouble();
        switch(name) {
          case "Energy":                     cal  = val; break;
          case "Protein":                    prot = val; break;
          case "Total lipid (fat)":          fat  = val; break;
          case "Carbohydrate, by difference":carb = val; break;
        }
      }
      return new Nutrientes(cal,prot,fat,carb);
    }
  }

  private void write(HttpServletResponse r, Object o) throws IOException {
    r.getWriter().print(gson.toJson(o));
  }

  private static class Nutrientes {
    double calorias, proteinas, grasas, carbohidratos;
    Nutrientes(double c,double p,double f,double cb){
      this.calorias=c; this.proteinas=p; this.grasas=f; this.carbohidratos=cb;
    }
  }
}
