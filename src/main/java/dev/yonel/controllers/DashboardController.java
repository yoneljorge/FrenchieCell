package dev.yonel.controllers;

import java.net.URL;
import java.util.ArrayList;
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
    private Button btnPackages;

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

    private @Getter static final ArrayList<VBox> listVBox = new ArrayList<>();

    // Declarar los controladores como variables de instancia
    private PrincipalController principalController;
    private CelularesController celularesController;
    private PromotoresController promotoresController;
    private ValesController valesController;

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

        btnGeneral.setOnAction(event -> {
            SetVisible.This(listVBox, principal);
        });

        btnCelulares.setOnAction(event -> {
            SetVisible.This(listVBox, celulares);
        });

        btnPromotores.setOnAction(event -> {
            SetVisible.This(listVBox, promotores);
            PromotoresController.getInstance().goToLista();
        });

        btnVales.setOnAction(event -> {
            SetVisible.This(listVBox, vales);
            ValesController.getInstance().goToLista();
        });

        btnSignout.setOnAction(event -> {
            AlertUtil.advertencia("Desea salir?", null, () -> Platform.exit());
        });

        btnSettings.setOnAction(event -> {

        });
    }
}
