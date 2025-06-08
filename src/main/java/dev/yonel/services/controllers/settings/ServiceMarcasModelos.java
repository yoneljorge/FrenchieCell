package dev.yonel.services.controllers.settings;

import dev.yonel.controllers.settings.MarcaModeloController;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.Mensajes;
import dev.yonel.services.celulares.ServiceMarca;
import dev.yonel.services.celulares.ServiceModelo;
import dev.yonel.utils.AlertUtil;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextInputDialog;

import java.util.List;
import java.util.Optional;

/**
 * Clase en la que vamos a implementar la lógica que tiene que ver con el apartado de Marcas y Modelos en el controller
 * Settings. Esta clase emplea el Patrón de Diseño Singlenton por lo que solo habrá una instancia de esta clase. Esta
 * clase para su ejecución depende de MarcaModeloController.
 */
public class ServiceMarcasModelos {

    private Mensajes mensajes = new Mensajes(getClass());

    /* Instancia de esta clase. */
    private static ServiceMarcasModelos instance;

    /**
     * Controlador privado para que solo halla una instancia de esta clase.
     */
    private ServiceMarcasModelos() {
        mensajes.info("Creada una instancia de clase.");
        instance = this;
    }

    /**
     * Método para obtener la instancia de clase.
     *
     * @return <code>instance</code> la instancia de esta clase.
     */
    public static ServiceMarcasModelos getInstance() {
        if (instance == null) {
            instance = new ServiceMarcasModelos();
        }

        instance.mensajes.info("Obteniendo una instancia de la clase.");

        return instance;
    }

    /*******************************************
     * ************* MÉTODOS *******************
     ********************************************/

