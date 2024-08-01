package dev.yonel.utils.validation;

/**
 * Clase que implementa utilidades para los números de teléfono.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator {
    private static final String PHONE_PATTERN = "^(\\+\\d{2}[- ]?)?\\d{8}$";

    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    
    /**
     * Método que verifica si un número de telefono es válido.
     * 
     * @param phoneNumber el número de telefono que se desea validar.
     * 
     * @return true en caso de que el número de teléfono sea correcto, falso en caso contrario.
     */
    public static boolean isPhoneNumber(String phoneNumber){
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
