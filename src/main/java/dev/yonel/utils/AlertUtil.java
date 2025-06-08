package dev.yonel.utils;

import dev.yonel.App;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AlertUtil {

    public AlertUtil(AlertType alertType) {
    }

    private static Alert getAlert(AlertType type){
        Alert alert = new Alert(type);
        Stage alerStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alerStage.getIcons().add(App.getInstance().getIcon());

        return alert;
    }

    public static void confirmation(Window window, String headerText, String contentText, Runnable onConfirm) {
        Alert alert = getAlert(AlertType.CONFIRMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(window);

        ButtonType botonAceptar = new ButtonType("Aceptar");
        ButtonType botonCancelar = new ButtonType("Cancelar");

        alert.getButtonTypes().setAll(botonAceptar, botonCancelar);

        ButtonType result = (ButtonType) alert.showAndWait().get();

        if (result == botonAceptar) {
            onConfirm.run();
        }
    }

    public static void error(String headerText, String contectText) {
        Alert alert = getAlert(AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contectText);

        alert.showAndWait();
    }

    public static void error(Window window, String headerText, String contectText) {
        Alert alert = getAlert(AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contectText);
        alert.initOwner(window);

        alert.showAndWait();
    }

    public static void information(String headerText, String contentText) {

        Alert alert = getAlert(AlertType.INFORMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        //alert.initOwner(window);
        alert.showAndWait();
    }

    public static void information(Window window, String headerText, String contentText) {

        Alert alert = getAlert(AlertType.INFORMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(window);
        alert.showAndWait();
    }

    public static void advertencia(String headerText, String contentText, Runnable onConfirm) {
        Alert alert = getAlert(AlertType.WARNING);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        ButtonType botonAceptar = new ButtonType("Aceptar");
        ButtonType botonCancelar = new ButtonType("Cancelar");

        alert.getButtonTypes().setAll(botonAceptar, botonCancelar);

        ButtonType result = (ButtonType) alert.showAndWait().get();

        if (result == botonAceptar) {
            onConfirm.run();
        }
    }

    public static void advertencia(Window window, String headerText, String contentText, Runnable onConfirm) {
        Alert alert = getAlert(AlertType.WARNING);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(window);
        ButtonType botonAceptar = new ButtonType("Aceptar");
        ButtonType botonCancelar = new ButtonType("Cancelar");

        alert.getButtonTypes().setAll(botonAceptar, botonCancelar);

        ButtonType result = (ButtonType) alert.showAndWait().get();

        if (result == botonAceptar) {
            onConfirm.run();
        }
    }
}
