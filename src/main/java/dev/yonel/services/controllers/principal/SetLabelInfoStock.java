package dev.yonel.services.controllers.principal;

import java.util.Map;

import javafx.scene.control.Label;

public class SetLabelInfoStock {

    private static SetLabelInfoStock instance;

    private SetLabelInfoStock(){

    }

    private static synchronized void createInstance(){
        instance = new SetLabelInfoStock();
    }

    public static SetLabelInfoStock getInstance(){
        if(instance == null){
            createInstance();
        }

        return instance;
    }

    private Label labelEnStock;
    private Label labelImporte;
    private Label labelEnGarantia;
    private Label labelPorPagar;

    public void configure(Label enStock, Label importe, Label enGarantia, Label porPagar){
        this.labelEnStock = enStock;
        this.labelImporte = importe;
        this.labelEnGarantia=enGarantia;
        this.labelPorPagar = porPagar;

        setInfo();
    }

    private void setInfo(){
        Map<String, String> info = InfoCelulares.getInstance().infoStock();
        
        labelEnStock.setText(info.get("inStock"));
        labelImporte.setText(info.get("importe"));
        labelEnGarantia.setText(info.get("enGarantia"));
        labelPorPagar.setText(info.get("porPagar"));
    }
}
