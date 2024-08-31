package dev.yonel.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;


public class PrincipalController implements Initializable{

    @FXML
    private VBox pnItems = null;

    private @Setter Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        

    }
}
