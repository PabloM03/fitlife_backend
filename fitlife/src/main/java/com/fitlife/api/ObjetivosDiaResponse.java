// src/main/java/com/fitlife/api/dto/ObjetivosDiaResponse.java
package com.fitlife.api;

import java.util.List;

public class ObjetivosDiaResponse {
    public boolean success;
    public String message;
    public List<ObjetivoDiaResponse> data;

    public ObjetivosDiaResponse() {}

    public ObjetivosDiaResponse(boolean success, String message, List<ObjetivoDiaResponse> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
