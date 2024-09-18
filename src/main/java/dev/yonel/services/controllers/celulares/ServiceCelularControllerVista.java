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
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ServiceCelularControllerVista {

    private static ServiceCelularControllerVista instance;

    private Marca todas;
    private Modelo todos;
    private boolean cambioEnMarca;

    private ServiceFecha serviceFecha;
    private ServiceMarca serviceMarca;
    private ServiceModelo serviceModelo;
    private FilterItemsCelular filterItems;

    private VBox vBoxItems;
    private MFXButton btnRecargar;
    private MFXFilterComboBox<Marca> comboMarca;
    private MFXFilterComboBox<Modelo> comboModelo;
    private MFXFilterComboBox<LocalDate> comboFecha;
    private MFXTextField txtFiltrarImei;
    private Label validarFiltrarImei;
    private CheckBox checkDual;
    private Label cantidad;
    private RadioButton radioButtonTodos;
    private RadioButton radioButtonSi;
    private RadioButton radioButtonNo;
    private ToggleGroup radioButtonsGrupo;

    // Lista para almacenar las instancias de ItemCelularController
    private List<ItemCelularController> itemCelularControllers;

    private CelularesController celularesController;

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
            this.celularesController = CelularesController.getInstance();

            setObjects();
        });

    }

    public static ServiceCelularControllerVista getInstance() {
        if (instance == null) {
            instance = new ServiceCelularControllerVista();
        }
        return instance;
    }

    private void setObjects() {
        this.vBoxItems = celularesController.getVboxItemVista();
        this.btnRecargar = celularesController.getBtnRecargar();
        this.comboMarca = celularesController.getCmbMarcaVista();
        this.comboModelo = celularesController.getCmbModeloVista();
        this.comboFecha = celularesController.getCmbFechaVista();
        this.txtFiltrarImei = celularesController.getTxtFiltrarImei();
        this.validarFiltrarImei = celularesController.getValidationLabel_FilterImei();
        this.checkDual = celularesController.getCheckDual();
        this.cantidad = celularesController.getLabelCantidad();
        this.radioButtonTodos = celularesController.getRadioButton_Todos();
        this.radioButtonNo = celularesController.getRadioButton_VendidosNo();
        this.radioButtonSi = celularesController.getRadioButton_VendidosSi();
        radioButtonsGrupo = new ToggleGroup();
        radioButtonNo.setToggleGroup(radioButtonsGrupo);
        radioButtonSi.setToggleGroup(radioButtonsGrupo);
        radioButtonTodos.setToggleGroup(radioButtonsGrupo);
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

    public void configure() {
        Platform.runLater(() -> {
            cargarItems();

            serviceMarca.configureComboBox(comboMarca, ServiceCelularControllerVista.class);
            serviceModelo.configureComboBox(comboModelo, comboMarca);
            serviceFecha.configureComboBox(comboFecha, comboMarca, comboModelo);

            // Validación para el txtFiltrarImei
            MFXTextFieldUtil.validateFilterIMEI(txtFiltrarImei, validarFiltrarImei);

            btnRecargar.setOnAction(event -> {
                recargar();
            });

            checkDual.selectedProperty().addListener((observable, oldValue, newValue) -> {
                filterItems = new FilterItemsCelular();
                filtrar(filterItems);
                filterItems.getAllItems();
            });

            comboMarca.selectedItemProperty().addListener(new ChangeListener<Marca>() {
                @Override
                public void changed(ObservableValue<? extends Marca> observable, Marca oldValue, Marca newValue) {
                    if (newValue != null) {
                        serviceFecha.configureComboBox(comboFecha, comboMarca, comboModelo);

                        filterItems = new FilterItemsCelular();
                        filtrar(filterItems);
                        filterItems.getAllItems();
                        // Para que no vuelva a realizar la misma accion en el comboBox modelo
                        cambioEnMarca = true;
                    }
                }
            });

            comboModelo.selectedItemProperty().addListener(new ChangeListener<Modelo>() {
                @Override
                public void changed(ObservableValue<? extends Modelo> observable, Modelo oldValue, Modelo newValue) {
                    if (newValue != null) {
                        serviceFecha.configureComboBox(comboFecha, comboMarca, comboModelo);

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

            comboFecha.selectedItemProperty().addListener(new ChangeListener<LocalDate>() {
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
            txtFiltrarImei.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue != null) {
                        if (txtFiltrarImei.isValid()) {
                            if (!newValue.matches("\\d*")) {
                                txtFiltrarImei.setText(oldValue);
                            }
                            filterItems = new FilterItemsCelular();
                            filtrar(filterItems);
                            filterItems.getAllItems();
                        }
                    }
                }
            });

            radioButtonNo.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    filterItems = new FilterItemsCelular();
                    filtrar(filterItems);
                    filterItems.getAllItems();
                }
            });
            radioButtonSi.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    filterItems = new FilterItemsCelular();
                    filtrar(filterItems);
                    filterItems.getAllItems();
                }
            });
            radioButtonTodos.selectedProperty().addListener((observable, oldValue, newValue) -> {
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
        vBoxItems.getChildren().remove(i);
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
                vBoxItems.getChildren().add(hbox);

                itemCelularControllers.add(controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanVBox() {
        vBoxItems.getChildren().clear();
        itemCelularControllers.clear();
    }

    public void invertirOrden() {
        // Invertimos el orden de los nodos en el VBox
        ObservableList<Node> children = vBoxItems.getChildren();
        List<Node> invertedList = new ArrayList<>(children);
        Collections.reverse(invertedList);
        vBoxItems.getChildren().setAll(invertedList);

        // Invertimos el orden de los nodos en la lista
        List<ItemCelularController> invertedItemsCelular = new ArrayList<>(itemCelularControllers);
        Collections.reverse(invertedItemsCelular);
        itemCelularControllers = invertedItemsCelular;

        // Mostramos la cantidad de celulares en el label cantidad.
        cantidad.setText("Cantidad: " + vBoxItems.getChildren().size());
    }

    public Marca getMarca() {
        return todas;
    }

    public Modelo getModelo() {
        return todos;
    }

    private void filtrar(FilterItemsCelular filter) {
        if (comboMarca.getValue() != null) {
            if (!comboMarca.getValue().getMarca().equals(todas.getMarca())) {
                filter.setMarca(comboMarca.getValue());
            }
        } else {
            filter.setMarca(null);
        }

        if (comboModelo.getValue() != null) {
            if (!comboModelo.getValue().getModelo().equals(todos.getModelo())) {
                filter.setModelo(comboModelo.getValue());
            }
        } else {
            filter.setModelo(null);
        }

        if (comboFecha.getValue() != null) {
            filter.setFecha(comboFecha.getValue());
        } else {
            filter.setFecha(null);
        }

        if (!txtFiltrarImei.getText().trim().equals("")) {
            filter.setImei(txtFiltrarImei.getText().trim());
        } else {
            filter.setImei(null);
        }

        filter.setDual(checkDual.isSelected());

        filter.setNoVendido(radioButtonNo.isSelected());
        filter.setVendido(radioButtonSi.isSelected());
    }

    public void recargar() {
        cargarItems();

        comboFecha.getSelectionModel().clearSelection();
        comboMarca.getSelectionModel().selectIndex(0);
        txtFiltrarImei.setText("");
        checkDual.setSelected(false);

        serviceMarca.configureComboBox(comboMarca, ServiceCelularControllerVista.class);
        serviceModelo.configureComboBox(comboModelo, comboMarca);
        serviceFecha.configureComboBox(comboFecha, comboMarca, comboModelo);

        radioButtonTodos.selectedProperty().set(true);
    }
}
