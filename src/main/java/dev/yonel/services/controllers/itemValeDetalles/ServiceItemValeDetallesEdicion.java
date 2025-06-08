package dev.yonel.services.controllers.itemValeDetalles;

import dev.yonel.controllers.popup.ItemValeDetallesController;
import dev.yonel.models.Celular;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerPromotor;
import dev.yonel.services.controllers.vales.ServiceValesControllerVista;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.services.vales.ServiceVales;
import dev.yonel.utils.AlertUtil;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase en la que se va a ejecutar la lógica de edición del controlador
 * ItemValeDetalles, por lo que depende del
 * controlador de ItemValeDetallesController. La clase desde la cual se va a
 * realizar la llamda es de la clase
 * ItemValeController y se tiene que pasar la instancia que se esta corriendo en
 * ese momento.
 */
public class ServiceItemValeDetallesEdicion {

    // Panel Edición
    private MFXButton btnGuardar;
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

    private ItemValeDetallesController controller;

    private ServicePromotor servicePromotor;
    private ServiceCelular serviceCelular;
    private ServiceVales serviceVales;

    private Vale newVale;
    private Celular oldCelular;
    private Celular newCelular;
    private Promotor oldPromotor;
    private Promotor newPromotor;

    private List<MFXTextField> listMFXTextField;

    public ServiceItemValeDetallesEdicion(ItemValeDetallesController controller) {
        this.controller = controller;

        // Inicializar los controles utilizando los getters del controlador
        this.btnGuardar = controller.getBtnGuardar();
        this.filterComboBoxPromotor = controller.getFilterComboBoxPromotor();
        this.txtCliente = controller.getTxtCliente();
        this.labelValidacionCliente = controller.getLabelValidacionCliente();
        this.txtTelefonoCliente = controller.getTxtTelefonoCliente();
        this.labelValidacionTelefonoCliente = controller.getLabelValidacionTelefonoCliente();
        this.filterComboBoxImei = controller.getFilterComboBoxImei();
        this.txtMarca = controller.getTxtMarca();
        this.txtModelo = controller.getTxtModelo();
        this.txtPrecio = controller.getTxtPrecio();
        this.labelValidacionPrecio = controller.getLabelValidacionPrecio();
        this.datePickerFechaVenta = controller.getDatePickerFechaVenta();
        this.txtComision = controller.getTxtComision();
        this.labelValidacionComision = controller.getLabelValidacionComision();
        this.checkBoxServicioMensajeria = controller.getCheckBoxServicioMensajeria();
        this.txtDireccion = controller.getTxtDireccion();
        this.txtCostoMensajeria = controller.getTxtCostoMensajeria();
        this.labelValidacionCostoMensajeria = controller.getLabelValidacionCostoMensajeria();
        this.labelEstado = controller.getLblEstado();

        servicePromotor = new ServicePromotor();
        serviceCelular = new ServiceCelular();

        newVale = Vale.getById(Vale.class, controller.getVale().getIdVales());

        configure();
    }

