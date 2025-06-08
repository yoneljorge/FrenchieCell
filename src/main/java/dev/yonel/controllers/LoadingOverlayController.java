package dev.yonel.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class LoadingOverlayController implements Initializable {

    @FXML
    private StackPane overlayPane;
    @FXML
    private MFXProgressSpinner progressIndicator;

    private static LoadingOverlayController instance;

    private LoadingOverlayController() {
        instance = this;
    }

    private static synchronized void createInstance() {
        instance = new LoadingOverlayController();
    }

    public static LoadingOverlayController getInstance() {
        if (instance == null) {
            createInstance();
        }

        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Configurar el Pane de overlay
        // overlayPane.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        overlayPane.setVisible(false);
        overlayPane.setPickOnBounds(true); // Bloquea interacciones con lo que está debajo
    }

    public void hide(HBox mainContent) {
        overlayPane.setVisible(false);
        mainContent.setEffect(null);// Quitar desenfoque
    }

    public void show(HBox mainContent) {
        overlayPane.setVisible(true);
        overlayPane.autosize();
        mainContent.setEffect(new GaussianBlur(10)); // Aplicar desenfoque
    }
}
