package dev.yonel.controllers;

import java.io.IOException;

import dev.yonel.App;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class LoadControllers {

    public static void load(String fxml, Object controllerInstance, String vBoxId, LoadCallback callback) {

        Task<Node> task = new Task<Node>() {
            @Override
            protected Node call() throws Exception {
                FXMLLoader loader = App.fxmlLoader(fxml);
                loader.setController(controllerInstance);
                Node vBoxLoading = null;

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
                Node vBox = getValue();

                if (vBox != null) {
                    Platform.runLater(() -> {
                        // Busca si ya existe un VBox con el mismo id en la lista de VBox
                        Node existingVBoxInList = DashboardController.getInstance().getListNodes().stream()
                                .filter(v -> vBoxId.equals(v.getId()))
                                .findFirst()
                                .orElse(null);

                        if (existingVBoxInList != null) {
                            DashboardController.getInstance().getListNodes().remove(existingVBoxInList);
                        }

                        // Buscamos si ya existe un VBox con el mismo id en el StackPane
                        Node existingVBoxInStackPane = (Node) DashboardController.getInstance().getStackPane()
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
                        DashboardController.getInstance().getListNodes().add(vBox);
                        DashboardController.getInstance().getStackPane().getChildren().add(vBox);

                        // Llamamos al callback una vez que el VBox está listo
                        callback.onLoaded((Node)vBox);
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

    public static void load(String fxml, Object controllerInstance, LoadCallback callback){
        Task<Node> task = new Task<Node>() {
            @Override
            protected Node call() throws Exception{
                FXMLLoader loader = App.fxmlLoader(fxml);
                loader.setController(controllerInstance);
                Node node = null;

                try {
                    node = loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return node;
            }

            @Override
            protected void succeeded(){
                Node node = getValue();
                if(node != null){
                    callback.onLoaded(node);
                }
            }
        };

        Thread thread = new Thread(task);
        thread.start();

        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public interface LoadCallback {
        void onLoaded(Node node);
    }
}