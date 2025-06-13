// src/main/java/com/fitlife/api/MisDesafiosListResponse.java
package com.fitlife.api;

import java.util.List;

public class MisDesafiosListResponse {
    private boolean success;
    private String message;
    private List<MisDesafioResponse> data;

    public MisDesafiosListResponse() {}

    public MisDesafiosListResponse(boolean success, String message, List<MisDesafioResponse> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<MisDesafioResponse> getData() { return data; }
    public void setData(List<MisDesafioResponse> data) { this.data = data; }
}
