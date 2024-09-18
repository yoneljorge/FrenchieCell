package dev.yonel;

import java.io.IOException;

import dev.yonel.controllers.DashboardController;
import dev.yonel.services.Updates;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * JavaFX App
 */
public class App extends Application {
    private double x, y;

    private Scene mainScene;
    private Parent root;
    private DashboardController dashboardController;
    private static @Getter Stage stage;

    @Override
    public void init() throws IOException {

        /*
         * Se crea un Thread para realizar la carga de la base de datos y del FXML sin
         * bloquear el hilo principal.
         */
        Thread initThread = new Thread(() -> {
            System.out.println("Ejecutando initThread");
            // Actualizamos la base de datos.
            Updates.run();

            FXMLLoader loader;
            try {
                loader = fxmlLoader("viewDashboard");
                this.dashboardController = new DashboardController();
                loader.setController(dashboardController);
                this.root = loader.load();
                this.mainScene = new Scene(root);
            } catch (IOException e) {
                System.out.println("Error en la ejecución del initThread");
                e.printStackTrace();
            }

            System.out.println("Terminando ejecución initThread");
        });

        initThread.start();

        /*
         * Usamos .join() para asegurarnos de que el hilo de inicialización termine
         * antes de que continúe la ejecución del programa.
         */
        try {
            System.out.println("Esperando a que se termine de ejecutar initThread");
            initThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        stage = primaryStage;

        primaryStage.setScene(mainScene);
        // Le quitamos los botones de maximizar y minimizar a la interfaz.
        // primaryStage.initStyle(StageStyle.UNDECORATED);

        // Cuando se haga clic sobre la ventanan esta se puede arrastrar
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        // Se arrastre la ventada
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

    public static FXMLLoader fxmlLoader(String fxml) throws IOException {
        return new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
    }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", "dev.yonel.MyPreloader");
        launch(args);
    }

}