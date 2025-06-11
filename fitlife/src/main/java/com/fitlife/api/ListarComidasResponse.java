// src/main/java/com/fitlife/api/ListarComidasResponse.java
package com.fitlife.api;

import java.util.List;

// Respuesta de listarComidas
public class ListarComidasResponse {
    public boolean success;
    public String message;
    public List<ComidaResponse> comidas;
    public int totalCalorias;
    public int caloriasRec;

    // Éxito
    public ListarComidasResponse(List<ComidaResponse> comidas,
                                 int totalCalorias,
                                 int caloriasRec) {
        this.success = true;
        this.message = null;
        this.comidas = comidas;
        this.totalCalorias = totalCalorias;
        this.caloriasRec = caloriasRec;
    }

    // Error
    public ListarComidasResponse(String message) {
        this.success = false;
        this.message = message;
    }
}
