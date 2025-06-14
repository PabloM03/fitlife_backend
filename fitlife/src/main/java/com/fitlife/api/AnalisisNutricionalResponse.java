// File: AnalisisNutricionalResponse.java
package com.fitlife.api;

public class AnalisisNutricionalResponse {
    public boolean exito;
    public String mensaje;       // opcional, texto de error o info
    public String etiqueta;      // la misma etiqueta recibida
    public double calorias;
    public double proteinas;
    public double grasas;
    public double carbohidratos;

    public AnalisisNutricionalResponse(
            boolean exito,
            String mensaje,
            String etiqueta,
            double calorias,
            double proteinas,
            double grasas,
            double carbohidratos
    ) {
        this.exito          = exito;
        this.mensaje        = mensaje;
        this.etiqueta       = etiqueta;
        this.calorias       = calorias;
        this.proteinas      = proteinas;
        this.grasas         = grasas;
        this.carbohidratos  = carbohidratos;
    }
}
