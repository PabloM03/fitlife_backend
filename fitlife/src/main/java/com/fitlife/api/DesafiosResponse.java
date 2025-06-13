package com.fitlife.api;

import com.fitlife.classes.Desafio;
import java.util.List;

public class DesafiosResponse {
    private boolean success;
    private String message;
    private List<Desafio> data;

    public DesafiosResponse() {}

    public DesafiosResponse(boolean success, String message, List<Desafio> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Desafio> getData() { return data; }
    public void setData(List<Desafio> data) { this.data = data; }
}
