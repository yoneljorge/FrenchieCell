package dev.yonel.utils.ui.popup;

import dev.yonel.App;
import dev.yonel.services.Mensajes;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class PopupUtilImp implements PopupUtil {

    private Mensajes mensajes = new Mensajes(PopupUtilImp.class);

    private Stage popupStage;
    private Pane contentPopup;
    private String fxml;
    private Object controller;

    private double x, y;

    public PopupUtilImp() {
    }

    @Override
    public void load() {
        // Ejecutar el código pesado en un hilo separado
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                mensajes.info("Cargando popup");

                try {
                    // La lógica pesada para cargar el FXML y otras tareas que no afectan la UI
                    FXMLLoader loader = App.fxmlLoader(fxml);
                    loader.setController(controller);
                    contentPopup = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Finalizar las tareas del hilo en Platform.runLater() para actualizar la UI
                Platform.runLater(() -> {
                    if (contentPopup != null) {
                        popupStage = new Stage();
                        popupStage.initModality(Modality.WINDOW_MODAL);
                        popupStage.initOwner(App.getStage());
                        popupStage.initStyle(StageStyle.UNDECORATED);

                        // Configurar los eventos de arrastre del popup
                        contentPopup.setOnMousePressed(event -> {
                            x = event.getSceneX();
                            y = event.getSceneY();
                        });

                        contentPopup.setOnMouseDragged(event -> {
                            popupStage.setX(event.getScreenX() - x);
                            popupStage.setY(event.getScreenY() - y);
                        });

                        Scene popupScene = new Scene(contentPopup);

                        popupStage.setScene(popupScene);
                        popupStage.setAlwaysOnTop(true);
                        mensajes.info("Popup cargado");

                        // Mostrar el popup una vez que esté listo
                        //show(null, null);
                    } else {
                        mensajes.err("Error: contentPopup no se pudo cargar.");
                    }
                });
                return null;
            }
        };
        // Ejecutar la tarea en un hilo separado
        Thread load = new Thread(task);
        load.start();

        /* Usamos .join() para asegurarnos de que el hilo termine antes de que continúe la ejecución del programa*/
        try {
            mensajes.info("Esperando a que termine de cargar");
            load.join();
        } catch (Exception e) {
            mensajes.err("Error esperando a que termine de cargar, " + e);
        }


    }

    @Override
    public void load(Runnable loading) {
        mensajes.info("Ejecutando la acción en en método load.");
        loading.run();

        // Ejecutar el código pesado en un hilo separado
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                mensajes.info("Cargando popup");

                try {
                    // La lógica pesada para cargar el FXML y otras tareas que no afectan la UI
                    FXMLLoader loader = App.fxmlLoader(fxml);
                    loader.setController(controller);
                    contentPopup = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Finalizar las tareas del hilo en Platform.runLater() para actualizar la UI
                Platform.runLater(() -> {
                    if (contentPopup != null) {
                        popupStage = new Stage();
                        popupStage.initModality(Modality.WINDOW_MODAL);
                        popupStage.initOwner(App.getStage());
                        popupStage.initStyle(StageStyle.UNDECORATED);

                        // Configurar los eventos de arrastre del popup
                        contentPopup.setOnMousePressed(event -> {
                            x = event.getSceneX();
                            y = event.getSceneY();
                        });

                        contentPopup.setOnMouseDragged(event -> {
                            popupStage.setX(event.getScreenX() - x);
                            popupStage.setY(event.getScreenY() - y);
                        });

                        Scene popupScene = new Scene(contentPopup);

                        popupStage.setScene(popupScene);
                        popupStage.setAlwaysOnTop(true);
                        mensajes.info("Popup cargado");

                        // Mostrar el popup una vez que esté listo
                        //show(null, null);
                    } else {
                        mensajes.err("Error: contentPopup no se pudo cargar.");
                    }
                });
                return null;
            }
        };
        // Ejecutar la tarea en un hilo separado
        Thread load = new Thread(task);
        load.start();

        /* Usamos .join() para asegurarnos de que el hilo termine antes de que continúe la ejecución del programa*/
        try {
            mensajes.info("Esperando a que termine de cargar");
            load.join();
        } catch (Exception e) {
            mensajes.err("Error esperando a que termine de cargar, " + e);
        }


    }

    /**
     * Méoto para mostrar el popup en el centro de la pantalla.
     */
    @Override
    public void show() {

        mensajes.info("Mostrando popup");
        //Mostrar el popup una vez para asegurarse de que los tamaños estén calculados
        popupStage.show();

        // Obtener las dimensiones del contenido una vez que se ha mostrado
        double popupWidth = contentPopup.getWidth();
        double popupHeight = contentPopup.getHeight();

        if (popupWidth == 0 || popupHeight == 0) {
            // Esto puede suceder si el contenido aún no ha sido layouted,
            // por lo que se fuerza un layout pass.
            contentPopup.applyCss();
            contentPopup.layout();
            popupWidth = contentPopup.getWidth();
            popupHeight = contentPopup.getHeight();
        }

        // Obtener el tamaño de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Calcular el centro de la pantalla
        x = (screenWidth - popupWidth) / 2;
        y = (screenHeight - popupHeight) / 2;

        // Posicionar el popup en el centro
        popupStage.setX(x);
        popupStage.setY(y);
        popupStage.close();

        popupStage.showAndWait();

    }

    /**
     * Método para mostrar el popup en el centro de la pantalla y a la vez pasarle una acción a ejecutar, como puede ser
     * el fin de carga.
     *
     * @param action la accion que se desea ejecutar.
     */
    @Override
    public void show(Runnable action) {
        mensajes.info("Corriendo acción.");
        action.run();

        mensajes.info("Mostrando popup");
        //Mostrar el popup una vez para asegurarse de que los tamaños estén calculados
        popupStage.show();

        // Obtener las dimensiones del contenido una vez que se ha mostrado
        double popupWidth = contentPopup.getWidth();
        double popupHeight = contentPopup.getHeight();

        if (popupWidth == 0 || popupHeight == 0) {
            // Esto puede suceder si el contenido aún no ha sido layouted,
            // por lo que se fuerza un layout pass.
            contentPopup.applyCss();
            contentPopup.layout();
            popupWidth = contentPopup.getWidth();
            popupHeight = contentPopup.getHeight();
        }

        // Obtener el tamaño de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Calcular el centro de la pantalla
        x = (screenWidth - popupWidth) / 2;
        y = (screenHeight - popupHeight) / 2;

        // Posicionar el popup en el centro
        popupStage.setX(x);
        popupStage.setY(y);
        popupStage.close();

        popupStage.showAndWait();

    }

    /**
     * Método con el que le damos las coordenadas al popup y lo mostramos.
     *
     * @param x posición en el eje de las x.
     * @param y posición en el eje de las y.
     */
    @Override
    public void show(Double x, Double y) {
        mensajes.info("Mostrando popup");

        //Posicionar en las coordenadas especificadas
        popupStage.setX(x);
        popupStage.setY(y);

        popupStage.showAndWait();
    }

    /**
     * Método con el que le damos las coordenadas al popup y ejecutamos una acción.
     *
     * @param x      coordenada horizontal,
     * @param y      coordenada vertical.
     * @param action acción a ejecutar.
     */
    @Override
    public void show(Double x, Double y, Runnable action) {

        mensajes.info("Mostrando popup");

        //Posicionar en las coordenadas especificadas
        popupStage.setX(x);
        popupStage.setY(y);


        popupStage.showAndWait();
    }

    @Override
    public Runnable close() {
        mensajes.info("Cerrando popup");
        return () -> popupStage.close();
    }

    @Override
    public Runnable close(Runnable action) {
        mensajes.info("Cerrando popup");
        action.run();
        return () -> popupStage.close();
    }

    @Override
    public void setFxml(String fxml) {
        this.fxml = fxml;
    }

    @Override
    public void setController(Object controller) {
        this.controller = controller;
    }

}
