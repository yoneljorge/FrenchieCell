package dev.yonel.services.controllers.promotores;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.yonel.App;
import dev.yonel.controllers.PromotoresController;
import dev.yonel.controllers.items.ItemValeController;
import dev.yonel.controllers.popup.PopupPromotorController;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.Mensajes;
import dev.yonel.services.controllers.principal.InfoCelulares;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.services.vales.ServiceVales;
import dev.yonel.utils.AlertUtil;
import dev.yonel.utils.ui.popup.PopupUtil;
import dev.yonel.utils.ui.popup.PopupUtilImp;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ServicePromotoresControllerPromotor {

    private static ServicePromotor servicePromotor;

    private Mensajes mensajes = new Mensajes(getClass());

    // Variable que almacena la instancia de la clase
    private static ServicePromotoresControllerPromotor instance;

    /**
     * Constructor privado para que solo se pueda cerar la instancia desde dentro de
     * la clase. Con esto logramos de que
     * solo halla una sola instancia de esta clase.
     */
    private ServicePromotoresControllerPromotor() {
        instance = this;
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

    /**
     * <p>
     * Método con el que vamos a reiniciar la instancia.
     * </p>
     */
    public static void restartInstance() {
        instance = null;
    }

    private List<Vale> listVales = new ArrayList<>();
    private List<Vale> listValesLiquidar = new ArrayList<>();
    private List<ItemValeController> itemValePromotorControllers = new ArrayList<>();

    /**
     * Méotodo con el que vamos a configurar los botones y objetos de la vista.
     */
    public void configure() {
        Platform.runLater(() -> {
            // Acción al presionar el botón REGRESAR
            PromotoresController.getInstance().getBtnRegresar().setOnAction(event -> {
                PromotoresController.getInstance().goToLista();
            });

            // Acción al presionar el botón LIQUIDAR
            PromotoresController.getInstance().getBtnLiquidar().setOnAction(event -> {
                liquidar();
            });

            // Acción al presionar el botón de EDITAR_PERFIL
            PromotoresController.getInstance().getMenuItemEditarPerfil().setOnAction(event -> {
                editarPromotor();
            });

            // Acción al presionar el botón ELIMINAR_PERFIL
            PromotoresController.getInstance().getMenuItemEliminarPerfil().setOnAction(event -> {
                eliminarPromotor();
            });

            // Acción al presiodnar el boton APLICAR_FILTRO
            PromotoresController.getInstance().getBtnAplicarFiltroFecha().setOnAction(event -> {
                filtrarPorFecha(PromotoresController.getInstance().getDatePickerFechaDesde().getValue(),
                        PromotoresController.getInstance().getDatePickerFechaHasta().getValue(),
                        servicePromotor.getIdPromotor());
            });

            // Acción al presionar el boton RESET
            PromotoresController.getInstance().getBtnReset().setOnAction(event -> {
                reset();
            });
        });
    }

    /**
     * Méotodo con el que vamos a cargargar la información a la vista.
     *
     * @param id el id del promtor que se desea cargar los datos.
     */
    public void loadPromotor(Long id) {

        PromotoresController.getInstance().goToPromotor();

        servicePromotor = new ServicePromotor(Promotor.getById(Promotor.class, id));

        /*
         * Introducimos la información del promotor en el Panel.
         */
        setDatosInPanel(servicePromotor);

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
        PromotoresController.getInstance().getLblPromotor()
                .setText(promotor.getNombre() + " " + promotor.getApellidos());
        PromotoresController.getInstance().getLblValesTotal().setText(String.valueOf(promotor.getValesTotal()));
        PromotoresController.getInstance().getLblValesGarantia().setText(String.valueOf(promotor.getValesEnGarantia()));
        PromotoresController.getInstance().getLblValesPorPagar().setText(String.valueOf(promotor.getValesPorPagar()));
        PromotoresController.getInstance().getLblDineroPorPagar()
                .setText(String.valueOf(promotor.getDineroTotalPorPagar()));
        PromotoresController.getInstance().getLblDineroPagado()
                .setText(String.valueOf(promotor.getDineroTotalPagado()));
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
                PromotoresController.getInstance().getFlowPanePromotorVales().getChildren().add(vbox);

                itemValePromotorControllers.add(controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para limpiar el vbox.
     */
    private void cleanVbox() {
        PromotoresController.getInstance().getFlowPanePromotorVales().getChildren().clear();
    }

    /**
     * Método para invertir el orden de los nodos en el VBox Se invierte el orden de
     * las instancias de los controladores
     * de cada Vale.
     */
    private void invertirOrden() {
        // Invertimos el orden de los nodos en el VBox
        ObservableList<Node> children = PromotoresController.getInstance().getFlowPanePromotorVales().getChildren();
        List<Node> invertedList = new ArrayList<>(children);
        Collections.reverse(invertedList);
        PromotoresController.getInstance().getFlowPanePromotorVales().getChildren().setAll(invertedList);

        // Invertimos el orden de los controles
        List<ItemValeController> invertedItemsValePromotor = new ArrayList<>(itemValePromotorControllers);
        Collections.reverse(invertedItemsValePromotor);
        itemValePromotorControllers = invertedItemsValePromotor;
    }

    /**
     * Método con el cual vamos a agregar desde el controlador del itemValePromotor
     * los vales que se pueden liquidar. En
     * caso de que la lista sea mayor a 0, se activa el boton liquidar.
     *
     * @param vale el vale que se va a liquidar.
     */
    public void addValeLiquidar(Vale vale) {
        listValesLiquidar.add(vale);
        PromotoresController.getInstance().getBtnLiquidar().setDisable(false);
    }

    /**
     * Método con el cual vamos a remover desde el controlador itemValePromotor los
     * vales que se iban a liquidar. En
     * caso de que la lista sea igual a cero, de desactiva el boton liquidar.
     *
     * @param vale
     */
    public void removeValeLiquidar(Vale vale) {
        listValesLiquidar.remove(vale);
        if (listValesLiquidar.size() == 0) {
            PromotoresController.getInstance().getBtnLiquidar().setDisable(true);
        }
    }

    private void editarPromotor() {
        PopupUtil popupUtil = new PopupUtilImp();
        PopupPromotorController controller = new PopupPromotorController(servicePromotor);
        controller.onCloseAction(popupUtil.close());
        popupUtil.setController(controller);
        popupUtil.setFxml("popup/popupPromotor");

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                popupUtil.load();

                return null;
            }

            @Override
            protected void succeeded() {
                popupUtil.show();
            }

            @Override
            protected void failed() {
                mensajes.err(getMessage());
                mensajes.err("Error cargando el PopupPromotorController");
            }
        };
        // Ejecutar la tarea en un hilo separado
        // Execute the task in a separate thread.
        new Thread(task).start();
    }

    private void eliminarPromotor() {
        AlertUtil.advertencia(PromotoresController.getInstance().getVBoxRoot().getScene().getWindow(),
                "Eliminar Gestor",
                "¿Estás seguro que deseas eliminar el Gestor?\nEsta acción eliminará todo lo relacionado con el.",
                () -> {

                    if (servicePromotor.delete()) {
                        AlertUtil.information(
                                PromotoresController.getInstance().getVBoxRoot().getScene().getWindow(),
                                "Eliminar Gestor",
                                "Gestor eliminado.\nSe debe reiniciar la app para aplicar todos los cambios.");

                        AlertUtil.confirmation(
                                PromotoresController.getInstance().getVBoxRoot().getScene().getWindow(),
                                "Reiniciar Aplicación",
                                "¿Deseas reiniciar la aplicación?",
                                () -> {
                                    App.getInstance().restartApp();
                                });
                    }
                });
    }

    private void liquidar() {
        mensajes.info("Liquidando vales promotor: " + servicePromotor.getNombre());
        long dineroAPagar = 0;
        /*
         * Iteramos sobre la lista y vamos liquidando todos los vales.
         */
        for (Vale v : listValesLiquidar) {
            v.setLiquidado(true);
            v.update();
            dineroAPagar = dineroAPagar + v.getComision();
        }
        AlertUtil.information(PromotoresController.getInstance().getVBoxRoot().getScene().getWindow(),
                "Vales liquidados",
                "Se le tiene que pagar al gestor: " + dineroAPagar);
        // Actualizamos el promotor y la vista con la nueva información.
        servicePromotor.updatePromotor();
        loadPromotor(servicePromotor.getIdPromotor());

        // Desactivamos el boton Liquidar.
        PromotoresController.getInstance().getBtnLiquidar().setDisable(true);

        // Limpiamos la lista
        listValesLiquidar.clear();

        //Actualizamos la informacion que se muestra en la pantalla principal
        Thread updateInfo = new Thread(() -> {
            InfoCelulares.getInstance().update();
        });
        updateInfo.start();
    }

    private void filtrarPorFecha(LocalDate fechaDesde, LocalDate fechaHasta, long id) {

        if (fechaDesde == null || fechaHasta == null) {
            setEstadoError("No pueden haber fechas en blanco.");
            throw new IllegalArgumentException("No pueden haber fechas en blanco.");
        }

        cleanVbox();

        listVales.clear();

        Vale v;
        while ((v = ServiceVales.findValesByPromotorOneToOne(id)) != null) {
            LocalDate fechaVale = v.getFechaVenta();
            if ((fechaVale.isEqual(fechaHasta) || fechaVale.isBefore(fechaHasta)) &&
                    (fechaVale.isEqual(fechaDesde) || fechaVale.isAfter(fechaDesde))) {
                setItems(v);
            }
        }

        invertirOrden();
    }

    /**
     * Método con el que vamos a pasar mensajes de error al Label de
     * estado.
     * 
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoError(String estado) {
        PromotoresController.getInstance().getLabelEstado_Promotor().setText(estado);
        PromotoresController.getInstance().getLabelEstado_Promotor().getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            PromotoresController.getInstance().getLabelEstado_Promotor().setText(null);
        });
        pause.play();
    }

    public void reset() {
        PromotoresController.getInstance().getDatePickerFechaDesde().setValue(null);
        PromotoresController.getInstance().getDatePickerFechaHasta().setValue(null);

        cleanVbox();

        listVales.clear();

        Vale v;
        while ((v = ServiceVales.findValesByPromotorOneToOne(servicePromotor.getIdPromotor())) != null) {
            setItems(v);
        }

        invertirOrden();
    }
}
