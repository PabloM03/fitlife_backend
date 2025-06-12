// src/main/java/com/fitlife/api/AsignarRutinaResponse.java
package com.fitlife.api;

import java.util.List;

public class AsignarRutinaResponse {
    public boolean success;
    public String message;
    public List<RutinaResponse> data;

    public AsignarRutinaResponse() {}
    public AsignarRutinaResponse(boolean success, String message, List<RutinaResponse> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
