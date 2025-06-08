package dev.yonel.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.services.controllers.principal.ConfigureAreChart;
import dev.yonel.services.controllers.principal.ConfigureListOldCelulares;
import dev.yonel.services.controllers.principal.PieChart_Marcas;
import dev.yonel.services.controllers.principal.SetLabelInfoStock;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

public class PrincipalController implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private AreaChart<String, Number> areaChart_VentasDiarias;
    @FXML
    private CategoryAxis categoryAxis;
    @FXML
    private NumberAxis numberAxis;
    @FXML
    private PieChart pieChart_Marcas;
    @FXML
    private Label labelEnStock;
    @FXML
    private Label labelImporte;
    @FXML
    private Label labelPorPagar;
    @FXML
    private Label labelEnGarantia;
    @FXML
    private ListView<String> listViewOldCelulares;

    private @Setter Stage stage;

    private static PrincipalController instance;

    private PrincipalController() {

    }

    public static PrincipalController getInstance() {
        if (instance == null) {
            instance = new PrincipalController();
        }

        return instance;
    }

    public static void restartInstance() {
        instance = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        load();

        root.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                load();
            }

        });
    }

    private void load() {
        ConfigureAreChart.getInstance().configure(areaChart_VentasDiarias, categoryAxis, numberAxis);
        PieChart_Marcas.getInstance().configure(pieChart_Marcas);
        SetLabelInfoStock.getInstance().configure(labelEnStock, labelImporte, labelEnGarantia, labelPorPagar);
        ConfigureListOldCelulares.getInstnace().configure(listViewOldCelulares);
    }

}
