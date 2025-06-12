// src/main/java/com/fitlife/api/RutinaRequest.java
package com.fitlife.api;

public class RutinaRequest {
    private int rutinaId;

    public RutinaRequest() {}
    public RutinaRequest(int rutinaId) {
        this.rutinaId = rutinaId;
    }

    public int getRutinaId() {
        return rutinaId;
    }

    public void setRutinaId(int rutinaId) {
        this.rutinaId = rutinaId;
    }
}
