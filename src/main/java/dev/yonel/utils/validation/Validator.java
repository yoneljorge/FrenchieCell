package dev.yonel.utils.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.BooleanBinding;

public class Validator {

    /********************************************************
     * IMEI *
     *********************************************************/

    /**
     * Método para verificar que la cadena corresponde a un IMEI válido.
     * 
     * @param imei el imei que se desea verificar.
     * @return true en caso de que sea válido, false caso contrario.
     */
    public static boolean isIMEI(String imei) {
        // Expresión regular para validar el IMEI
        String regex = "^\\d{15}$";
        return imei.matches(regex);
    }

    /********************************************************
     * EMAIL *
     *********************************************************/

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern patternEmail = Pattern.compile(EMAIL_PATTERN);

    /**
     * Método que evalua si un email está escrito de forma correcto
     * 
     * @param email - el String que se desea evaluar.
     * 
     * @return true si el email es correcto, falso en caso contrario.
     */
    public static boolean isEmail(String email) {
        Matcher matcher = patternEmail.matcher(email);
        return matcher.matches();
    }

    /********************************************************
     * TELÉFONO *
     *********************************************************/

    private static final String PHONE_PATTERN = "^(\\+\\d{2}[- ]?)?\\d{8}$";

    private static final Pattern patternPhone = Pattern.compile(PHONE_PATTERN);


    public static BooleanBinding isPhoneNumber(MFXTextField textField) {
        return new BooleanBinding() {
            {
                super.bind(textField.textProperty());
            }

            @Override
            protected boolean computeValue() {
                Matcher matcher = patternPhone.matcher(textField.getText());
                return matcher.matches();
            }
        };

    }
}
