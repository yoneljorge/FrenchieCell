package dev.yonel.services;

import dev.yonel.services.controllers.principal.InfoCelulares;

public class Gatillo {

    public static void newMarca() {
        ProxyABaseDeDatos.setCambioMarca(true);
    }

    public static void newModelo() {
        ProxyABaseDeDatos.setCambioModelo(true);
    }

    public static void newCelular() {
        ProxyABaseDeDatos.setCambioCelular(true);
        ProxyABaseDeDatos.setCambioImei(true);
        load();
    }

    public static void newImei() {
        ProxyABaseDeDatos.setCambioImei(true);
    }

    public static void newPromotor() {
        ProxyABaseDeDatos.setCambioPromotor(true);
    }

    public static void newVale() {
        ProxyABaseDeDatos.setCambioValeByPromotor(true);
        ProxyABaseDeDatos.setCambioVale(true);
        load();
    }

    private static void load(){
        Thread load = new Thread(() -> {
            InfoCelulares.getInstance().update();
        });

        load.start();

        try {
            load.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
