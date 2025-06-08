package dev.yonel.services.controllers.principal;

import javafx.scene.chart.BarChart;

public class BarChart_VentasMensuales {

    private static BarChart_VentasMensuales instance;

    private BarChart_VentasMensuales(){

    }

    private static synchronized void createInstance(){
        if(instance == null){
            instance = new BarChart_VentasMensuales();
        }
    }

    public static BarChart_VentasMensuales getInstance(){
        if(instance == null){
            createInstance();
        }
        return instance;
    }

    /* */
    private BarChart<String, Number> barChart;

    public void configure(BarChart<String, Number> barChart){
        this.barChart = barChart;
        configureBarChart();
    }

    private void configureBarChart(){
        barChart.setTitle("Venta Mensual");
        barChart.getData().add(SalesDataFetcher.getSerieVentasPorMarca());
        barChart.setCategoryGap(20);
    }
}
