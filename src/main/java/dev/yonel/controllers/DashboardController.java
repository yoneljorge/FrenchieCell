package dev.yonel.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.*;

import dev.yonel.App;
import dev.yonel.utils.others.SetVisible;

public class DashboardController implements Initializable {

    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnGeneral;

    @FXML
    private Button btnCelulares;

    @FXML
    private Button btnPromotores;

    @FXML
    private Button btnVales;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private StackPane stackPane;

    private VBox principal;
    private VBox celulares;
    private VBox promotores;
    private VBox vales;

    private final ArrayList<VBox> listVBox = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            principal = (VBox) App.loadFXML("viewPrincipal");
            stackPane.getChildren().add(principal);
            listVBox.add(principal);

            celulares = (VBox) App.loadFXML("viewCelulares");
            stackPane.getChildren().add(celulares);
            celulares.setVisible(false);
            listVBox.add(celulares);

            promotores = (VBox) App.loadFXML("viewPromotores");
            stackPane.getChildren().add(promotores);
            promotores.setVisible(false);
            listVBox.add(promotores);

            vales = (VBox) App.loadFXML("viewVales");
            stackPane.getChildren().add(vales);
            vales.setVisible(false);
            listVBox.add(vales);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // ********************BOTONES DEL MENU*********** */
    // *********************************************** */

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnGeneral) {
            SetVisible.This(listVBox, principal);
        }

        if (actionEvent.getSource() == btnCelulares) {
            SetVisible.This(listVBox, celulares);
        }

        if (actionEvent.getSource() == btnPromotores) {
            SetVisible.This(listVBox, promotores);
        }

        if (actionEvent.getSource() == btnVales) {
            SetVisible.This(listVBox, vales);
        }

        if (actionEvent.getSource() == btnSignout) {
            alertExit();
        }
    }

    // ***********************MÉTODOS***************** */
    // ********************************************** */

    private void alertExit() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("¿Deseas salir?");
        alert.setContentText("Guarde toda la información antes de salir.");

        // Agregar botones al alert
        // alert.setAlertType(AlertType.CONFIRMATION);
        ButtonType botonAceptar = new ButtonType("Aceptar");
        ButtonType botonCancelar = new ButtonType("Cancelar");
        alert.getButtonTypes().setAll(botonAceptar, botonCancelar);

        // Mostrar el Alert
        ButtonType result = (ButtonType) alert.showAndWait().get();

        // Se especifica que se hace en cada caso
        if (result == botonAceptar) {
            Platform.exit();
        }
    }

}
