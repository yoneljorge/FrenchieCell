package dev.yonel.services.controllers.celulares;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dev.yonel.App;
import dev.yonel.controllers.items.ItemCelularController;
import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.celulares.ServiceFecha;
import dev.yonel.services.celulares.ServiceMarca;
import dev.yonel.services.celulares.ServiceModelo;
import dev.yonel.utils.data_access.UtilsHibernate;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ServiceCelularControllerVista {

    private static final SessionFactory sessionFactory = UtilsHibernate.getSessionFactory();
    private static boolean newItem = false;
    private static Marca todas = new Marca("TODAS");
    private static Modelo todos = new Modelo("TODOS", todas);
    private static boolean cambioEnMarca = true;

    private ServiceMarca serviceMarca = new ServiceMarca();
    private ServiceModelo serviceModelo = new ServiceModelo();
    private ServiceFecha serviceFecha;
    private FilterItems filterItems;

    private static VBox vBoxItems;
    private MFXButton btnRecargar;
    private MFXFilterComboBox<Marca> comboMarca;
    private MFXFilterComboBox<Modelo> comboModelo;
    private MFXFilterComboBox<LocalDate> comboFecha;
    private MFXTextField txtFiltrarImei;
    private Label validarFiltrarImei;
    private CheckBox checkDual;
    private CheckBox checkVendido;

    // Lista para almacenar las instancias de ItemCelularController
    private static List<ItemCelularController> itemCelularControllers = new ArrayList<>();

    @SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
    public ServiceCelularControllerVista(Map<String, Object> lista) {
        this.vBoxItems = (VBox) lista.get("vbox");
        this.btnRecargar = (MFXButton) lista.get("recargar");
        this.comboMarca = (MFXFilterComboBox) lista.get("comboMarca");
        this.comboModelo = (MFXFilterComboBox) lista.get("comboModelo");
        this.comboFecha = (MFXFilterComboBox) lista.get("comboFecha");
        this.txtFiltrarImei = (MFXTextField) lista.get("filtrarImei");
        this.validarFiltrarImei = (Label) lista.get("validation");
        this.checkDual = (CheckBox) lista.get("checkDual");
        this.checkVendido = (CheckBox) lista.get("checkVendido");
        this.serviceFecha = new ServiceFecha(this.comboFecha, this.comboMarca, this.comboModelo);
    }

    /******************************************
     * **********MÉTODOS ESTÁTICO**************
     ******************************************/

    public static int getIndexItemCelularController(ItemCelularController controller) {
        return itemCelularControllers.indexOf(controller);
    }

    public static void removeItem(int i) {
        vBoxItems.getChildren().remove(i);
    }

    public static void cargarItems() {
        cleanVBox();

        getAllItems();

        invertirOrden();
    }

    private static void getAllItems() {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            String hql = "FROM Celular"; // Tu entidad celular
            Query<Celular> query = sessionFactory.getCurrentSession().createQuery(hql, Celular.class);
            query.setFirstResult(0); // Desde el primer registro
            query.setMaxResults(1); // Solo un registro

            while (true) {
                List<Celular> resultList = query.list();
                if (resultList.isEmpty()) {
                    break; // Si no hay más resultados, salir
                }
                setItems(resultList.get(0));
                System.out.println("imei del celular -> " + resultList.get(0).getImeiUno());

                // Incrementa el desplazamiento para el siguiente
                int currentFirstResult = query.getFirstResult();
                query.setFirstResult(currentFirstResult + 1);
            }

            tx.commit(); // Finaliza la transacción.
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();// Deshacer la transacción en caso de error.
            }
            e.printStackTrace();// Maneja o registra la excepción
        }
    }

    public static void setItems(Celular celular) {
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

    public static void cleanVBox() {
        vBoxItems.getChildren().clear();
        itemCelularControllers.clear();
    }

    public static void invertirOrden() {
        // Invertimos el orden de los nodos en el VBox
        ObservableList<Node> children = vBoxItems.getChildren();
        List<Node> invertedList = new ArrayList<>(children);
        Collections.reverse(invertedList);
        vBoxItems.getChildren().setAll(invertedList);

        // Invertimos el orden de los nodos en la lista
        List<ItemCelularController> invertedItemsCelular = new ArrayList<>(itemCelularControllers);
        Collections.reverse(invertedItemsCelular);
        itemCelularControllers = invertedItemsCelular;
    }

    public static boolean getNewItem() {
        return newItem;
    }

    public static void setNewItem(boolean estado) {
        newItem = estado;
    }

    public static Marca getMarca() {
        return todas;
    }

    public static Modelo getModelo() {
        return todos;
    }

    /******************************************
     * **********CONFIGURACION*****************
     ******************************************/
    public void configure() {

        cargarItems();

        serviceMarca.configureComboBox(comboMarca, ServiceCelularControllerVista.class);
        serviceModelo.configureComboBox(comboModelo, comboMarca);
        serviceFecha.configureComboBox();

        // Validación para el txtFiltrarImei
        MFXTextFieldUtil.validateFilterIMEI(txtFiltrarImei, validarFiltrarImei);

        btnRecargar.setOnAction(event -> {
            recargar();
        });

        checkDual.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filterItems = new FilterItems();
            filtrar(filterItems);
            filterItems.getAllItems();
        });

        checkVendido.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filterItems = new FilterItems();
            filtrar(filterItems);
            filterItems.getAllItems();
        });

        comboMarca.selectedItemProperty().addListener(new ChangeListener<Marca>() {
            @Override
            public void changed(ObservableValue<? extends Marca> observable, Marca oldValue, Marca newValue) {
                if (newValue != null) {
                    serviceFecha.configureComboBox();

                    filterItems = new FilterItems();
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
                    serviceFecha.configureComboBox();

                    if (!cambioEnMarca) {
                        filterItems = new FilterItems();
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
                    filterItems = new FilterItems();
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
                        filterItems = new FilterItems();
                        filtrar(filterItems);
                        filterItems.getAllItems();
                    }
                }
            }
        });
    }

    /******************************************
     * **********MÉTODOS***********************
     ******************************************/

    private void filtrar(FilterItems filter) {
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

        filter.setVendido(checkVendido.isSelected());
    }

    public void recargar() {
        cargarItems();

        comboFecha.getSelectionModel().clearSelection();
        comboMarca.getSelectionModel().selectIndex(0);
        txtFiltrarImei.setText("");
        checkDual.setSelected(false);
        checkVendido.setSelected(false);

        serviceMarca.configureComboBox(comboMarca, ServiceCelularControllerVista.class);
        serviceModelo.configureComboBox(comboModelo, comboMarca);
        serviceFecha.configureComboBox();

    }
}
