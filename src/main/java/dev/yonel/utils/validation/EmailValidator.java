package dev.yonel.utils.validation;

/**
 * Clase que implementa utilidades para el correo.
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    private static final String EMAIL_PATTERN = 
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" 
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * Método que evalua si un email está escrito de forma correcto
     * 
     * @param email - el String que se desea evaluar.
     * 
     * @return true si el email es correcto, falso en caso contrario.
     */
    public static boolean isEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
