package dev.yonel.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import dev.yonel.App;
import dev.yonel.services.controllers.dashboard.Sumary;
import dev.yonel.services.controllers.dashboard.TYPE_CLOUD;
import dev.yonel.utils.AlertUtil;
import dev.yonel.utils.FileChooserUtil;
import dev.yonel.utils.ui.SetVisible;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class DashboardController implements Initializable {

    @FXML
    private StackPane root;
    @FXML
    private HBox mainContent;
    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnGeneral;
    @FXML
    private Button btnCelulares;
    @FXML
    private Button btnPromotores;
    @FXML
    private Button btnVales;
    @FXML
    private Button btnSettings;
    @FXML
    private Button btnSignout;
    @FXML
    private Button btnSumario;

    @FXML
    private @Getter StackPane stackPane;

    @FXML
    private StackPane stackPaneCloud;
    @FXML
    private VBox vbox_cloud;
    @FXML
    private VBox vbox_cloudCross;
    @FXML
    private VBox vbox_cloudDownload;
    @FXML
    private VBox vbox_cloudSync;
    @FXML
    private VBox vbox_cloudUpload;
    @FXML
    private VBox vbox_conected;
    @FXML
    private VBox vbox_disconected;

    /*
     * Nodos en los cuales se van a ir almacenando las interfaces de las vistas
     */
    private Node principal;
    private Node celulares;
    private Node promotores;
    private Node vales;
    private Node settings;

    /*
     * Guardamos la vista para el overlyLoading
     */
    private StackPane loadingOverlay;

    /*
     * Lista donde vamos a ir almacenando los nodos de las vistas
     */
    private @Getter final ArrayList<Node> listNodes = new ArrayList<>();
    /*
     * Lista donde vamos a ir almacenando los botones
     */
    private List<Button> listaBotones = new ArrayList<>();

    /*
     * Lista donde almacenamos los vbox que tienen las imagenes de la nube y
     * conexión.
     */
    private List<VBox> listVBoxClouds = new ArrayList<>();

    /**
     * Única instancia de esta clase.
     */
    private static DashboardController instance;

    /**
     * Constructor privado para que solo se pueda acceder desde dentro de esta
     * clase.
     */
    private DashboardController() {
        instance = this;
    }

    public static DashboardController getInstance() {
        if (instance == null) {
            instance = new DashboardController();
        }

        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(() -> {
            LoadControllers.load("viewPrincipal", PrincipalController.getInstance(), "principal", vbox -> {
                principal = vbox;
            });
            LoadControllers.load("viewCelulares", CelularesController.getInstance(),
                    "celulares", vbox -> {
                        celulares = vbox;
                        celulares.setVisible(false);
                    });
            LoadControllers.load("viewPromotores", PromotoresController.getInstance(),
                    "promotores", vbox -> {
                        promotores = vbox;
                        promotores.setVisible(false);
                    });
            LoadControllers.load("viewVales", ValesController.getInstance(), "vales",
                    vbox -> {
                        vales = vbox;
                        vales.setVisible(false);
                    });
            LoadControllers.load("viewSettings", SettingsController.getInstance(),
                    "settings", vbox -> {
                        settings = vbox;
                        settings.setVisible(false);
                    });

            /*
             * Cargamos el panel LoadingOverlay.
             */
            try {
                FXMLLoader loader = App.fxmlLoader("viewLoadingOverlay");
                loader.setController(LoadingOverlayController.getInstance());
                loadingOverlay = loader.load();
                root.getChildren().add(loadingOverlay);
                StackPane.setAlignment(loadingOverlay, javafx.geometry.Pos.CENTER);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        // Agregamos todos los botones a la lista para una mejor gestión.
        listaBotones.addAll(Arrays.asList(
                btnGeneral,
                btnCelulares,
                btnPromotores,
                btnSettings,
                btnSignout,
                btnVales));

        // Agregamos todos los vbox relacionados con la nube en una lista
        listVBoxClouds.addAll(Arrays.asList(
                vbox_conected,
                vbox_disconected,
                vbox_cloud,
                vbox_cloudCross,
                vbox_cloudDownload,
                vbox_cloudSync,
                vbox_cloudUpload));

        // Como la primera ventana que aparece es la principal entonces ponemos
        // el boton general como seleccionado.
        setStyleToBotton(btnGeneral);

        btnGeneral.setOnAction(event -> {
            // Manejar la visibilidad de los VBox en la lista
            SetVisible.This(listNodes, principal);
            setStyleToBotton(btnGeneral);
        });

        btnCelulares.setOnAction(event -> {
            loadCelular();

            SetVisible.This(listNodes, celulares);
            CelularesController.getInstance().goToLista();
            setStyleToBotton(btnCelulares);
        });

        btnPromotores.setOnAction(event -> {
            loadPromotores();

            SetVisible.This(listNodes, promotores);
            PromotoresController.getInstance().goToLista();
            setStyleToBotton(btnPromotores);
        });

        btnVales.setOnAction(event -> {
            loadVales();

            SetVisible.This(listNodes, vales);
            ValesController.getInstance().goToLista();
            setStyleToBotton(btnVales);
        });

        btnSettings.setOnAction(event -> {
            loadSettings();

            SetVisible.This(listNodes, settings);
            setStyleToBotton(btnSettings);
        });

        btnSignout.setOnAction(event -> {
            AlertUtil.advertencia("Desea salir?", null, () -> Platform.exit());
        });

        btnSumario.setOnAction(event -> {
            saveSumary();
        });

    }

    private void setStyleToBotton(Button boton) {
        for (Button b : listaBotones) {
            if (b.equals(boton)) {
                b.getStyleClass().clear();
                b.getStyleClass().add("button-seleccionado");
            } else {
                b.getStyleClass().clear();
                b.getStyleClass().add("button");
            }
        }
    }

    private void loadCelular() {
        if (celulares == null) {
            CelularesController.restartInstance();

            LoadControllers.load("viewCelulares", CelularesController.getInstance(), "celulares", vbox -> {
                celulares = (VBox) vbox;
            });
        }
    }

    private void loadPromotores() {
        if (promotores == null) {
            PromotoresController.restartInstance();

            LoadControllers.load("viewPromotores", PromotoresController.getInstance(), "promotores", vbox -> {
                promotores = (VBox) vbox;
            });
        }
    }

    private void loadVales() {
        if (vales == null) {
            ValesController.restartInstance();

            LoadControllers.load("viewVales", ValesController.getInstance(), "vales", vbox -> {
                vales = (VBox) vbox;
            });
        }
    }

    private void loadSettings() {
        if (settings == null) {
            SettingsController.restartInstance();

            LoadControllers.load("viewSettings", SettingsController.getInstance(), "settings", vbox -> {
                settings = (VBox) vbox;
            });
        }
    }

    private void saveSumary() {
        FileChooserUtil fileChooserUtil = new FileChooserUtil();
        fileChooserUtil.getSaveDialog(Sumary.getSumary());
    }

    public void showLoadingOverlay() {
        LoadingOverlayController.getInstance().show(mainContent);
    }

    public void hideLoadingOverlay() {
        LoadingOverlayController.getInstance().hide(mainContent);
    }

    public void setTypeCloud(TYPE_CLOUD type) {
        switch (type.getType()) {
            case "Conected" -> {
                SetVisible.This(listVBoxClouds, vbox_conected);
                break;
            }

            case "Disconected" -> {
                SetVisible.This(listVBoxClouds, vbox_disconected);
                break;
            }

            case "Cloud Cross" -> {
                SetVisible.This(listVBoxClouds, vbox_cloudCross);
                break;
            }

            case "Cloud Sync" -> {
                SetVisible.This(listVBoxClouds, vbox_cloudSync);
                break;
            }

            case "Download" -> {
                SetVisible.This(listVBoxClouds, vbox_cloudDownload);
                break;
            }

            case "Upload" -> {
                SetVisible.This(listVBoxClouds, vbox_cloudUpload);
                break;
            }

            case "Cloud" -> {
                SetVisible.This(listVBoxClouds, vbox_cloud);
                break;
            }

            default -> {
                break;
            }
        }
    }
}
