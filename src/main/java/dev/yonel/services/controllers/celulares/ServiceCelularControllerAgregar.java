package dev.yonel.services.controllers.celulares;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.celulares.ServiceMarca;
import dev.yonel.services.celulares.ServiceModelo;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

public class ServiceCelularControllerAgregar {
    public static boolean banderaMarcaExiste = false;
    public static boolean banderaModeloExiste = false;

    private ServiceMarca serviceMarca = new ServiceMarca();
    private ServiceModelo serviceModelo = new ServiceModelo();

    private MFXFilterComboBox<Marca> marcaCombo;
    private MFXFilterComboBox<Modelo> modeloCombo;
    private MFXButton btnAgregarMarca;
    private MFXButton btnAgregarModelo;
    private MFXButton btnGuardar;
    private MFXButton btnLimpiar;
    private MFXTextField txtImeiUno;
    private MFXTextField txtImeiDos;
    private MFXTextField txtPrecio;
    private TextArea txtObservaciones;
    private DatePicker fecha;
    private static Label lblEstado;
    private Label validationLabel_ImeiUno;
    private Label validationLabel_ImeiDos;
    private Label validationLabel_Precio;
    private Label validationLabel_Fecha;

    @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
    public ServiceCelularControllerAgregar(Map<String, Object> listaControlesVistaAgregar) {
        this.marcaCombo = (MFXFilterComboBox) listaControlesVistaAgregar.get("marca");
        this.modeloCombo = (MFXFilterComboBox) listaControlesVistaAgregar.get("modelo");
        this.btnAgregarMarca = (MFXButton) listaControlesVistaAgregar.get("agregarMarca");
        this.btnAgregarModelo = (MFXButton) listaControlesVistaAgregar.get("agregarModelo");
        this.btnGuardar = (MFXButton) listaControlesVistaAgregar.get("btnGuardar");
        this.btnLimpiar = (MFXButton) listaControlesVistaAgregar.get("btnLimpiar");
        this.txtImeiUno = (MFXTextField) listaControlesVistaAgregar.get("imeiUno");
        this.txtImeiDos = (MFXTextField) listaControlesVistaAgregar.get("imeiDos");
        this.txtPrecio = (MFXTextField) listaControlesVistaAgregar.get("precio");
        this.txtObservaciones = (TextArea) listaControlesVistaAgregar.get("observaciones");
        this.fecha = (DatePicker) listaControlesVistaAgregar.get("fecha");
        this.lblEstado = (Label) listaControlesVistaAgregar.get("estado");
        this.validationLabel_ImeiUno = (Label) listaControlesVistaAgregar.get("validation_ImeiUno");
        this.validationLabel_ImeiDos = (Label) listaControlesVistaAgregar.get("validation_ImeiDos");
        this.validationLabel_Precio = (Label) listaControlesVistaAgregar.get("validation_Precio");
        this.validationLabel_Fecha = (Label) listaControlesVistaAgregar.get("validation_Fecha");
        setEstadoInformation("");
    }

