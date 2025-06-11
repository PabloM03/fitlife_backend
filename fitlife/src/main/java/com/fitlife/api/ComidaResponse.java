// src/main/java/com/fitlife/api/ComidaResponse.java
package com.fitlife.api;

// Representa una comida individual
public class ComidaResponse {
    public String nombre;
    public int calorias;
    public double carbohidratos;
    public double proteinas;
    public double grasas;
    public String observaciones;
    public String fecha;    // formato "YYYY-MM-DD"

    public ComidaResponse(String nombre,
                          int calorias,
                          double carbohidratos,
                          double proteinas,
                          double grasas,
                          String observaciones,
                          String fecha) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.carbohidratos = carbohidratos;
        this.proteinas = proteinas;
        this.grasas = grasas;
        this.observaciones = observaciones;
        this.fecha = fecha;
    }
}