    public void configure() {
        mensajes.info("Configurando.");

        /*
         * Configuramos la lista para que cargue todas las marcas que hay en la base de
         * datos.
         */
        llenarListaMarca();
        /*
         * Escuchamos los cambios en el listView_Marca para cuando se haga clic en una
         * marca se llene automáticamente el listView_Modelos.
         */
        MarcaModeloController.getInstance().getListView_Marcas().getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        llenarListaModelo(newValue);
                    }
                });

        // Acción del botón AgregarMarca
        MarcaModeloController.getInstance().getBtn_AgregarMarca().setOnAction(event -> agregarMarca());

        // Acción del botón EliminarMarca
        MarcaModeloController.getInstance().getBtn_EliminarMarca().setOnAction(event -> eliminarMarca());

        // Acción del botón AgregarModelo
        MarcaModeloController.getInstance().getBtn_AgregarModelo().setOnAction(event -> agregarModelo());

        // Ación del botón EliminarModelo
        MarcaModeloController.getInstance().getBtn_EliminarModelo().setOnAction(event -> eliminarModelo());
    }

    /**
     * <p>
     * Método con el que vamos a llenar la lista de marca.
     * </p>
     */
    public void llenarListaMarca() {
        List<Marca> listaMarcas = Marca.getAll(Marca.class);
        ObservableList<Marca> observableListMarcas = FXCollections.observableArrayList(listaMarcas);
        MarcaModeloController.getInstance().getListView_Marcas().setItems(observableListMarcas);
    }

    /**
     * <p>
     * Método con el cual llenamos el listViewModelo con los Modelos correspondientes a la marca pasada como parámetro.
     * </p>
     *
     * @param marca a la que se le desea buscar los modelos.
     */
    private void llenarListaModelo(Marca marca) {
        ServiceMarca serviceMarca = new ServiceMarca();
        List<Modelo> listaModelos;

        if ((listaModelos = serviceMarca.getModelosForMarca(marca)) != null) {
            ObservableList<Modelo> observableListModelos = FXCollections.observableArrayList(listaModelos);
            MarcaModeloController.getInstance().getListView_Modelos().setItems(observableListModelos);
        }
    }

    private void agregarMarca() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(MarcaModeloController.getInstance().getNode_Root().getScene().getWindow());
        dialog.setTitle("Agregar Marca");
        dialog.setHeaderText("Introduzca la marca que desea agregar.");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            ServiceMarca serviceMarca = new ServiceMarca();
            serviceMarca.setMarca(new Marca(name));

            if (serviceMarca.saveV2()) {
                setEstadoInformation("Marca " + serviceMarca.getMarca().getMarca() + " agregada.");
                llenarListaMarca();
                MarcaModeloController.getInstance().getListView_Marcas().getSelectionModel().selectLast();
            } else if (ServiceMarca.isBanderaMarcaExiste()) {
                agregarMarca();
                ServiceMarca.setBanderaMarcaExiste(false);
            }

        });

    }

    private void eliminarMarca() {

        Marca marcaSeleccionada = MarcaModeloController.getInstance().getListView_Marcas().getSelectionModel()
                .getSelectedItem();
        if (marcaSeleccionada != null) {
            ServiceMarca serviceMarca = new ServiceMarca();
            serviceMarca.setMarca(marcaSeleccionada);

            AlertUtil.advertencia(MarcaModeloController.getInstance().getNode_Root().getScene().getWindow(),
                    "¿Desea eliminar la marca " + marcaSeleccionada + "?",
                    "Si hay modelos asociados a la marca\nse eliminarán junto con los celulares\npertenecientes a esos modelos.\nEsta acción no tiene retroceso.",
                    () -> {
                        List<Modelo> listaModelos;
                        // Verificar que la marca no tenga más modelos.
                        if ((listaModelos = serviceMarca.getModelosForMarca(marcaSeleccionada)).size() > 0) {

                            AlertUtil.advertencia(
                                    MarcaModeloController.getInstance().getNode_Root().getScene().getWindow(), "¡Peligro!",
                                    "Se van a eliminar " + listaModelos.size()
                                            + " modelos\nmás los celulares asociados y\nlos vales asociados a esos modelos\n¿Estás de acuerdo?",
                                    () -> {
                                        serviceMarca.delete();
                                        setEstadoInformation("Marca " + marcaSeleccionada + "eliminada.");
                                        MarcaModeloController.getInstance().getListView_Marcas().getItems()
                                                .remove(marcaSeleccionada);
                                        llenarListaMarca();
                                        llenarListaModelo(marcaSeleccionada);
                                    });
                        } else {
                            serviceMarca.delete();
                            setEstadoInformation("Marca " + marcaSeleccionada + "eliminada.");
                            MarcaModeloController.getInstance().getListView_Marcas().getItems().remove(marcaSeleccionada);
                            llenarListaMarca();
                            llenarListaModelo(marcaSeleccionada);
                        }
                    });
        } else {
            AlertUtil.error(MarcaModeloController.getInstance().getNode_Root().getScene().getWindow(),
                    "Eliminar Marca", "No tienes una marca seleccionada.");
        }

    }

    private void agregarModelo() {
        Marca marcaSeleccionada = MarcaModeloController.getInstance().getListView_Marcas().getSelectionModel()
                .getSelectedItem();
        if (marcaSeleccionada != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.initOwner(MarcaModeloController.getInstance().getNode_Root().getScene().getWindow());
            dialog.setTitle("Agregar Modelo");
            dialog.setHeaderText("El modelo se agregará a la marca " + marcaSeleccionada);
            dialog.setContentText("Introduzca el modelo que desea agregar: ");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                Modelo modelo = new Modelo();
                modelo.setMarca(marcaSeleccionada);
                modelo.setModelo(name);

                ServiceModelo serviceModelo = new ServiceModelo();
                serviceModelo.setModelo(modelo);

                if (serviceModelo.saveV2()) {
                    setEstadoInformation("Modelo " + modelo.getModelo() + " agregado.");
                    llenarListaModelo(marcaSeleccionada);
                    MarcaModeloController.getInstance().getListView_Modelos().getSelectionModel().selectLast();
                } else if (ServiceModelo.isBanderaModeloExiste()) {
                    agregarModelo();
                    ServiceModelo.setBanderaModeloExiste(false);
                }
            });
        } else {
            AlertUtil.error(MarcaModeloController.getInstance().getNode_Root().getScene().getWindow(),
                    "Agregar Modelo", "No tienes una marca seleccionada.");
        }

    }

    private void eliminarModelo() {
        Modelo modeloSeleccionado = MarcaModeloController.getInstance().getListView_Modelos().getSelectionModel()
                .getSelectedItem();
        AlertUtil.advertencia(MarcaModeloController.getInstance().getNode_Root().getScene().getWindow(),
                "Eliminar Modelo",
                "Al eliminar el modelo se van a\neliminar los celulares asociados.\n¿Estás de acuerdo?",
                () -> {
                    ServiceModelo serviceModelo = new ServiceModelo();
                    serviceModelo.setModelo(modeloSeleccionado);
                    if (serviceModelo.delete()) {
                        setEstadoInformation("Modelo " + modeloSeleccionado + " elliminado");
                        Marca marcaSeleccionada = MarcaModeloController.getInstance().getListView_Marcas().getSelectionModel().getSelectedItem();
                        llenarListaModelo(marcaSeleccionada);
                    }
                });
    }

    /**
     * Método con el cual vamos a pasar mensajes de información al Label de estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoInformation(String estado) {
        MarcaModeloController.getInstance().getLblEstadoMarcasModelos().setText(estado);
        MarcaModeloController.getInstance().getLblEstadoMarcasModelos().getStyleClass().add("label");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            MarcaModeloController.getInstance().getLblEstadoMarcasModelos().setText("");
        });
        pause.play();
    }

    /**
     * Método con el que vamos a pasar mensajes de error al Label de estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoError(String estado) {
        MarcaModeloController.getInstance().getLblEstadoMarcasModelos().setText(estado);
        MarcaModeloController.getInstance().getLblEstadoMarcasModelos().getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            MarcaModeloController.getInstance().getLblEstadoMarcasModelos().setText(null);
        });
        pause.play();
    }
}
