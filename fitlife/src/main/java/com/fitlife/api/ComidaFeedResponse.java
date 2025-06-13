// src/main/java/com/fitlife/api/ComidaFeedResponse.java
package com.fitlife.api;

import java.util.Date;

public class ComidaFeedResponse {
    private int idPublicacion;
    private String usuarioNombre;
    private String fotoURL;
    private String nombreComida;
    private int calorias;
    private double carbohidratos;
    private double proteinas;
    private double grasas;
    private String descripcion;
    private Date fechaPublicacion;
    private boolean haDadoLike;
    private int likes;

    // Getters y setters
    public int getIdPublicacion() {
        return idPublicacion;
    }
    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getFotoURL() {
        return fotoURL;
    }
    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public String getNombreComida() {
        return nombreComida;
    }
    public void setNombreComida(String nombreComida) {
        this.nombreComida = nombreComida;
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

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }
    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public boolean isHaDadoLike() {
        return haDadoLike;
    }
    public void setHaDadoLike(boolean haDadoLike) {
        this.haDadoLike = haDadoLike;
    }

    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
}
