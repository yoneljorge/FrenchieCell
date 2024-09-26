package dev.yonel.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

public class AlertUtil {

    private static Alert alert;


    public AlertUtil(AlertType alertType) {
    }

    public static void confirmation(Window window, String headerText, String contentText, Runnable onConfirm) {
        alert = new Alert(AlertType.CONFIRMATION);
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
        alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contectText);

        alert.showAndWait();
    }

    public static void error(Window window, String headerText, String contectText) {
        alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contectText);
        alert.initOwner(window);

        alert.showAndWait();
    }

    public static void information(String headerText, String contentText) {

        alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        //alert.initOwner(window);
        alert.showAndWait();
    }

    public static void information(Window window, String headerText, String contentText) {

        alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(window);
        alert.showAndWait();
    }

    public static void advertencia(String headerText, String contentText, Runnable onConfirm) {
        alert = new Alert(AlertType.WARNING);
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
        alert = new Alert(AlertType.WARNING);
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
