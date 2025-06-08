package dev.yonel.services.controllers.promotores;

import dev.yonel.controllers.PromotoresController;
import dev.yonel.services.Gatillo;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import javafx.animation.PauseTransition;
import javafx.application.Platform;

public class ServicePromotoresControllerAgregar {

    private static ServicePromotoresControllerAgregar instance;

    private ServicePromotoresControllerAgregar() {
        instance = this;
    }

    public static ServicePromotoresControllerAgregar getInstance() {
        if (instance == null) {
            instance = new ServicePromotoresControllerAgregar();
        }
        return instance;
    }

    public static void restartInstance(){
        instance = null;
    }

    /*********************************************
     * **********CONFIGURACIÓN********************
     *********************************************/

    public void configure() {
        Platform.runLater(() -> {
            MFXTextFieldUtil.validateString(PromotoresController.getInstance().getTxtNombre(),
            PromotoresController.getInstance().getValidacionNombre());
            PromotoresController.getInstance().getTxtNombre().setText("");
            PromotoresController.getInstance().getValidacionNombre().setText("");

            MFXTextFieldUtil.validateString(PromotoresController.getInstance().getTxtApellidos(), 
            PromotoresController.getInstance().getValidacionApellidos());
            PromotoresController.getInstance().getTxtApellidos().setText("");
            PromotoresController.getInstance().getValidacionApellidos().setText("");

            MFXTextFieldUtil.validatePhoneNumber(PromotoresController.getInstance().getTxtCelular(),
            PromotoresController.getInstance().getValidacionCelular());
            PromotoresController.getInstance().getTxtCelular().setText("");
            PromotoresController.getInstance().getValidacionCelular().setText("");

            PromotoresController.getInstance().getBtnAgregar().setOnAction(event -> {
                agreagar();
            });

            PromotoresController.getInstance().getBtnLimpiar().setOnAction(event -> {
                limpiar();
            });
        });
    }

    private void agreagar() {
        ServicePromotor promotor = new ServicePromotor();

        if (isValid()) {
            promotor.setNombre(PromotoresController.getInstance().getTxtNombre().getText());
            promotor.setApellidos(PromotoresController.getInstance().getTxtApellidos().getText());
            promotor.setTelefono(Long.parseLong(PromotoresController.getInstance().getTxtCelular().getText()));

            if (promotor.save()) {
                PromotoresController.getInstance().getTxtNombre().setText("");
                PromotoresController.getInstance().getTxtApellidos().setText("");
                PromotoresController.getInstance().getTxtCelular().setText("");
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

        if (PromotoresController.getInstance().getTxtNombre().getText() != null) {
            if (!PromotoresController.getInstance().getTxtNombre().getText().equals("")) {
                if (PromotoresController.getInstance().getTxtNombre().isValid()) {
                    i++;
                }
            } else {
                PromotoresController.getInstance().getValidacionNombre().setText("Campo obligatorio.");
                PromotoresController.getInstance().getValidacionNombre().setVisible(true);
            }
        }

        if (PromotoresController.getInstance().getTxtApellidos().getText() != null) {
            if (!PromotoresController.getInstance().getTxtApellidos().getText().equals("")) {
                if (PromotoresController.getInstance().getTxtApellidos().isValid()) {
                    i++;
                }
            } else {
                PromotoresController.getInstance().getValidacionApellidos().setText("Campo obligatorio.");
                PromotoresController.getInstance().getValidacionApellidos().setVisible(true);
            }
        }

        if (PromotoresController.getInstance().getTxtCelular().getText() != null) {
            if (!PromotoresController.getInstance().getTxtCelular().getText().equals("")) {
                if (PromotoresController.getInstance().getTxtCelular().isValid()) {
                    i++;
                }
            } else {
                PromotoresController.getInstance().getValidacionCelular().setText("Campo obligatorio.");
                PromotoresController.getInstance().getValidacionCelular().setVisible(true);
            }
        }

        return i == 3;
    }

    private void limpiar() {
        PromotoresController.getInstance().getTxtNombre().clear();
        PromotoresController.getInstance().getTxtApellidos().clear();
        PromotoresController.getInstance().getTxtCelular().clear();
    }

    /**
     * Método con el cual vamos a pasar mensajes de información al Label de
     * estado.
     * 
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoInformativo(String estado) {
        PromotoresController.getInstance().getLabelEstado_AgregarPromotor().setText(estado);
        PromotoresController.getInstance().getLabelEstado_AgregarPromotor().getStyleClass().add("label");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            PromotoresController.getInstance().getLabelEstado_AgregarPromotor().setText("");
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
        PromotoresController.getInstance().getLabelEstado_AgregarPromotor().setText(estado);
        PromotoresController.getInstance().getLabelEstado_AgregarPromotor().getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            PromotoresController.getInstance().getLabelEstado_AgregarPromotor().setText(null);
        });
        pause.play();
    }
}
