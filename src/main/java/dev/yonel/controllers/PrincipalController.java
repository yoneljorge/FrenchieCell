package dev.yonel.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;


public class PrincipalController implements Initializable{

    @FXML
    private VBox pnItems = null;

    private @Setter Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Node[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            try {

                nodes[i] = App.loadFXML("items/itemPrincipal");
                
                pnItems.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
