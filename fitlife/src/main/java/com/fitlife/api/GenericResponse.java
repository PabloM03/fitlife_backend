package com.fitlife.api;

public class GenericResponse {
    public boolean exito;
    public String mensaje;

    public GenericResponse(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }
}
