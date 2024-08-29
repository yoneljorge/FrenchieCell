package dev.yonel.services.controllers.promotores;

import java.util.Map;

import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;

public class ServicePromotoresControllerAgregar {

    private MFXTextField txtNombre;
    private MFXTextField txtApellidos;
    private MFXTextField txtCelular;
    private MFXButton btnLimpiar;
    private MFXButton btnAgregar;
    private Label validacionNombre;
    private Label validacionApellidos;
    private Label validacionCelular;
    private static Label labelEstado;

    @SuppressWarnings("static-access")
    public ServicePromotoresControllerAgregar(Map<String, Object> lista) {
        this.txtNombre = (MFXTextField) lista.get("nombre");
        this.txtApellidos = (MFXTextField) lista.get("apellidos");
        this.txtCelular = (MFXTextField) lista.get("celular");
        this.btnAgregar = (MFXButton) lista.get("agregar");
        this.btnLimpiar = (MFXButton) lista.get("limpiar");
        this.validacionNombre = (Label) lista.get("validacionNombre");
        this.validacionApellidos = (Label) lista.get("validacionApellidos");
        this.validacionCelular = (Label) lista.get("validacionCelular");
        this.labelEstado = (Label) lista.get("estado");
    }

    /*********************************************
     * **********CONFIGURACIÓN********************
     *********************************************/

    public void configure() {
        MFXTextFieldUtil.validateString(txtNombre, validacionNombre);
        MFXTextFieldUtil.validateString(txtApellidos, validacionApellidos);
        MFXTextFieldUtil.validatePhoneNumber(txtCelular, validacionCelular);

        btnAgregar.setOnAction(event -> {
            agreagar();
        });

        btnLimpiar.setOnAction(event -> {
            limpiar();
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
