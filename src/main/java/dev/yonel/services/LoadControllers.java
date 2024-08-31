package dev.yonel.services;

import java.io.IOException;

import dev.yonel.App;
import dev.yonel.controllers.DashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class LoadControllers {

    public static VBox load(String fxml, Object controllerInstance){
        try {
            VBox vBox;
            FXMLLoader loader = App.fxmlLoader(fxml);
            loader.setController(controllerInstance);
            vBox = loader.load();
            DashboardController.getListVBox().add(vBox);
            return vBox;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
