// src/main/java/com/fitlife/api/PublicarComidaRequest.java
package com.fitlife.api;

import javax.servlet.http.Part;

public class PublicarComidaRequest {
    private Part imagen;
    private String nombre;
    private int calorias;
    private double carbohidratos;
    private double proteinas;
    private double grasas;
    private String descripcion;

    public Part getImagen() {
        return imagen;
    }
    public void setImagen(Part imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCalorias() {
        return calorias;
    }
    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public double getCarbohidratos() {
        return carbohidratos;
    }
    public void setCarbohidratos(double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public double getProteinas() {
        return proteinas;
    }
    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getGrasas() {
        return grasas;
    }
    public void setGrasas(double grasas) {
        this.grasas = grasas;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
