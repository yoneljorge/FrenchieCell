package dev.yonel.services.controllers.principal;

import dev.yonel.utils.ui.CustomAxis;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

public class ConfigureAreChart {

    private static ConfigureAreChart instance;

    private ConfigureAreChart(){
        instance = this;
    }

    public static ConfigureAreChart getInstance(){
        if(instance == null){
            instance = new ConfigureAreChart();
        }

        return instance;
    }

    private AreaChart<String, Number> areaChart;

    private NumberAxis yAxis;
    private CategoryAxis xAxis;

    public void configure(AreaChart<String, Number> areaChart, CategoryAxis categoryAxis, NumberAxis numberAxis){
        this.areaChart = areaChart;
        this.xAxis = categoryAxis;
        this.yAxis = numberAxis;

        configureAxes();
        configureChart();
        //Ponemos el areChart dentro del ScrollPane
        //PrincipalController.getInstance().getGruopAreaChart().getChildren().add(areaChart);
        this.areaChart.getData().clear();
        this.areaChart.getData().add(SalesDataFetcher.getSerieVentas());
        //this.areaChart.getData().add(SalesDataFetcher.getSerieVentasPorMarca());
        

    }

    private void configureAxes(){
        //yAxis = new NumberAxis();
        //yAxis.setLabel("Cantidad de Ventas");
        CustomAxis.onlyInteger(yAxis);

        //xAxis = new CategoryAxis();
        //xAxis.setLabel("Fechas");
        xAxis.setTickLabelRotation(0);
        xAxis.setTickMarkVisible(true);
        xAxis.setTickLabelsVisible(true);
        xAxis.setTickLabelGap(10);
    }

    private void configureChart(){
        areaChart.setTitle("Ventas Diarias");
        areaChart.setAlternativeRowFillVisible(false);
    }
}
