package dev.yonel.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dev.yonel.services.controllers.promotores.ServicePromotoresControllerAgregar;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerPromotor;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerVista;
import dev.yonel.utils.ui.SetVisible;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class PromotoresController implements Initializable {

    private static PromotoresController instance;

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
    private VBox vboxLista_Items;
    @FXML
    private RadioButton radioButton_EnGarantia;
    @FXML
    private RadioButton radioButton_PorPagar;
    @FXML
    private RadioButton radioButton_Todos;
    @FXML
    private ScrollPane scrollPane;

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
    @FXML
    private Label lblPromotor;
    @FXML
    private Label lblDineroPagado;
    @FXML
    private ScrollPane scrollPane2;

    // General
    @Getter(AccessLevel.NONE)
    @FXML
    private StackPane stackPane;
    @Getter(AccessLevel.NONE)
    @FXML
    private MFXButton btnPromotores;
    @Getter(AccessLevel.NONE)
    @FXML
    private MFXButton btnAgregarPromotor;

    @Getter(AccessLevel.NONE)
    private ArrayList<VBox> listVBox = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    private ServicePromotoresControllerAgregar serviceAgregar;
    @Getter(AccessLevel.NONE)
    private ServicePromotoresControllerVista serviceVista;
    @Getter(AccessLevel.NONE)
    private ServicePromotoresControllerPromotor servicePromotor;

    private PromotoresController() {
        instance = this;
    }

    public static PromotoresController getInstance() {
        if (instance == null) {
            instance = new PromotoresController();
        }
        return instance;
    }

    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(() -> {
            this.serviceAgregar = ServicePromotoresControllerAgregar.getInstance();
            this.serviceVista = ServicePromotoresControllerVista.getInstance();
            this.servicePromotor = ServicePromotoresControllerPromotor.getInstance();

            // Agregamos los VBox al ArrayList
            listVBox.add(vboxAgregar);
            listVBox.add(vboxLista);
            listVBox.add(vboxPromotor);

            vboxAgregar.setVisible(false);
            vboxLista.setVisible(false);
            vboxPromotor.setVisible(false);

            /*
            Implementaciones siguientes:
            En cada panel hay un listener esperando a que se ponga visible o se oculte el panel.
            Si el panel se pone visible entonces ejecuta la acción que se encuentra dentro del if().
             */
            vboxAgregar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    serviceAgregar.configure();
                }
            });

            vboxLista.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    if (serviceVista.isNotNull()) {
                        serviceVista.getAllItems();
                    }
                }
            });

            vboxPromotor.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    servicePromotor.configure();
                }
            });

            btnPromotores.setOnAction(event -> {
                goToLista();
            });

            btnAgregarPromotor.setOnAction(event -> {
                goToAgregar();
            });

            this.scrollPane.setFitToWidth(true);
            this.scrollPane.setFitToHeight(true);

            this.scrollPane2.setFitToWidth(true);
            this.scrollPane2.setFitToHeight(true);

        });

    }

    public void goToLista() {
        serviceVista.configure();
        changeView(getInstance().vboxLista, true, false);
    }

    public void goToAgregar() {
        changeView(getInstance().vboxAgregar, false, true);
    }

    public void goToPromotor() {
        changeView(getInstance().vboxPromotor, true, true);
    }

    private void changeView(VBox vboxToShow, boolean promotoresDisable, boolean agregarPromotorDisable) {
        SetVisible.This(listVBox, vboxToShow);
        btnPromotores.setDisable(promotoresDisable);
        btnAgregarPromotor.setDisable(agregarPromotorDisable);
    }
}
