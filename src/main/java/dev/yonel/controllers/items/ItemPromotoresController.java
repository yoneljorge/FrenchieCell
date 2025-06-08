package dev.yonel.controllers.items;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.services.controllers.promotores.ServicePromotoresControllerPromotor;
import dev.yonel.services.promotores.ServicePromotor;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ItemPromotoresController implements Initializable {

    @FXML
    private HBox itemPromotor;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelNumeroCelular;
    @FXML
    private Label labelVentas;
    @FXML
    private Label labelEnGarantia;
    @FXML
    private Label labelPorPagar;
    @FXML
    private Label labelTotal;
    @FXML
    private MFXButton btnVer;

    private String nombre, celular, ventas, enGarantia, porPagar, total;
    private Long idPromotor;

    public ItemPromotoresController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        this.labelNombre.setText(nombre);
        this.labelNumeroCelular.setText(celular);
        this.labelVentas.setText(ventas);
        this.labelEnGarantia.setText(enGarantia);
        this.labelPorPagar.setText(porPagar);
        this.labelTotal.setText(total);

        itemPromotor.setOnMouseClicked(event -> {
            
        });

        btnVer.setOnAction(event -> {
            ServicePromotoresControllerPromotor.getInstance().loadPromotor(idPromotor);
        });
    }

    public void setPromotor(ServicePromotor servicePromotor) {
        this.idPromotor = servicePromotor.getIdPromotor();
        this.nombre = servicePromotor.getNombre() + " " + servicePromotor.getApellidos();
        this.celular = String.valueOf(servicePromotor.getTelefono());
        this.ventas = String.valueOf(servicePromotor.getValesTotal());
        this.enGarantia = String.valueOf(servicePromotor.getValesEnGarantia());
        this.porPagar = String.valueOf(servicePromotor.getValesPorPagar());
        this.total = String.valueOf(servicePromotor.getDineroTotalPorPagar());
    }
}
