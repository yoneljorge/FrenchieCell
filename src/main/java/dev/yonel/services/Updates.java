package dev.yonel.services;

import dev.yonel.services.controllers.principal.InfoCelulares;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.services.vales.ServiceVales;

public class Updates {

    public static void run() {
        InfoCelulares.getInstance().update();

        ServiceVales.updateAllVales();
        
        ServicePromotor.updateAllPromotor();
    }
}
