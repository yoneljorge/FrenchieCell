package dev.yonel.services.controllers.vales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.yonel.controllers.ValesController;
import dev.yonel.models.Celular;
import dev.yonel.models.Vale;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.services.vales.ServiceVales;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ServiceValesControllerAgregar {

    private static ServiceValesControllerAgregar instance;

    private ServicePromotor servicePromotor;
    private ServiceCelular serviceCelular;
    private List<MFXTextField> listMFXTextField;
    private ServiceVales serviceVales;

    private ServiceValesControllerAgregar() {
        instance = this;

        Platform.runLater(() -> {
            this.servicePromotor = new ServicePromotor();
            this.serviceCelular = new ServiceCelular();
            this.listMFXTextField = new ArrayList<>();
        });
    }

    public static ServiceValesControllerAgregar getInstance() {
        if (instance == null) {
            instance = new ServiceValesControllerAgregar();
        }
        return instance;
    }


    public void configure() {
        Platform.runLater(() -> {

            servicePromotor.configureFilterComboBox(
                    ValesController.getInstance().getFilterComboBoxPromotor());
            serviceCelular.configureFilterComboBoxImeiForAgregarVales(
                    ValesController.getInstance().getFilterComboBoxImei());
            MFXTextFieldUtil.validateString(
                    ValesController.getInstance().getTxtCliente(),
                    ValesController.getInstance().getLabelValidacionCliente());
            ValesController.getInstance().getTxtCliente().setText("");
            MFXTextFieldUtil.validatePhoneNumber(
                    ValesController.getInstance().getTxtTelefonoCliente(),
                    ValesController.getInstance().getLabelValidacionTelefonoCliente());
            ValesController.getInstance().getTxtTelefonoCliente().setText("");
            ValesController.getInstance().getFilterComboBoxImei().selectedItemProperty()
                    .addListener(new ChangeListener<Celular>() {
                        @Override
                        public void changed(ObservableValue<? extends Celular> observable, Celular oldValue,
                                Celular newValue) {
                            if (newValue != null) {
                                getDatosCelular(ValesController.getInstance().getFilterComboBoxImei().getValue());
                            }
                        }
                    });
            ValesController.getInstance().getTxtMarca().setText("");
            ValesController.getInstance().getTxtModelo().setText("");
            MFXTextFieldUtil.validatePrecio(ValesController.getInstance().getTxtPrecio(),
                    ValesController.getInstance().getLabelValidacionPrecio());
            ValesController.getInstance().getTxtPrecio().setText("");

            ValesController.getInstance().getDatePickerFechaVenta().setEditable(false);
            ValesController.getInstance().getDatePickerFechaVenta().setValue(null);
            MFXTextFieldUtil.validatePrecio(
                    ValesController.getInstance().getTxtComision(),
                    ValesController.getInstance().getLabelValidacionComision());
            ValesController.getInstance().getTxtComision().setText("");
            ValesController.getInstance().getTxtDireccion().setText("");
            ValesController.getInstance().getTxtCostoMensajeria().setText("");
            ValesController.getInstance().getCheckBoxServicioMensajeria().selectedProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        if (ValesController.getInstance().getCheckBoxServicioMensajeria().isSelected()) {
                            ValesController.getInstance().getTxtDireccion().setDisable(false);
                            ValesController.getInstance().getTxtCostoMensajeria().setDisable(false);
                        } else {
                            ValesController.getInstance().getTxtDireccion().setDisable(true);
                            ValesController.getInstance().getTxtCostoMensajeria().setDisable(true);
                        }
                    });
            MFXTextFieldUtil.validatePrecio(
                    ValesController.getInstance().getTxtCostoMensajeria(),
                    ValesController.getInstance().getLabelValidacionCostoMensajeria());

            listMFXTextField.addAll(Arrays.asList(
                    ValesController.getInstance().getTxtCliente(),
                    ValesController.getInstance().getTxtTelefonoCliente(),
                    ValesController.getInstance().getTxtMarca(),
                    ValesController.getInstance().getTxtModelo(),
                    ValesController.getInstance().getTxtPrecio(),
                    ValesController.getInstance().getTxtComision()));

            ValesController.getInstance().getBtnGuardar().setOnAction(event -> {
                guardar();
            });

            ValesController.getInstance().getBtnLimpiar().setOnAction(event -> {
                limpiarCampos();
            });
        });
    }

    public void getDatosCelular(Celular celular) {
        ValesController.getInstance().getTxtMarca().setText(celular.getMarca().getMarca());
        ValesController.getInstance().getTxtModelo().setText(celular.getModelo().getModelo());
        ValesController.getInstance().getTxtPrecio().setText(String.valueOf(celular.getPrecio()));
    }

    public void guardar() {
        if (isValid()) {

            updateVale();

            if (serviceVales.save(
                    ValesController.getInstance().getFilterComboBoxImei().getValue(),
                    ValesController.getInstance().getFilterComboBoxPromotor().getValue())) {

                setEstadoInformativo("Vale guardado.");

                // Limpiamos los campos
                ValesController.getInstance().getFilterComboBoxPromotor().getSelectionModel().clearSelection();
                ValesController.getInstance().getTxtCliente().setText("");
                ValesController.getInstance().getTxtTelefonoCliente().setText("");
                ValesController.getInstance().getFilterComboBoxImei().getSelectionModel().clearSelection();
                ValesController.getInstance().getTxtMarca().setText("");
                ValesController.getInstance().getTxtModelo().setText("");
                ValesController.getInstance().getTxtPrecio().setText("");
                ValesController.getInstance().getDatePickerFechaVenta().setValue(null);
                ValesController.getInstance().getTxtComision().setText("");
                ValesController.getInstance().getCheckBoxServicioMensajeria().selectedProperty().set(false);
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

        if (ValesController.getInstance().getFilterComboBoxPromotor().getValue() != null) {
            i++;
        } else {
            setEstadoError("Seleccione un gestor.");
        }

        if (ValesController.getInstance().getFilterComboBoxImei().getValue() != null) {
            i++;
        } else {
            setEstadoError("Seleccione un IMEI");
        }

        if (ValesController.getInstance().getDatePickerFechaVenta().getValue() != null) {
            i++;
        } else {
            setEstadoError("Fecha de venta está vacio.");
        }

        if (ValesController.getInstance().getCheckBoxServicioMensajeria().isSelected()) {
            if (!ValesController.getInstance().getTxtDireccion().getText().equals("")) {
                i++;
            } else {
                setEstadoError("La Dirección está vacía.");
            }

            if (!ValesController.getInstance().getTxtCostoMensajeria().getText().equals("")) {
                if (ValesController.getInstance().getTxtCostoMensajeria().isValid()) {
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
                .setPromotor(ValesController.getInstance().getFilterComboBoxPromotor().getValue())
                .setCliente(ValesController.getInstance().getTxtCliente().getText())
                .setClienteTelefono(Long.parseLong(ValesController.getInstance().getTxtTelefonoCliente().getText()))
                .setImei(ValesController.getInstance().getFilterComboBoxImei().getValue())
                .setMarca(ValesController.getInstance().getFilterComboBoxImei().getValue().getMarca())
                .setModelo(ValesController.getInstance().getFilterComboBoxImei().getValue().getModelo())
                .setPrecio(Double.parseDouble(ValesController.getInstance().getTxtPrecio().getText()))
                .setFechaVenta(ValesController.getInstance().getDatePickerFechaVenta().getValue())
                .setComision(Long.parseLong(ValesController.getInstance().getTxtComision().getText()))
                .setDireccion(ValesController.getInstance().getTxtDireccion().getText());

        if (!ValesController.getInstance().getTxtCostoMensajeria().getText().equals("")) {
            if (ValesController.getInstance().getTxtCostoMensajeria().isValid()) {
                vale.setCostoMensajeria(Long.parseLong(ValesController.getInstance().getTxtCostoMensajeria().getText()));
            }
        }
        this.serviceVales = new ServiceVales(vale);
    }

    /**
     * Método estático con el cual vamos a pasar mensajes de información al Label de
     * estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    private void setEstadoInformativo(String estado) {
        ValesController.getInstance().getLabelEstado().setText(estado);
        ValesController.getInstance().getLabelEstado().getStyleClass().add("label");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            ValesController.getInstance().getLabelEstado().setText("");
        });
        pause.play();
    }

    /**
     * Método estático con el que vamos a pasar mensajes de error al Label de
     * estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    private void setEstadoError(String estado) {
        ValesController.getInstance().getLabelEstado().setText(estado);
        ValesController.getInstance().getLabelEstado().getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            ValesController.getInstance().getLabelEstado().setText(null);
        });
        pause.play();
    }

    private void limpiarCampos(){
        ValesController.getInstance().getFilterComboBoxPromotor().getSelectionModel().clearSelection();
        ValesController.getInstance().getTxtCliente().setText("");
        ValesController.getInstance().getTxtTelefonoCliente().setText("");
        ValesController.getInstance().getFilterComboBoxImei().getSelectionModel().clearSelection();
        ValesController.getInstance().getTxtMarca().setText("");
        ValesController.getInstance().getTxtModelo().setText("");
        ValesController.getInstance().getTxtPrecio().setText("");
        ValesController.getInstance().getDatePickerFechaVenta().setValue(null);
        ValesController.getInstance().getTxtComision().setText("");
        ValesController.getInstance().getCheckBoxServicioMensajeria().setSelected(false);
        ValesController.getInstance().getTxtDireccion().setText("");
        ValesController.getInstance().getTxtCostoMensajeria().setText("");
    }
}
