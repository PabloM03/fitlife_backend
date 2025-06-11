package com.fitlife.api;

public class GenericResponse {
    public boolean success;
    public String message;

    public GenericResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
