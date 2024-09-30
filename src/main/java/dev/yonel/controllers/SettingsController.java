package dev.yonel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.controllers.settings.ServiceSettingsControllerMarcasModelos;
import io.github.palexdev.materialfx.controls.MFXButton;

/**
 * Controlador para el FXML viewController. Se implementa el Patrón de Diseño
 * Singlenton para que solo halla una sola
 * instancia de esta clase.
 */
public class SettingsController implements Initializable {

    @FXML
    private @Getter VBox vBox_Root;

    @FXML
    private @Getter ListView<Marca> listView_Marcas;
    @FXML
    private @Getter ListView<Modelo> listView_Modelos;
    @FXML 
    private @Getter MFXButton btn_AgregarMarca;
    @FXML
    private @Getter MFXButton btn_EliminarMarca;
    @FXML
    private @Getter MFXButton btn_AgregarModelo;
    @FXML
    private @Getter MFXButton btn_EliminarModelo;
    @FXML
    private @Getter Label lblEstadoMarcasModelos;

    /* IMPLEMENTAMOS EL PATRÓN DE DISEÑO SINGLENTON */
    private static SettingsController instance;

    private SettingsController() {
        instance = this;
    }

    /**
     * Méotodo con el que obtenemos la instancia de SettingsController.
     *
     * @return <code>instance</code> -> que es la instancia de esta clase.
     */
    public static SettingsController getInstance() {
        if (instance == null) {
            instance = new SettingsController();
        }

        return instance;
    }

    public static void restartInstance(){
        instance = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        
        ServiceSettingsControllerMarcasModelos.getInstance().configure();
    }

    
}
