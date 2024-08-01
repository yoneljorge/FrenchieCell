package dev.yonel.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PromotoresController implements Initializable{

    @FXML
    private Button btnAccion_Agregar;

    @FXML
    private Button btnAccion_Limpiar;

    @FXML
    private Button btnAccion_Liquidar;

    @FXML
    private Button btnIr_AgregarPromotor;

    @FXML
    private Button btnIr_PromotoresLista;

    @FXML
    private Button btnIr_RegresarLista;

    

    @FXML
    private FlowPane flowPanePromotorVales;



    @FXML
    private Label lblPromotor_EnGarantia;

    @FXML
    private Label lblPromotor_Nombre;

    @FXML
    private Label lblPromotor_PorPagar;

    @FXML
    private Label lblPromotor_Total;

    @FXML
    private Label lblPromotor_Ventas;



    @FXML
    private MenuItem menuItemPromotores_EditarPerfil;

    @FXML
    private MenuItem menuItemPromotores_EliminarPerfil;



    @FXML
    private StackPane stackPane;



    @FXML
    private TextField txtPromotores_Apellidos;
    
    @FXML 
    private TextField txtPromotores_Celular;

    @FXML
    private TextField txtPromotores_Nombre;



    @FXML
    private VBox vboxPromotor_Agregar;

    @FXML
    private VBox vboxPromotor_Lista;

    @FXML
    private VBox vboxPromotor_Promotor;

    @FXML
    private VBox vboxPromotor_ViewItems;

    @FXML
    private VBox Promotores;

    private ArrayList<VBox> listVBox = new ArrayList<>();
    
    public void initialize(URL location, ResourceBundle resources){

        //Agregamos los VBox al ArrayList
        listVBox.add(vboxPromotor_Lista);
        listVBox.add(vboxPromotor_Promotor);
        listVBox.add(vboxPromotor_Agregar);

        //Ponemos visible el VBox donde aparece la lista de promotores
        //setVisibleThis(vboxPromotor_Lista);
    }

    public void handleClicks(ActionEvent event){
        if(event.getSource() == btnIr_AgregarPromotor){ setVisibleThis(vboxPromotor_Agregar);}

        if(event.getSource() == btnIr_PromotoresLista){ setVisibleThis(vboxPromotor_Lista);}

        if(event.getSource() == btnIr_RegresarLista){ setVisibleThis(vboxPromotor_Lista);}
    }

    public void setVisibleThis(VBox vbox){
        for (VBox vBoxElement : listVBox) {
            if(vBoxElement != vbox){
                vBoxElement.setVisible(false);
            }else{
                vBoxElement.setVisible(true);
            }
        }
    }
}