    public void configure() {
        servicePromotor.configureFilterComboBox(filterComboBoxPromotor, controller.getVale().getPromotor());
        serviceCelular.configureFilterComboBoxImeiForEditarVales(filterComboBoxImei, controller.getVale().getImei());

        MFXTextFieldUtil.validateString(txtCliente, labelValidacionCliente);
        txtCliente.setText(controller.getVale().getCliente());

        MFXTextFieldUtil.validatePhoneNumber(txtTelefonoCliente, labelValidacionTelefonoCliente);
        txtTelefonoCliente.setText(String.valueOf(controller.getVale().getClienteTelefono()));

        txtMarca.setText(controller.getVale().getMarca().getMarca());
        txtModelo.setText(controller.getVale().getModelo().getModelo());

        MFXTextFieldUtil.validatePrecio(txtPrecio, labelValidacionPrecio);
        txtPrecio.setText(String.valueOf(controller.getVale().getPrecio()));

        datePickerFechaVenta.setEditable(false);
        datePickerFechaVenta.setValue(controller.getVale().getFechaVenta());

        MFXTextFieldUtil.validatePrecio(txtComision, labelValidacionComision);
        txtComision.setText(String.valueOf(controller.getVale().getComision()));

        MFXTextFieldUtil.validatePrecio(txtCostoMensajeria, labelValidacionCostoMensajeria);

        checkBoxServicioMensajeria.setSelected(controller.getVale().getMensajeria());

        // Si tiene mensajeria se habilita y se le pasan los datos.
        if (controller.getVale().getMensajeria()) {
            txtCostoMensajeria.setText(String.valueOf(controller.getVale().getCostoMensajeria()));
            txtDireccion.setText(controller.getVale().getDireccion());
        } else {
            txtCostoMensajeria.setDisable(true);
            txtDireccion.setDisable(true);
        }

        this.listMFXTextField = new ArrayList<>();

        listMFXTextField.addAll(Arrays.asList(
                txtCliente,
                txtTelefonoCliente,
                txtMarca,
                txtModelo,
                txtPrecio,
                txtComision));

        filterComboBoxPromotor.selectedItemProperty().addListener(new ChangeListener<Promotor>() {
            @Override
            public void changed(ObservableValue<? extends Promotor> observable, Promotor oldValue, Promotor newValue) {
                if (newValue != null) {
                    enableActualizar();
                }
            }
        });
        txtCliente.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    enableActualizar();
                }
            }
        });
        txtTelefonoCliente.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    enableActualizar();
                }
            }
        });
        filterComboBoxImei.selectedItemProperty().addListener(new ChangeListener<Celular>() {
            @Override
            public void changed(ObservableValue<? extends Celular> observable, Celular oldValue, Celular newValue) {
                if (newValue != null) {
                    enableActualizar();
                    getDatosCelular(filterComboBoxImei.getValue());
                }
            }
        });
        txtPrecio.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    enableActualizar();
                }
            }
        });
        datePickerFechaVenta.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
                    LocalDate newValue) {
                if (newValue != null) {
                    enableActualizar();
                }
            }
        });
        txtComision.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    enableActualizar();
                }
            }
        });
        checkBoxServicioMensajeria.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue != null) {
                    enableActualizar();
                    if (checkBoxServicioMensajeria.isSelected()) {
                        txtDireccion.setDisable(false);
                        txtCostoMensajeria.setDisable(false);
                    } else {
                        txtDireccion.setDisable(true);
                        txtCostoMensajeria.setDisable(true);
                    }
                }
            }
        });
        txtDireccion.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    enableActualizar();
                }
            }
        });
        txtCostoMensajeria.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    enableActualizar();
                }
            }
        });

        btnGuardar.setOnAction(event -> {
            update();
        });

        // Obtenemos el celular y el promotor actual para comparar a la hora de
        // actualizar.
        oldCelular = filterComboBoxImei.getValue();
        oldPromotor = filterComboBoxPromotor.getValue();
    }

    private void update() {
        if (isValid()) {
            updateVale();

            newCelular = filterComboBoxImei.getValue();
            newPromotor = filterComboBoxPromotor.getValue();
            if (serviceVales.update(controller.getVale(), oldCelular, newCelular, oldPromotor, newPromotor)) {
                AlertUtil.information(controller.getRoot().getScene().getWindow(), "Vale actualizado", null);
                controller.getOnCloseAction().run();
                //Actualizamos la vista de los vales
                ServiceValesControllerVista.getInstance().getAllItems();

                //Actualizamos el viejo promotor
                servicePromotor.setPromotor(oldPromotor);
                    servicePromotor.updatePromotor();
                //Actualizamos el nuevo promotor
                servicePromotor.setPromotor(newPromotor);
                servicePromotor.updatePromotor();

                //Actualizamos la vista del promotor
                ServicePromotoresControllerPromotor.getInstance().loadPromotor(oldPromotor.getIdPromotor());

            } else {
                AlertUtil.error(controller.getRoot().getScene().getWindow(), "Error actualizando vale.",
                        "Si el error persiste contacte al desarrollador.");
            }

        }
    }

    /**
     * Método para insertar los datos en los campos al seleccionar un celular en la
     * lista de imeis.
     *
     * @param celular el celular que se desea agregar los datos.
     */
    public void getDatosCelular(Celular celular) {
        txtMarca.setText(celular.getMarca().getMarca());
        txtModelo.setText(celular.getModelo().getModelo());
        txtPrecio.setText(String.valueOf(celular.getPrecio()));
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
        newVale.setPromotor(filterComboBoxPromotor.getValue());
        newVale.setCliente(txtCliente.getText());
        newVale.setClienteTelefono(Long.parseLong(txtTelefonoCliente.getText()));
        newVale.setImei(filterComboBoxImei.getValue());
        newVale.setMarca(filterComboBoxImei.getValue().getMarca());
        newVale.setModelo(filterComboBoxImei.getValue().getModelo());
        newVale.setPrecio(Double.parseDouble(txtPrecio.getText()));
        newVale.setFechaVenta(datePickerFechaVenta.getValue());
        newVale.setComision(Long.parseLong(txtComision.getText()));

        if (checkBoxServicioMensajeria.isSelected()) {
            if (!txtCostoMensajeria.getText().equals("")) {
                if (txtCostoMensajeria.isValid()) {
                    newVale.setCostoMensajeria(Long.parseLong(txtCostoMensajeria.getText()));
                }
            }
            newVale.setDireccion(txtDireccion.getText());
        } else {
            newVale.setCostoMensajeria(0L);
            newVale.setDireccion("");
        }

        serviceVales = new ServiceVales(newVale);
    }


    /**
     * Método estático con el que vamos a pasar mensajes de error al Label de
     * estado.
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

    private void enableActualizar() {
        if (!filterComboBoxPromotor.getValue().getNombre().equals(controller.getVale().getPromotor().getNombre())) {
            btnGuardar.setDisable(false);
        } else if (!txtCliente.getText().equals(controller.getVale().getCliente())) {
            btnGuardar.setDisable(false);
        } else if (!txtTelefonoCliente.getText().equals(String.valueOf(controller.getVale().getClienteTelefono()))) {
            btnGuardar.setDisable(false);
        } else if (!filterComboBoxImei.getValue().getImeiUno().equals(controller.getVale().getImei().getImeiUno())) {
            btnGuardar.setDisable(false);
        } else if (!txtPrecio.getText().equals(String.valueOf(controller.getVale().getPrecio()))) {
            btnGuardar.setDisable(false);
        } else if (!datePickerFechaVenta.getValue().equals(controller.getVale().getFechaVenta())) {
            btnGuardar.setDisable(false);
        } else if (!txtComision.getText().trim().equals(String.valueOf(controller.getVale().getComision()))) {
            btnGuardar.setDisable(false);
        } else if (checkBoxServicioMensajeria.isSelected() != controller.getVale().getMensajeria()) {
            btnGuardar.setDisable(false);
        } else if (!txtDireccion.getText().equals(controller.getVale().getDireccion())) {
            btnGuardar.setDisable(false);
        } else if (controller.getVale().getCostoMensajeria() == 0 && !txtCostoMensajeria.getText().equals("")) {
            btnGuardar.setDisable(false);
        } else if (controller.getVale().getCostoMensajeria() != 0
                && !txtCostoMensajeria.getText().equals(String.valueOf(controller.getVale().getCostoMensajeria()))) {
            btnGuardar.setDisable(false);
        } else {
            btnGuardar.setDisable(true);
        }
    }

}
