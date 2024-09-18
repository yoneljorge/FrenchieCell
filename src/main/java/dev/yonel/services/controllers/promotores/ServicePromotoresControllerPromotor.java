package dev.yonel.services.controllers.promotores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.yonel.App;
import dev.yonel.controllers.PromotoresController;
import dev.yonel.controllers.items.ItemValeController;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.services.vales.ServiceVales;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class ServicePromotoresControllerPromotor {

    //Variable que almacena la instancia de la clase
    private static ServicePromotoresControllerPromotor instance;

    /**
     * Constructor privado para que solo se pueda cerar la instancia desde dentro de la clase.
     * Con esto logramos de que solo halla una sola instancia de esta clase.
     */
    private ServicePromotoresControllerPromotor() {
        instance = this;

        /*
        Con Platform.runLater() logramos de que ejecute el código que está dentro de
        el luego de que la halla iniciado la aplicación.
         */
        Platform.runLater(() -> {
            //Cargamos los objetos o componentes.
            setObjects();
        });
    }

    /**
     * Método con el que obtenemos la única instancia de la clase.
     *
     * @return la instancia de clase.
     */
    public static ServicePromotoresControllerPromotor getInstance() {
        if (instance == null) {
            instance = new ServicePromotoresControllerPromotor();
        }
        return instance;
    }


    //Controles que tiene la vista.
    private FlowPane promotorVales;
    private MFXButton btnRegresar;
    private MFXButton btnLiquidar;
    private MenuItem editarPerfil;
    private MenuItem eliminarPerfil;
    private Label dineroPorPagar;
    private Label valesPorPagar;
    private Label valesGarantia;
    private Label valesTotal;
    private Label labelPromotor;
    private Label dineroPagado;
    private List<Vale> listVales;
    private List<Vale> listValesLiquidar;
    private List<ItemValeController> itemValePromotorControllers;
    //Instancia del constructor
    private PromotoresController promotoresController = PromotoresController.getInstance();


    private void setObjects() {
        this.listVales = new ArrayList<>();
        this.listValesLiquidar = new ArrayList<>();
        this.itemValePromotorControllers = new ArrayList<>();
        this.promotorVales = promotoresController.getFlowPanePromotorVales();
        this.btnRegresar = promotoresController.getBtnRegresar();
        this.btnLiquidar = promotoresController.getBtnLiquidar();
        this.editarPerfil = promotoresController.getMenuItemEditarPerfil();
        this.eliminarPerfil = promotoresController.getMenuItemEliminarPerfil();
        this.dineroPorPagar = promotoresController.getLblDineroPorPagar();
        this.valesPorPagar = promotoresController.getLblValesPorPagar();
        this.valesGarantia = promotoresController.getLblValesGarantia();
        this.valesTotal = promotoresController.getLblValesTotal();
        this.labelPromotor = promotoresController.getLblPromotor();
        this.dineroPagado = promotoresController.getLblDineroPagado();
    }

    /**
     * Méotodo con el que vamos a configurar los botones y objetos de la vista.
     */
    public void configure() {
        Platform.runLater(() -> {
            btnRegresar.setOnAction(event -> {
                promotoresController.goToLista();
            });

            btnLiquidar.setOnAction(event -> {

            });
        });
    }

    /**
     * Méotodo con el que vamos a cargargar la información a la vista.
     *
     * @param id el id del promtor que se desea cargar los datos.
     */
    public void loadPromotor(Long id) {

        promotoresController.goToPromotor();

        ServicePromotor promotor = new ServicePromotor(Promotor.getById(Promotor.class, id));

        /*
         * Introducimos la información del promotor en el Panel.
         */
        setDatosInPanel(promotor);

        /*
         * Limpiamos el VBox para que no se repitan los vales
         */

        cleanVbox();

        /*
         * Limpiamos la lista y agregamos a la lista los valores correspondientes.
         * Luego comprobamos que la lista no esté vacía y en ese caso entonces se
         * procede a introducir todos los vales en el FlowPane
         * del promotor.
         */
        listVales.clear();

        Vale v;
        while ((v = ServiceVales.findValesByPromotorOneToOne(id)) != null) {
            setItems(v);
        }

        /*
         * Invertimos el orden de los vales para que aparezcan primero los ultimos.
         */

        invertirOrden();
    }

    private void setDatosInPanel(ServicePromotor promotor) {

        // Ingresamos los datos del promotor en cada label.
        labelPromotor.setText(promotor.getNombre() + " " + promotor.getApellidos());
        valesTotal.setText(String.valueOf(promotor.getValesTotal()));
        valesGarantia.setText(String.valueOf(promotor.getValesEnGarantia()));
        valesPorPagar.setText(String.valueOf(promotor.getValesPorPagar()));
        dineroPorPagar.setText(String.valueOf(promotor.getDineroTotalPorPagar()));
        dineroPagado.setText(String.valueOf(promotor.getDineroTotalPagado()));
    }

    private void setItems(Vale vale) {
        // Si el vale no coincide con el filtrado se pasa un null para que no haga
        // nada.
        if (vale != null) {
            try {
                VBox vbox;
                FXMLLoader loader = App.fxmlLoader("items/itemVale");
                ItemValeController controller = new ItemValeController();
                // ----> Todo lo que se le va a agregar al controlador
                controller.setVale(vale);
                controller.setStage(App.getStage());
                loader.setController(controller);
                vbox = loader.load();
                // vBoxItems.getChildren().add(hbox);
                promotorVales.getChildren().add(vbox);

                itemValePromotorControllers.add(controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para limpiar el vbox.
     */
    public void cleanVbox() {
        promotorVales.getChildren().clear();
    }

    /**
     * Método para invertir el orden de los nodos en el VBox
     * Se invierte el orden de las instancias de los controladores de cada Vale.
     */
    public void invertirOrden() {
        // Invertimos el orden de los nodos en el VBox
        ObservableList<Node> children = promotorVales.getChildren();
        List<Node> invertedList = new ArrayList<>(children);
        Collections.reverse(invertedList);
        promotorVales.getChildren().setAll(invertedList);

        // Invertimos el orden de los controles
        List<ItemValeController> invertedItemsValePromotor = new ArrayList<>(itemValePromotorControllers);
        Collections.reverse(invertedItemsValePromotor);
        itemValePromotorControllers = invertedItemsValePromotor;
    }

    /**
     * Método con el cual vamos a agregar desde el controlador del itemValePromotor
     * los vales que se pueden liquidar.
     * En caso de que la lista sea mayor a 0, se activa el boton liquidar.
     *
     * @param vale el vale que se va a liquidar.
     */
    public void addValeLiquidar(Vale vale) {
        listValesLiquidar.add(vale);
        btnLiquidar.setDisable(false);
    }

    /**
     * Método con el cual vamos a remover desde el controlador itemValePromotor los
     * vales que se iban a liquidar.
     * En caso de que la lista sea igual a cero, de desactiva el boton liquidar.
     *
     * @param vale
     */
    public void removeValeLiquidar(Vale vale) {
        listValesLiquidar.remove(vale);
        if (listValesLiquidar.size() == 0) {
            btnLiquidar.setDisable(true);
        }
    }
}
