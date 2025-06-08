package dev.yonel.services.controllers.settings;

import dev.yonel.App;
import dev.yonel.controllers.SettingsController;
import dev.yonel.controllers.settings.BackUpController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class LoadBackUpController {

    private static LoadBackUpController instance;

    private LoadBackUpController() {

    }

    private static synchronized void createInstance() {
        instance = new LoadBackUpController();
    }

    public static LoadBackUpController getInstance() {
        if (instance == null) {
            createInstance();
        }

        return instance;
    }

    public void configure() {
        
        Task<AnchorPane> task = new Task<AnchorPane>() {
            @Override
            protected AnchorPane call() throws Exception{
                FXMLLoader loader = App.fxmlLoader("settings/viewBackup");
                BackUpController controller = BackUpController.getInstance();
                loader.setController(controller);
                AnchorPane anchorPane = null;

                try {
                    anchorPane = loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return anchorPane;
            }

            @Override
            protected void succeeded() {
                AnchorPane anchorPane = getValue();
                
                if(anchorPane != null){
                    Platform.runLater(() -> {
                        SettingsController.getInstance().getVBox_InScrollPane().getChildren().add(anchorPane);
                        //callback.onLoaded(anchorPane);
                    });
                }
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }
}
