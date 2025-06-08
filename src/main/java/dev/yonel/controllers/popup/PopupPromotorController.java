package dev.yonel.controllers.popup;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.services.Gatillo;
import dev.yonel.services.Mensajes;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerPromotor;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.utils.AlertUtil;
import dev.yonel.utils.ui.popup.PopupController;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PopupPromotorController implements Initializable, PopupController {

    @FXML
    private VBox vBoxRoot;
    @FXML
    private MFXTextField txtNombre;
    @FXML
    private MFXTextField txtApellidos;
    @FXML
    private MFXTextField txtCelular;
    @FXML
    private MFXButton btnCancelar;
    @FXML
    private MFXButton btnActualizar;
    @FXML
    private Label validacionNombre;
    @FXML
    private Label validacionApellidos;
    @FXML
    private Label validacionCelular;

    private Runnable onClose;

    private Mensajes mensajes = new Mensajes(getClass());

    private ServicePromotor servicePromotor;

    public PopupPromotorController(ServicePromotor servicePromotor){
        this.servicePromotor = new ServicePromotor();
        this.servicePromotor.setPromotor(servicePromotor.getPromotor());
    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {

        Platform.runLater(() -> {
            MFXTextFieldUtil.validateString(txtNombre, validacionNombre);
            MFXTextFieldUtil.validateString(txtApellidos, validacionApellidos);
            MFXTextFieldUtil.validatePhoneNumber(txtCelular, validacionCelular);

            txtNombre.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                    if(newValue != null){
                        enableActualizar();
                    }
                }
            });

            txtApellidos.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                    if(newValue != null){
                        enableActualizar();
                    }
                }
            });

            txtCelular.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                    if(newValue != null){
                        enableActualizar();
                    }
                }
            });

            txtNombre.setText(servicePromotor.getNombre());
            txtApellidos.setText(servicePromotor.getApellidos());
            txtCelular.setText(String.valueOf(servicePromotor.getTelefono()));


            btnCancelar.setOnAction(event -> {
                onClose.run();
            });

            btnActualizar.setOnAction(event -> {
                update();
            });
        });
    }

    @Override
    public void onCloseAction(Runnable action) {
        onClose = action;
    }

    private void update() {
    
        if (isValid()) {
            servicePromotor.setNombre(txtNombre.getText());
            servicePromotor.setApellidos(txtApellidos.getText());
            servicePromotor.setTelefono(Long.parseLong(txtCelular.getText()));

            if (servicePromotor.updatePromotor()) {
                mensajes.info("Promotor actualizado");
                Gatillo.newPromotor();
                AlertUtil.information(vBoxRoot.getScene().getWindow(),
                        "Actualizando Gestor", "Gestor actualizado con éxito.");
                onClose.run();
                ServicePromotoresControllerPromotor.getInstance().loadPromotor(servicePromotor.getIdPromotor());
            } else {
                mensajes.err("Error actualizando promotor");
                AlertUtil.error(vBoxRoot.getScene().getWindow(),
                        "Actualizando Gestor",
                        "No se puedo actualizar al gestor,\nsi el error persiste contacte con el desarrollador.");
            }
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
                validacionNombre.setText("Campo obligatorio");
                validacionNombre.setVisible(true);
            }
        }

        if (txtApellidos.getText() != null) {
            if (!txtApellidos.getText().equals("")) {
                if (txtApellidos.isValid()) {
                    i++;
                }
            } else {
                validacionApellidos.setText("Campo obligatorio");
                validacionApellidos.setVisible(true);
            }
        }

        if (txtCelular.getText() != null) {
            if (!txtCelular.getText().equals("")) {
                if (txtCelular.isValid()) {
                    i++;
                }
            } else {
                validacionCelular.setText("Campo obligatorio");
                validacionCelular.setVisible(true);
            }
        }

        return i == 3;
    }

    private void enableActualizar(){
        if(!txtNombre.getText().equals(servicePromotor.getNombre())){
            btnActualizar.setDisable(false);
        }else if(!txtApellidos.getText().equals(servicePromotor.getApellidos())){
            btnActualizar.setDisable(false);
        }else if(!txtCelular.getText().equals(String.valueOf(servicePromotor.getTelefono()))){
            btnActualizar.setDisable(false);
        }else{
            btnActualizar.setDisable(true);
        }
    }
}
