package dev.yonel.controllers.items;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.controllers.PromotoresController;
import dev.yonel.controllers.ValesController;
import dev.yonel.controllers.popup.ItemValeDetallesController;
import dev.yonel.models.Vale;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerPromotor;
import dev.yonel.utils.ui.popup.PopupUtil;
import dev.yonel.utils.ui.popup.PopupUtilImp;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

public class ItemValeController implements Initializable {

    @FXML
    private Label lblGestor;
    @FXML
    private VBox vboxVale;
    @FXML
    private Label lblCliente;
    @FXML
    private Label lblTelefonoCliente;
    @FXML
    private Label lblMarca;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblFechaVenta;
    @FXML
    private Label lblComision;
    @FXML
    private CheckBox checkBoxLiquidar;

    private String gestor, cliente, telefonoCliente, marca, modelo,
            fechaVenta, comision;

    private @Setter Stage stage;
    private Vale vale;

    public ItemValeController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        this.lblGestor.setText(gestor);
        this.lblCliente.setText(cliente);
        this.lblTelefonoCliente.setText(telefonoCliente);
        this.lblMarca.setText(marca);
        this.lblModelo.setText(modelo);
        this.lblFechaVenta.setText(fechaVenta);
        this.lblComision.setText(comision);

        /*
         * Si el vales ya fue liquidado ocultamos el checkBox
         */
        if (!vale.getLiquidado()) {
            /*
             * Si el vale está en garantia entonces deshabilitamos el checkBox.
             */
            if (hideCheckBox) {
                checkBoxLiquidar.setVisible(false);
                if (vale.getGarantia()) {
                    vboxVale.getStyleClass().add("vale-enGarantia");
                } else {
                    vboxVale.getStyleClass().add("vale-sinGarantia");
                }
            } else {
                if (vale.getGarantia()) {
                    checkBoxLiquidar.setDisable(true);
                    vboxVale.getStyleClass().add("vale-enGarantia");
                } else {
                    checkBoxLiquidar.setDisable(false);
                    vboxVale.getStyleClass().add("vale-sinGarantia");
                }
            }
        }else{
            vboxVale.getStyleClass().add("vale-liquidado");
            checkBoxLiquidar.setVisible(false);
        }

        /*
         * Al hacer clic en el vbox se muestra el popup.
         */
        vboxVale.setOnMouseClicked(event -> {
            mostrarPupup();
        });

        checkBoxLiquidar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (checkBoxLiquidar.isSelected()) {
                ServicePromotoresControllerPromotor.getInstance().addValeLiquidar(vale);
            } else {
                ServicePromotoresControllerPromotor.getInstance().removeValeLiquidar(vale);
            }
        });
    }

    /**
     * Método con el que vamos a igualar cada variable correspondiente a cada label
     * con la informacion del vale para
     * luego mostrarla en la GUI.
     *
     * @param vale Objeto que representa una instancia del vale que se quiere
     *             mostrar.
     */
    public void setVale(Vale vale) {
        this.vale = vale;
        this.gestor = vale.getPromotor().getNombre();
        this.cliente = vale.getCliente();
        this.telefonoCliente = String.valueOf(vale.getClienteTelefono());
        this.marca = vale.getMarca().getMarca();
        this.modelo = vale.getModelo().getModelo();
        this.fechaVenta = String.valueOf(vale.getFechaVenta());
        this.comision = String.valueOf(vale.getComision());
    }

    private boolean hideCheckBox = false;

    /**
     * Método con el que ocultamos el checkBox con el cual chequeamos el vale para
     * liquidarlo o pagarlo.
     */
    public void hideCheckBox() {
        hideCheckBox = true;
    }

    private void mostrarPupup() {
        PopupUtil popupUtil = new PopupUtilImp();
        ItemValeDetallesController controller = new ItemValeDetallesController(vale);
        controller.onCloseAction(popupUtil.close());
        popupUtil.setFxml("popup/itemValeDetalles");
        popupUtil.setController(controller);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                popupUtil.load(() -> {
                    ValesController.getInstance().loading(true);
                    PromotoresController.getInstance().loading(true);
                });
                return null;
            }

            @Override
            protected void succeeded() {
                popupUtil.show(() -> {
                    ValesController.getInstance().loading(false);
                    PromotoresController.getInstance().loading(false);
                });
            }
        };

        // Ejecutar la tarea en un hilo separado
        new Thread(task).start();

    }
}
