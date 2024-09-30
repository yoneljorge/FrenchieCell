package dev.yonel.services;

import java.io.IOException;

import dev.yonel.App;
import dev.yonel.controllers.DashboardController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class LoadControllers {

    public static void load(String fxml, Object controllerInstance, String vBoxId, LoadCallback callback) {

        Task<VBox> task = new Task<VBox>() {
            @Override
            protected VBox call() throws Exception {
                FXMLLoader loader = App.fxmlLoader(fxml);
                loader.setController(controllerInstance);
                VBox vBoxLoading = null;

                try {
                    // Cargar el VBox en segundo plano
                    vBoxLoading = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Agregamos un id único al vbox.
                if (vBoxLoading != null) {
                    vBoxLoading.setId(vBoxId);
                }

                return vBoxLoading;
            }

            @Override
            protected void succeeded() {
                VBox vBox = getValue();

                if (vBox != null) {
                    Platform.runLater(() -> {
                        // Busca si ya existe un VBox con el mismo id en la lista de VBox
                        VBox existingVBoxInList = DashboardController.getInstance().getListVBox().stream()
                                .filter(v -> vBoxId.equals(v.getId()))
                                .findFirst()
                                .orElse(null);

                        if (existingVBoxInList != null) {
                            DashboardController.getInstance().getListVBox().remove(existingVBoxInList);
                        }

                        // Buscamos si ya existe un VBox con el mismo id en el StackPane
                        VBox existingVBoxInStackPane = (VBox) DashboardController.getInstance().getStackPane()
                                .getChildren()
                                .stream()
                                .filter(v -> vBoxId.equals(v.getId()))
                                .findFirst()
                                .orElse(null);

                        if (existingVBoxInStackPane != null) {
                            DashboardController.getInstance().getStackPane().getChildren()
                                    .remove(existingVBoxInStackPane);
                        }

                        // Agrega el nuevo VBox a la lista y al stackPane
                        DashboardController.getInstance().getListVBox().add(vBox);
                        DashboardController.getInstance().getStackPane().getChildren().add(vBox);

                        // Llamamos al callback una vez que el VBox está listo
                        callback.onLoaded(vBox);
                    });
                }
            }
        };

        Thread load = new Thread(task);
        load.start();

        try {
            load.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadCallback {
        void onLoaded(VBox vbox);
    }
}