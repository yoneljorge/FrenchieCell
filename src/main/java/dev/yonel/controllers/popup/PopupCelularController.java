package dev.yonel.controllers.popup;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import dev.yonel.controllers.CelularesController;
import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.celulares.ServiceMarca;
import dev.yonel.services.celulares.ServiceModelo;
import dev.yonel.utils.AlertUtil;
import dev.yonel.utils.ui.popup.PopupController;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PopupCelularController implements Initializable, PopupController {

    @FXML
    private VBox vBoxRoot;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private MFXTextField txtPrecio;
    @FXML
    private MFXTextField txtImeiUno;
    @FXML
    private MFXTextField txtImeiDos;
    @FXML
    private MFXFilterComboBox<Modelo> cmbModelo;
    @FXML
    private MFXFilterComboBox<Marca> cmbMarca;
    @FXML
    private MFXButton btnGuardar;
    @FXML
    private MFXButton btnCancelar;
    @FXML
    private Label validationLabel_Precio;
    @FXML
    private Label validationLabel_ImeiUno;
    @FXML
    private Label validationLabel_ImeiDos;
    @FXML
    private Label validationLabel_Fecha;
    @FXML
    private Label lblEstado;
    @FXML
    private DatePicker dateFechaInventario;

    /* Instancia del objeto celular que vamos a modificar */
    private Celular celular;

    /*
     * Instancias de los servicios que vamos a utilizar
     */
    private ServiceMarca serviceMarca;
    private ServiceModelo serviceModelo;
    private ServiceCelular serviceCelular;

    /**
     * Constructor para el PopupCelular.
     * Se inicializan los servicios.
     *
     * @param celular un objeto de tipo celular.
     */
    public PopupCelularController(Celular celular) {
        this.celular = celular;
        this.serviceCelular = new ServiceCelular(celular);
        this.serviceMarca = new ServiceMarca();
        this.serviceMarca.setMarca(celular.getMarca());
        this.serviceModelo = new ServiceModelo();
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {

        //Ponemos en el cache
        vBoxRoot.setCache(true);
        /*
         * Configuramos los controles
         */

        // Primero hay que configurar los comboBox y después hacer la selección.
        Platform.runLater(() -> {
            serviceMarca.configureComboBox(cmbMarca);
            cmbMarca.getSelectionModel().selectItem(celular.getMarca());
            // Exception in thread "JavaFX Application Thread"
            // java.lang.NullPointerException: Cannot invoke
            // "dev.yonel.models.Marca.getMarca()" because the return value of
            // "dev.yonel.models.Modelo.getMarca()" is null
            serviceModelo.configureComboBoxEdicion(cmbModelo, cmbMarca,
                    celular.getMarca().getMarca(), celular.getModelo().getModelo());

            MFXTextFieldUtil.validateIMEI(txtImeiUno, validationLabel_ImeiUno);
            txtImeiUno.setText(String.valueOf(celular.getImeiUno()));
            validationLabel_ImeiUno.setText("");

            MFXTextFieldUtil.validateIMEI(txtImeiDos, validationLabel_ImeiDos);
            if (celular.getImeiDos() != 0) {
                txtImeiDos.setText(String.valueOf(celular.getImeiDos()));
            } else {
                txtImeiDos.setText("");
            }
            validationLabel_ImeiDos.setText("");

            MFXTextFieldUtil.validatePrecio(txtPrecio, validationLabel_Precio);
            txtPrecio.setText(String.valueOf(celular.getPrecio()));
            validationLabel_Precio.setText("");

            dateFechaInventario.setValue(celular.getFechaInventario());

            txtObservaciones.setText(celular.getObservaciones());

            btnCancelar.setOnAction(event -> {
                onCloseAction.run();
            });

            btnGuardar.setOnAction(event -> {
                update();
            });

            // ###########################################
            cmbMarca.selectedItemProperty().addListener(new ChangeListener<Marca>() {
                @Override
                public void changed(ObservableValue<? extends Marca> observable, Marca oldValue, Marca newValue) {
                    if (newValue != null) {
                        enableActualizar();
                    }
                }
            });
            cmbModelo.selectedItemProperty().addListener(new ChangeListener<Modelo>() {
                @Override
                public void changed(ObservableValue<? extends Modelo> observable, Modelo oldValue, Modelo newValue) {
                    if (newValue != null) {
                        enableActualizar();
                    }
                }
            });
            txtImeiUno.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue != null) {
                        enableActualizar();
                    }
                }
            });
            txtImeiDos.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue != null) {
                        enableActualizar();
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
            txtObservaciones.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue != null) {
                        enableActualizar();
                    }
                }
            });
            dateFechaInventario.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
                        LocalDate newValue) {
                    if (newValue != null) {
                        enableActualizar();
                    }
                }
            });
        });
    }

    /**
     * Método con el cual vamos a pasar mensajes de información al Label de
     * estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    private void setEstadoInfo(String estado) {
        Platform.runLater(() -> {
            lblEstado.setText(estado);
            lblEstado.getStyleClass().clear();
            lblEstado.getStyleClass().add("label");

            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                lblEstado.setText("");
            });
            pause.play();
        });
    }

    /**
     * Método con el que vamos a pasar mensajes de error al Label de
     * estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    private void setEstadoError(String estado) {
        Platform.runLater(() -> {
            lblEstado.setText(estado);
            lblEstado.getStyleClass().clear();
            lblEstado.getStyleClass().add("label-error");

            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                lblEstado.setText("");
            });
            pause.play();
        });
    }

    private void update() {
        // Parte de validación
        if (cmbMarca.getValue() == null) {
            setEstadoError("Seleccione una Marca");
        } else if (cmbModelo.getValue() == null) {
            setEstadoError("Seleccione un Moedlo");
        } else if (isValid()) {
            // Parte de actualización
            setEstadoInfo("Guardando...");
            // Mostramos que está cargando
            CelularesController.getInstance().loading(true);

            serviceCelular.setMarca(cmbMarca.getValue());
            serviceCelular.setModelo(cmbModelo.getValue());
            serviceCelular.setImei_Uno(txtImeiUno.getText());
            serviceCelular.setImei_Dos(txtImeiDos.getText());
            serviceCelular.setPrecio(txtPrecio.getText());
            serviceCelular.setFechaInventario(dateFechaInventario.getValue());
            serviceCelular.setObservaciones(txtObservaciones.getText());

            if (serviceCelular.update()) {
                CelularesController.getInstance().loading(false);
                AlertUtil.information(vBoxRoot.getScene().getWindow(), "Celular actualizado", null);
                // DialogsUtils.openInfo(vBoxRoot, "Celular actualizado", "Edición");
                onCloseAction.run();
            } else {
                CelularesController.getInstance().loading(false);
                AlertUtil.error("Celular no actualizado",
                        "Posiblemente a un error de conexión,\nsi el error persiste contacte al desarrollador.");
            }
        }
    }

    private boolean isValid() {

        if (txtImeiUno.getLength() == 0 || txtPrecio.getLength() == 0 || dateFechaInventario.getValue() == null) {
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

            if (dateFechaInventario.getValue() == null) {
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

    private void enableActualizar() {
        if (!cmbMarca.getValue().getMarca().equals(celular.getMarca().getMarca())) {
            btnGuardar.setDisable(false);
        } else if (!cmbModelo.getValue().getModelo().equals(celular.getModelo().getModelo())) {
            btnGuardar.setDisable(false);
        } else if (!txtImeiUno.getText().equals(String.valueOf(celular.getImeiUno()))) {
            btnGuardar.setDisable(false);
        } else if (celular.getImeiDos() == 0 && !txtImeiDos.getText().equals("")) {
            btnGuardar.setDisable(false);
        } else if (celular.getImeiDos() != 0 && !txtImeiDos.getText().equals(String.valueOf(celular.getImeiDos()))) {
            btnGuardar.setDisable(false);
        } else if (!txtPrecio.getText().equals(String.valueOf(celular.getPrecio()))) {
            btnGuardar.setDisable(false);
        } else if (!dateFechaInventario.getValue().equals(celular.getFechaInventario())) {
            btnGuardar.setDisable(false);
        } else if (!txtObservaciones.getText().equals(celular.getObservaciones())) {
            btnGuardar.setDisable(false);
        } else {
            btnGuardar.setDisable(true);
        }
    }

    /* Variable que va a almacenar la instancia Runnable */
    private Runnable onCloseAction;

    /**
     * Método con el cual vamos a ejecutar la acción de cerrar el popup.
     * La ejecución va a estar en la clase desde la cual es llamado este popup.
     * La acción va a estar en esta clase.
     *
     * @param action método que se a correr.
     */
    @Override
    public void onCloseAction(Runnable action) {
        this.onCloseAction = action;
    }

}
