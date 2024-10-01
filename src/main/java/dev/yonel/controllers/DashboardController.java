package dev.yonel.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dev.yonel.services.LoadControllers;
import dev.yonel.utils.AlertUtil;
import dev.yonel.utils.ui.SetVisible;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class DashboardController implements Initializable {

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
    private @Getter StackPane stackPane;

    private VBox principal;
    private VBox celulares;
    private VBox promotores;
    private VBox vales;
    private VBox settings;

    private @Getter final ArrayList<VBox> listVBox = new ArrayList<>();
    private List<Button> listaBotones = new ArrayList<>();

    private static DashboardController instance;

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

        });

        /*
         * Platform.runLater(() -> {
         * 
         * 
         * 
         * 
         * });
         */

        // Agregamos todos los botones a la lista para una mejor gestión.
        listaBotones.add(btnGeneral);
        listaBotones.add(btnCelulares);
        listaBotones.add(btnPromotores);
        listaBotones.add(btnSettings);
        listaBotones.add(btnSignout);
        listaBotones.add(btnVales);

        // Como la primera ventana que aparece es la principal entonces ponemos
        // el boton general como seleccionado.
        setStyleToBotton(btnGeneral);

        btnGeneral.setOnAction(event -> {
            // Manejar la visibilidad de los VBox en la lista
            SetVisible.This(listVBox, principal);
            setStyleToBotton(btnGeneral);
        });

        btnCelulares.setOnAction(event -> {
            loadCelular();

            SetVisible.This(listVBox, celulares);
            CelularesController.getInstance().goToLista();
            setStyleToBotton(btnCelulares);
        });

        btnPromotores.setOnAction(event -> {
            loadPromotores();

            SetVisible.This(listVBox, promotores);
            PromotoresController.getInstance().goToLista();
            setStyleToBotton(btnPromotores);
        });

        btnVales.setOnAction(event -> {
            loadVales();

            SetVisible.This(listVBox, vales);
            ValesController.getInstance().goToLista();
            setStyleToBotton(btnVales);
        });

        btnSettings.setOnAction(event -> {
            loadSettings();

            SetVisible.This(listVBox, settings);
            setStyleToBotton(btnSettings);
        });

        btnSignout.setOnAction(event -> {
            AlertUtil.advertencia("Desea salir?", null, () -> Platform.exit());
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
                celulares = vbox;
            });
        }
    }

    private void loadPromotores() {
        if (promotores == null) {
            PromotoresController.restartInstance();

            LoadControllers.load("viewPromotores", PromotoresController.getInstance(), "promotores", vbox -> {
                promotores = vbox;
            });
        }
    }

    private void loadVales() {
        if (vales == null) {
            ValesController.restartInstance();

            LoadControllers.load("viewVales", ValesController.getInstance(), "vales", vbox -> {
                vales = vbox;
            });
        }
    }

    private void loadSettings() {
        if (settings == null) {
            SettingsController.restartInstance();

            LoadControllers.load("viewSettings", SettingsController.getInstance(), "settings", vbox -> {
                settings = vbox;
            });
        }
    }
}
