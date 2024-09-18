package dev.yonel.services.controllers.itemValeDetalles;

import dev.yonel.controllers.popup.ItemValeDetallesController;
import dev.yonel.models.Celular;
import dev.yonel.models.Promotor;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ServiceItemValeDetallesEdicion {

    // Panel Edición
    private MFXButton btnCancelar;
    private MFXButton btnGuardar;
    private MFXFilterComboBox<Promotor> filterComboBoxPromotor;
    private MFXTextField txtCliente;
    private MFXTextField txtTelefonoCliente;
    private MFXFilterComboBox<Celular> filterComboBoxImei;
    private MFXTextField txtMarca;
    private MFXTextField txtModelo;
    private MFXTextField txtPrecio;
    private DatePicker datePickerFechaVenta;
    private MFXTextField txtComision;
    private CheckBox checkBoxServicioMensajeria;
    private TextArea txtDireccion;
    private MFXTextField txtCostoMensajeria;
    private Label labelEstado;

    private ItemValeDetallesController instance;

    public ServiceItemValeDetallesEdicion(ItemValeDetallesController instance) {
        this.instance = instance;
    }

    private void setObjects() {
        this.btnCancelar = instance.getBtnCancelar();
        this.btnGuardar = instance.getBtnGuardar();
        this.filterComboBoxPromotor = instance.getFilterComboBoxPromotor();
        this.txtCliente = instance.getTxtCliente();
        this.txtTelefonoCliente = instance.getTxtTelefonoCliente();
        this.filterComboBoxImei = instance.getFilterComboBoxImei();
        this.txtMarca = instance.getTxtMarca();
        this.txtModelo = instance.getTxtModelo();
        this.txtPrecio = instance.getTxtPrecio();
        this.datePickerFechaVenta = instance.getDatePickerFechaVenta();
        this.txtComision = instance.getTxtComision();
        this.txtDireccion = instance.getTxtDireccion();
        this.txtCostoMensajeria = instance.getTxtCostoMensajeria();
        this.labelEstado = instance.getLabelEstado();
    }

    public void configure() {

    }

    
}
