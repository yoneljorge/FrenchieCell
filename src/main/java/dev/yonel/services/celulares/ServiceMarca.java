package dev.yonel.services.celulares;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import dev.yonel.models.Marca;
import dev.yonel.services.ServiceLista;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerAgregar;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerVista;
import dev.yonel.utils.AlertUtil;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

public class ServiceMarca {
    private List<Marca> listMarca;
    private ObservableList<Marca> observableListMarca;
    private MFXFilterComboBox<Marca> comboBox;
    private boolean asignadoComboBox = false;

    public ServiceMarca() {

    }

    /**
     * Método con el que comprobamos que el objeto marca no esté vacío o sea nulo.
     * 
     * @param marca el objeto que deseamos comparar.
     * @return true en caso de que esté completo, false en caso contrario.
     */
    public boolean isFull(Marca marca) {
        if (marca.getMarca().equals("") || marca == null) {
            System.err.println("Marca vacía o null.");
            return false;
        }

        return true;
    }

    /**
     * Método que verifica si existe el objeto en la base de datos.
     * 
     * @param marcaBuscar
     * @return true si existe, false caso contrario.
     */
    public boolean exist(Marca marcaBuscar) {
        Map<String, Object> propiedades = new HashMap<>();
        propiedades.put("class", Marca.class);
        propiedades.put("marca", marcaBuscar.getMarca());

        return Marca.existe(propiedades);
    }

    /**
     * Método con el que guardamos un objeto marca.
     * 
     * @param marca el objeto que se desea guardar.
     * @return true en caso de que se guarde, false en caso contrario.
     */
    public boolean save(Marca marca) {
        if (isFull(marca)) {
            if (!exist(marca)) {
                if (marca.save()) {
                    AlertUtil.information("Exito", "Marca: " + marca.getMarca() + " guardada.");
                    ServiceCelularControllerAgregar.setEstadoInformation("Marca guardada.");
                    ServiceCelularControllerVista.setNewItem(true);
                    System.out.println("Marca -> " + marca.getMarca() + " guardada.");
                    //Notificamos al ServiceList que hay cambios
                    ServiceLista.cambioMarca = true;
                    configureComboBox(comboBox);
                    
                    return true;
                } else {
                    AlertUtil.error("Error en guardado.",
                            "Error interno. Si el problema persiste \ncontacte con el desarrollador.");
                    ServiceCelularControllerAgregar.setEstadoError("Error en conexión con base de datos.");
                    System.out.println("No se pudo guardar la marca ->" + marca.getMarca());
                    
                    return false;
                }
            } else {
                AlertUtil.error("Error en guardado.", "La marca que desea guardar \nya existe.");
                ServiceCelularControllerAgregar.setEstadoError("Marca no guardada. Ya existe.");
                System.out.println("Marca -> " + marca.getMarca() + " ya existe.");
                ServiceCelularControllerAgregar.banderaMarcaExiste = true;
                return false;
            }
        } else {
            AlertUtil.error("Error en guardado.", "Faltan datos.");
            ServiceCelularControllerAgregar.setEstadoError("Marca no guardada. Faltan datos.");
            return false;
        }
    }

    /**
     * Método para configurar un MFXFilterComboBox de tipo Marca.
     * 
     * @param comboBox el objeto.
     */
    public void configureComboBox(MFXFilterComboBox<Marca> comboBox) {
        if (!asignadoComboBox) {
            this.comboBox = comboBox;
            asignadoComboBox = true;
        }

        if (listMarca == null) {
            listMarca = new ArrayList<>();
        }
        listMarca.clear();
        listMarca.addAll(ServiceLista.getListMarcas());// Cargamos los datos desde la base de datos

        if (observableListMarca == null) {
            observableListMarca = FXCollections.observableArrayList();
        }
        observableListMarca.clear();
        observableListMarca = FXCollections.observableArrayList(listMarca);

        StringConverter<Marca> converter = FunctionalStringConverter
                .to(marca -> (marca == null) ? "" : marca.getMarca());
        Function<String, Predicate<Marca>> filterFunction = s -> marca -> StringUtils
                .containsIgnoreCase(converter.toString(marca), s);

        comboBox.setItems(observableListMarca);
        comboBox.setConverter(converter);
        comboBox.setFilterFunction(filterFunction);
    }

    /**
     * Método para configurar el ComboBox<Marca>.
     * Si se especifica la clase, se agregar el elemento Todos. Este método es
     * específico para la clase ServiceCelularControllerVista.
     * 
     * @param comboBox que se desea configurar.
     * @param clazz    para comparar la clase.
     */
    public void configureComboBox(MFXFilterComboBox<Marca> comboBox, Class<?> clazz) {
        if (!asignadoComboBox) {
            this.comboBox = comboBox;
            asignadoComboBox = true;
        }

        if (listMarca == null) {
            listMarca = new ArrayList<>();
        }
        listMarca.clear();
        // Agregamos el elemento Todos a la lista observable.
        listMarca.add(ServiceCelularControllerVista.getMarca());
        listMarca.addAll(ServiceLista.getListMarcas());// Cargamos los datos desde la base de datos

        if (observableListMarca == null) {
            observableListMarca = FXCollections.observableArrayList();
        }
        observableListMarca.clear();
        observableListMarca = FXCollections.observableArrayList(listMarca);

        StringConverter<Marca> converter = FunctionalStringConverter
                .to(marca -> (marca == null) ? "" : marca.getMarca());
        Function<String, Predicate<Marca>> filterFunction = s -> marca -> StringUtils
                .containsIgnoreCase(converter.toString(marca), s);

        if (clazz.equals(ServiceCelularControllerVista.class)) {
            comboBox.setItems(observableListMarca);
            comboBox.setConverter(converter);
            comboBox.setFilterFunction(filterFunction);
            //Seleccionamos Todas 
            System.out.println("Seleccionado TOdas en ComboMarca -> ServicioMarca");
            comboBox.selectItem(ServiceCelularControllerVista.getMarca());
        }

    }


    /*************************************************
     * PRUEBAS
     **************************************************/
}
