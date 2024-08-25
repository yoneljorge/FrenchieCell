package dev.yonel.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerAgregar;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerVista;
import dev.yonel.utils.ui.SetVisible;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CelularesController implements Initializable {
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
    private CheckBox checkVendido;
    @FXML
    private Label labelCantidad;

    // **************************************************** */
    // **************************************************** */

    // ArrayList en el que vamos a almacenar los Pane para una mejor gestión.
    private ArrayList<Pane> listPane = new ArrayList<>();

    // MapS en que vamos a agregar los controles de los panels para una mejor
    // gestión.
    private Map<String, Object> listaControlesVistaAgregar = new HashMap<>();
    private Map<String, Object> listaControlesVista = new HashMap<>();
    // Servicios de las vistas
    private ServiceCelularControllerAgregar serviceAgregar;
    private ServiceCelularControllerVista serviceVista;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Agregamos los pane a la lista y ponemos la vista como el visible.
        listPane.add(pnlAgregar);
        listPane.add(pnlVista);
        SetVisible.This(listPane, pnlVista);

        /****************************************
         * *********** PANEL AGREGAR ***********
         ****************************************/

        listaControlesVistaAgregar.put("marca", cmbMarcaAgregar);
        listaControlesVistaAgregar.put("agregarMarca", btnAgregarMarca);
        listaControlesVistaAgregar.put("modelo", cmbModeloAgregar);
        listaControlesVistaAgregar.put("agregarModelo", btnAgregarModelo);
        listaControlesVistaAgregar.put("imeiUno", txtImeiUno);
        listaControlesVistaAgregar.put("imeiDos", txtImeiDos);
        listaControlesVistaAgregar.put("precio", txtPrecio);
        listaControlesVistaAgregar.put("fecha", dateFechaInventario);
        listaControlesVistaAgregar.put("estado", lblEstado);
        listaControlesVistaAgregar.put("observaciones", txtObservaciones);
        listaControlesVistaAgregar.put("validation_ImeiUno", validationLabel_ImeiUno);
        listaControlesVistaAgregar.put("validation_ImeiDos", validationLabel_ImeiDos);
        listaControlesVistaAgregar.put("validation_Precio", validationLabel_Precio);
        listaControlesVistaAgregar.put("btnAgregarMarca", btnAgregarMarca);
        listaControlesVistaAgregar.put("btnAgregarModelo", btnAgregarModelo);
        listaControlesVistaAgregar.put("btnGuardar", btnGuardar);
        listaControlesVistaAgregar.put("btnLimpiar", btnLimpiar);
        listaControlesVistaAgregar.put("validation_Fecha", validationLabel_Fecha);
        this.serviceAgregar = new ServiceCelularControllerAgregar(listaControlesVistaAgregar);

        serviceAgregar.configure();

        /****************************************
         * *********** PANEL VISTA ***********
         ****************************************/
        listaControlesVista.put("vbox", vboxItemVista);
        listaControlesVista.put("recargar", btnRecargar);
        listaControlesVista.put("comboMarca", cmbMarcaVista);
        listaControlesVista.put("comboModelo", cmbModeloVista);
        listaControlesVista.put("comboFecha", cmbFechaVista);
        listaControlesVista.put("filtrarImei", txtFiltrarImei);
        listaControlesVista.put("validation", validationLabel_FilterImei);
        listaControlesVista.put("checkDual", checkDual);
        listaControlesVista.put("checkVendido", checkVendido);
        listaControlesVista.put("cantidad", labelCantidad);
        this.serviceVista = new ServiceCelularControllerVista(listaControlesVista);

        serviceVista.configure();

    }

    // ******************EVENTO DE LOS BOTONES********** */
    // ************************************************* */

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnVista) {
            SetVisible.This(listPane, pnlVista);
            btnAgregar.setDisable(false);
            btnVista.setDisable(true);

            // Si se agrega una marca, modelo o celular entonces se actualiza la vista
            // completa.
            if (ServiceCelularControllerVista.getNewItem()) {
                serviceVista.recargar();
                ServiceCelularControllerVista.setNewItem(false);
            }
        }

        if (actionEvent.getSource() == btnAgregar) {
            SetVisible.This(listPane, pnlAgregar);
            btnAgregar.setDisable(true);
            btnVista.setDisable(false);
        }
    }
}
