package dev.yonel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import dev.yonel.models.Celular;
import dev.yonel.models.Promotor;
import dev.yonel.services.controllers.vales.ServiceValesControllerAgregar;
import dev.yonel.utils.ui.SetVisible;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;

public class ValesController implements Initializable{

    //General
    @FXML
    private MFXButton btnLista;
    @FXML
    private MFXButton btnAgregar;
    @FXML
    private VBox vboxPanelLista;
    @FXML
    private VBox vboxPanelAgregar;

    //Panel Agregar
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
     * Lista con la que vamos a pasar los objetos al servicio agregar.
     */
    private Map<String, Object> listAgregar = new HashMap<>();

    /*
     * Cargamos cada servicio de cada vista.
     */
    private ServiceValesControllerAgregar serviceAgregar;

    /*
     * Lista para agregar los VBox de las vistas para una mejor gestión.
     */
    private List<VBox> listVBoxs = new ArrayList<>();

    private @Setter Stage stage;

    public void initialize(URL location, ResourceBundle resource){
        /*
         * Agregamos los VBox de cada vista a la lista
         * VBox de la lista de promotores lo ponemos visible y el de agregar no.
         * Boton lista lo deshabilitamos.
         */
        listVBoxs.add(vboxPanelAgregar);
        listVBoxs.add(vboxPanelLista);
        SetVisible.This(listVBoxs, vboxPanelLista);
        btnLista.setDisable(true);

        /*
         * Agregamos todos los objetos del panel agregar a la lista agregar
         */
        listAgregar.put("guardar", btnGuardar);
        listAgregar.put("limpiar", btnLimpiar);
        listAgregar.put("comboPromotor", filterComboBoxPromotor);
        listAgregar.put("cliente", txtCliente);
        listAgregar.put("validarCliente", labelValidacionCliente);
        listAgregar.put("telefonoCliente", txtTelefonoCliente);
        listAgregar.put("validacionTelefonoCliente", labelValidacionTelefonoCliente);
        listAgregar.put("comboImei", filterComboBoxImei);
        listAgregar.put("marca", txtMarca);
        listAgregar.put("modelo", txtModelo);
        listAgregar.put("precio", txtPrecio);
        listAgregar.put("validacionPrecio", labelValidacionPrecio);
        listAgregar.put("fechaVenta", datePickerFechaVenta);
        listAgregar.put("comision", txtComision);
        listAgregar.put("validacionComision", labelValidacionComision);
        listAgregar.put("checkMensajeria", checkBoxServicioMensajeria);
        listAgregar.put("direccion", txtDireccion);
        listAgregar.put("costoMensajeria", txtCostoMensajeria);
        listAgregar.put("validacionCostoMensajeria", labelValidacionCostoMensajeria);
        listAgregar.put("labelEstado", labelEstado);
        this.serviceAgregar = new ServiceValesControllerAgregar(listAgregar);
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
    }
}
