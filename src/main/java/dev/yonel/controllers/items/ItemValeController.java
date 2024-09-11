package dev.yonel.controllers.items;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import dev.yonel.App;
import dev.yonel.models.Vale;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerPromotor;
import dev.yonel.utils.Fecha;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;

public class ItemValeController implements Initializable {

    @FXML
    private Label lblGestor;
    @FXML
    private VBox vboxVale;
    @FXML
    private Label lblCliente;
    @FXML
    private Label lblTelefonoCliente;
    @FXML
    private Label lblMarca;
    @FXML
    private Label lblModelo;
    @FXML
    private Label lblFechaVenta;
    @FXML
    private Label lblComision;
    @FXML
    private CheckBox checkBoxLiquidar;

    private String gestor, cliente, telefonoCliente, marca, modelo,
            fechaVenta, comision;
    private Popup popup;
    private @Setter Stage stage;
    private Vale vale;

    private double x, y;

    public ItemValeController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        this.lblGestor.setText(gestor);
        this.lblCliente.setText(cliente);
        this.lblTelefonoCliente.setText(telefonoCliente);
        this.lblMarca.setText(marca);
        this.lblModelo.setText(modelo);
        this.lblFechaVenta.setText(fechaVenta);
        this.lblComision.setText(comision);

        popup = new Popup();

        try {
            /*
             * Creamos el controller del itemVale y se lo agregamos.
             * Agregamos el VBox al popup.
             */
            FXMLLoader loader = App.fxmlLoader("items/itemValeDetalles");

            ItemValeDetallesController controller = new ItemValeDetallesController(stage, vale);
            /*
             * Le pasamos como parámetro el método para cuando se haga clic en un boton se
             * cierre el pupup.
             */
            controller.setOnCloseAction(() -> hidePopup());

            loader.setController(controller);

            VBox vBox = loader.load();

            /*
             * Bloque de cóidigo para que cuando se presione el popup x tome la distancia
             * que ha sido arrastrado y luego se lo reste a la escena.
             * Esto hace que se pueda arrastrar el popup.
             */
            vBox.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            vBox.setOnMouseDragged(event -> {
                popup.setX(event.getScreenX() - x);
                popup.setY(event.getScreenY() - y);
            });
            popup.getContent().add(vBox);

        } catch (IOException e) {

            e.printStackTrace();
        }

        /*
         * Al hacer clic en el vbox se muestra el popup.
         */
        vboxVale.setOnMouseClicked(event -> {
            /*
             * Esperamos un poco antes de mostrar el popup para dar tiempo a que cargue y no
             * tenga inconsistencias.
             */
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
                showPopup(event.getSceneX(), event.getSceneY(), stage);
            }));
            timeline.play();
        });


        /*
         * Verificamos que se halla cumplido la garantia.
         * La garantia es de 7 días, si el número retornado es mayor a 7 entonces se
         * cumplió la garantia por lo que se puede habilitar el checkBox para liquidar
         * el teléfono.
         */
        if (Fecha.getDiasEntre(vale.getFechaVenta(), LocalDate.now()) >= 7) {
            checkBoxLiquidar.setDisable(false);
        }

        checkBoxLiquidar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (checkBoxLiquidar.isSelected()) {
                ServicePromotoresControllerPromotor.getInstance().addValeLiquidar(vale);
            } else {
                ServicePromotoresControllerPromotor.getInstance().removeValeLiquidar(vale);
            }
        });
    }

    /**
     * Método con el que vamos a igualar cada variable correspondiente a cada label
     * con la informacion del vale para luego mostrarla en la GUI.
     * 
     * @param vale Objeto que representa una instancia del vale que se quiere
     *             mostrar.
     */
    public void setVale(Vale vale) {

        this.vale = vale;

        this.gestor = vale.getPromotor().getNombre();
        this.cliente = vale.getCliente();
        this.telefonoCliente = String.valueOf(vale.getClienteTelefono());
        this.marca = vale.getMarca().getMarca();
        this.modelo = vale.getModelo().getModelo();
        this.fechaVenta = String.valueOf(vale.getFechaVenta());
        this.comision = String.valueOf(vale.getComision());
    }

    /**
     * Método que muestra el popup.
     * Para evitar errores se verifica que no se esté mostrando.
     * 
     * @param x          posición en el eje de las x del mouse.
     * @param y          posición en el eje de las y del mouse.
     * @param ownerStage Stage principal por el que se va a regir el popup.
     */
    private void showPopup(double x, double y, Stage ownerStage) {
        if (!popup.isShowing()) { // Solo mostrar si no está visible
            popup.setAutoHide(true); // Para cerrar el popup al hacer clic fuera
            popup.setX(x - 20);
            popup.setY(y);
            popup.show(ownerStage);
        }

    }

    /**
     * Método con el que vamos a ocultar el popup.
     */
    private void hidePopup() {
        popup.hide(); // Ocultar el popup
    }
}
