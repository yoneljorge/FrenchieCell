package dev.yonel.services.controllers.promotores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dev.yonel.App;
import dev.yonel.controllers.PromotoresController;
import dev.yonel.controllers.items.ItemValePromotorController;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.ServiceLista;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.utils.ui.SetVisible;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

public class ServicePromotoresControllerPromotor {



    private static FlowPane promotorVales;
    private MFXButton btnRegresar;
    private static MFXButton btnLiquidar;
    private MenuItem editarPerfil;
    private MenuItem eliminarPerfil;
    private static Label dineroPorPagar;
    private static Label valesPorPagar;
    private static Label valesGarantia;
    private static Label valesTotal;
    private static Label labelPromotor;
    private static Label dineroPagado;

    private @Setter static Long idPromotor;
    private static List<Vale> listVales = new ArrayList<>();
    private static List<Vale> listValesLiquidar = new ArrayList<>();
    private static List<ItemValePromotorController> itemValePromotorControllers = new ArrayList<>();

    private @Setter static Stage stage;

    @SuppressWarnings("static-access")
    public ServicePromotoresControllerPromotor(Map<String, Object> list, Stage stage) {
        this.promotorVales = (FlowPane) list.get("promotorVales");
        this.btnRegresar = (MFXButton) list.get("regresar");
        this.btnLiquidar = (MFXButton) list.get("liquidar");
        this.editarPerfil = (MenuItem) list.get("editar");
        this.eliminarPerfil = (MenuItem) list.get("eliminar");
        this.dineroPorPagar = (Label) list.get("dineroPorPagar");
        this.valesPorPagar = (Label) list.get("valesPorPagar");
        this.valesGarantia = (Label) list.get("valesGarantia");
        this.valesTotal = (Label) list.get("valesTotal");
        this.labelPromotor = (Label) list.get("promotor");
        this.dineroPagado = (Label) list.get("dineroPagado");
        this.stage = stage;
    }

    public void configure() {
        btnRegresar.setOnAction(event -> {
            PromotoresController.goToLista();
        });

        btnLiquidar.setOnAction(event -> {

        });
    }

    private void liquidar(){
        
    }
    /*********************************************
     * **********MÉTODOS ESTÁTICOS****************
     *********************************************/

    public static void loadPromotor(Long i) {

        PromotoresController.goToPromotor();

        ServicePromotor promotor = new ServicePromotor(Promotor.getById(Promotor.class, i));

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
        listVales.addAll(ServiceLista.getListValesByPromotor(i));
        if (!listVales.isEmpty()) {
            for (Vale v : listVales) {
                setItems(v);
            }
        }

        /*
         * Invertimos el orden de los vales para que aparezcan primero los ultimos.
         */

        invertirOrden();
    }

    private static void setDatosInPanel(ServicePromotor promotor) {

        // Ingresamos los datos del promotor en cada label.
        labelPromotor.setText(promotor.getNombre() + " " + promotor.getApellidos());
        valesTotal.setText(String.valueOf(promotor.getValesTotal()));
        valesGarantia.setText(String.valueOf(promotor.getValesEnGarantia()));
        valesPorPagar.setText(String.valueOf(promotor.getValesPorPagar()));
        dineroPorPagar.setText(String.valueOf(promotor.getDineroTotalPorPagar()));
        dineroPagado.setText(String.valueOf(promotor.getDineroTotalPagado()));
    }

    private static void setItems(Vale vale) {
        // Si el vale no coincide con el filtrado se pasa un null para que no haga
        // nada.
        if (vale != null) {
            try {
                VBox vbox;
                FXMLLoader loader = App.fxmlLoader("items/itemValePromotor");
                ItemValePromotorController controller = new ItemValePromotorController();
                // ----> Todo lo que se le va a agregar al controlador
                controller.setVale(vale);
                controller.setStage(stage);
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
    public static void cleanVbox() {
        //vboxLista.getChildren().clear();
    }

    /**
     * Método para invertir el orden de los nodos en el VBox
     * Se invierte el orden de las instancias de los controladores de cada Vale.
     */
    public static void invertirOrden() {
        // Invertimos el orden de los nodos en el VBox
        //ObservableList<Node> children = vboxLista.getChildren();
        //List<Node> invertedList = new ArrayList<>(children);
       // Collections.reverse(invertedList);
        //vboxLista.getChildren().setAll(invertedList);

        // Invertimos el orden de los nodos en la lista
        List<ItemValePromotorController> invertedItemsValePromotor = new ArrayList<>(itemValePromotorControllers);
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
    public static void addValeLiquidar(Vale vale) {
        listValesLiquidar.add(vale);
        if (listValesLiquidar.size() > 0) {
            btnLiquidar.setDisable(false);
        }
    }

    /**
     * Método con el cual vamos a remover desde el controlador itemValePromotor los
     * vales que se iban a liquidar.
     * En caso de que la lista sea igual a cero, de desactiva el boton liquidar.
     * 
     * @param vale
     */
    public static void removeValeLiquidar(Vale vale) {
        listValesLiquidar.remove(vale);
        if (listValesLiquidar.size() == 0) {
            btnLiquidar.setDisable(true);
        }
    }

    
}
