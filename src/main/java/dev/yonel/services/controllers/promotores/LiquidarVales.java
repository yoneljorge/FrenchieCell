package dev.yonel.services.controllers.promotores;

import dev.yonel.models.Vale;

import java.util.ArrayList;
import java.util.List;

/*
 * Clase que va a implementar el patrón de diseño Singlenton para que solo halla un instancia de esta clase en cada vale, de forma tal que se pueda agregar el id de cada vale para su posterior liquidación.
 */
public class LiquidarVales {

    /*
     * Método donde se configura el patrón de diseño Singlenton.
     */
    private static LiquidarVales instance;

    private LiquidarVales() {
        instance = this;
    }

    public static LiquidarVales getInstance() {
        if (instance == null) {
            instance = new LiquidarVales();
        }

        return instance;
    }


    private List<Vale> listaVales = new ArrayList<>();

    public void setVale(Vale vale) {
        listaVales.add(vale);
    }

    public void removeVale(Vale vale) {
        listaVales.remove(vale);
    }

    public void printVales() {
        for (Vale vale : listaVales) {
            System.out.println(vale);
        }
    }

    public void liquidar() {
        for (Vale v : listaVales) {
            v.setLiquidado(true);
        }
    }
}
