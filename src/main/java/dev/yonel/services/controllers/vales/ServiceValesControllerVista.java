package dev.yonel.services.controllers.vales;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.yonel.App;
import dev.yonel.controllers.ValesController;
import dev.yonel.controllers.items.ItemValeController;
import dev.yonel.models.Vale;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import lombok.Setter;

/**
 * Clase que utiliza el patrón de diseño Singlenton para que solo halla una sola
 * instancia de esta clase.
 * Esta clase es la encargada del funcionamiento de
 * la vista del apartado de vales.
 * Esta clase depende de que el controlador la instancie para su funcionamiento.
 *
 * @author Yonel J. Sánchez
 */
public class ServiceValesControllerVista {

    /*
     * ##############################################
     * ####### APARTADO DEL CONSTRUCTOR ############
     * ##############################################
     */

    /*
     * Se crea una instancia estática de esta clase.
     * Se crea un constructor privado para que solo se pueda tener acceso desde el
     * interior de la clase.
     */
    private static ServiceValesControllerVista instance;

    private List<ItemValeController> listaItemsValesController;

    private ServiceValesControllerVista() {
        instance = this;

        this.listaItemsValesController = new ArrayList<>();
    }

    public static ServiceValesControllerVista getInstance() {
        if (instance == null) {
            instance = new ServiceValesControllerVista();
        }

        return instance;
    }

    /*
     * ###################################################
     * ########## MÉTODOS PARA AGREGAR ITEMS ############
     * ###################################################
     */

    /*
     * Variable con la cual vamos a monitoriar los cambios de la interfaz
     */
    private @Setter boolean cambioEnInterfaz = true;

    /**
     * Método con el que vamos a insertar en la interfaz item de tipo vale.
     * Este método crea un objeto de tipo FXMLLoader mediante el fxml "itemVale".
     * Crea una instancia del controlador de ese fxml y lo establece en el loader,
     * además de establecer las propiedades del controlador.
     * Agregar las respectivas listas el VBox del fxml y el controlador.
     *
     * @param vale
     */
    private void setItems(Vale vale) {

        if (vale != null) {
            ItemValeController controller = new ItemValeController();
            controller.setVale(vale);
            controller.hideCheckBox();
            controller.setStage(App.getStage());
            VBox vBox;
            try {
                FXMLLoader loader = App.fxmlLoader("items/itemVale");
                // Quitamos el checkBox para liquidar o pagar debido a que se va a liquidar
                // desde la interfaz del promotor o gestor.

                loader.setController(controller);
                vBox = loader.load();
                ValesController.getInstance().getFlowPane_ListaDeItems().getChildren().add(vBox);

                listaItemsValesController.add(controller);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getAllItems() {
        if (cambioEnInterfaz) {

            // Limpia la interfaz antes de comenzar a agreagar items.
            removeAllItems();

            /*
             * Vale vale;
             * while ((vale = Vale.getAllOneToOne(Vale.class)) != null) {
             * setItems(vale);
             * }
             * 
             * // Invertimos el orden de los vales para que aparezcan primero los últimos
             * invertirOrden();
             * cambioEnInterfaz = false;
             * 
             */

            // Crea un hilo separado para obtener y procesar los items
            new Thread(() -> {
                Vale vale;

                // Itera sobre todos los vales
                while ((vale = Vale.getAllOneToOne(Vale.class)) != null) {
                    final Vale currentVale = vale;

                    // Ejecuta el método setItems() en el hilo de la UI
                    Platform.runLater(() -> {
                        setItems(currentVale);
                    });
                }

                // Una vez que todos los items se hayan agregado, invierte el orden en el hilo
                // de la UI
                Platform.runLater(() -> {
                    invertirOrden();
                    cambioEnInterfaz = false;
                });
            }).start(); // Inicia el hilo separad
        }
    }
    /*
     * ###################################################
     * ########## MÉTODOS PARA QUITAR ITEMS##############
     * ###################################################
     */

    private void removeAllItems() {
        ValesController.getInstance().getFlowPane_ListaDeItems().getChildren().clear();
        listaItemsValesController.clear();
    }

    /**
     * Método con el que vamos a obtener la posición en la lista de
     * ItemsValeController del controlador que se pase como argumento.
     *
     * @param controller el controlador que se le desea buscar la posicón.
     * @return -1 en caso de que no encuentre la posicón, del 0 en adelante la
     *         posición en que se encuentra el controlador.
     */
    public int getIndexItem(ItemValeController controller) {
        return listaItemsValesController.indexOf(controller);
    }

    /**
     * Método que quita del FlowPane el item que se encuentre en la posición que se
     * pase como parámetro.
     *
     * @param i la posición del item que se desea quitar.
     */
    public void removeItem(int i) {
        ValesController.getInstance().getFlowPane_ListaDeItems().getChildren().remove(i);
    }

    /*
     * ###################################################
     * ################## OTHERS METODS ##################
     * ###################################################
     */

    private void invertirOrden() {
        // Invertimos el orden de los nodos en el FlowPane
        ObservableList<Node> children = ValesController.getInstance().getFlowPane_ListaDeItems().getChildren();
        List<Node> invertedList = new ArrayList<>(children);
        Collections.reverse(invertedList);
        ValesController.getInstance().getFlowPane_ListaDeItems().getChildren().setAll(invertedList);

        // Invertimos el orden de los controles en la lista de controles
        List<ItemValeController> invertedItemVale = new ArrayList<>(listaItemsValesController);
        Collections.reverse(invertedList);
        listaItemsValesController.clear();
        listaItemsValesController.addAll(invertedItemVale);
    }

    /*
     * Agregamos una varibale booleana la cual va a ser true si ya hay un popup de
     * un valeDetalles abierto.
     * La misma va a estar a la espera de que se establezca en true o false en
     * dependencia de si se abre o se cierra.
     */
}
