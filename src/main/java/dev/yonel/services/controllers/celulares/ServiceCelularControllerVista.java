package dev.yonel.services.controllers.celulares;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.yonel.App;
import dev.yonel.controllers.CelularesController;
import dev.yonel.controllers.items.ItemCelularController;
import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.celulares.ServiceFecha;
import dev.yonel.services.celulares.ServiceMarca;
import dev.yonel.services.celulares.ServiceModelo;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class ServiceCelularControllerVista {

    private static ServiceCelularControllerVista instance;

    private Marca todas;
    private Modelo todos;
    private boolean cambioEnMarca;

    private ServiceFecha serviceFecha;
    private ServiceMarca serviceMarca;
    private ServiceModelo serviceModelo;
    private FilterItemsCelular filterItems;

 
    private ToggleGroup radioButtonsGrupo = new ToggleGroup();

    // Lista para almacenar las instancias de ItemCelularController
    private List<ItemCelularController> itemCelularControllers;

    private ServiceCelularControllerVista() {
        instance = this;

        Platform.runLater(() -> {
            this.todas = new Marca("TODAS");
            this.todos = new Modelo("TODOS", todas);
            this.cambioEnMarca = true;
            this.serviceMarca = new ServiceMarca();
            this.serviceModelo = new ServiceModelo();
            this.serviceFecha = new ServiceFecha();
            this.itemCelularControllers = new ArrayList<>();
        });

    }

    public static ServiceCelularControllerVista getInstance() {
        if (instance == null) {
            instance = new ServiceCelularControllerVista();
        }
        return instance;
    }

    public static void restartInstance() {
        instance = null;
    }

    public void configure() {
        Platform.runLater(() -> {
            CelularesController.getInstance().getRadioButton_Todos().setToggleGroup(radioButtonsGrupo);
            CelularesController.getInstance().getRadioButton_VendidosNo().setToggleGroup(radioButtonsGrupo);
            CelularesController.getInstance().getRadioButton_VendidosSi().setToggleGroup(radioButtonsGrupo);

            CelularesController.getInstance().getCheckDual().setSelected(false);
            cargarItems();

            serviceMarca.configureComboBox(
                    CelularesController.getInstance().getCmbMarcaVista(),
                    ServiceCelularControllerVista.class);
            serviceModelo.configureComboBox(
                    CelularesController.getInstance().getCmbModeloVista(),
                    CelularesController.getInstance().getCmbMarcaVista());
            serviceFecha.configureComboBox(
                    CelularesController.getInstance().getCmbFechaVista(),
                    CelularesController.getInstance().getCmbMarcaVista(),
                    CelularesController.getInstance().getCmbModeloVista());

            // Validación para el txtFiltrarImei
            MFXTextFieldUtil.validateFilterIMEI(
                    CelularesController.getInstance().getTxtFiltrarImei(),
                    CelularesController.getInstance().getValidationLabel_FilterImei());

            CelularesController.getInstance().getBtnRecargar().setOnAction(event -> {
                recargar();
            });

            CelularesController.getInstance().getCheckDual().selectedProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        filterItems = new FilterItemsCelular();
                        filtrar(filterItems);
                        filterItems.getAllItems();
                    });

            CelularesController.getInstance().getCmbMarcaVista().selectedItemProperty()
                    .addListener(new ChangeListener<Marca>() {
                        @Override
                        public void changed(ObservableValue<? extends Marca> observable, Marca oldValue,
                                Marca newValue) {
                            if (newValue != null) {
                                serviceFecha.configureComboBox(
                                        CelularesController.getInstance().getCmbFechaVista(),
                                        CelularesController.getInstance().getCmbMarcaVista(),
                                        CelularesController.getInstance().getCmbModeloVista());

                                filterItems = new FilterItemsCelular();
                                filtrar(filterItems);
                                filterItems.getAllItems();
                                // Para que no vuelva a realizar la misma accion en el comboBox modelo
                                cambioEnMarca = true;
                            }
                        }
                    });

            CelularesController.getInstance().getCmbModeloVista().selectedItemProperty()
                    .addListener(new ChangeListener<Modelo>() {
                        @Override
                        public void changed(ObservableValue<? extends Modelo> observable, Modelo oldValue,
                                Modelo newValue) {
                            if (newValue != null) {
                                serviceFecha.configureComboBox(CelularesController.getInstance().getCmbFechaVista(),
                                        CelularesController.getInstance().getCmbMarcaVista(),
                                        CelularesController.getInstance().getCmbModeloVista());

                                if (!cambioEnMarca) {
                                    filterItems = new FilterItemsCelular();
                                    filtrar(filterItems);
                                    filterItems.getAllItems();
                                } else {
                                    cambioEnMarca = false;
                                }
                            }
                        }
                    });

            CelularesController.getInstance().getCmbFechaVista().selectedItemProperty()
                    .addListener(new ChangeListener<LocalDate>() {
                        @Override
                        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
                                LocalDate newValue) {
                            if (newValue != null) {
                                filterItems = new FilterItemsCelular();
                                filtrar(filterItems);
                                filterItems.getAllItems();
                            }
                        }
                    });

            // Falta programar para que cuando se escirba en el txtfilterimei solo se
            // permita numeros y que busque los celulares que contengan solo ese imei.
            CelularesController.getInstance().getTxtFiltrarImei().textProperty()
                    .addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                            if (newValue != null) {
                                if (CelularesController.getInstance().getTxtFiltrarImei().isValid()) {
                                    if (!newValue.matches("\\d*")) {
                                        CelularesController.getInstance().getTxtFiltrarImei().setText(oldValue);
                                    }
                                    filterItems = new FilterItemsCelular();
                                    filtrar(filterItems);
                                    filterItems.getAllItems();
                                }
                            }
                        }
                    });

            CelularesController.getInstance().getRadioButton_VendidosNo().selectedProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            filterItems = new FilterItemsCelular();
                            filtrar(filterItems);
                            filterItems.getAllItems();
                        }
                    });
            CelularesController.getInstance().getRadioButton_VendidosSi().selectedProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            filterItems = new FilterItemsCelular();
                            filtrar(filterItems);
                            filterItems.getAllItems();
                        }
                    });
            CelularesController.getInstance().getRadioButton_Todos().selectedProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            filterItems = new FilterItemsCelular();
                            filtrar(filterItems);
                            filterItems.getAllItems();
                        }
                    });
        });
    }

    public int getIndexItemCelularController(ItemCelularController controller) {
        return itemCelularControllers.indexOf(controller);
    }

    public void removeItem(int i) {
        CelularesController.getInstance().getVboxItemVista().getChildren().remove(i);
    }

    public void cargarItems() {
        cleanVBox();

        getAllItems();

        invertirOrden();
    }

    private void getAllItems() {
        Celular celular;

        while ((celular = Celular.getAllOneToOne(Celular.class)) != null) {
            setItems(celular);
        }
    }

    public void setItems(Celular celular) {
        // Si el celular no coincide con el filtrado se pasa un null para que no haga
        // nada.
        if (celular != null) {
            try {
                HBox hbox;
                FXMLLoader loader = App.fxmlLoader("items/itemCelular");
                ItemCelularController controller = new ItemCelularController();
                // ----> Todo lo que se le va a agregar al controlador
                controller.setCelular(new ServiceCelular(celular));
                loader.setController(controller);
                hbox = loader.load();
                CelularesController.getInstance().getVboxItemVista().getChildren().add(hbox);

                itemCelularControllers.add(controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanVBox() {
        CelularesController.getInstance().getVboxItemVista().getChildren().clear();
        itemCelularControllers.clear();
    }

    public void invertirOrden() {
        // Invertimos el orden de los nodos en el VBox
        ObservableList<Node> children = CelularesController.getInstance().getVboxItemVista().getChildren();
        List<Node> invertedList = new ArrayList<>(children);
        Collections.reverse(invertedList);
        CelularesController.getInstance().getVboxItemVista().getChildren().setAll(invertedList);

        // Invertimos el orden de los nodos en la lista
        List<ItemCelularController> invertedItemsCelular = new ArrayList<>(itemCelularControllers);
        Collections.reverse(invertedItemsCelular);
        itemCelularControllers = invertedItemsCelular;

        // Mostramos la cantidad de celulares en el label cantidad.
        CelularesController.getInstance().getLabelCantidad()
                .setText("Cantidad: " + CelularesController.getInstance().getVboxItemVista().getChildren().size());
    }

    public Marca getMarca() {
        return todas;
    }

    public Modelo getModelo() {
        return todos;
    }

    private void filtrar(FilterItemsCelular filter) {
        if (CelularesController.getInstance().getCmbMarcaVista().getValue() != null) {
            if (!CelularesController.getInstance().getCmbMarcaVista().getValue().getMarca().equals(todas.getMarca())) {
                filter.setMarca(CelularesController.getInstance().getCmbMarcaVista().getValue());
            }
        } else {
            filter.setMarca(null);
        }

        if (CelularesController.getInstance().getCmbModeloVista().getValue() != null) {
            if (!CelularesController.getInstance().getCmbModeloVista().getValue().getModelo()
                    .equals(todos.getModelo())) {
                filter.setModelo(CelularesController.getInstance().getCmbModeloVista().getValue());
            }
        } else {
            filter.setModelo(null);
        }

        if (CelularesController.getInstance().getCmbFechaVista().getValue() != null) {
            filter.setFecha(CelularesController.getInstance().getCmbFechaVista().getValue());
        } else {
            filter.setFecha(null);
        }

        if (!CelularesController.getInstance().getTxtFiltrarImei().getText().trim().equals("")) {
            filter.setImei(CelularesController.getInstance().getTxtFiltrarImei().getText().trim());
        } else {
            filter.setImei(null);
        }

        filter.setDual(CelularesController.getInstance().getCheckDual().isSelected());

        filter.setNoVendido(CelularesController.getInstance().getRadioButton_VendidosNo().isSelected());
        filter.setVendido(CelularesController.getInstance().getRadioButton_VendidosSi().isSelected());
    }

    public void recargar() {
        cargarItems();

        CelularesController.getInstance().getCmbFechaVista().getSelectionModel().clearSelection();
        CelularesController.getInstance().getCmbMarcaVista().getSelectionModel().selectIndex(0);
        CelularesController.getInstance().getTxtFiltrarImei().setText("");
        CelularesController.getInstance().getCheckDual().setSelected(false);

        serviceMarca.configureComboBox(
                CelularesController.getInstance().getCmbMarcaVista(),
                ServiceCelularControllerVista.class);
        serviceModelo.configureComboBox(
                CelularesController.getInstance().getCmbModeloVista(),
                CelularesController.getInstance().getCmbMarcaVista());
        serviceFecha.configureComboBox(
                CelularesController.getInstance().getCmbFechaVista(),
                CelularesController.getInstance().getCmbMarcaVista(),
                CelularesController.getInstance().getCmbModeloVista());

        CelularesController.getInstance().getRadioButton_Todos().selectedProperty().set(true);
    }
}
