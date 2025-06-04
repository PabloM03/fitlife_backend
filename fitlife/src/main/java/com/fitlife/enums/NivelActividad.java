package com.fitlife.enums;

public enum NivelActividad {
    SEDENTARIO(1.2),
    LIGERAMENTE_ACTIVO(1.375),
    MODERADAMENTE_ACTIVO(1.55),
    MUY_ACTIVO(1.725),
    EXTRA_ACTIVO(1.9);

    private final double factor;

    NivelActividad(double factor) {
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }

    @Override
    public String toString() {
        return name().replace("_", " ").toLowerCase();
    }

    public static NivelActividad fromString(String nivel) {
        if (nivel == null) return null;
        nivel = nivel.toUpperCase().replace(" ", "_");
        return NivelActividad.valueOf(nivel);
    }
    
}
