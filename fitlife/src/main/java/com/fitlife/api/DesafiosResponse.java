package com.fitlife.api;

import java.util.List;

public class DesafiosResponse {
    private boolean success;
    private String message;
    private List<DesafioResponse> data; 

    public DesafiosResponse() {}

    public DesafiosResponse(boolean success, String message, List<DesafioResponse> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<DesafioResponse> getData() { return data; }
    public void setData(List<DesafioResponse> data) { this.data = data; }
}
