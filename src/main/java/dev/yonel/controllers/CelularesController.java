package dev.yonel.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;

import dev.yonel.App;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.celulares.AgregarCelular;
import dev.yonel.utils.ComboBoxUtil;
import dev.yonel.utils.others.SetVisible;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CelularesController implements Initializable {
    // ****************************************************************** */
    // ***************************COMPONENTES DEL FXML******************* */
    @FXML
    private Button btnVista;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private ComboBox cmbFechaIngresoVista;

    @FXML
    private ComboBox combMarcaVista;

    @FXML
    private ComboBox cmbModeloVista;

    @FXML
    private ComboBox cmbVendidosVista;

    @FXML
    private ComboBox<Marca> cmbMarcaAgregar;
    
    @FXML
    private ComboBox<Modelo> cmbModeloAgregar;



    @FXML
    private Pane pnlAgregar;

    @FXML
    private Pane pnlVista;

    @FXML
    private TextField txtBuscarVista;

    @FXML
    private TextField txtImeiUno;

    @FXML
    private TextField txtImeiDos;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private TextField txtPrecio;

    @FXML
    private VBox vboxItemVista;

    @FXML
    private DatePicker dateFechaInventario;

    @FXML 
    private Label lblEstado;

    // **************************************************** */
    // **************************************************** */


    // ArrayList en el que vamos a almacenar los Pane para una mejor gestión.
    private ArrayList<Pane> listPane = new ArrayList<>();

    AgregarCelular agregarCelular = new AgregarCelular();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Agregamos los pane a la lista y ponemos la vista como el visible.
        listPane.add(pnlAgregar);
        listPane.add(pnlVista);
        SetVisible.This(listPane, pnlVista);

        Node[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            try {
                nodes[i] = App.loadFXML("items/itemCelulares");

                // give the items some effect

                vboxItemVista.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cmbMarcaAgregar.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){//Chequea si el ComboBox gana el foco
                filtrarComboBoxMarca();
            }
        });
    }

    // ******************EVENTO DE LOS BOTONES********** */
    // ************************************************* */

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnVista) { SetVisible.This(listPane, pnlVista);}

        if (actionEvent.getSource() == btnAgregar) { SetVisible.This(listPane, pnlAgregar);}

        if (actionEvent.getSource() == btnGuardar) { btnGuardarLogica();}

        if(actionEvent.getSource() == btnLimpiar){}
    }

    // ****************GUARDADO DE LOS DATOS DEL CELULAR********** */
    // *********************************************************** */

    private void setDatosAgregarCelular() {
        agregarCelular.setMarca(String.valueOf(cmbMarcaAgregar.getValue()));
        agregarCelular.setModelo(String.valueOf(cmbModeloAgregar.getValue()));
        agregarCelular.setImei_Uno(Long.parseLong(txtImeiUno.getText().trim()));
        agregarCelular.setImei_Dos(Long.parseLong(txtImeiDos.getText().trim()));
        agregarCelular.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
        agregarCelular.setFechaInventario(dateFechaInventario.getValue());
        agregarCelular.setObservaciones(txtObservaciones.getText());
    }
    // **********************MÉTODOS***************************** */
    // ********************************************************** */


    private void filtrarComboBoxMarca(){
        List<Marca> listMarca = Marca.getAll(Marca.class);//Cargamos los datos desde la base de datos

        //Quitar este constructor en un futuro porque crea una sobrecarga de trabajo
        ComboBoxUtil<Marca> comboBox = new ComboBoxUtil<>(cmbMarcaAgregar, Marca::getMarca);
        comboBox.filtrar(listMarca);//Llamamos el método y le pasamos los datos
        listMarca.clear();//Limpiamos la lista
    }

    private void btnGuardarLogica(){
        setDatosAgregarCelular();
        if (agregarCelular.saveCelular()) {
            lblEstado.setText("Succes");
            lblEstado.setTextFill(Color.GREEN);
            txtImeiUno.setText("");
            txtImeiDos.setText("");
            txtPrecio.setText("");
            txtObservaciones.setText("");
        }else{
            lblEstado.setText("Error");
            lblEstado.setTextFill(Color.RED);
        }
    }
}
