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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

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
    private CheckBox checkEnGarantia;
    @FXML
    private CheckBox checkPorPagar;

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

    @Getter(AccessLevel.NONE)
    private @Setter Stage stage;

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
        instance = this; // Asigna la instancia del controlador a la referencia estática

        Platform.runLater(() -> {
            this.serviceAgregar = ServicePromotoresControllerAgregar.getInstance();
            this.serviceVista = ServicePromotoresControllerVista.getInstance();
            this.servicePromotor = ServicePromotoresControllerPromotor.getInstance();

            // cargamos la configuración de la vista agregar
            serviceAgregar.configure();
            // cargamos la configuración de la vista vista
            serviceVista.configure();
            // Agregamos Objetos al Map listPanePromotor
            servicePromotor.configure();

            // Agregamos los VBox al ArrayList
            listVBox.add(vboxAgregar);
            listVBox.add(vboxLista);
            listVBox.add(vboxPromotor);

            // Ponemos visible el VBox donde aparece la lista de promotores
            goToLista();

            btnPromotores.setOnAction(event -> {
                goToLista();
            });

            btnAgregarPromotor.setOnAction(event -> {
                goToAgregar();
            });
        });

    }

    public void goToLista() {
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
        btnAgregar.setDisable(agregarPromotorDisable);
    }
}
