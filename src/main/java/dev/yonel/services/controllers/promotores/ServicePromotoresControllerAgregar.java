package dev.yonel.services.controllers.promotores;

import dev.yonel.controllers.PromotoresController;
import dev.yonel.services.Gatillo;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class ServicePromotoresControllerAgregar {

    private static ServicePromotoresControllerAgregar instance;

    private MFXTextField txtNombre;
    private MFXTextField txtApellidos;
    private MFXTextField txtCelular;
    private MFXButton btnLimpiar;
    private MFXButton btnAgregar;
    private Label validacionNombre;
    private Label validacionApellidos;
    private Label validacionCelular;
    private Label labelEstado;

    private PromotoresController promotoresController = PromotoresController.getInstance();

    private ServicePromotoresControllerAgregar() {
        instance = this;

        Platform.runLater(() -> {
            setObjects();
        });
    }

    public static ServicePromotoresControllerAgregar getInstance() {
        if (instance == null) {
            instance = new ServicePromotoresControllerAgregar();
        }
        return instance;
    }

    private void setObjects() {
        this.txtNombre = promotoresController.getTxtNombre();
        this.txtApellidos = promotoresController.getTxtApellidos();
        this.txtCelular = promotoresController.getTxtCelular();
        this.btnLimpiar = promotoresController.getBtnLimpiar();
        this.btnAgregar = promotoresController.getBtnAgregar();
        this.validacionNombre = promotoresController.getValidacionNombre();
        this.validacionApellidos = promotoresController.getValidacionApellidos();
        this.validacionCelular = promotoresController.getValidacionCelular();
        this.labelEstado = promotoresController.getLabelEstado_AgregarPromotor();
    }

    /*********************************************
     * **********CONFIGURACIÓN********************
     *********************************************/

    public void configure() {
        Platform.runLater(() -> {
            MFXTextFieldUtil.validateString(txtNombre, validacionNombre);
            txtNombre.setText("");
            validacionNombre.setText("");

            MFXTextFieldUtil.validateString(txtApellidos, validacionApellidos);
            txtApellidos.setText("");
            validacionApellidos.setText("");

            MFXTextFieldUtil.validatePhoneNumber(txtCelular, validacionCelular);
            txtCelular.setText("");
            validacionCelular.setText("");

            btnAgregar.setOnAction(event -> {
                agreagar();
            });

            btnLimpiar.setOnAction(event -> {
                limpiar();
            });
        });
    }

    private void agreagar() {
        ServicePromotor promotor = new ServicePromotor();

        if (isValid()) {
            promotor.setNombre(txtNombre.getText());
            promotor.setApellidos(txtApellidos.getText());
            promotor.setTelefono(Long.parseLong(txtCelular.getText()));

            if (promotor.save()) {
                txtNombre.setText("");
                txtApellidos.setText("");
                txtCelular.setText("");
                /*
                 * Informamos que hay cambios en el apartado de promotores
                 */
                
                Gatillo.newPromotor();
                //Mandamos el mensaje de que se guardo el promotor 
                setEstadoInformativo("Gestor guardado");

                //Limpiamos los campos del formulario
            } else {
                setEstadoInformativo("Gestor no guardado");
            }
        } else {
            setEstadoError("Verifique los campos.");
        }
    }

    private boolean isValid() {
        int i = 0;

        if (txtNombre.getText() != null) {
            if (!txtNombre.getText().equals("")) {
                if (txtNombre.isValid()) {
                    i++;
                }
            } else {
                validacionNombre.setText("Campo obligatorio.");
                validacionNombre.setVisible(true);
            }
        }

        if (txtApellidos.getText() != null) {
            if (!txtApellidos.getText().equals("")) {
                if (txtApellidos.isValid()) {
                    i++;
                }
            } else {
                validacionApellidos.setText("Campo obligatorio.");
                validacionApellidos.setVisible(true);
            }
        }

        if (txtCelular.getText() != null) {
            if (!txtCelular.getText().equals("")) {
                if (txtCelular.isValid()) {
                    i++;
                }
            } else {
                validacionCelular.setText("Campo obligatorio.");
                validacionCelular.setVisible(true);
            }
        }

        return i == 3;
    }

    private void limpiar() {
        txtNombre.clear();
        txtApellidos.clear();
        txtCelular.clear();
    }

    /**
     * Método con el cual vamos a pasar mensajes de información al Label de
     * estado.
     * 
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoInformativo(String estado) {
        labelEstado.setText(estado);
        labelEstado.getStyleClass().add("label");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            labelEstado.setText("");
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
        labelEstado.setText(estado);
        labelEstado.getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            labelEstado.setText(null);
        });
        pause.play();
    }
}
