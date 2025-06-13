package com.fitlife.api;

import java.sql.Date;

public class MisDesafioResponse {
    private int desafioId;
    private String titulo;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean completado;

    public int getDesafioId() { return desafioId; }
    public void setDesafioId(int desafioId) { this.desafioId = desafioId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }
}
