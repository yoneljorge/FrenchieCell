package dev.yonel.services;

import dev.yonel.services.controllers.promotores.ServicePromotoresControllerVista;
import dev.yonel.services.controllers.vales.ServiceValesControllerVista;

public class Gatillo {

    public static void newMarca(){
        ProxyABaseDeDatos.setCambioMarca(true);
    }

    public static void newModelo(){
        ProxyABaseDeDatos.setCambioModelo(true);
    }

    public static void newCelular(){
        ProxyABaseDeDatos.setCambioCelular(true);
        ProxyABaseDeDatos.setCambioImei(true);
    }

    public static void newImei(){
        ProxyABaseDeDatos.setCambioImei(true);
    }

    public static void newPromotor(){
        ProxyABaseDeDatos.setCambioPromotor(true);
        ServicePromotoresControllerVista.getInstance().setCambiosEnItems(true);
    }

    public static void newVale(){
        ProxyABaseDeDatos.setCambioValeByPromotor(true);
        ProxyABaseDeDatos.setCambioVale(true);
        ServiceValesControllerVista.getInstance().setCambioEnInterfaz(true);
    }
}
