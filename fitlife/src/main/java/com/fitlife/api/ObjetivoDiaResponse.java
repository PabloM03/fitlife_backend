// src/main/java/com/fitlife/api/dto/ObjetivoDiaResponse.java
package com.fitlife.api;

public class ObjetivoDiaResponse {
    private int id;
    private int rutinaId;
    private String diaSemana;    
    private String descripcion;  

    public ObjetivoDiaResponse() {}

    public ObjetivoDiaResponse(int id, int rutinaId, String diaSemana, String descripcion) {
        this.id = id;
        this.rutinaId = rutinaId;
        this.diaSemana = diaSemana;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRutinaId() { return rutinaId; }
    public void setRutinaId(int rutinaId) { this.rutinaId = rutinaId; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
