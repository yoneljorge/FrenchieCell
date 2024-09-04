package dev.yonel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import dev.yonel.controllers.DashboardController;


/**
 * JavaFX App
 */
public class App extends Application {
	private double x, y;

    private Scene mainScene;
	private Parent root;
	private DashboardController dashboardController;


	@Override 
	public void init() throws IOException{

		FXMLLoader loader = fxmlLoader("viewDashboard");
		this.dashboardController = new DashboardController();
		
		loader.setController(dashboardController);
		this.root = loader.load();
		this.mainScene = new Scene(root);
	}


    @Override
    public void start(Stage primaryStage) throws IOException {

		dashboardController.setStage(primaryStage);

		primaryStage.setScene(mainScene);
		//set stage borderless
		primaryStage.initStyle(StageStyle.UNDECORATED);
		
		//drag it here
		root.setOnMousePressed(event -> {
			x = event.getSceneX();
			y = event.getSceneY();
		});
		
		root.setOnMouseDragged(event -> {
			primaryStage.setX(event.getScreenX() - x);
			primaryStage.setY(event.getScreenY() - y);
		});
		
		primaryStage.show();
    }


    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

	public static FXMLLoader fxmlLoader(String fxml) throws IOException{
		return new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
	}

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", "dev.yonel.MyPreloader");
		launch(args);
    }

}