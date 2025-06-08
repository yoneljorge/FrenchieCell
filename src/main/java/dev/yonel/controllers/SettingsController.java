package dev.yonel.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.controllers.settings.BackUpController;
import dev.yonel.controllers.settings.ConfigBackUpController;
import dev.yonel.controllers.settings.MarcaModeloController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Controlador para el FXML viewController. Se implementa el Patrón de Diseño
 * Singlenton para que solo halla una sola
 * instancia de esta clase.
 */
@Getter
public class SettingsController implements Initializable {

    
    @FXML
    private ScrollPane node_Root;
    @FXML
    private VBox vBox_InScrollPane;

    /* IMPLEMENTAMOS EL PATRÓN DE DISEÑO SINGLENTON */
    @Getter(AccessLevel.NONE)
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

    public static void restartInstance() {
        instance = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {

        node_Root.setFitToHeight(true);
        node_Root.setFitToWidth(true);

        LoadControllers.load("settings/viewBackUp", BackUpController.getInstance(), node -> {
            vBox_InScrollPane.getChildren().add(node);
        });

        LoadControllers.load("settings/viewConfigBackUp", ConfigBackUpController.getInstance(), node -> {
            vBox_InScrollPane.getChildren().add(node);
        });
        
        LoadControllers.load("settings/viewMarcaModelo", MarcaModeloController.getInstance(), node -> {
            vBox_InScrollPane.getChildren().add(node);
        });

    }

}
