package dev.yonel.services;

import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.services.vales.ServiceVales;

public class Updates {

    public static void run() {
        ServiceVales.updateAllVales();
        ServicePromotor.updateAllPromotor();
    }
}
