package com.fitlife.api;

public class EditProfileRequest {
    public Integer edad;
    public Double peso;
    public Double altura;
    public String nivelActividad;
    public String objetivo;

    // No necesitas constructor; Gson lo rellenará por reflexión.
}
