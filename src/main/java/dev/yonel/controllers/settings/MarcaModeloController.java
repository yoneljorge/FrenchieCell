package dev.yonel.controllers.settings;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.controllers.settings.ServiceMarcasModelos;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class MarcaModeloController implements Initializable{

    @FXML
    private MFXButton btn_AgregarMarca;
    @FXML
    private MFXButton btn_AgregarModelo;
    @FXML
    private MFXButton btn_EliminarMarca;
    @FXML
    private MFXButton btn_EliminarModelo;
    @FXML
    private Label lblEstadoMarcasModelos;
    @FXML
    private ListView<Marca> listView_Marcas;
    @FXML
    private ListView<Modelo> listView_Modelos;
    @FXML
    private VBox node_Root;

    private static MarcaModeloController instance;

    private MarcaModeloController(){
        instance = this;
    }



    public static MarcaModeloController getInstance(){
        if(instance == null){
            instance = new MarcaModeloController();
        }

        return instance;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServiceMarcasModelos.getInstance().configure();
    }
}
