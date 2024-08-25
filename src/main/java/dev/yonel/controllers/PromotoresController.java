package dev.yonel.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import dev.yonel.services.controllers.promotores.ServicePromotoresControllerAgregar;
import dev.yonel.utils.ui.SetVisible;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PromotoresController implements Initializable {

    // Panel Agregar Promotor
    @FXML
    private VBox vboxAgregar;

    @FXML
    private MFXTextField txtNombre;
    @FXML
    private MFXTextField txtApellidos;
    @FXML
    private MFXTextField txtCelular;
    @FXML
    private MFXButton btnLimpiar;
    @FXML
    private MFXButton btnAgregar;
    @FXML
    private Label validacionNombre;
    @FXML
    private Label validacionApellidos;
    @FXML
    private Label validacionCelular;
    @FXML
    private Label labelEstado_AgregarPromotor;

    // Panel View Promotores
    @FXML
    private VBox vboxLista;

    @FXML
    private VBox vboxPromotor_ViewItems;

    // Panel Promotor
    @FXML
    private VBox vboxPromotor;

    @FXML
    private FlowPane flowPanePromotorVales;
    @FXML
    private MFXButton btnRegresar;
    @FXML
    private MFXButton btnLiquidar;
    @FXML
    private MenuItem menuItemEditarPerfil;
    @FXML
    private MenuItem menuItemEliminarPerfil;
    @FXML
    private Label lblDineroPorPagar;
    @FXML
    private Label lblValesPorPagar;
    @FXML
    private Label lblValesGarantia;
    @FXML
    private Label lblValesTotal;
    

    // General
    @FXML
    private StackPane stackPane;
    @FXML
    private MFXButton btnPromotores;
    @FXML
    private MFXButton btnAgregarPromotor;

    private ArrayList<VBox> listVBox = new ArrayList<>();
    private Map<String, Object> listPaneAgregar = new HashMap<>();

    private ServicePromotoresControllerAgregar serviceAgregar;

    public void initialize(URL location, ResourceBundle resources) {
        //Agregar Objetos al Map listPaneAgregar
        listPaneAgregar.put("nombre", txtNombre);
        listPaneAgregar.put("apellidos", txtApellidos);
        listPaneAgregar.put("celular", txtCelular);
        listPaneAgregar.put("limpiar", btnLimpiar);
        listPaneAgregar.put("agregar", btnAgregar);
        listPaneAgregar.put("validacionNombre", validacionNombre);
        listPaneAgregar.put("validacionApellidos", validacionApellidos);
        listPaneAgregar.put("validacionCelular", validacionCelular);
        listPaneAgregar.put("estado", labelEstado_AgregarPromotor);
        this.serviceAgregar = new ServicePromotoresControllerAgregar(listPaneAgregar);
        //cargamos la configuración de la vista agregar
        serviceAgregar.configure();
        // Agregamos los VBox al ArrayList
        listVBox.add(vboxAgregar);
        listVBox.add(vboxLista);
        listVBox.add(vboxPromotor);

        // Ponemos visible el VBox donde aparece la lista de promotores
        SetVisible.This(listVBox, vboxLista);
        btnPromotores.setDisable(true);
        btnAgregarPromotor.setDisable(false);

        btnPromotores.setOnAction(event -> {
            SetVisible.This(listVBox, vboxLista);
            btnPromotores.setDisable(true);
            btnAgregarPromotor.setDisable(false);
        });

        btnAgregarPromotor.setOnAction(event -> {
            SetVisible.This(listVBox, vboxAgregar);
            btnPromotores.setDisable(false);
            btnAgregarPromotor.setDisable(true);
        });
    }

}
