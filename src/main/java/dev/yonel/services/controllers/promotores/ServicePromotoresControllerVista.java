package dev.yonel.services.controllers.promotores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import dev.yonel.App;
import dev.yonel.controllers.PromotoresController;
import dev.yonel.controllers.items.ItemPromotoresController;
import dev.yonel.models.Promotor;
import dev.yonel.services.promotores.ServicePromotor;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Setter;

/**
 * Clase que utiliza el patrón de diseño Singlenton para que solo halla una
 * instancia de esta clase.
 * Esta clase es la encargada del funcionamiento de listar los promotores en el
 * apartado de los promotores o gestores.
 * Esta clase depende de que el controlador la instancie para su funcionamiento.
 */
public class ServicePromotoresControllerVista {

    /*
     * ##############################################
     * ####### APARTADO DEL CONSTRUCTOR ############
     * ##############################################
     */
    /*
     * Se crea una instancia estática de esta clase.
     */
    private static ServicePromotoresControllerVista instance;


    /*
     * Constructor privado para que solo se pueda instanciar esta clase desde
     * dentro.
     */
    private ServicePromotoresControllerVista() {
        instance = this;
    }

    /**
     * Método con el que se obtiene la única instancia de esta clase.
     *
     * @return la instancia de esta clase.
     */
    public static ServicePromotoresControllerVista getInstance() {
        if (instance == null) {
            instance = new ServicePromotoresControllerVista();
        }
        return instance;
    }

    /*
     * ##############################################
     * ####### APARTADO DE LOS OBJETOS ##############
     * ##############################################
     */

    private VBox vBoxItems;
    private RadioButton radioButton_EnGarantia;
    private RadioButton radioButton_PorPagar;
    private RadioButton radioButton_Todos;
    private final ToggleGroup radioButtonsGroup = new ToggleGroup();


    /**
     * Método con el que establecemos los objetos para la instancia actual desde el
     * controlador.
     */

    private void setObjects() {
        this.vBoxItems = PromotoresController.getInstance().getVboxLista_Items();
        this.radioButton_PorPagar = PromotoresController.getInstance().getRadioButton_PorPagar();
        this.radioButton_EnGarantia = PromotoresController.getInstance().getRadioButton_EnGarantia();
        this.radioButton_Todos = PromotoresController.getInstance().getRadioButton_Todos();
    }

    /*
     * Instancia de la clase FiltrarItemsPromotor que se encarga de proporcionar los
     * método para el filtrado de los ItemsPromotor en dependencia de los parámetros
     * promorcionados.
     */
    private FiltrarItemsPromotor filterItems;

    /*
     * Variable que se encarga de proporcionar si han habido cambios en la interfaz,
     * esta es modificada por cada objeto que este encargado de filtrar los items y
     * desde la clase Gatillo.
     */
    private @Setter boolean cambiosEnItems = true;
    private boolean configurado = false;

    public void configure() {
        if (!configurado) {

            this.itemPromotorControllers = new ArrayList<>();

            if (filterItems == null) {
                filterItems = new FiltrarItemsPromotor();
            }

            if (vBoxItems == null) {
                setObjects();
            }

            Platform.runLater(() -> {

                this.radioButton_EnGarantia.setToggleGroup(radioButtonsGroup);
                this.radioButton_PorPagar.setToggleGroup(radioButtonsGroup);
                this.radioButton_Todos.setToggleGroup(radioButtonsGroup);

                radioButton_PorPagar.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        filterItems = new FiltrarItemsPromotor();
                        filtrar(filterItems);
                        filterItems.getAllItems();
                    }
                });

                radioButton_EnGarantia.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        filterItems = new FiltrarItemsPromotor();
                        filtrar(filterItems);
                        filterItems.getAllItems();
                    }
                });
                radioButton_Todos.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        filterItems = new FiltrarItemsPromotor();
                        filtrar(filterItems);
                        filterItems.getAllItems();
                    }
                });
            });
            //Con esto le decimos que no hay necesidad de volver a repetir todo el proceso si ya se ha configurado.
            configurado = true;
        }
    }

    /**
     * Método que comprueba si el vBox es null, para evitar el
     * error de NullPointerException.
     *
     * @return false en caso de que sea null. true en caso contrario.
     */
    public boolean isNotNull() {
        if (vBoxItems == null) {
            return false;
        }
        return true;
    }

    /*
     * ##############################################
     * ####### MÉTODOS PARA AGREAGAR ITEMS ##########
     * ##############################################
     */
    private List<ItemPromotoresController> itemPromotorControllers;

    public void getAllItems() {
        /*Si han habido cambios es que se obtienen los items */
        Platform.runLater(() -> {
            if (cambiosEnItems) {
                removeAllItems();

                Promotor promotor;
                while ((promotor = Promotor.getAllOneToOne(Promotor.class)) != null) {
                    setItems(promotor);
                }
                cambiosEnItems = false;
            }
        });
    }

    public void setItems(Promotor promotor) {
        // Si el promotor no coincide con el filtrado se pasa un null para que no haga
        // nada.
        if (promotor != null) {
            try {
                HBox hbox;
                FXMLLoader loader = App.fxmlLoader("items/itemPromotores");
                ItemPromotoresController controller = new ItemPromotoresController();
                // ----> Todo lo que se le va a agregar al controlador
                controller.setPromotor(new ServicePromotor(promotor));
                loader.setController(controller);
                hbox = loader.load();
                vBoxItems.getChildren().add(hbox);

                itemPromotorControllers.add(controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * ##############################################
     * ####### MÉTODOS PARA QUITAR ITEMS ############
     * ##############################################
     */

    public void removeAllItems() {
        vBoxItems.getChildren().clear();
        itemPromotorControllers.clear();
    }

    public int getIndexItemPromotorController(ItemPromotoresController controller) {
        return itemPromotorControllers.indexOf(controller);
    }

    public void removeItem(int i) {
        vBoxItems.getChildren().remove(i);
    }

    /*
     * ##############################################
     * ################# MÉTODOS ####################
     * ##############################################
     */

    private void filtrar(FiltrarItemsPromotor filter) {
        filter.setEnGarantia(radioButton_EnGarantia.isSelected());
        filter.setPorPagar(radioButton_PorPagar.isSelected());
    }

}
