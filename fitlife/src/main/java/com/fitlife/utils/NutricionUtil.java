package com.fitlife.utils;

import com.fitlife.classes.Usuario;
import com.fitlife.enums.NivelActividad;

public class NutricionUtil {

    // Fórmula de Mifflin-St Jeor
    public static double calcularTMB(Usuario usuario) {
        double tmb = (10 * usuario.getPeso()) +
                     (6.25 * usuario.getAltura()) -
                     (5 * usuario.getEdad());

        if (usuario.getSexo() != null && usuario.getSexo().equalsIgnoreCase("Mujer")) {
            tmb -= 161;
        } else {
            tmb += 5;
        }

        return tmb;
    }


    public static int getCaloriasRecomendadas(Usuario usuario) {
        double tmb = calcularTMB(usuario);
        double factor = 1.2; // Valor por defecto

        if (usuario.getNivelActividad() != null) {
            factor = usuario.getNivelActividad().getFactor(); // Usamos el Enum aquí
        }

        return (int) (tmb * factor);
    }
}
