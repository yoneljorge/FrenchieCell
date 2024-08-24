package dev.yonel.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dev.yonel.App;
import dev.yonel.utils.AlertUtil;
import dev.yonel.utils.others.SetVisible;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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

    private final ArrayList<VBox> listVBox = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        
        try {
            principal = (VBox) App.loadFXML("viewPrincipal");
            stackPane.getChildren().add(principal);
            listVBox.add(principal);

            celulares = (VBox) App.loadFXML("viewCelulares");
            stackPane.getChildren().add(celulares);
            celulares.setVisible(false);
            listVBox.add(celulares);

            promotores = (VBox) App.loadFXML("viewPromotores");
            stackPane.getChildren().add(promotores);
            promotores.setVisible(false);
            listVBox.add(promotores);

            vales = (VBox) App.loadFXML("viewVales");
            stackPane.getChildren().add(vales);
            vales.setVisible(false);
            listVBox.add(vales);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
