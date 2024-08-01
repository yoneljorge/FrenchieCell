package dev.yonel.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Fecha {

    // Creamos los formatos de fecha
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Método para comprobar que una fecha de una cadena de texto es válida.
     * 
     * @param fechaString la cadena de texto a evaluar.
     * 
     * @return true en caso de que este correcta, false caso contrario.
     */
    public static boolean esFechaValida(String fechaString) {

        try {
            @SuppressWarnings("unused")
            LocalDate fecha = LocalDate.parse(fechaString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método que convierte una cadena de texto con el formato dd-MM-yyyy a un
     * LocalDateTime con el formato dd-MM-yyyy.
     * 
     * @param dateString cadena de texto que representa la fehca.
     * 
     * @return la fecha en formato LocalDateTime.
     */
    public static LocalDate getLocalDate(String dateString) {

        return LocalDate.parse(dateString, formatter);

    }

    /**
     * Método que convierte una fecha con el formato dd-MM-yyyy a una cadena de
     * texto con el mismo formato.
     * 
     * @param date fecha con el formato LocalDateTime.
     * 
     * @return la fecha en una cadena de texto.
     */
    public static String getStringOfLocalDate(LocalDate date) {
        return date.format(formatter);
    }
}
