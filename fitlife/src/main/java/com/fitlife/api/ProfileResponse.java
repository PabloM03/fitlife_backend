package com.fitlife.api;

public class ProfileResponse {
    public boolean success;
    public String message;   // en caso de error
    public String nombre;
    public String email;
    public int edad;
    public double peso;
    public double altura;
    public String nivelActividad;
    public String objetivo;

    // Constructor para éxito
    public ProfileResponse(String nombre, String email,
                           int edad, double peso, double altura,
                           String nivelActividad, String objetivo) {
        this.success = true;
        this.message = null;
        this.nombre = nombre;
        this.email = email;
        this.edad = edad;
        this.peso = peso;
        this.altura = altura;
        this.nivelActividad = nivelActividad;
        this.objetivo = objetivo;
    }

    // Constructor para fallo
    public ProfileResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
