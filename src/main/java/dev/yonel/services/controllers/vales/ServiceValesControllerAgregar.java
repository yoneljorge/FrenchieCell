package dev.yonel.services.controllers.vales;

import dev.yonel.controllers.ValesController;
import dev.yonel.models.Celular;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.services.vales.ServiceVales;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceValesControllerAgregar {

    private static ServiceValesControllerAgregar instance;

    private VBox panel;
    private MFXButton btnGuardar;
    private MFXButton btnLimpiar;
    private MFXFilterComboBox<Promotor> filterComboBoxPromotor;
    private MFXTextField txtCliente;
    private Label labelValidacionCliente;
    private MFXTextField txtTelefonoCliente;
    private Label labelValidacionTelefonoCliente;
    private MFXFilterComboBox<Celular> filterComboBoxImei;
    private MFXTextField txtMarca;
    private MFXTextField txtModelo;
    private MFXTextField txtPrecio;
    private Label labelValidacionPrecio;
    private DatePicker datePickerFechaVenta;
    private MFXTextField txtComision;
    private Label labelValidacionComision;
    private CheckBox checkBoxServicioMensajeria;
    private TextArea txtDireccion;
    private MFXTextField txtCostoMensajeria;
    private Label labelValidacionCostoMensajeria;
    private Label labelEstado;

    private ServicePromotor servicePromotor;
    private ServiceCelular serviceCelular;
    private List<MFXTextField> listMFXTextField;
    private ServiceVales serviceVales;

    private ValesController valesController = ValesController.getInstance();

    private ServiceValesControllerAgregar() {
        instance = this;

        Platform.runLater(() -> {
            this.servicePromotor = new ServicePromotor();
            this.serviceCelular = new ServiceCelular();
            this.listMFXTextField = new ArrayList<>();

            setObjects();
        });
    }

    public static ServiceValesControllerAgregar getInstance() {
        if (instance == null) {
            instance = new ServiceValesControllerAgregar();
        }
        return instance;
    }

    private void setObjects() {
        this.panel = valesController.getVboxPanelAgregar();
        this.btnGuardar = valesController.getBtnGuardar();
        this.btnLimpiar = valesController.getBtnLimpiar();
        this.filterComboBoxPromotor = valesController.getFilterComboBoxPromotor();
        this.txtCliente = valesController.getTxtCliente();
        this.labelValidacionCliente = valesController.getLabelValidacionCliente();
        this.txtTelefonoCliente = valesController.getTxtTelefonoCliente();
        this.labelValidacionTelefonoCliente = valesController.getLabelValidacionTelefonoCliente();
        this.filterComboBoxImei = valesController.getFilterComboBoxImei();
        this.txtMarca = valesController.getTxtMarca();
        this.txtModelo = valesController.getTxtModelo();
        this.txtPrecio = valesController.getTxtPrecio();
        this.labelValidacionPrecio = valesController.getLabelValidacionPrecio();
        this.datePickerFechaVenta = valesController.getDatePickerFechaVenta();
        this.txtComision = valesController.getTxtComision();
        this.labelValidacionComision = valesController.getLabelValidacionComision();
        this.checkBoxServicioMensajeria = valesController.getCheckBoxServicioMensajeria();
        this.txtDireccion = valesController.getTxtDireccion();
        this.txtCostoMensajeria = valesController.getTxtCostoMensajeria();
        this.labelValidacionCostoMensajeria = valesController.getLabelValidacionCostoMensajeria();
        this.labelEstado = valesController.getLabelEstado();
    }

    public void configure() {
        Platform.runLater(() -> {

            servicePromotor.configureFilterComboBox(filterComboBoxPromotor);
            serviceCelular.configureFilterComboBoxImeiForAgregarVales(filterComboBoxImei);
            MFXTextFieldUtil.validateString(txtCliente, labelValidacionCliente);
            txtCliente.setText("");
            MFXTextFieldUtil.validatePhoneNumber(txtTelefonoCliente, labelValidacionTelefonoCliente);
            txtTelefonoCliente.setText("");
            filterComboBoxImei.selectedItemProperty().addListener(new ChangeListener<Celular>() {
                @Override
                public void changed(ObservableValue<? extends Celular> observable, Celular oldValue,
                                    Celular newValue) {
                    if (newValue != null) {
                        getDatosCelular(filterComboBoxImei.getValue());
                    }
                }
            });
            txtMarca.setText("");
            txtModelo.setText("");
            MFXTextFieldUtil.validatePrecio(txtPrecio, labelValidacionPrecio);
            txtPrecio.setText("");

            datePickerFechaVenta.setEditable(false);
            datePickerFechaVenta.setValue(null);
            MFXTextFieldUtil.validatePrecio(txtComision, labelValidacionComision);
            txtComision.setText("");
            txtDireccion.setText("");
            txtCostoMensajeria.setText("");
            checkBoxServicioMensajeria.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (checkBoxServicioMensajeria.isSelected()) {
                    txtDireccion.setDisable(false);
                    txtCostoMensajeria.setDisable(false);
                } else {
                    txtDireccion.setDisable(true);
                    txtCostoMensajeria.setDisable(true);
                }
            });
            MFXTextFieldUtil.validatePrecio(txtCostoMensajeria, labelValidacionCostoMensajeria);

            listMFXTextField.addAll(Arrays.asList(
                    txtCliente,
                    txtTelefonoCliente,
                    txtMarca,
                    txtModelo,
                    txtPrecio,
                    txtComision));

            btnGuardar.setOnAction(event -> {
                guardar();
            });
        });
    }

    public void getDatosCelular(Celular celular) {
        txtMarca.setText(celular.getMarca().getMarca());
        txtModelo.setText(celular.getModelo().getModelo());
        txtPrecio.setText(String.valueOf(celular.getPrecio()));
    }

    public void guardar() {
        if (isValid()) {

            updateVale();

            if (serviceVales.save(filterComboBoxImei.getValue(), filterComboBoxPromotor.getValue())) {
                // TODO: Método Save Vale
                setEstadoInformativo("Vale guardado.");

                // Limpiamos los campos
                filterComboBoxPromotor.getSelectionModel().clearSelection();
                txtCliente.setText("");
                txtTelefonoCliente.setText("");
                filterComboBoxImei.getSelectionModel().clearSelection();
                txtMarca.setText("");
                txtModelo.setText("");
                txtPrecio.setText("");
                datePickerFechaVenta.setValue(null);
                txtComision.setText("");
                checkBoxServicioMensajeria.selectedProperty().set(false);

                configure();

            }

        }
    }

    public boolean isValid() {
        int i = 0;
        for (MFXTextField mfxTextField : listMFXTextField) {
            if (mfxTextField.getText() != null) {
                if (!mfxTextField.getText().equals("")) {
                    if (mfxTextField.isValid()) {
                        i++;
                    } else {
                        setEstadoError("Verifique todos los campos.");
                    }
                } else {
                    setEstadoError("Campos vacíos.");
                }
            } else {
                setEstadoError("Campos vacíos.");
            }
        }

        if (filterComboBoxPromotor.getValue() != null) {
            i++;
        } else {
            setEstadoError("Seleccione un gestor.");
        }

        if (filterComboBoxImei.getValue() != null) {
            i++;
        } else {
            setEstadoError("Seleccione un IMEI");
        }

        if (datePickerFechaVenta.getValue() != null) {
            i++;
        } else {
            setEstadoError("Fecha de venta está vacio.");
        }

        if (checkBoxServicioMensajeria.isSelected()) {
            if (!txtDireccion.getText().equals("")) {
                i++;
            } else {
                setEstadoError("La Dirección está vacía.");
            }

            if (!txtCostoMensajeria.getText().equals("")) {
                if (txtCostoMensajeria.isValid()) {
                    i++;
                } else {
                    setEstadoError("Verifique los campos.");
                }
            } else {
                setEstadoError("Campos vacíos.");
            }

            return i == listMFXTextField.size() + 5;
        }
        return i == listMFXTextField.size() + 3;
    }

    public void updateVale() {
        Vale vale = new Vale()
                .setPromotor(filterComboBoxPromotor.getValue())
                .setCliente(txtCliente.getText())
                .setClienteTelefono(Long.parseLong(txtTelefonoCliente.getText()))
                .setImei(filterComboBoxImei.getValue().getImeiUno())
                .setMarca(filterComboBoxImei.getValue().getMarca())
                .setModelo(filterComboBoxImei.getValue().getModelo())
                .setPrecio(Double.parseDouble(txtPrecio.getText()))
                .setFechaVenta(datePickerFechaVenta.getValue())
                .setComision(Long.parseLong(txtComision.getText()))
                .setDireccion(txtDireccion.getText());

        if (!txtCostoMensajeria.getText().equals("")) {
            if (txtCostoMensajeria.isValid()) {
                vale.setCostoMensajeria(Long.parseLong(txtCostoMensajeria.getText()));
            }
        }
        this.serviceVales = new ServiceVales(vale);
    }

    /**
     * Método estático con el cual vamos a pasar mensajes de información al Label de estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    private void setEstadoInformativo(String estado) {
        labelEstado.setText(estado);
        labelEstado.getStyleClass().add("label");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            labelEstado.setText("");
        });
        pause.play();
    }

    /**
     * Método estático con el que vamos a pasar mensajes de error al Label de estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    private void setEstadoError(String estado) {
        labelEstado.setText(estado);
        labelEstado.getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            labelEstado.setText(null);
        });
        pause.play();
    }
}
