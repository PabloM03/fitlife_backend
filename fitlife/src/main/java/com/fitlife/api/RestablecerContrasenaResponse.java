// Pero si quieres mantenerlo separado por claridad, puedes crear:
package com.fitlife.api;

import java.util.List;

public class RestablecerContrasenaResponse {
    private boolean success;
    private String message;
    private List<?> data;

    public RestablecerContrasenaResponse() {}

    public RestablecerContrasenaResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }

    // Getters y setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
