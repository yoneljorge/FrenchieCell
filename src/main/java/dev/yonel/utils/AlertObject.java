package dev.yonel.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertObject {
    private String title = "";
    private String headerText = "";
    private String contentText = "";
    
    Alert alert;

    
    public AlertObject(AlertType type, String title, String contentText){
        alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(contentText);
    }
}
