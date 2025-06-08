package dev.yonel.controllers.settings;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.App;
import dev.yonel.controllers.DashboardController;
import dev.yonel.services.ConectionChecker;
import dev.yonel.services.ConfigManager;
import dev.yonel.services.clouds.dropbox.DropboxAuthService;
import dev.yonel.services.clouds.dropbox.DropboxUploader;
import dev.yonel.services.controllers.dashboard.TYPE_CLOUD;
import dev.yonel.services.controllers.settings.FillTreeView;
import dev.yonel.utils.AlertUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class BackUpController implements Initializable {

    @FXML
    private MFXButton btn_BackUp;
    @FXML
    private MFXButton btn_Eliminar;
    @FXML
    private MFXButton btn_IniciarSesion;
    @FXML
    private MFXButton btn_Recuperar;
    @FXML
    private MFXButton btn_Recargar;
    @FXML
    private @Getter TreeView<String> treeView;
    @FXML
    private VBox vBox_IniciarSesion;
    @FXML
    private VBox vbox_BackUp_Restaurar;
    @FXML
    private Label lblEstado;

    private static BackUpController instance;

    private String FILE_BASE_DATOS = ConfigManager.getInstance().get("app.base_datos");

    private BackUpController() {
    }

    private static synchronized void createInstance() {
        instance = new BackUpController();
    }

    public static BackUpController getInstance() {
        if (instance == null) {
            createInstance();
        }

        return instance;
    }

    DropboxAuthService dropboxAuthService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dropboxAuthService = DropboxAuthService.getInstance();

        if (dropboxAuthService.isTokenValid()) {
            setVisibleViewBackUp();

            FillTreeView.config(treeView);
        } else {
            setVisibleViewIniciarSesion();
        }

        btn_IniciarSesion.setOnAction(event -> {
            iniciarSesion();
        });

        btn_Recargar.setOnAction(event -> {
            FillTreeView.config(treeView);
        });

        btn_Recuperar.setOnAction(event -> {
            restaurar();
        });

        btn_BackUp.setOnAction(event -> {
            AlertUtil.confirmation(vbox_BackUp_Restaurar.getScene().getWindow(), "Copia de Seguridad",
                    "¿Desea guardar una copia de seguridad de los datos actuales?", () -> backUp());
            // backUp();
        });
    }

    private void iniciarSesion() {

        Thread iniciarSesion = new Thread(() -> {

            // Deshabilitamos el boton para evitar que se le vuelva a dar clic.
            Platform.runLater( () -> btn_IniciarSesion.setDisable(true));

            if (ConectionChecker.hasInternetConnection()) {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            DashboardController.getInstance().showLoadingOverlay();

                            btn_IniciarSesion.setDisable(true);
                            dropboxAuthService.getAccessToken();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();

                        if (dropboxAuthService.isTokenValid()) {
                            // Actualizamos el menu y ponemos de que esta online la nube
                            DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.CLOUD);
                            // Hacemos visible el VBox de Copia de Seguridad
                            setVisibleViewBackUp();
                            // Llenamos el TreeView con la informacion de la nube
                            FillTreeView.config(treeView);

                            Platform.runLater(() -> {
                                DashboardController.getInstance().hideLoadingOverlay();
                                btn_IniciarSesion.setDisable(false);
                            });

                        } else {
                            // Actualizamos el menu y ponemos de que la nube esta offline
                            DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.CLOUD_CROSS);
                            // Hacemos visible el VBox de Inicio de Sesión.
                            setVisibleViewIniciarSesion();

                            Platform.runLater(() -> {

                                DashboardController.getInstance().hideLoadingOverlay();
                                btn_IniciarSesion.setDisable(false);
                                setEstadoError("Error iniciando sesión.");
                            });
                        }
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        DashboardController.getInstance().hideLoadingOverlay();
                        btn_IniciarSesion.setDisable(false);
                        setEstadoError("Tiempo agotado.");
                    }

                };
                task.run();

                Platform.runLater(() -> {
                    btn_IniciarSesion.setDisable(false);
                });
            } else {
                Platform.runLater(() -> {
                    AlertUtil.error(App.getStage().getScene().getWindow(), "ERROR",
                            "No hay conexión a internet.");
                    btn_IniciarSesion.setDisable(false);
                });
            }
        });

        iniciarSesion.start();

        // Esperamos a que se termine de ejecutar el hilo para ejecutar luego el código
        // que sigue, que sería habilitar el botón de nuevo.

        // btn_IniciarSesion.setDisable(false);
    }

    private void backUp() {

        DropboxUploader uploader = new DropboxUploader();
        File localFile = new File(FILE_BASE_DATOS);

        Task<Void> uploadTask = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                DashboardController.getInstance().showLoadingOverlay();
                try {
                    if (uploader.uploadFile(localFile)) {
                        Platform.runLater(() -> setEstadoInformation("Copia de seguridad guardada."));
                    } else {
                        Platform.runLater(() -> setEstadoError("Error guardando copia de seguridad."));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                DashboardController.getInstance().hideLoadingOverlay();
            }
        };

        new Thread(uploadTask).start();

    }

    private void restaurar() {

    }

    /**
     * Método con el cual vamos a pasar mensajes de información al Label de estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoInformation(String estado) {
        lblEstado.setText(estado);
        lblEstado.getStyleClass().add("label");

        PauseTransition pause = new PauseTransition(
                javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            lblEstado.setText("");
        });
        pause.play();
    }

    /**
     * Método con el que vamos a pasar mensajes de error al Label de estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoError(String estado) {
        lblEstado.setText(estado);
        lblEstado.getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(
                javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            lblEstado.setText(null);
        });
        pause.play();
    }

    public void setVisibleViewBackUp() {
        vbox_BackUp_Restaurar.setVisible(true);
        vBox_IniciarSesion.setVisible(false);
    }

    public void setVisibleViewIniciarSesion() {
        vBox_IniciarSesion.setVisible(true);
        vbox_BackUp_Restaurar.setVisible(false);
    }

    public void setButtonsDisable(Boolean var) {
        btn_BackUp.setDisable(var);
        btn_Eliminar.setDisable(var);
        btn_Recargar.setDisable(var);
        btn_Recuperar.setDisable(var);
    }
}
