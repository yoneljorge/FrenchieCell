package dev.yonel;

import java.io.IOException;

import dev.yonel.controllers.DashboardController;
import dev.yonel.services.Mensajes;
import dev.yonel.services.Scheduled;
import dev.yonel.services.Updates;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
    private @Getter Image icon = new Image(getClass().getResourceAsStream("icons/icon.png"));
    private Mensajes mensajes = new Mensajes(getClass());

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void init() throws IOException {

        /*
         * Se crea un Thread para realizar la carga de la base de datos y del FXML sin
         * bloquear el hilo principal.
         */
        Thread initThread = new Thread(() -> {
            mensajes.info("Ejecutando initThread");
            // Actualizamos la base de datos.
            Updates.run();

            FXMLLoader loader;
            try {
                loader = fxmlLoader("viewDashboard");
                this.dashboardController = DashboardController.getInstance();
                loader.setController(dashboardController);
                this.root = loader.load();
                this.mainScene = new Scene(root);
            } catch (IOException e) {
                mensajes.err("Error en la ejecución del initThread");
                e.printStackTrace();
            }

            mensajes.info("Terminando ejecución initThread");
        });

        initThread.start();

        /*
         * Usamos .join() para asegurarnos de que el hilo de inicialización termine
         * antes de que continúe la ejecución del programa.
         */
        try {
            mensajes.info("Esperando a que se termine de ejecutar initThread");
            initThread.join();
        } catch (Exception e) {
            mensajes.err("Error esperando a que termine de ejecutar initThread");
            mensajes.err(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        instance = this;

        stage = primaryStage;

        // Establecemos el icono de la aplicacion
        primaryStage.getIcons().add(icon);

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

        Scheduled.runTasks();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static FXMLLoader fxmlLoader(String fxml) throws IOException {
        return new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
    }

    public void restartApp() {
        // Cerra la ventana actual

        stage.close();

        /*
         * Se crea un Thread para realizar la carga de la base de datos y del FXML sin
         * bloquear el hilo principal.
         */
        Thread initThread = new Thread(() -> {
            mensajes.info("Ejecutando initThread");
            // Actualizamos la base de datos.
            Updates.run();

            FXMLLoader loader;
            try {
                loader = fxmlLoader("viewDashboard");
                this.dashboardController = DashboardController.getInstance();
                loader.setController(dashboardController);
                this.root = loader.load();
                this.mainScene = new Scene(root);
            } catch (IOException e) {
                mensajes.err("Error en la ejecución del initThread");
                e.printStackTrace();
            }

            mensajes.info("Terminando ejecución initThread");
        });

        initThread.start();

        /*
         * Usamos .join() para asegurarnos de que el hilo de inicialización termine
         * antes de que continúe la ejecución del programa.
         */
        try {
            mensajes.info("Esperando a que se termine de ejecutar initThread");
            initThread.join();
        } catch (Exception e) {
            mensajes.err("Error esperando a que termine de ejecutar initThread");
            mensajes.err(e.getMessage());
            e.printStackTrace();
        }

        stage = new Stage();

        // Image icon = new Image(getClass().getResourceAsStream("/icons/icon.png"));
        stage.getIcons().add(icon);

        stage.setScene(mainScene);
        // Le quitamos los botones de maximizar y minimizar a la interfaz.
        // primaryStage.initStyle(StageStyle.UNDECORATED);

        // Cuando se haga clic sobre la ventanan esta se puede arrastrar
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        // Se arrastre la ventada
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        stage.show();
    }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", "dev.yonel.MyPreloader");
        launch(args);
    }

}