package dev.yonel.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import dev.yonel.App;
import dev.yonel.services.Mensajes;
import javafx.stage.FileChooser;

public class FileChooserUtil {

    private Mensajes mensajes = new Mensajes(getClass());

    private FileChooser fileChooser;

    public FileChooserUtil() {
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de Texto (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    public void getSaveDialog(String content){
        File file = fileChooser.showSaveDialog(App.getStage());
        saveFile(content, file);
    }

    private void saveFile(String content, File file){
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err(e.getMessage());
        }
    }
}
