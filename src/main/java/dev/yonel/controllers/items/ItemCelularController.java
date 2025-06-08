package dev.yonel.controllers.items;

import dev.yonel.controllers.CelularesController;
import dev.yonel.controllers.popup.PopupCelularController;
import dev.yonel.services.Mensajes;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerVista;
import dev.yonel.utils.AlertUtil;
import dev.yonel.utils.ui.popup.PopupUtil;
import dev.yonel.utils.ui.popup.PopupUtilImp;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemCelularController implements Initializable {

    @FXML
    private HBox itemCelular;
    @FXML
    private Label labelMarca;
    @FXML
    private Label labelModelo;
    @FXML
    private Label labelDualSim;
    @FXML
    private Label labelPrecio;
    @FXML
    private Label labelFecha;
    @FXML
    private Label labelImei;
    @FXML
    private Label labelVendido;
    @FXML
    private MFXButton btnQuitar;
    @FXML
    private MFXButton btnEditar;

    private String marca, modelo, dual, precio, fecha, imei;
    private boolean vendido;

    private ServiceCelular serviceCelular;

    private Mensajes mensajes = new Mensajes(ItemCelularController.class);

    public ItemCelularController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        this.labelMarca.setText(marca);
        this.labelModelo.setText(modelo);
        this.labelDualSim.setText(dual);
        this.labelPrecio.setText(precio);
        this.labelImei.setText(imei);
        this.labelFecha.setText(fecha);
        if (!this.vendido) {
            this.labelVendido.setText("NO");
            itemCelular.getStyleClass().add("vale-enGarantia");
        } else {
            this.labelVendido.setText("SI");
            itemCelular.getStyleClass().add("vale-sinGarantia");
        }

        btnQuitar.setOnAction(event -> {
            AlertUtil.advertencia("Desea eliminar el celular de la base de datos?", getInfoCelular(), () -> {
                if (serviceCelular.delete()) {
                    ServiceCelularControllerVista.getInstance().removeItem(
                        ServiceCelularControllerVista.getInstance().getIndexItemCelularController(this));
                    ServiceCelularControllerVista.getInstance().cargarItems();
                } else {
                    AlertUtil.error("Error en base de datos?",
                            "No se pudo eliminar el celular\nsi el error persiste contacte\nal desarrolador.");
                }
            });
        });

        btnEditar.setOnAction(event -> {
            mostrarPopup();
        });
    }

    public void setCelular(ServiceCelular serviceCelular) {
        this.serviceCelular = serviceCelular;
        this.marca = serviceCelular.getMarca();
        this.modelo = serviceCelular.getModelo();
        this.dual = serviceCelular.getDualSim();
        this.precio = "$ " + serviceCelular.getPrecio();
        this.fecha = serviceCelular.getFechaInventario();
        this.imei = serviceCelular.getImei_Uno();
        this.vendido = serviceCelular.getVendido();
    }

    private String getInfoCelular() {
        return this.marca + " " + this.modelo + "\n" + "IMEI: " + this.imei;
    }

    private void mostrarPopup() {
        PopupUtil popupUtil = new PopupUtilImp();
        PopupCelularController controller = new PopupCelularController(serviceCelular.getCelular());
        controller.onCloseAction(popupUtil.close());
        popupUtil.setController(controller);
        popupUtil.setFxml("popup/popupCelular");

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                popupUtil.load(() -> CelularesController.getInstance().loading(true));
                return null;
            }

            @Override
            protected void succeeded() {
                popupUtil.show(() -> CelularesController.getInstance().loading(false));
            }

            @Override
            protected void failed() {
                CelularesController.getInstance().loading(false);
                mensajes.err("Error cargando popup.");
            }
        };
        //Ejecutar la tarea en un hilo separado
        new Thread(task).start();
    }
}
