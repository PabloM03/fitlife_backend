package com.fitlife.api;

/**
 * Enviado por la app Android con el token que recibió por correo
 * y la nueva contraseña elegida por el usuario.
 */
public class RestablecerContrasenaRequest {

    private String token;
    private String password;

    public RestablecerContrasenaRequest() {}

    public RestablecerContrasenaRequest(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
