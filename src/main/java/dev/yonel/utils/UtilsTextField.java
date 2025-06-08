package dev.yonel.utils;

/**
 * Clase que implementa utilidades para los textfield como:
 *  * Verificar que no se introduzca un espacio en blanco y si se introduce uno lo elimina. 
 *  * Limpiar una lista de textfield.
 *  * Verificar que un Array de TextField se encuentren los TextField vacíos.
 */

import java.util.ArrayList;
import java.util.function.UnaryOperator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import javafx.util.converter.DefaultStringConverter;

public class UtilsTextField {

    /**
     * Se define una interfaz que recibe un objeto de tipo TextFormatter.Change, la
     * cual verifica si la modificación del text incluye un espacio en blanco y
     * elimina ese espacio.
     *
     */
    private static UnaryOperator<TextFormatter.Change> filterEspaceInBlanc = change -> {
        if (change.getText().matches("[\\s]")) {
            change.setText("");
        }
        return change;
    };

    /**
     * Método que aplica el TextFormatter con el filtro a un TextField específico.
     * Con esta implementación, el filtro que elimina los espacios en blanco se
     * aplicará dinámicamente al TextField correspondiente cuando se escriba en él,
     * a trvés de la función eventKey.
     */
    public static void setFilterEspaceInBlanc(TextField textField) {
        if (textField.getText().equalsIgnoreCase(" ")) {
            textField.setText(textField.getText().trim());
        }
        textField.setTextFormatter(
                new TextFormatter<>(new DefaultStringConverter(), "", filterEspaceInBlanc));
    }



    private static UnaryOperator<TextFormatter.Change> filterNumbers = change -> {
        //Si el texto ya existe, obtenemos el texto nuevo
        String newText = change.getControlNewText();
        //Comprobamos si el nuevo texto es vacío o si es un número.
        if(newText.matches("\\d*")){
            return change;//Permitir el cambio
        }
        return null; //Rechazar el cambio.
    };

    public static void setFilterNumber(TextField textField){
        TextFormatter<String> textFormatter = new TextFormatter<>(filterNumbers);
        textField.setTextFormatter(textFormatter);
    }

    /**
     * Método que limpia cada textFiel en un ArrayList<TextField>.
     *
     * @param list de TextField
     */
    public static void cleanText(ArrayList<TextField> list) {
        for (TextField textField : list) {
            textField.setText("");
        }
    }

    /**
     * Método que verifica si todos los textField de la lista están llenos, este
     * método se puede emplear en un login o en un formulario de registro en el cual
     * debemos de verificar que todos los campos de textos estén completos.
     *
     * @param list - el ArrayList de TextField que queremos comprobar que estén
     *             llenos.
     *
     * @return true en caso de que todos los campos estén llenos, false lo
     *         contrario.
     */
    public static boolean isFullTextField(ArrayList<TextField> list) {
        for (TextField textField : list) {
            if (textField.getText().equalsIgnoreCase("")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Método que sirve para darle
     *
     * @param list
     * @param color
     */
    public static void setColorIfClean(ArrayList<TextField> list, Color color) {
        for (TextField textField : list) {
            if (textField.getText().equalsIgnoreCase("")) {
            }
        }
    }

    
}
