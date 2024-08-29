package dev.yonel.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import dev.yonel.services.controllers.promotores.ServicePromotoresControllerAgregar;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerPromotor;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerVista;
import dev.yonel.utils.ui.SetVisible;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

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
    @FXML
    private StackPane stackPane;
    @FXML
    private MFXButton btnPromotores;
    @FXML
    private MFXButton btnAgregarPromotor;

    /*
     * Creamos una instancia estática de PromotorController.
     */
    private static PromotoresController instance;

    private ArrayList<VBox> listVBox = new ArrayList<>();

    private Map<String, Object> listPaneAgregar = new HashMap<>();
    private Map<String, Object> listPaneVista = new HashMap<>();
    private Map<String, Object> listPanePromotor = new HashMap<>();

    private ServicePromotoresControllerAgregar serviceAgregar;
    private ServicePromotoresControllerVista serviceVista;
    private ServicePromotoresControllerPromotor servicePromotor;

    private @Setter Stage stage;

    @SuppressWarnings("static-access")
    public void initialize(URL location, ResourceBundle resources) {
        instance = this; // Asigna la instancia del controlador a la referencia estática

        // Agregar Objetos al Map listPaneAgregar
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
        // cargamos la configuración de la vista agregar
        serviceAgregar.configure();

        // Agregamos Objetos al Map listPaneVista
        listPaneVista.put("vbox", vboxLista_Items);
        listPaneVista.put("checkEnGarantia", checkEnGarantia);
        listPaneVista.put("checkPorPagar", checkPorPagar);
        this.serviceVista = new ServicePromotoresControllerVista(listPaneVista);
        // cargamos la configuración de la vista vista
        serviceVista.configure();

        // Agregamos Objetos al Map listPanePromotor
        listPanePromotor.put("promotorVales", flowPanePromotorVales);
        listPanePromotor.put("regresar", btnRegresar);
        listPanePromotor.put("liquidar", btnLimpiar);
        listPanePromotor.put("editar", menuItemEditarPerfil);
        listPanePromotor.put("eliminar", menuItemEliminarPerfil);
        listPanePromotor.put("dineroPorPagar", lblDineroPorPagar);
        listPanePromotor.put("valesPorPagar", lblValesPorPagar);
        listPanePromotor.put("valesGarantia", lblValesGarantia);
        listPanePromotor.put("valesTotal", lblValesTotal);
        listPanePromotor.put("promotor", lblPromotor);
        listPanePromotor.put("dineroPagado", lblDineroPagado);
        this.servicePromotor = new ServicePromotoresControllerPromotor(listPanePromotor, stage);
        servicePromotor.configure();
        //servicePromotor.setListVBox(listVBox);
        //servicePromotor.setVboxLista(vboxLista);
        //servicePromotor.setVboxPromotor(vboxPromotor);

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
    }

    private static PromotoresController getInstance() {
        return instance;
    }

    public static void goToLista() {
        getInstance().changeView(getInstance().vboxLista, true, false);
    }

    public static void goToAgregar() {
        getInstance().changeView(getInstance().vboxAgregar, false, true);
    }

    public static void goToPromotor() {
        getInstance().changeView(getInstance().vboxPromotor, true, true);
    }

    private void changeView(VBox vboxToShow, boolean promotoresDisable, boolean agregarPromotorDisable) {
        SetVisible.This(listVBox, vboxToShow);
        btnPromotores.setDisable(promotoresDisable);
        btnAgregar.setDisable(agregarPromotorDisable);
    }
}
