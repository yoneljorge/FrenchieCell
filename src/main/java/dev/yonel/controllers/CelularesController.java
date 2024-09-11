package dev.yonel.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.ProxyABaseDeDatos;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerAgregar;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerVista;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class CelularesController implements Initializable {

    private static CelularesController instance;

    // ****************************************************************** */
    // ***************************COMPONENTES DEL FXML******************* */
    @FXML
    private MFXButton btnVista;
    @FXML
    private MFXButton btnAgregar;
    @FXML
    private Pane pnlAgregar;
    @FXML
    private Pane pnlVista;

    // Controles Vista Agregar
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnLimpiar;
    @FXML
    private MFXButton btnAgregarMarca;
    @FXML
    private MFXButton btnAgregarModelo;
    @FXML
    private MFXFilterComboBox<Marca> cmbMarcaAgregar;
    @FXML
    private MFXFilterComboBox<Modelo> cmbModeloAgregar;
    @FXML
    private MFXTextField txtImeiUno;
    @FXML
    private MFXTextField txtImeiDos;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private MFXTextField txtPrecio;
    @FXML
    private DatePicker dateFechaInventario;
    @FXML
    private Label lblEstado;
    @FXML
    private Label validationLabel_ImeiUno;
    @FXML
    private Label validationLabel_ImeiDos;
    @FXML
    private Label validationLabel_Precio;
    @FXML
    private Label validationLabel_Fecha;

    // Controles Vista Tienda
    @FXML
    private VBox vboxItemVista;
    @FXML
    private MFXButton btnRecargar;
    @FXML
    private MFXFilterComboBox<Marca> cmbMarcaVista;
    @FXML
    private MFXFilterComboBox<Modelo> cmbModeloVista;
    @FXML
    private MFXFilterComboBox<LocalDate> cmbFechaVista;
    @FXML
    private MFXTextField txtFiltrarImei;
    @FXML
    private Label validationLabel_FilterImei;
    @FXML
    private CheckBox checkDual;
    @FXML
    private Label labelCantidad;
    @FXML
    private RadioButton radioButton_VendidosNo;
    @FXML 
    private RadioButton radioButton_VendidosSi;
    @FXML
    private RadioButton radioButton_Todos;

    // **************************************************** */
    // **************************************************** */

    // ArrayList en el que vamos a almacenar los Pane para una mejor gestión.
    @Getter(AccessLevel.NONE)
    private ArrayList<Pane> listPane = new ArrayList<>();

    // Servicios de las vistas
    @Getter(AccessLevel.NONE)
    private ServiceCelularControllerAgregar serviceAgregar;
    @Getter(AccessLevel.NONE)
    private ServiceCelularControllerVista serviceVista;

    /**
     * Constructor privado para poder implementar el patrón de diseño Singlenton.
     */
    private CelularesController() {
        instance = this;

    }

    /**
     * Método mediante el cual vamos a obtener la única instancia de esta clase.
     * 
     * @return la instancia de esta clase.
     */
    public static CelularesController getInstance() {
        if (instance == null) {
            instance = new CelularesController();
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*
         * Para evitar errores a la hora de inicializar los componentes, se corre todo
         * este código cuando ya se halla inicalizado completamente la clase.
         */
        Platform.runLater(() -> {

            this.serviceAgregar = ServiceCelularControllerAgregar.getInstance();
            this.serviceVista = ServiceCelularControllerVista.getInstance();

            // Agregamos los pane a la lista y ponemos la vista como el visible.
            listPane.add(pnlAgregar);
            listPane.add(pnlVista);
            SetVisible.This(listPane, pnlVista);

            pnlAgregar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    System.out.println(">>>>>>>>>>>Panel Agregar Celular");
                    serviceAgregar.configure();
                }

            });

            serviceVista.configure();
            //Cada vez que se abra la vista si hay cambios entonces se actualiza
            pnlVista.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    if(ProxyABaseDeDatos.isCambioCelular()){
                        serviceVista.configure();
                    }
                }
            });

            

            btnVista.setOnAction(event -> {
                SetVisible.This(listPane, pnlVista);
                btnAgregar.setDisable(false);
                btnVista.setDisable(true);
            });

            btnAgregar.setOnAction(evetnt -> {
                SetVisible.This(listPane, pnlAgregar);
                btnAgregar.setDisable(true);
                btnVista.setDisable(false);
            });
        });

    }
}
