// RecuperarContrasenaResponse.java
package com.fitlife.api;

import java.util.List;

/**
 * Respuesta genérica que devolverá el backend en JSON.
 * Aquí `data` no contiene nada relevante, así que la dejamos en null.
 */
public class RecuperarContrasenaResponse {

    private boolean success;      // true si el correo se envió correctamente
    private String  message;      // mensaje para mostrar en la app
    private List<?> data;         // siempre null en este caso

    public RecuperarContrasenaResponse() {}

    public RecuperarContrasenaResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data    = null;
    }

    /* Getters y setters */
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
