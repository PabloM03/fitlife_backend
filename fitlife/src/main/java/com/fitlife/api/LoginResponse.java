// LoginResponse.java
package com.fitlife.api;
public class LoginResponse {
    public boolean success;
    public String nombre;
    public int idUsuario;

    public LoginResponse(boolean success, String nombre, int idUsuario) {
        this.success = success;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
    }
}
