package dev.yonel.services.controllers.vales;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dev.yonel.App;
import dev.yonel.controllers.ValesController;
import dev.yonel.controllers.items.ItemValeController;
import dev.yonel.models.Vale;
import dev.yonel.utils.data_access.UtilsHibernate;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
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

    private final List<ItemValeController> listaItemsValesController;

    private ServiceValesControllerVista() {
        instance = this;

        setObject();
        this.listaItemsValesController = new ArrayList<>();
    }

    public static ServiceValesControllerVista getInstance() {
        if (instance == null) {
            instance = new ServiceValesControllerVista();
        }

        return instance;
    }

    /* ################################################## */
    /*
     * Creamos los Objetos que se encuentran en el FXML para luego enlazarlos con
     * los del controlador.
     */
    private FlowPane flowPane_ListaDeItems;

    private void setObject() {
        this.flowPane_ListaDeItems = ValesController.getInstance().getFlowPane_ListaDeItems();
    }

    public void configure() {
        Platform.runLater(() -> {

        });
    }

    /*
     * ###################################################
     * ########## MÉTODOS PARA AGREGAR ITEMS ############
     * ###################################################
     */

    private final SessionFactory sessionFactory = UtilsHibernate.getSessionFactory();
    private @Setter boolean cambioEnInterfaz = true;

    private void setItems(Vale vale) {
        if (vale != null) {
            try {
                VBox vBox;
                FXMLLoader loader = App.fxmlLoader("items/itemVale");
                ItemValeController controller = new ItemValeController();
                controller.setVale(vale);
                controller.setStage(App.getStage());
                loader.setController(controller);
                vBox = loader.load();
                flowPane_ListaDeItems.getChildren().add(vBox);

                listaItemsValesController.add(controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getAllItems() {
            if (cambioEnInterfaz) {
                 //Limpia la interfaz antes de comenzar a agreagar items.
                removeAllItems();

                Vale vale;
                while ((vale = Vale.getAllOneToOne(Vale.class))!= null){
                    setItems(vale);
                }

                cambioEnInterfaz = false;
            }
    }
    /*
     * ###################################################
     * ########## MÉTODOS PARA QUITAR ITEMS##############
     * ###################################################
     */

    private void removeAllItems() {
        flowPane_ListaDeItems.getChildren().clear();
        listaItemsValesController.clear();
    }

    /**
     * Método con el que vamos a obtener la posición en la lista de
     * ItemsValeController del controlador que se pase como argumento.
     * 
     * @param controller el controlador que se le desea buscar la posicón.
     * @return -1 en caso de que no encuentre la posicón, del 0 en adelante la posición en que se encuentra el controlador.
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
        flowPane_ListaDeItems.getChildren().remove(i);
    }

}
