package dev.yonel.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import dev.yonel.App;
import dev.yonel.controllers.DashboardController;
import dev.yonel.services.Mensajes;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;

public class FileChooserUtil {

    private Mensajes mensajes = new Mensajes(getClass());

    private FileChooser fileChooser;

    public FileChooserUtil() {
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de Texto (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    public void getSaveDialog(String content) {
        File file = fileChooser.showSaveDialog(App.getStage());
        // Evitamos de que salte un error null cuando no seleccionamos ninguna ruta para
        // guardar
        if (file != null) {
            Task<Void> taskSaveFile = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    System.out.println("Guardando sumario....");
                    Platform.runLater(() -> DashboardController.getInstance().showLoadingOverlay());
                    saveFile(content, file);
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    Platform.runLater(() -> DashboardController.getInstance().hideLoadingOverlay());
                    System.out.println("Sumario guardado.");
                }
            };
            new Thread(taskSaveFile).start();
        }

    }

    private void saveFile(String content, File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err(e.getMessage());
        }
    }
}
