package dev.yonel.controllers.popup;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.models.Celular;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.utils.ui.popup.PopupController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class ItemValeDetallesController implements Initializable, PopupController {

    // General
    @FXML
    private VBox vBoxVista;
    @FXML
    private VBox vBoxEdicion;

    // Panel Vista
    @FXML
    private Label lblPromotor;
    @FXML
    private Label lblCliente;
    @FXML
    private Label lblTelefonoCliente;
    @FXML
    private Label lblImei;
    @FXML
    private Label lblMarca;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblPrecio;
    @FXML
    private Label lblPrecioMensajeria;
    @FXML
    private Label lblDireccion;
    @FXML
    private Label lblFechaVenta;
    @FXML
    private Label lblFechaGarantia;
    @FXML
    private Label lblDiasGarantia;
    @FXML
    private Label lblComision;
    @FXML
    private MFXButton btnEditar;
    @FXML
    private MFXButton btnEliminar;
    @FXML
    private MFXButton btnCerrar;

    // Panel Edición
    @FXML
    private @Getter MFXButton btnCancelar;
    @FXML
    private @Getter MFXButton btnGuardar;
    @FXML
    private @Getter MFXFilterComboBox<Promotor> filterComboBoxPromotor;
    @FXML
    private @Getter MFXTextField txtCliente;
    @FXML
    private @Getter MFXTextField txtTelefonoCliente;
    @FXML
    private @Getter MFXFilterComboBox<Celular> filterComboBoxImei;
    @FXML
    private @Getter MFXTextField txtMarca;
    @FXML
    private @Getter MFXTextField txtModelo;
    @FXML
    private @Getter MFXTextField txtPrecio;
    @FXML
    private @Getter DatePicker datePickerFechaVenta;
    @FXML
    private @Getter MFXTextField txtComision;
    @FXML
    private @Getter TextArea txtDireccion;
    @FXML
    private @Getter MFXTextField txtCostoMensajeria;
    @FXML
    private @Getter Label labelEstado;

    private @Getter Vale vale;

    private Runnable onCloseAction;

    public ItemValeDetallesController(Vale vale) {
        this.vale = vale;
    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        this.lblPromotor.setText(vale.getPromotor().getNombre());
        this.lblCliente.setText(vale.getCliente());
        this.lblTelefonoCliente.setText(String.valueOf(vale.getClienteTelefono()));
        this.lblImei.setText(String.valueOf(vale.getImei()));
        this.lblMarca.setText(vale.getMarca().getMarca());
        this.lblModelo.setText(vale.getModelo().getModelo());
        this.lblPrecio.setText(String.valueOf(vale.getPrecio()));
        this.lblPrecioMensajeria.setText(String.valueOf(vale.getCostoMensajeria()));
        this.lblDireccion.setText(vale.getDireccion());
        this.lblFechaVenta.setText(String.valueOf(vale.getFechaVenta()));
        this.lblFechaGarantia.setText(String.valueOf(vale.getFechaGarantia()));

        // Ponemos siempre el VBox de la vista como visible.
        vBoxVista.setVisible(true);
        vBoxEdicion.setVisible(false);

        /*
         * Si el vale está en garantia ponemos el color verde de fondo.
         */
        if (vale.getGarantia()) {
            vBoxVista.getStyleClass().add("vale-enGarantia");
            vBoxEdicion.getStyleClass().add("vale-enGarantia");
        } else {
            vBoxVista.getStyleClass().add("vale-sinGarantia");
            vBoxEdicion.getStyleClass().add("vale-sinGarantia");
        }

        if (vale.getDiasGarantia() <= 7) {
            this.lblDiasGarantia.setText("En Garantía -" + String.valueOf(7 - vale.getDiasGarantia()));
        } else {
            this.lblDiasGarantia.setText("Terminada +" + String.valueOf(vale.getDiasGarantia() - 7));
        }
        this.lblComision.setText(String.valueOf(vale.getComision()));

        btnCerrar.setOnAction(event -> {
            onCloseAction.run();
        });

        btnEditar.setOnAction(event -> {
            goToEdicion();
        });

        btnCancelar.setOnAction(event -> {
            goToVista();
        });
    }

    /**
     * Método con el cual vamos a crear la interfaz Runnable para cuando se
     * haga clic en el boton se ejecute el métod hidePopup desde donde se llamó
     * el popup.
     *
     * @param onCloseAction variable de instancia.
     */
    public void onCloseAction(Runnable onCloseAction) {
        this.onCloseAction = onCloseAction;
    }

    /**
     * Método con el que vamos a poner visible el vbox de la vista y oculto el de
     * editar.
     */
    public void goToVista() {
        vBoxVista.setDisable(false);
        vBoxVista.setVisible(true);

        vBoxEdicion.setVisible(false);
        vBoxEdicion.setDisable(true);
    }

    /**
     * Método con el que vamos a poner visible el vbox de edicion y oculto el de la
     * vista.
     */
    public void goToEdicion() {
        vBoxVista.setDisable(true);
        vBoxVista.setVisible(false);

        vBoxEdicion.setVisible(true);
        vBoxEdicion.setDisable(false);
    }

}
