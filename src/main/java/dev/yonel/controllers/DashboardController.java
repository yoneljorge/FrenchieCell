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
    private StackPane stackPane;

    private VBox principal;
    private VBox celulares;
    private VBox promotores;
    private VBox vales;
    private VBox settings;

    private @Getter
    static final ArrayList<VBox> listVBox = new ArrayList<>();
    private List<Button> listaBotones = new ArrayList<>();

    // Declarar los controladores como variables de instancia
    private PrincipalController principalController;
    private CelularesController celularesController;
    private PromotoresController promotoresController;
    private ValesController valesController;
    private SettingsController settingsController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        principalController = new PrincipalController();
        stackPane.getChildren().add(principal = LoadControllers.load("viewPrincipal", principalController));

        //boolean celularesBoolean = true;
        celularesController = CelularesController.getInstance();
        stackPane.getChildren().add(celulares = LoadControllers.load("viewCelulares", celularesController));
        celulares.setVisible(false);


        promotoresController = PromotoresController.getInstance();
        stackPane.getChildren().add(promotores = LoadControllers.load("viewPromotores", promotoresController));
        promotores.setVisible(false);

        valesController = ValesController.getInstance();
        stackPane.getChildren().add(vales = LoadControllers.load("viewVales", valesController));
        vales.setVisible(false);

        settingsController = SettingsController.getInstance();
        stackPane.getChildren().add(settings = LoadControllers.load("viewSettings", settingsController));
        settings.setVisible(false);


        //Agregamos todos los botones a la lista para una mejor gestión.
        listaBotones.add(btnGeneral);
        listaBotones.add(btnCelulares);
        listaBotones.add(btnPromotores);
        listaBotones.add(btnSettings);
        listaBotones.add(btnSignout);
        listaBotones.add(btnVales);

        //Como la primera ventana que aparece es la principal entonces ponemos
        // el boton general como seleccionado.
        setStyleToBotton(btnGeneral);
        
        btnGeneral.setOnAction(event -> {
            SetVisible.This(listVBox, principal);
            setStyleToBotton(btnGeneral);
        });

        btnCelulares.setOnAction(event -> {
            SetVisible.This(listVBox, celulares);
            setStyleToBotton(btnCelulares);
        });

        btnPromotores.setOnAction(event -> {
            SetVisible.This(listVBox, promotores);
            PromotoresController.getInstance().goToLista();
            setStyleToBotton(btnPromotores);
        });

        btnVales.setOnAction(event -> {
            SetVisible.This(listVBox, vales);
            ValesController.getInstance().goToLista();
            setStyleToBotton(btnVales);
        });

        btnSignout.setOnAction(event -> {
            AlertUtil.advertencia("Desea salir?", null, () -> Platform.exit());
        });

        btnSettings.setOnAction(event -> {
            SetVisible.This(listVBox, settings);
            setStyleToBotton(btnSettings);
        });
    }

    private void setStyleToBotton(Button boton) {
        for (Button b :
                listaBotones) {
            if (b.equals(boton)) {
                b.getStyleClass().clear();
                b.getStyleClass().add("button-seleccionado");
            } else {
                b.getStyleClass().clear();
                b.getStyleClass().add("button");
            }
        }
    }
}
