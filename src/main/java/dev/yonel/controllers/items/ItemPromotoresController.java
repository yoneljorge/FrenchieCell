package dev.yonel.controllers.items;

import java.net.URL;
import java.util.ResourceBundle;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

public class ItemPromotoresController  implements Initializable{

    @FXML
    private HBox itemPromotor;
    @FXML 
    private Label labelNombre;
    @FXML
    private Label labelNumeroCelular;
    @FXML
    private Label labelVentas;
    @FXML
    private Label labelEnGarantia;
    @FXML
    private Label labelPorPagar;
    @FXML
    private Label labelTotal;
    @FXML
    private MFXButton btnVer;

    private String nombre, celular, ventas, enGarantia, porPagar, total;

    public ItemPromotoresController(){
    }

    @Override
    public void initialize(URL location, ResourceBundle resource){
        this.labelNombre.setText(nombre);
        this.labelNumeroCelular.setText(celular);
        this.labelVentas.setText(ventas);
        this.labelEnGarantia.setText(enGarantia);
        this.labelPorPagar.setText(porPagar);
        this.labelTotal.setText(total);

        itemPromotor.setOnMouseClicked(event -> {
            System.out.println("me estas haciendo clic");
        });
    }

    //public void setPromotor()
}
