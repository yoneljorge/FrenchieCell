package dev.yonel.services.controllers.itemValeDetalles;

import dev.yonel.controllers.popup.ItemValeDetallesController;
import dev.yonel.models.Celular;
import dev.yonel.services.controllers.vales.ServiceValesControllerVista;
import dev.yonel.services.vales.ServiceVales;
import dev.yonel.utils.AlertUtil;
import io.github.palexdev.materialfx.controls.MFXButton;

public class ServiceItemValeDetallesVista {

    private MFXButton btnEliminar;

    private ItemValeDetallesController controller;

    public ServiceItemValeDetallesVista(ItemValeDetallesController controller) {
        this.controller = controller;
        btnEliminar = controller.getBtnEliminar();

        btnEliminar.setOnAction(event -> {
            AlertUtil.advertencia(controller.getRoot().getScene().getWindow(),
                    "¿Desea eliminar el vale?",
                    "Al eliminar el vale, el celular vendido\n" +
                            "pasará a estar disponible.",
                    () -> eliminar());
        });
    }

    private void eliminar() {
        ServiceVales serviceVales = new ServiceVales(controller.getVale());
        Celular celular = new Celular();

        while ((celular = Celular.getAllOneToOne(Celular.class)) != null) {
            if (celular.getImeiUno().equals(controller.getVale().getImei().getImeiUno())) {
                break;
            }
        }
        if (serviceVales.eliminar(celular)) {
            AlertUtil.information(controller.getRoot().getScene().getWindow(), "Vale eliminado", null);
            controller.getOnCloseAction().run();
            ServiceValesControllerVista.getInstance().getAllItems();
        } else {
            AlertUtil.error(controller.getRoot().getScene().getWindow(), "Error eliminando vale", "Si el error persiste contacte al desarrollador.");
        }
    }


}
