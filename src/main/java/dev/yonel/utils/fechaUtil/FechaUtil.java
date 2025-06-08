package dev.yonel.utils.fechaUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class FechaUtil {

    // Creamos los formatos de fecha
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Método para comprobar que una fecha de una cadena de texto es válida.
     *
     * @param fechaString la cadena de texto a evaluar.
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
     * @return la fecha en formato LocalDateTime.
     */
    public static LocalDate getLocalDate(String dateString) {

        if (esFechaValida(dateString)) {
            return LocalDate.parse(dateString, formatter);
        }
        return null;
    }

    /**
     * Método que convierte una fecha con el formato dd-MM-yyyy a una cadena de
     * texto con el mismo formato.
     *
     * @param date fecha con el formato LocalDateTime.
     * @return la fecha en una cadena de texto.
     */
    public static String getStringOfLocalDate(LocalDate date) {
        return date.format(formatter);
    }

    /**
     * Método que obtiene la cantidad de días entre dos fechas una inicial y otra
     * final.
     *
     * @param fechaInicio fecha desde la que se quiere empezar a contar.
     * @param fechaFin    fecha hasta la que se quiere contar.
     * @return un Long que representa la cantidad de días que hay entre las dos
     *         fechas.
     */
    public static Integer getDiasEntre(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("El argumento no puede ser null");
        }
        String dias = String.valueOf(ChronoUnit.DAYS.between(fechaInicio, fechaFin));
        return Integer.parseInt(dias);
    }

    /**
     * <p>
     * Méotodo con el que a partir de un {@code LocalDate} obtenemos el día y el més
     * en el siguiente formato {@code 1 de ENE}.
     * </p>
     * <p>
     * Se construye un {@code StreamBuilder} con el dato del día y del mes a partir
     * del ENUM {@link Meses}.
     * </p>
     * 
     * @param fecha
     * @return
     */
    public static String getMesDia(LocalDate fecha) {
        StringBuilder diaMes = new StringBuilder();

        diaMes.append(fecha.getDayOfMonth());
        diaMes.append(" de ");
        // Meses meses = Meses.fromNumber(fecha.getMonthValue());
        diaMes.append(Meses.fromNumber(fecha.getMonthValue()).getShortName());

        return diaMes.toString();
    }
}
