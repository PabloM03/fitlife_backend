package com.fitlife.classes;

import java.sql.Date;

public class Participacion {
    private int id;
    private int usuarioId;
    private int desafioId;
    private Date fechaJoin;
    private boolean completado;
    // getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getDesafioId() { return desafioId; }
    public void setDesafioId(int desafioId) { this.desafioId = desafioId; }

    public Date getFechaJoin() { return fechaJoin; }
    public void setFechaJoin(Date fechaJoin) { this.fechaJoin = fechaJoin; }

    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }
}
