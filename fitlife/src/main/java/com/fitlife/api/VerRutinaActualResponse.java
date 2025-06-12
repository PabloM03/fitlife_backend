// src/main/java/com/fitlife/api/VerRutinaActualResponse.java
package com.fitlife.api;

import java.util.List;

public class VerRutinaActualResponse {
    public boolean success;
    public String message;
    public List<RutinaResponse> data; // normalmente contendrá un solo elemento

    public VerRutinaActualResponse() {}
    public VerRutinaActualResponse(boolean success, String message, List<RutinaResponse> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
