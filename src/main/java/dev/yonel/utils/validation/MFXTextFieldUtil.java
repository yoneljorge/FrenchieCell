package dev.yonel.utils.validation;

import java.util.List;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import static io.github.palexdev.mfxcore.utils.StringUtils.containsAny;

public class MFXTextFieldUtil {

    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    // Because fuck regex, stupid shit
    private static final String[] upperChar = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");
    private static final String[] lowerChar = "a b c d e f g h i j k l m n o p q r s t u v w x y z".split(" ");
    private static final String[] digits = "0 1 2 3 4 5 6 7 8 9".split(" ");
    private static final String[] specialCharacters = "! @ # & ( ) – [ { } ]: ; ' , . ? / * ~ $ ^ + = < > -".split(" ");
    private static final String[] specialCharacters_vDos = "! @ # & ( ) – [ { } ]: ; ' , ? / * ~ $ ^ + = < > -"
            .split(" ");

    /**
     * La cantidad de caracteres tiene que ser igual a 15.
     *
     * @param textField
     * @return
     */
    private static Constraint getLengthConstraint(MFXTextField textField) {
        return Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("El IMEI debe contener 15 dígitos.")
                .setCondition(textField.textProperty().length().isEqualTo(15))
                .get();
    }

    /**
     * La cantidad de caracteres tiene que ser menor que 16.
     *
     * @param textField
     * @return
     */
    private static Constraint getLengthLessThanConstraint(MFXTextField textField) {
        return Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("No pueden haber más de 15 dígitos.")
                .setCondition(textField.textProperty().length().lessThan(16))
                .get();
    }

    /**
     * No puede contener letras minúsculas ni mayúsculas.
     *
     * @param textField
     * @return
     */
    private static Constraint getCharactersConstraint(MFXTextField textField) {
        return Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("No puede contener letras")
                .setCondition(Bindings.createBooleanBinding(
                        () -> !containsAny(textField.getText(), "", upperChar)
                                && !containsAny(textField.getText(), "", lowerChar),
                        textField.textProperty()))
                .get();
    }

    /**
     * No puede contener espacios en blanco.
     *
     * @param textField
     * @return
     */
    private static Constraint getSpaceInBlancConstraint(MFXTextField textField) {
        return Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("No puede contener espacios en blanco")
                .setCondition(Bindings.createBooleanBinding(
                        () -> !containsAny(textField.getText(), "", " "),
                        textField.textProperty()))
                .get();
    }

    /**
     * No puede contener caracteres especiales.
     *
     * @param textField
     * @return
     */
    private static Constraint getSpecialCharactersConstraint(MFXTextField textField) {
        return Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("No puede contener caracteres")
                .setCondition(Bindings.createBooleanBinding(
                        () -> !containsAny(textField.getText(), "", specialCharacters),
                        textField.textProperty()))
                .get();
    }

    /**
     * No puede contener caracteres especiales excepto el punto.
     *
     * @param textField
     * @return
     */
    private static Constraint getSpecialCharactersVDosConstraint(MFXTextField textField) {
        return Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("No puede contener caracteres")
                .setCondition(Bindings.createBooleanBinding(
                        () -> !containsAny(textField.getText(), "", specialCharacters_vDos),
                        textField.textProperty()))
                .get();
    }

    /**
     * Solo puede contener números.
     *
     * @param textFiel
     * @return
     */
    @SuppressWarnings("unused")
    private static Constraint getDigitsConstraint(MFXTextField textFiel) {
        return Constraint.Builder.build()
                .setSeverity(Severity.INFO)
                .setMessage("Solo números")
                .setCondition(Bindings.createBooleanBinding(
                        () -> containsAny(textFiel.getText(), "", digits),
                        textFiel.textProperty()))
                .get();
    }

    /**
     * No puede contener números.
     *
     * @param textFiel
     * @return
     */
    private static Constraint getNotDigitsConstraint(MFXTextField textFiel) {
        return Constraint.Builder.build()
                .setSeverity(Severity.INFO)
                .setMessage("No puede contener números.")
                .setCondition(Bindings.createBooleanBinding(
                        () -> !containsAny(textFiel.getText(), "", digits),
                        textFiel.textProperty()))
                .get();
    }

    private static Constraint getTelefonoCubano(MFXTextField textField) {
        return Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Número de teléfono no válido")
                .setCondition(Validator.isPhoneNumber(textField))
                .get();
    }

