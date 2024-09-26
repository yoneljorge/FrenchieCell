package dev.yonel.services.controllers.celulares;

import java.time.LocalDate;
import java.util.Optional;

import dev.yonel.controllers.CelularesController;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.Mensajes;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.celulares.ServiceMarca;
import dev.yonel.services.celulares.ServiceModelo;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import lombok.Getter;
import lombok.Setter;

public class ServiceCelularControllerAgregar {

    private static ServiceCelularControllerAgregar instance;


    private ServiceMarca serviceMarca;
    private ServiceModelo serviceModelo;

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
    private Label lblEstado;
    private Label validationLabel_ImeiUno;
    private Label validationLabel_ImeiDos;
    private Label validationLabel_Precio;
    private Label validationLabel_Fecha;

    private CelularesController celularesController;

    private ServiceCelularControllerAgregar() {
        instance = this;

        Platform.runLater(() -> {
            this.serviceMarca = new ServiceMarca();
            this.serviceModelo = new ServiceModelo();
            this.celularesController = CelularesController.getInstance();

            setObjects();
        });
    }

    public static ServiceCelularControllerAgregar getInstance() {
        if (instance == null) {
            instance = new ServiceCelularControllerAgregar();
        }
        return instance;
    }

    private void setObjects() {

        this.marcaCombo = celularesController.getCmbMarcaAgregar();
        this.modeloCombo = celularesController.getCmbModeloAgregar();
        this.btnAgregarMarca = celularesController.getBtnAgregarMarca();
        this.btnAgregarModelo = celularesController.getBtnAgregarModelo();
        this.btnGuardar = celularesController.getBtnGuardar();
        this.btnLimpiar = celularesController.getBtnLimpiar();
        this.txtImeiUno = celularesController.getTxtImeiUno();
        this.txtImeiDos = celularesController.getTxtImeiDos();
        this.txtPrecio = celularesController.getTxtPrecio();
        this.txtObservaciones = celularesController.getTxtObservaciones();
        this.fecha = celularesController.getDateFechaInventario();
        this.lblEstado = celularesController.getLblEstado();
        this.validationLabel_ImeiUno = celularesController.getValidationLabel_ImeiUno();
        this.validationLabel_ImeiDos = celularesController.getValidationLabel_ImeiDos();
        this.validationLabel_Precio = celularesController.getValidationLabel_Precio();
        this.validationLabel_Fecha = celularesController.getValidationLabel_Fecha();

        setEstadoInformation("");
    }

    /*******************************************************
     * ***********CONFIGURACIÓN DE LOS CONTROLES************
     *******************************************************/

    public void configure() {
        Platform.runLater(() -> {
            serviceMarca.configureComboBox(marcaCombo);
            serviceModelo.configureComboBox(modeloCombo, marcaCombo, btnAgregarModelo);

            MFXTextFieldUtil.validateIMEI(txtImeiUno, validationLabel_ImeiUno);
            txtImeiUno.setText("");
            validationLabel_ImeiUno.setText("");

            MFXTextFieldUtil.validateIMEI(txtImeiDos, validationLabel_ImeiDos);
            txtImeiDos.setText("");
            validationLabel_ImeiDos.setText("");

            MFXTextFieldUtil.validatePrecio(txtPrecio, validationLabel_Precio);
            txtPrecio.setText("");
            validationLabel_Precio.setText("");
            fecha.setValue(null);
            txtObservaciones.setText("");

            btnAgregarMarca.setOnAction(event -> {
                guardarMarca();
            });

            btnAgregarModelo.setOnAction(event -> {
                guardarModelo();
            });

            btnLimpiar.setOnAction(event -> {
                limpiar();
            });

            btnGuardar.setOnAction(event -> {
                guardarCelular();
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


        });
    }

    /**************************************************
     ***************** MÉTODOS*************************
     **************************************************/

    /**
     * Método con el cual vamos a pasar mensajes de información al Label de
     * estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoInformation(String estado) {
        getInstance().lblEstado.setText(estado);
        getInstance().lblEstado.getStyleClass().add("label");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            getInstance().lblEstado.setText("");
        });
        pause.play();
    }

    /**
     * Método con el que vamos a pasar mensajes de error al Label de
     * estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoError(String estado) {
        getInstance().lblEstado.setText(estado);
        getInstance().lblEstado.getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            getInstance().lblEstado.setText(null);
        });
        pause.play();
    }

    private void guardarMarca() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Vista Agregar Celular");
        dialog.setHeaderText("Introduzca la marca que desea agregar.");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            serviceMarca.setMarca(new Marca(name));

            if (serviceMarca.save()) {
                // Igualar el modelo existente por el de la lista para evitar el error al
                // seleccionar el item en el combobox
                ObservableList<Marca> newObservableList = marcaCombo.getItems();
                for (Marca m : newObservableList) {
                    if (serviceMarca.getMarca().getMarca().equals(m.getMarca())) {
                        serviceMarca.setMarca(m);
                    }
                }
                marcaCombo.selectItem(serviceMarca.getMarca());
                setEstadoInformation("Marca " + serviceMarca.getMarca().getMarca() + " agregada.");
            } else if (ServiceMarca.isBanderaMarcaExiste()) {
                guardarMarca();
                ServiceMarca.setBanderaMarcaExiste(false);
            }
        });
    }

    private void guardarModelo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Modelo");
        dialog.setHeaderText("El modelo se agregará a la marca " + marcaCombo.getValue().getMarca());
        dialog.setContentText("Introduzca el modelo que desea agregar.");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            Modelo modelo = new Modelo();
            modelo.setMarca(marcaCombo.getValue());
            modelo.setModelo(name);

            serviceModelo.setModelo(modelo);

            if (serviceModelo.save()) {

                // Igualar el modelo existente por el de la lista para eviatar el error en
                // selectItem.
                ObservableList<Modelo> pruebaObservable = modeloCombo.getItems();
                for (Modelo m : pruebaObservable) {
                    if (m.getModelo().equals(modelo.getModelo())) {
                        modelo = m;
                    }
                }
                modeloCombo.selectItem(modelo);
                setEstadoInformation("Modelo " + modelo.getModelo() + " agregado.");
            } else if (ServiceModelo.isBanderaModeloExiste()) {
                guardarModelo();
                ServiceModelo.setBanderaModeloExiste(false);
            }
        });
    }

    private void guardarCelular() {
        // Parte de validación
        if (marcaCombo.getValue() == null) {
            setEstadoError("Seleccione una MARCA");
        } else if (modeloCombo.getValue() == null) {
            setEstadoError("Seleccione un MODELO");
        } else if (isValid()) {


            // Parte de guardado
            setEstadoInformation("Guardando...");
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

            setEstadoError("Verifique los campos.");
            return false;
        } else if (!txtImeiUno.isValid() || !txtPrecio.isValid()
                || !txtImeiDos.isValid() && txtImeiDos.getLength() > 0) {
            setEstadoError("Verifique los campos");
            return false;
        } else {
            return true;
        }
    }
}
