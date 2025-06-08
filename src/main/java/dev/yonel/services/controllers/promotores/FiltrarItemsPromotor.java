package dev.yonel.services.controllers.promotores;

import dev.yonel.models.Promotor;
import javafx.application.Platform;
import lombok.Setter;

public class FiltrarItemsPromotor {

    private @Setter boolean porPagar = false;
    private @Setter boolean enGarantia = false;

    private ServicePromotoresControllerVista servicePromotoresVista = ServicePromotoresControllerVista.getInstance();

    public void getAllItems() {
        Platform.runLater(() -> {
            // Quitamos todos los items del VBox para que no se repitan
            servicePromotoresVista.removeAllItems();
            Promotor promotor;
            while ((promotor = Promotor.getAllOneToOne(Promotor.class)) != null){
                servicePromotoresVista.setItems(filtrar(promotor));
            }
        });
    }

    private Promotor filtrar(Promotor promotor) {

        if (enGarantia) {
            if (porPagar) {
                if (promotor.getValesEnGarantia() != null && promotor.getValesEnGarantia() > 0) {
                    if (promotor.getValesPorPagar() != null && promotor.getValesPorPagar() > 0) {
                        return promotor;
                    }
                }
            } else {
                if (promotor.getValesEnGarantia() != null && promotor.getValesEnGarantia() > 0) {
                    return promotor;
                }
            }
        } else if (porPagar) {
            if (promotor.getValesPorPagar() != null && promotor.getValesPorPagar() > 0) {
                return promotor;
            }
        } else {
            return promotor;
        }

        return null;
    }
}
