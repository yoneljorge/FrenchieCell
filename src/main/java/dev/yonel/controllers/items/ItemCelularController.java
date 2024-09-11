package dev.yonel.controllers.items;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerVista;
import dev.yonel.utils.AlertUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

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

    private String marca, modelo, dual, precio, fecha, imei;
    private boolean vendido;

    private ServiceCelular serviceCelular;
    private ServiceCelularControllerVista serviceVista = ServiceCelularControllerVista.getInstance();

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
        } else {
            this.labelVendido.setText("SI");
        }

        btnQuitar.setOnAction(event -> {
            AlertUtil.advertencia("Desea eliminar el celular de la base de datos?", getInfoCelular(), () -> {
                if (serviceCelular.delete()) {
                    serviceVista.removeItem(serviceVista.getIndexItemCelularController(this));
                    serviceVista.cargarItems();
                } else {
                    AlertUtil.error("Error en base de datos?",
                            "No se pudo eliminar el celular\nsi el error persiste contacte\nal desarrolador.");
                }
            });
        });

        itemCelular.setOnMouseClicked(event -> {
            System.out.println("me esta haciendo clic");
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
}
