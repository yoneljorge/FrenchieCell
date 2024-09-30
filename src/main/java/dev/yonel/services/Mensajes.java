package dev.yonel.services;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase creada para almacenar los log y mensajes que se generan en la aplicación.
 */
public class Mensajes {

    /* Variable estática para almacenar los mensajes de forma global.*/
    private static Map<Class, String> mensajes = new HashMap<>();


    private Runnable onSetMensaje;
    private Class clazz;


    public Mensajes(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("La clase no puede ser nula");
        }
        this.clazz = clazz;
    }

    public void info(String mensaje) {
        mensajes.put(this.clazz, mensaje);
        System.out.println("INFO: " + mensaje + " :: " + clazz.getName());
    }

    public void err(String mensaje) {
        mensajes.put(this.clazz, mensaje);
        System.err.println("ERROR: " + mensaje + " :: " + clazz.getName());
    }

    public String getMensaje() {
        return mensajes.get(clazz);
    }

    /**
     * Método con el cual vamos a crear la interfaz Runnable para cuando se ejecute
     * el método setMensaje en un servicio, se actualice el mensaje de estado en la
     * clase desde donde se está ejecutando el servicio.
     *
     * @param load
     */

    //public TipoMensaje runOnSetMensaje(Runnable load) {
    //        onSetMensaje = load;
    //        return this.tipoMensaje;
    //    }

}
