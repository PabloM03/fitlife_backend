// RecuperarContrasenaRequest.java
package com.fitlife.api;

/**
 * Request que enviará el cliente Android (Retrofit) al endpoint /api/recuperar
 * con el e-mail del usuario que quiere restablecer su contraseña.
 */
public class RecuperarContrasenaRequest {

    private String email;   // correo del usuario

    // Constructor vacío (obligatorio para Gson)
    public RecuperarContrasenaRequest() {}

    public RecuperarContrasenaRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
