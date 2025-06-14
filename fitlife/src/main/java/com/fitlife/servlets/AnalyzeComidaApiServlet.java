package com.fitlife.servlets;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/analyze")
@MultipartConfig
public class AnalyzeComidaApiServlet extends HttpServlet {
  private static final Gson gson = new Gson();
  private String usdaKey;

  @Override
  public void init() throws ServletException {
    // Carga la key USDA al iniciar el servlet
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

    Part part = req.getPart("imagen");
    if (part == null) {
      writeJson(resp, Map.of("exito", false, "mensaje", "Falta parte 'imagen'"));
      return;
    }

    byte[] imgBytes = part.getInputStream().readAllBytes();
    List<EntityAnnotation> labels = detectLabels(imgBytes);
    if (labels.isEmpty()) {
      writeJson(resp, Map.of("exito", false, "mensaje", "No se detectó comida"));
      return;
    }
    String foodLabel = pickBestFoodLabel(labels);

    Nutrients nutri = fetchNutrientsFromUSDA(foodLabel);
    if (nutri == null) {
      writeJson(resp, Map.of("exito", false, "mensaje", "No hallé datos nutricionales"));
      return;
    }

    Map<String,Object> out = Map.of(
      "exito", true,
      "etiqueta", foodLabel,
      "calorias",      nutri.calorias,
      "proteinas",     nutri.proteinas,
      "grasas",        nutri.grasas,
      "carbohidratos", nutri.carbohidratos
    );
    writeJson(resp, out);
  }

  private List<EntityAnnotation> detectLabels(byte[] imgBytes) throws IOException {
    try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
      ByteString bs = ByteString.copyFrom(imgBytes);
      Image img = Image.newBuilder().setContent(bs).build();
      Feature feat = Feature.newBuilder()
          .setType(Feature.Type.LABEL_DETECTION)
          .setMaxResults(10)
          .build();
      AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
          .addFeatures(feat)
          .setImage(img)
          .build();
      List<AnnotateImageResponse> responses =
          client.batchAnnotateImages(Collections.singletonList(request))
                .getResponsesList();
      return responses.get(0).getLabelAnnotationsList();
    }
  }

  private String pickBestFoodLabel(List<EntityAnnotation> labels) {
    // Filtrar etiquetas con score alto y texto alfanumérico
    return labels.stream()
        .filter(l -> l.getScore() > 0.7)
        .map(EntityAnnotation::getDescription)
        .findFirst()
        .orElse(labels.get(0).getDescription());
  }

  private Nutrients fetchNutrientsFromUSDA(String query) throws IOException {
    String url = "https://api.nal.usda.gov/fdc/v1/foods/search"
        + "?api_key=" + URLEncoder.encode(usdaKey, "UTF-8")
        + "&query="   + URLEncoder.encode(query,  "UTF-8")
        + "&pageSize=1";
    HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
    c.setRequestMethod("GET");

    try (InputStream is = c.getInputStream();
         Reader r = new InputStreamReader(is)) {
      JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
      if (!root.has("foods") || root.getAsJsonArray("foods").size() == 0) {
        return null;
      }
      JsonObject food = root.getAsJsonArray("foods")
                            .get(0).getAsJsonObject();
      var arr = food.getAsJsonArray("foodNutrients");
      double cal=0, prot=0, fat=0, carb=0;
      for (var el : arr) {
        var nut = el.getAsJsonObject();
        String name = nut.get("nutrientName").getAsString();
        double val  = nut.get("value").getAsDouble();
        switch (name) {
          case "Energy":                   cal  = val; break;
          case "Protein":                  prot = val; break;
          case "Total lipid (fat)":        fat  = val; break;
          case "Carbohydrate, by difference": carb = val; break;
        }
      }
      return new Nutrients(cal, prot, fat, carb);
    }
  }

  private void writeJson(HttpServletResponse resp, Object o) throws IOException {
    resp.getWriter().print(gson.toJson(o));
  }

  private static class Nutrients {
    double calorias, proteinas, grasas, carbohidratos;
    Nutrients(double c,double p,double f,double cb){
      this.calorias=c; this.proteinas=p; this.grasas=f; this.carbohidratos=cb;
    }
  }
}
