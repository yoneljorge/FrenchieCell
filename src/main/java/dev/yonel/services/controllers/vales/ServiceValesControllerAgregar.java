package dev.yonel.services.controllers.vales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ServiceValesControllerAgregar {

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
    private MFXTextField txtConstoMensajeria;
    private Label labelValidacionCostoMensajeria;
    private static Label labelEstado;

    private ServicePromotor servicePromotor;
    private ServiceCelular serviceCelular;
    private List<MFXTextField> listMFXTextField;
    private ServiceVales serviceVales;

    @SuppressWarnings({ "unchecked", "static-access" })
    public ServiceValesControllerAgregar(Map<String, Object> lista) {
        this.btnGuardar = (MFXButton) lista.get("guardar");
        this.btnLimpiar = (MFXButton) lista.get("limpiar");
        this.filterComboBoxPromotor = (MFXFilterComboBox<Promotor>) lista.get("comboPromotor");
        ;
        this.txtCliente = (MFXTextField) lista.get("cliente");
        this.labelValidacionCliente = (Label) lista.get("validarCliente");
        this.txtTelefonoCliente = (MFXTextField) lista.get("telefonoCliente");
        this.labelValidacionTelefonoCliente = (Label) lista.get("validacionTelefonoCliente");
        this.filterComboBoxImei = (MFXFilterComboBox<Celular>) lista.get("comboImei");
        this.txtMarca = (MFXTextField) lista.get("marca");
        this.txtModelo = (MFXTextField) lista.get("modelo");
        this.txtPrecio = (MFXTextField) lista.get("precio");
        this.labelValidacionPrecio = (Label) lista.get("validacionPrecio");
        this.datePickerFechaVenta = (DatePicker) lista.get("fechaVenta");
        this.txtComision = (MFXTextField) lista.get("comision");
        this.labelValidacionComision = (Label) lista.get("validacionComision");
        this.checkBoxServicioMensajeria = (CheckBox) lista.get("checkMensajeria");
        this.txtDireccion = (TextArea) lista.get("direccion");
        this.txtConstoMensajeria = (MFXTextField) lista.get("costoMensajeria");
        this.labelValidacionCostoMensajeria = (Label) lista.get("validacionCostoMensajeria");
        this.labelEstado = (Label) lista.get("labelEstado");

        this.servicePromotor = new ServicePromotor();
        this.serviceCelular = new ServiceCelular();

        this.listMFXTextField = new ArrayList<>();
    }

    public void configure() {

        servicePromotor.configureFilterComboBox(filterComboBoxPromotor);
        MFXTextFieldUtil.validateString(txtCliente, labelValidacionCliente);
        MFXTextFieldUtil.validatePhoneNumber(txtTelefonoCliente, labelValidacionTelefonoCliente);
        serviceCelular.configureFilterComboBoxImei(filterComboBoxImei);
        filterComboBoxImei.selectedItemProperty().addListener(new ChangeListener<Celular>() {
            @Override
            public void changed(ObservableValue<? extends Celular> observable, Celular oldValue, Celular newValue) {
                if (newValue != null) {
                    getDatosCelular(filterComboBoxImei.getValue());
                }
            }
        });
        MFXTextFieldUtil.validatePrecio(txtPrecio, labelValidacionPrecio);
        MFXTextFieldUtil.validatePrecio(txtComision, labelValidacionComision);
        checkBoxServicioMensajeria.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (checkBoxServicioMensajeria.isSelected()) {
                txtDireccion.setDisable(false);
                txtConstoMensajeria.setDisable(false);
            } else {
                txtDireccion.setDisable(true);
                txtConstoMensajeria.setDisable(true);
            }
        });
        MFXTextFieldUtil.validatePrecio(txtConstoMensajeria, labelValidacionCostoMensajeria);

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
    }

    public void getDatosCelular(Celular celular) {
        txtMarca.setText(celular.getMarca().getMarca());
        txtModelo.setText(celular.getModelo().getModelo());
        txtPrecio.setText(String.valueOf(celular.getPrecio()));
    }

    public void guardar(){
        if(isValid()){

            updateVale();

            if(serviceVales.save(filterComboBoxImei.getValue())){
                setEstadoInformativo("Vale guardado.");
            }
            
        }
    }

    public boolean isValid(){
        int i = 0;
        for (MFXTextField mfxTextField : listMFXTextField) {
            if(mfxTextField.getText() != null){
                if(!mfxTextField.getText().equals("")){
                    if(mfxTextField.isValid()){
                        i++;
                    }else{
                        setEstadoError("Verifique todos los campos.");
                    }
                }else{
                    setEstadoError("Campos vacíos.");
                }
            }else{
                setEstadoError("Campos vacíos.");
            }
        }

        if(filterComboBoxPromotor.getValue() != null){
            i ++;
        }else{
            setEstadoError("Seleccione un gestor.");
        }

        if(filterComboBoxImei.getValue() != null){
            i ++;
        }else{
            setEstadoError("Seleccione un IMEI");
        }

        if(datePickerFechaVenta.getValue() != null){
            i ++;
        }else{
            setEstadoError("Fecha de venta está vacio.");
        }

        if(checkBoxServicioMensajeria.isSelected()){
            if(!txtDireccion.getText().equals("")){
                i ++;
            }else{
                setEstadoError("La Dirección está vacía.");
            }

            if(!txtConstoMensajeria.getText().equals("")){
                if(txtConstoMensajeria.isValid()){
                    i ++;
                }else{
                    setEstadoError("Verifique los campos.");
                }
            }else{
                setEstadoError("Campos vacíos.");
            }

            return i == listMFXTextField.size() + 5;
        }
        return i == listMFXTextField.size() + 3;
    }

    public void updateVale(){
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
        .setDireccion(txtDireccion.getText())
        ;

        if(!txtConstoMensajeria.getText().equals("")){
            if(txtConstoMensajeria.isValid()){
                vale.setCostoMensajeria(Long.parseLong(txtConstoMensajeria.getText()));
            }
        }
        this.serviceVales = new ServiceVales(vale);
    }

    /*********************************************
     * **********MÉTODOS ESTÁTICOS****************
     *********************************************/

    /**
     * Método estático con el cual vamos a pasar mensajes de información al Label de
     * estado.
     * 
     * @param estado el mensaje que se desea mostrar.
     */
    public static void setEstadoInformativo(String estado) {
        labelEstado.setText(estado);
        labelEstado.getStyleClass().add("label");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            labelEstado.setText("");
        });
        pause.play();
    }

    /**
     * Método estático con el que vamos a pasar mensajes de error al Label de
     * estado.
     * 
     * @param estado el mensaje que se desea mostrar.
     */
    public static void setEstadoError(String estado) {
        labelEstado.setText(estado);
        labelEstado.getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            labelEstado.setText(null);
        });
        pause.play();
    }
}
