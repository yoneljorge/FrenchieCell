package dev.yonel.services.controllers.principal;

import javafx.scene.chart.PieChart;

public class PieChart_Marcas {

    private static PieChart_Marcas instance;

    private PieChart_Marcas(){

    }

    private static synchronized void createInstance(){
        instance = new PieChart_Marcas();
    }

    public static PieChart_Marcas getInstance(){
        if(instance == null){
            createInstance();
        }
        return instance;
    }
    
    private PieChart pieChart;

    public void configure(PieChart pieChart){
        this.pieChart = pieChart;

        configurePieChart();
    }

    private void configurePieChart(){
        pieChart.setTitle("Ventas por Marcas");
        //pieChart.setLegendSide(Side.RIGHT); //Poner la leyenda a la derecha
        pieChart.getData().clear();
        pieChart.getData().addAll(SalesDataFetcher.getDataVentasPorMarca());

    }
}
