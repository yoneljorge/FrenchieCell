package dev.yonel.services.controllers.promotores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.yonel.App;
import dev.yonel.controllers.PromotoresController;
import dev.yonel.controllers.items.ItemPromotoresController;
import dev.yonel.models.Promotor;
import dev.yonel.services.promotores.ServicePromotor;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * <p>
 * Clase que utiliza el patrón de diseño Singlenton para que solo halla una
 * instancia de esta clase.
 * Esta clase es la encargada del funcionamiento de listar los promotores en el
 * apartado de los promotores o gestores.
 * Esta clase depende de que el controlador la instancie para su funcionamiento.
 * </p>
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

    public static void restartInstance() {
        instance = null;
    }

    private final ToggleGroup radioButtonsGroup = new ToggleGroup();

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
    private boolean configurado = false;

    public void configure() {
        if (!configurado) {

            this.itemPromotorControllers = new ArrayList<>();

            if (filterItems == null) {
                filterItems = new FiltrarItemsPromotor();
            }

            /*
             * if (PromotoresController.getInstance().getVboxLista_Items() == null) {
             * setObjects();
             * }
             */

            Platform.runLater(() -> {
                PromotoresController.getInstance().getRadioButton_EnGarantia().setToggleGroup(radioButtonsGroup);
                PromotoresController.getInstance().getRadioButton_PorPagar().setToggleGroup(radioButtonsGroup);
                PromotoresController.getInstance().getRadioButton_Todos().setToggleGroup(radioButtonsGroup);

                PromotoresController.getInstance().getRadioButton_PorPagar().selectedProperty()
                        .addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                filterItems = new FiltrarItemsPromotor();
                                filtrar(filterItems);
                                filterItems.getAllItems();
                            }
                        });
                PromotoresController.getInstance().getRadioButton_EnGarantia().selectedProperty()
                        .addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                filterItems = new FiltrarItemsPromotor();
                                filtrar(filterItems);
                                filterItems.getAllItems();
                            }
                        });
                PromotoresController.getInstance().getRadioButton_Todos().selectedProperty()
                        .addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                filterItems = new FiltrarItemsPromotor();
                                filtrar(filterItems);
                                filterItems.getAllItems();
                            }
                        });
            });
            // Con esto le decimos que no hay necesidad de volver a repetir todo el proceso
            // si ya se ha configurado.
            configurado = true;
        }
    }

    /*
     * ##############################################
     * ####### MÉTODOS PARA AGREAGAR ITEMS ##########
     * ##############################################
     */
    private List<ItemPromotoresController> itemPromotorControllers;

    public void getAllItems() {
        /* Si han habido cambios es que se obtienen los items */
        Platform.runLater(() -> {

            removeAllItems();

            Promotor promotor;
            while ((promotor = Promotor.getAllOneToOne(Promotor.class)) != null) {
                setItems(promotor);
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
                PromotoresController.getInstance().getVboxLista_Items().getChildren().add(hbox);

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
        PromotoresController.getInstance().getVboxLista_Items().getChildren().clear();
        itemPromotorControllers.clear();
    }

    public int getIndexItemPromotorController(ItemPromotoresController controller) {
        return itemPromotorControllers.indexOf(controller);
    }

    public void removeItem(int i) {
        PromotoresController.getInstance().getVboxLista_Items().getChildren().remove(i);
    }

    /*
     * ##############################################
     * ################# MÉTODOS ####################
     * ##############################################
     */

    private void filtrar(FiltrarItemsPromotor filter) {
        filter.setEnGarantia(PromotoresController.getInstance().getRadioButton_EnGarantia().isSelected());
        filter.setPorPagar(PromotoresController.getInstance().getRadioButton_PorPagar().isSelected());
    }

}
