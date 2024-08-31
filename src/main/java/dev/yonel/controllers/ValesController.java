package dev.yonel.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dev.yonel.models.Celular;
import dev.yonel.models.Promotor;
import dev.yonel.services.controllers.vales.ServiceValesControllerAgregar;
import dev.yonel.utils.ui.SetVisible;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ValesController implements Initializable {

    @Getter(AccessLevel.NONE)
    private static ValesController instance;

    // General
    @FXML
    private MFXButton btnLista;
    @FXML
    private MFXButton btnAgregar;
    @FXML
    private VBox vboxPanelLista;
    @FXML
    private VBox vboxPanelAgregar;

    // Panel Agregar
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnLimpiar;
    @FXML
    private MFXFilterComboBox<Promotor> filterComboBoxPromotor;
    @FXML
    private MFXTextField txtCliente;
    @FXML
    private Label labelValidacionCliente;
    @FXML
    private MFXTextField txtTelefonoCliente;
    @FXML
    private Label labelValidacionTelefonoCliente;
    @FXML
    private MFXFilterComboBox<Celular> filterComboBoxImei;
    @FXML
    private MFXTextField txtMarca;
    @FXML
    private MFXTextField txtModelo;
    @FXML
    private MFXTextField txtPrecio;
    @FXML
    private Label labelValidacionPrecio;
    @FXML
    private DatePicker datePickerFechaVenta;
    @FXML
    private MFXTextField txtComision;
    @FXML
    private Label labelValidacionComision;
    @FXML
    private CheckBox checkBoxServicioMensajeria;
    @FXML
    private TextArea txtDireccion;
    @FXML
    private MFXTextField txtCostoMensajeria;
    @FXML
    private Label labelValidacionCostoMensajeria;
    @FXML
    private Label labelEstado;

    /*
     * Cargamos cada servicio de cada vista.
     */
    @Getter(AccessLevel.NONE)
    private ServiceValesControllerAgregar serviceAgregar;

    /*
     * Lista para agregar los VBox de las vistas para una mejor gestión.
     */
    @Getter(AccessLevel.NONE)
    private List<VBox> listVBoxs;

    @Getter(AccessLevel.NONE)
    private @Setter Stage stage;

    private ValesController() {
        instance = this;
    }

    public static ValesController getInstance() {
        if (instance == null) {
            instance = new ValesController();
        }
        return instance;
    }

    public void initialize(URL location, ResourceBundle resource) {
        /*
         * Agregamos los VBox de cada vista a la lista
         * VBox de la lista de promotores lo ponemos visible y el de agregar no.
         * Boton lista lo deshabilitamos.
         */
        
        Platform.runLater(() -> {
            this.listVBoxs = new ArrayList<>();
            this.serviceAgregar = ServiceValesControllerAgregar.getInstance();
            listVBoxs.add(vboxPanelAgregar);
            listVBoxs.add(vboxPanelLista);
            SetVisible.This(listVBoxs, vboxPanelLista);
            btnLista.setDisable(true);

            this.serviceAgregar.configure();

            btnAgregar.setOnAction(event -> {
                SetVisible.This(listVBoxs, vboxPanelAgregar);
                btnAgregar.setDisable(true);
                btnLista.setDisable(false);
            });
            btnLista.setOnAction(event -> {
                SetVisible.This(listVBoxs, vboxPanelLista);
                btnAgregar.setDisable(false);
                btnLista.setDisable(true);
            });
        });
    }
}