    /**************************************************
     ***************** MÉTODOS ESTÁTICOS*****************
     **************************************************/
    /**
     * Método estático con el cual vamos a pasar mensajes de información al Label de
     * estado.
     * 
     * @param estado el mensaje que se desea mostrar.
     */
    public static void setEstadoInformation(String estado) {
        lblEstado.setText(estado);
        lblEstado.getStyleClass().add("label");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            lblEstado.setText("");
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
        lblEstado.setText(estado);
        lblEstado.getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            lblEstado.setText(null);
        });
        pause.play();
    }

    /*******************************************************
     * ***********CONFIGURACIÓN DE LOS CONTROLES************
     *******************************************************/

    public void configure() {
        serviceMarca.configureComboBox(marcaCombo);
        serviceModelo.configureComboBox(modeloCombo, marcaCombo, btnAgregarModelo);

        MFXTextFieldUtil.validateIMEI(txtImeiUno, validationLabel_ImeiUno);
        MFXTextFieldUtil.validateIMEI(txtImeiDos, validationLabel_ImeiDos);

        MFXTextFieldUtil.validatePrecio(txtPrecio, validationLabel_Precio);

        btnAgregarMarca.setOnAction(event -> {
            agregarMarca();
        });

        btnAgregarModelo.setOnAction(event -> {
            agregarModelo();
        });

        btnLimpiar.setOnAction(event -> {
            limpiar();
        });

        btnGuardar.setOnAction(event -> {
            guardar();
        });

        // Escuchamos los cambios del modeloCombo
        modeloCombo.selectedItemProperty().addListener(new ChangeListener<Modelo>() {
            @Override
            public void changed(ObservableValue<? extends Modelo> observable, Modelo oldValue, Modelo newValue) {
                if (newValue != null) {
                    txtImeiUno.setDisable(false);
                    txtImeiDos.setDisable(false);
                    txtPrecio.setDisable(false);
                    fecha.setDisable(false);
                    txtObservaciones.setDisable(false);
                }
            }
        });

        // Escuchamos los cambios de la fecha
        fecha.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
                    LocalDate newValue) {
                if (newValue != null) {
                    validationLabel_Fecha.setVisible(false);
                    validationLabel_Fecha.setText("");
                }
            }
        });
    }

    /***************************************************
     * *****************MÉTODOS*************************
     ***************************************************/

    private void agregarMarca() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Vista Agregar Celular");
        dialog.setHeaderText("Introduzca la marca que desea agregar.");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            Marca marca = new Marca();
            marca.setMarca(name);

            if (serviceMarca.save(marca)) {
                // Igualar el modelo existente por el de la lista para evitar el error al
                // seleccionar el item en el combobox
                ObservableList<Marca> newObservableList = marcaCombo.getItems();
                for (Marca m : newObservableList) {
                    if(marca.getMarca().equals(m.getMarca())){
                        marca = m;
                    }
                }
                marcaCombo.selectItem(marca);

                ServiceCelularControllerAgregar
                        .setEstadoInformation("Marca " + marca.getMarca() + " agregada.");
                        
            } else if (banderaMarcaExiste) {
                agregarMarca();
                banderaMarcaExiste = false;
            }
        });
    }

    private void agregarModelo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Vista Agregar Celular");
        dialog.setHeaderText("Introduzca el modelo que desea agregar.");
        dialog.setContentText("El modelo se agregará a la marca " + marcaCombo.getValue().getMarca());

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            Modelo modelo = new Modelo();
            modelo.setMarca(marcaCombo.getValue());
            modelo.setModelo(name);

            if (serviceModelo.save(modelo)) {

                // Igualar el modelo existente por el de la lista para eviatar el error en
                // selectItem.
                ObservableList<Modelo> pruebaObservable = modeloCombo.getItems();
                for (Modelo m : pruebaObservable) {
                    if (m.getModelo().equals(modelo.getModelo())) {
                        modelo = m;
                    }
                }
                modeloCombo.selectItem(modelo);
                ServiceCelularControllerAgregar.setEstadoInformation("Modelo " + modelo.getModelo() + " agregado.");
            } else if (banderaModeloExiste) {
                agregarModelo();
                banderaModeloExiste = false;

            }
        });
    }

    private void guardar() {
        // Parte de validación
        if (marcaCombo.getValue() == null) {
            ServiceCelularControllerAgregar.setEstadoError("Seleccione una MARCA");
        } else if (modeloCombo.getValue() == null) {
            ServiceCelularControllerAgregar.setEstadoError("Seleccione un MODELO");
        } else if (isValid()) {

            // Parte de guardado
            ServiceCelularControllerAgregar.setEstadoInformation("Guardando...");
            ServiceCelular serviceCelular = new ServiceCelular();
            serviceCelular.setMarca(marcaCombo.getValue());
            serviceCelular.setModelo(modeloCombo.getValue());
            serviceCelular.setImei_Uno(txtImeiUno.getText());
            serviceCelular.setImei_Dos(txtImeiDos.getText());
            serviceCelular.setPrecio(txtPrecio.getText());
            serviceCelular.setFechaInventario(fecha.getValue());
            serviceCelular.setObservaciones(txtObservaciones.getText());

            if (serviceCelular.saveCelular()) {
                txtImeiUno.setText("");
                txtImeiDos.setText("");
                txtPrecio.setText("");
                txtObservaciones.setText("");
                validationLabel_ImeiUno.setVisible(false);
                validationLabel_ImeiDos.setVisible(false);
                validationLabel_Precio.setVisible(false);
                // Establecemos que se ha agregado un nuevo celular para que al pasar a la vista
                // este recargue los celulares
                ServiceCelularControllerVista.setNewItem(true);
            }
        }
    }

    private void limpiar() {
        marcaCombo.getSelectionModel().clearSelection();
        txtImeiUno.setDisable(true);
        txtImeiUno.setText("");
        txtImeiDos.setDisable(true);
        txtImeiDos.setText("");
        txtPrecio.setDisable(true);
        txtPrecio.setText("");
        fecha.setDisable(true);
        fecha.setValue(null);
        txtObservaciones.setDisable(true);
        txtObservaciones.setText("");
        validationLabel_ImeiUno.setVisible(false);
        validationLabel_ImeiDos.setVisible(false);
    }

    private boolean isValid() {

        if (txtImeiUno.getLength() == 0 || txtPrecio.getLength() == 0 || fecha.getValue() == null) {
            if (txtImeiUno.getLength() == 0) {
                validationLabel_ImeiUno.setText("Campo obligatorio");
                validationLabel_ImeiUno.setAlignment(Pos.CENTER_RIGHT);
                validationLabel_ImeiUno.setVisible(true);
            }

            if (txtPrecio.getLength() == 0) {
                validationLabel_Precio.setText("Campo obligatorio");
                validationLabel_Precio.setAlignment(Pos.CENTER_RIGHT);
                validationLabel_Precio.setVisible(true);
            }

            if (fecha.getValue() == null) {
                validationLabel_Fecha.setText("Campo obligatorio.");
                validationLabel_Fecha.setAlignment(Pos.CENTER_RIGHT);
                validationLabel_Fecha.setVisible(true);
            }

            ServiceCelularControllerAgregar.setEstadoError("Verifique los campos.");
            return false;
        } else if (!txtImeiUno.isValid() || !txtPrecio.isValid()
                || !txtImeiDos.isValid() && txtImeiDos.getLength() > 0) {
            ServiceCelularControllerAgregar.setEstadoError("Verifique los campos");
            return false;
        } else {
            return true;
        }
    }
}
