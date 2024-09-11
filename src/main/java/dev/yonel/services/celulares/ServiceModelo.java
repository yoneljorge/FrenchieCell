package dev.yonel.services.celulares;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.Gatillo;
import dev.yonel.services.ProxyABaseDeDatos;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerAgregar;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerVista;
import dev.yonel.utils.AlertUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceModelo {

    private List<Modelo> listModelo = new ArrayList<>();
    private List<Modelo> listModeloNew = new ArrayList<>();
    private ObservableList<Modelo> observableListModelo = FXCollections.observableArrayList();
    private Modelo modelo;

    private MFXFilterComboBox<Marca> comboBoxMarca;
    private MFXFilterComboBox<Modelo> comboBoxModelo;
    private MFXButton btnAsociado;

    private ServiceCelularControllerVista serviceVista = ServiceCelularControllerVista.getInstance();
    private ServiceCelularControllerAgregar serviceAgregar = ServiceCelularControllerAgregar.getInstance();

    public ServiceModelo(Modelo modelo) {
        this.modelo = modelo;
        this.modelo.setModelo(this.modelo.getModelo().toUpperCase());
    }

    public Modelo getModelo() {
        return this.modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        this.modelo.setModelo(this.modelo.getModelo().toUpperCase());
    }

    /**
     * Méotodo para guardar un objeto de tipo Modelo en la base de datos.
     * 
     * @param modelo el objeto.
     * @return true en caso de que se guarde, false en caso de que exista, no se
     *         guarde o le falten datos.
     */
    public boolean save() {
        if (isFull()) {

            if (!exist()) {
                if (this.modelo.save()) {
                    // Notificamos al ServiceList que hay cambios
                    Gatillo.newModelo();

                    AlertUtil.information("Éxito", "Modelo: " + modelo.getModelo() + " guardado.");
                    configureComboBox(comboBoxModelo, comboBoxMarca, btnAsociado);
                    updateListaAgregar();
                    serviceAgregar.setEstadoInformation("Modelo guardado.");

                    return true;
                } else {
                    AlertUtil.error("Error en guardado.",
                            "Error interno. Si el problema persiste \ncontacte con el desarrollador.");
                    serviceAgregar.setEstadoError("Error en conexión con base de datos.");
                    return false;
                }

            } else {
                AlertUtil.error("Error en guardado.", "El modelo que desea guardar \nya existe.");
                serviceAgregar.setEstadoError("Modelo no guardado. Ya existe.");
                System.err.println("Modelo no guardado -> ya existe.");
                serviceAgregar.setBanderaModeloExiste(true);
                return false;
            }
        } else {
            AlertUtil.error("Error en guardado.", "Faltan datos.");
            serviceAgregar.setEstadoError("Modelo no guardado. Faltan datos.");
            System.err.println("Modelo no guardado -> faltan datos.");
            return false;
        }
    }

    /**
     * Método que verifica si el objeto modelo no es nulo o si no está vacío.
     * 
     * @return true en caso de que este lleno, false
     *         en caso contrario.
     */
    private boolean isFull() {
        if (this.modelo.getModelo() == null) {
            System.err.println("Modelo is null.");
            return false;
        }
        if (this.modelo.getModelo().equals("")) {
            System.err.println("Modelo está vacío.");
            return false;
        }
        if (this.modelo.getMarca().getMarca().equals("") || this.modelo.getMarca() == null) {
            System.err.println("Marca está vacia o es null.");
            return false;
        }

        return true;
    }

    /**
     * Método que verifica si existe un objeto de tipo modelo en la base de datos.
     * 
     * @return true en caso de que exista, false en caso contrario.
     */
    public boolean exist() {
        Map<String, Object> propiedades = new HashMap<>();
        propiedades.put("class", Modelo.class);
        propiedades.put("modelo", this.modelo.getModelo());

        return Modelo.existe(propiedades);
    }

    public void updateListaAgregar() {
        this.listModelo.clear();
        this.listModelo.addAll(ProxyABaseDeDatos.getListModelos());// Cargamos los datos desde la base de datos

        // Lista en la que vamos a almacenar los modelos en dependencia de la marca
        // seleccionada.
        this.listModeloNew.clear();

        if (this.comboBoxMarca.getValue() != null) {
            for (Modelo m : listModelo) {
                if (m.getMarca().getMarca().equals(this.comboBoxMarca.getValue().getMarca())) {
                    this.listModeloNew.add(m);
                }
            }
        }

        this.observableListModelo.clear();
        this.observableListModelo = FXCollections.observableArrayList(this.listModeloNew);

        StringConverter<Modelo> converter = FunctionalStringConverter
                .to(modelo -> (modelo == null) ? "" : modelo.getModelo());
        Function<String, Predicate<Modelo>> filterFunction = s -> modelo -> StringUtils
                .containsIgnoreCase(converter.toString(modelo), s);
        this.comboBoxModelo.setItems(observableListModelo);
        this.comboBoxModelo.setConverter(converter);
        this.comboBoxModelo.setFilterFunction(filterFunction);
        this.comboBoxModelo.getSelectionModel().clearSelection();
    }

    public void updateListaVista() {
        this.listModelo.clear();
        this.listModelo.addAll(ProxyABaseDeDatos.getListModelos());// Cargamos los datos desde la base de datos

        // Lista en la que vamos a almacenar los modelos en dependencia de la marca
        // seleccionada.
        this.listModeloNew.clear();

        this.listModeloNew.add(serviceVista.getModelo());
        if (this.comboBoxMarca.getValue() != null) {
            for (Modelo m : listModelo) {
                if (m.getMarca().getMarca().equals(this.comboBoxMarca.getValue().getMarca())) {
                    this.listModeloNew.add(m);
                }
            }
        }

        this.observableListModelo.clear();
        this.observableListModelo = FXCollections.observableArrayList(listModeloNew);

        StringConverter<Modelo> converter = FunctionalStringConverter
                .to(modelo -> (modelo == null) ? "" : modelo.getModelo());
        Function<String, Predicate<Modelo>> filterFunction = s -> modelo -> StringUtils
                .containsIgnoreCase(converter.toString(modelo), s);
        this.comboBoxModelo.setItems(observableListModelo);
        this.comboBoxModelo.setConverter(converter);
        this.comboBoxModelo.setFilterFunction(filterFunction);

        this.comboBoxModelo.selectItem(serviceVista.getModelo());
    }

    /**
     * Método para configurar un MFXFilterComboBox de tipo Modelo.
     * Este método crea una lista de Modelos a partir de la Marca seleccionada.
     * 
     * @param comboBoxModelo
     * @param comboBoxMarca
     * @param btnAsociado    boton para agregar Modelos.
     */
    public void configureComboBox(MFXFilterComboBox<Modelo> comboBoxModelo,
            MFXFilterComboBox<Marca> comboBoxMarca, MFXButton btnAsociado) {

        if (this.comboBoxMarca == null) {
            this.comboBoxMarca = comboBoxMarca;
        }
        if (this.comboBoxModelo == null) {
            this.comboBoxModelo = comboBoxModelo;
        }
        if (this.btnAsociado == null) {
            this.btnAsociado = btnAsociado;
        }

        // Cuando cambie el item seleccionado en el comboBoxMarca entonces se activa el
        // comboBoxModelo junto con el botonAsociado y se crea la lista que se va a
        // mostrar en el comboBoxModelo.
        comboBoxMarca.selectedItemProperty().addListener(new ChangeListener<Marca>() {
            @Override
            public void changed(ObservableValue<? extends Marca> observable, Marca oldValue, Marca newValue) {
                // listModeloNew.clear();
                if (newValue != null) {
                    updateListaAgregar();

                    comboBoxModelo.setDisable(false);
                    comboBoxModelo.getSelectionModel().clearSelection();
                    btnAsociado.setDisable(false);

                } else {
                    comboBoxModelo.setDisable(true);
                    btnAsociado.setDisable(true);
                }
            }
        });

    }

    public void configureComboBox(MFXFilterComboBox<Modelo> comboBoxModelo,
            MFXFilterComboBox<Marca> comboBoxMarca) {

        if (this.comboBoxMarca == null) {
            this.comboBoxMarca = comboBoxMarca;
        }
        if (this.comboBoxModelo == null) {
            this.comboBoxModelo = comboBoxModelo;
        }

        // Cuando cambie el item seleccionado en el comboBoxMarca entonces se activa el
        // comboBoxModelo junto con el botonAsociado y se crea la lista que se va a
        // mostrar en el comboBoxModelo.
        comboBoxMarca.selectedItemProperty().addListener(new ChangeListener<Marca>() {
            @Override
            public void changed(ObservableValue<? extends Marca> observable, Marca oldValue, Marca newValue) {
                // listModeloNew.clear();
                if (newValue != null) {
                    if (!newValue.getMarca().equals("TODAS")) {
                        updateListaVista();

                        comboBoxModelo.setDisable(false);
                        // comboBoxModelo.getSelectionModel().clearSelection();
                    } else {
                        comboBoxModelo.setDisable(true);
                    }

                } else {
                    comboBoxModelo.setDisable(true);
                }
            }
        });

    }

}
