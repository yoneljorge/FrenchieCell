package dev.yonel.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dev.yonel.models.Celular;
import dev.yonel.models.Promotor;
import dev.yonel.services.controllers.vales.ServiceValesControllerAgregar;
import dev.yonel.services.controllers.vales.ServiceValesControllerVista;
import dev.yonel.utils.ui.SetVisible;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ValesController implements Initializable {

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

    // Panel Vista
    @Setter
    @FXML
    private FlowPane flowPane_ListaDeItems;
    @FXML
    private ScrollPane scrollPane;
    @Getter(AccessLevel.NONE)
    @FXML
    private HBox hBoxLoadingInVista;
    @FXML
    private DatePicker datePickerFechaDesde;
    @FXML
    private DatePicker datePickerFechaHasta;
    @FXML
    private MFXButton btnAplicarFiltroFecha;
    @FXML
    private Label labelEstado_Promotor;
    @FXML
    private MFXButton btnReset;

    @Getter(AccessLevel.NONE)
    private static ValesController instance;

    /*
     * Lista para agregar los VBox de las vistas para una mejor gestión.
     */
    @Getter(AccessLevel.NONE)
    private List<VBox> listVBoxs;

    private ValesController() {
        instance = this;
    }

    public static ValesController getInstance() {
        if (instance == null) {
            instance = new ValesController();
        }
        return instance;
    }

    public static void restartInstance() {
        instance = null;
    }

    public void initialize(URL location, ResourceBundle resource) {
        /*
         * Agregamos los VBox de cada vista a la lista
         * VBox de la lista de promotores lo ponemos visible y el de agregar no.
         * Boton lista lo deshabilitamos.
         */

        Platform.runLater(() -> {
            this.listVBoxs = new ArrayList<>();

            listVBoxs.add(vboxPanelAgregar);
            listVBoxs.add(vboxPanelLista);

            vboxPanelAgregar.setVisible(false);
            vboxPanelLista.setVisible(false);
            btnLista.setDisable(true);

            vboxPanelAgregar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    ServiceValesControllerAgregar.getInstance().configure();
                }
            });

            //Cuando inicia la app esta carga la lista y configura
            ServiceValesControllerVista.getInstance().configure();
            ServiceValesControllerVista.getInstance().getAllItems();
            //Cuando se muestra la interfáz tambien carga la lista.
            vboxPanelLista.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    //ServiceValesControllerVista.getInstance().configure();
                    ServiceValesControllerVista.getInstance().getAllItems();
                }
            });

            btnAgregar.setOnAction(event -> {
                goToAgregar();
            });

            btnLista.setOnAction(event -> {
                goToLista();
            });

            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
        });
    }

    public void goToAgregar() {
        SetVisible.This(listVBoxs, vboxPanelAgregar);
        btnAgregar.setDisable(true);
        btnLista.setDisable(false);
    }

    public void goToLista() {
        SetVisible.This(listVBoxs, vboxPanelLista);
        btnAgregar.setDisable(false);
        btnLista.setDisable(true);
    }

    public void loading(boolean valor) {
        Thread thread = new Thread(() -> {
            hBoxLoadingInVista.setVisible(valor);
        });
        thread.start();
    }
}
