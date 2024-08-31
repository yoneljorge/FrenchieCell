package dev.yonel.controllers.items;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import dev.yonel.models.Vale;
import dev.yonel.utils.Fecha;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

public class ItemValeDetallesController implements Initializable{

    @FXML
    private VBox vBox;
    @FXML
    private Label lblPromotor;
    @FXML
    private Label lblCliente;
    @FXML
    private Label lblTelefonoCliente;
    @FXML
    private Label lblImei;
    @FXML
    private Label lblMarca;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblPrecio;
    @FXML
    private Label lblPrecioMensajeria;
    @FXML
    private Label lblDireccion;
    @FXML
    private Label lblFechaVenta;
    @FXML
    private Label lblFechaGarantia;
    @FXML
    private Label lblDiasGarantia;
    @FXML
    private Label lblComision;
    @FXML
    private MFXButton btnEditar;
    @FXML
    private MFXButton btnEliminar;
    @FXML
    private MFXButton btnCerrar;

    private @Setter Stage stage;

    private Vale vale;

    private Runnable onCloseAction;
    
    public ItemValeDetallesController(Stage stage, Vale vale){
        this.stage = stage;
        this.vale = vale;
    }

    @Override
    public void initialize(URL location, ResourceBundle resource){
        this.lblPromotor.setText(vale.getPromotor().getNombre());
        this.lblCliente.setText(vale.getCliente());
        this.lblTelefonoCliente.setText(String.valueOf(vale.getClienteTelefono()));
        this.lblImei.setText(String.valueOf(vale.getImei()));
        this.lblMarca.setText(vale.getMarca().getMarca());
        this.lblModelo.setText(vale.getModelo().getModelo());
        this.lblPrecio.setText(String.valueOf(vale.getPrecio()));
        this.lblPrecioMensajeria.setText(String.valueOf(vale.getCostoMensajeria()));
        this.lblDireccion.setText(vale.getDireccion());
        this.lblFechaVenta.setText(String.valueOf(vale.getFechaVenta()));
        this.lblFechaGarantia.setText(String.valueOf(vale.getFechaGarantia()));

        if(Fecha.getDiasEntre(LocalDate.now(), vale.getFechaGarantia()) > 0){
            this.lblDiasGarantia.setText(String.valueOf(Fecha.getDiasEntre(LocalDate.now(), vale.getFechaGarantia())));
        }else{
            this.lblDiasGarantia.setText("0");
        }
        this.lblComision.setText(String.valueOf(vale.getComision()));

        btnCerrar.setOnAction(event -> {
            onCloseAction.run();
        });
    }

    public void setOnCloseAction(Runnable onCloseAction){
        this.onCloseAction = onCloseAction;
    }

}