    public static void validateIMEI(MFXTextField textField, Label validationLabel) {
        // Establecemos las restricciones por las cuales se va a regir el textfield.
        textField.getValidator()
                .constraint(getSpaceInBlancConstraint(textField))
                .constraint(getSpecialCharactersConstraint(textField))
                .constraint(getCharactersConstraint(textField))
                .constraint(getLengthConstraint(textField));

        textField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                validationLabel.setVisible(false);
                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });

        // Este código se ejecuta cuando el campo de texto textfield pierde el foco.
        // Valida el contenido del campo y, si hay errores, cambia su estilo para
        // indicar que es inválido y muestra un mensaje de error en un Label.
        textField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getLength() != 0) {
                if (oldValue && !newValue) {
                    List<Constraint> constraints = textField.validate();
                    if (!constraints.isEmpty()) {
                        textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        validationLabel.setText(constraints.get(0).getMessage());
                        validationLabel.setVisible(true);
                        validationLabel.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        validationLabel.setVisible(false);
                    }
                }
            } else {
                // textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                validationLabel.setVisible(false);
            }
        });

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            validationLabel.setVisible(true);
            validationLabel.setText(String.valueOf(textField.getLength()));
            validationLabel.setAlignment(Pos.CENTER);
        });
    }

    public static void validateFilterIMEI(MFXTextField textField, Label validationLabel) {
        // Establecemos las restricciones por las cuales se va a regir el textfield.
        textField.getValidator()
                .constraint(getSpaceInBlancConstraint(textField))
                .constraint(getSpecialCharactersConstraint(textField))
                .constraint(getCharactersConstraint(textField))
                .constraint(getLengthLessThanConstraint(textField));

        textField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                validationLabel.setVisible(false);
                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });

        // Este código se ejecuta cuando el campo de texto textfield pierde el foco.
        // Valida el contenido del campo y, si hay errores, cambia su estilo para
        // indicar que es inválido y muestra un mensaje de error en un Label.
        textField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getLength() != 0) {
                if (oldValue && !newValue) {
                    List<Constraint> constraints = textField.validate();
                    if (!constraints.isEmpty()) {
                        textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        validationLabel.setText(constraints.get(0).getMessage());
                        validationLabel.setVisible(true);
                        validationLabel.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        validationLabel.setVisible(false);
                    }
                }
            } else {
                // textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                validationLabel.setVisible(false);
            }
        });

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            validationLabel.setVisible(true);
            validationLabel.setText(String.valueOf(textField.getLength()));
            validationLabel.setAlignment(Pos.CENTER);
        });
    }

    public static void validatePrecio(MFXTextField textField, Label validationLabel) {
        textField.getValidator()
                .constraint(getSpaceInBlancConstraint(textField))
                .constraint(getSpecialCharactersVDosConstraint(textField))
                .constraint(getCharactersConstraint(textField));

        textField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                validationLabel.setVisible(false);
                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });

        textField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = textField.validate();
                if (!constraints.isEmpty()) {
                    textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    validationLabel.setText(constraints.get(0).getMessage());
                    validationLabel.setVisible(true);
                    validationLabel.setAlignment(Pos.CENTER_RIGHT);
                } else {
                    validationLabel.setVisible(false);
                }
            }
        });
    }

    public static void validateString(MFXTextField textField, Label validationLabel) {
        textField.getValidator()
                .constraint(getNotDigitsConstraint(textField))
                .constraint(getSpecialCharactersConstraint(textField));

        textField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                validationLabel.setVisible(false);
                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });

        textField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = textField.validate();
                if (!constraints.isEmpty()) {
                    textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    validationLabel.setText(constraints.get(0).getMessage());
                    validationLabel.setVisible(true);
                    validationLabel.setAlignment(Pos.CENTER_RIGHT);
                } else {
                    validationLabel.setVisible(false);
                }
            }
        });
    }

    public static void validatePhoneNumber(MFXTextField textField, Label validationLabel) {
        textField.getValidator()
                .constraint(getTelefonoCubano(textField));

        textField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                validationLabel.setVisible(false);
                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });

        textField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = textField.validate();
                if (!constraints.isEmpty()) {
                    textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    validationLabel.setText(constraints.get(0).getMessage());
                    validationLabel.setVisible(true);
                    validationLabel.setAlignment(Pos.CENTER_RIGHT);
                } else {
                    validationLabel.setVisible(false);
                }
            }
        });
    }
}
