// RegisterResponse.java
package com.fitlife.api;

public class RegisterResponse {
    public boolean success;
    public String message;
    public int idUsuario;

    public RegisterResponse(boolean success, String message, int idUsuario) {
        this.success   = success;
        this.message   = message;
        this.idUsuario = idUsuario;
    }
}
