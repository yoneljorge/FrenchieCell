package dev.yonel;


import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MyPreloader extends Preloader{
    
    private Stage preloaderStage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = App.fxmlLoader("viewPreloader");
        Parent root = loader.load();
        Scene scene = new Scene(root);
        preloaderStage = primaryStage;

        preloaderStage.setScene(scene);
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            preloaderStage.hide();
        }
    }
}
