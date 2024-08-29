package dev.yonel.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dev.yonel.services.LoadControllers;
import dev.yonel.utils.AlertUtil;
import dev.yonel.utils.ui.SetVisible;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

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

    private @Setter Stage stage;
    private @Getter static final ArrayList<VBox> listVBox = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        
        PrincipalController principalController = new PrincipalController();
        principalController.setStage(stage);
        stackPane.getChildren().add(principal = LoadControllers.load("viewPrincipal", principalController, stage));

        CelularesController celularesController = new CelularesController();
        celularesController.setStage(stage);
        stackPane.getChildren().add(celulares = LoadControllers.load("viewCelulares", celularesController, stage));
        celulares.setVisible(false);

        PromotoresController promotoresController = new PromotoresController();
        promotoresController.setStage(stage);
        stackPane.getChildren().add(promotores = LoadControllers.load("viewPromotores", promotoresController, stage));
        promotores.setVisible(false);


        ValesController valesController = new ValesController();
        valesController.setStage(stage);
        stackPane.getChildren().add(vales = LoadControllers.load("viewVales", valesController, stage));
        vales.setVisible(false);
    }


    // ********************BOTONES DEL MENU*********** */
    // *********************************************** */

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnGeneral) {
            SetVisible.This(listVBox, principal);
        }

        if (actionEvent.getSource() == btnCelulares) {
            SetVisible.This(listVBox, celulares);
        }

        if (actionEvent.getSource() == btnPromotores) {
            SetVisible.This(listVBox, promotores);
        }

        if (actionEvent.getSource() == btnVales) {
            SetVisible.This(listVBox, vales);
        }

        if (actionEvent.getSource() == btnSignout) {
            AlertUtil.advertencia("Desea salir?", null, () -> Platform.exit());
        }
    }

    // ***********************MÉTODOS***************** */
    // ********************************************** */

}
