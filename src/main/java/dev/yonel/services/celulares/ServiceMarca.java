package dev.yonel.services.celulares;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.Gatillo;
import dev.yonel.services.Mensajes;
import dev.yonel.services.ProxyABaseDeDatos;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerAgregar;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerVista;
import dev.yonel.utils.AlertUtil;
import dev.yonel.utils.data_access.UtilsHibernate;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ServiceMarca {

    private List<Marca> listMarca = new ArrayList<>();
    private ObservableList<Marca> observableListMarca = FXCollections.observableArrayList();

    private MFXFilterComboBox<Marca> comboBox;
    private boolean asignadoComboBox = false;
    private Marca marca;

    private ServiceCelularControllerVista serviceVista = ServiceCelularControllerVista.getInstance();
    private ServiceCelularControllerAgregar serviceAgregar = ServiceCelularControllerAgregar.getInstance();

    private Mensajes mensajes = new Mensajes(ServiceMarca.class);

    private static @Setter @Getter boolean banderaMarcaExiste = false;

    public void setMarca(Marca marca) {
        this.marca = marca;
        this.marca.setMarca(this.marca.getMarca().toUpperCase());
    }

    public Marca getMarca() {
        return this.marca;
    }

    /**
     * Método con el que guardamos un objeto marca.
     *
     * @return true en caso de que se guarde, false en caso contrario.
     */
    public boolean save() {
        if (isFull()) {
            if (!exist()) {
                if (this.marca.save()) {
                    AlertUtil.information("Exito", "Marca: " + this.marca.getMarca() + " guardada.");
                    serviceAgregar.setEstadoInformation("Marca guardada.");
                    mensajes.info("Marca -> " + this.marca.getMarca() + " guardada.");
                    // Notificamos al ServiceList que hay cambios
                    Gatillo.newMarca();
                    ;
                    configureComboBox(comboBox);

                    return true;
                } else {
                    AlertUtil.error("Error en guardado.",
                            "Error interno. Si el problema persiste \ncontacte con el desarrollador.");
                    serviceAgregar.setEstadoError("Error en conexión con base de datos.");
                    mensajes.err("No se pudo guardar la marca ->" + marca.getMarca());
                    return false;
                }
            } else {
                AlertUtil.error("Error en guardado.", "La marca que desea guardar \nya existe.");
                serviceAgregar.setEstadoError("Marca no guardada. Ya existe.");
                mensajes.err("Marca -> " + marca.getMarca() + " ya existe.");
                banderaMarcaExiste = true;
                return false;
            }
        } else {
            AlertUtil.error("Error en guardado.", "Faltan datos.");
            serviceAgregar.setEstadoError("Marca no guardada. Faltan datos.");
            return false;
        }
    }

    public boolean saveV2() {
        if (isFull()) {
            if (!exist()) {
                if (this.marca.save()) {
                    AlertUtil.information("Exito", "Marca: " + this.marca.getMarca() + " guardada.");
                    mensajes.info("Marca -> " + this.marca.getMarca() + " guardada.");
                    // Notificamos al ServiceList que hay cambios
                    Gatillo.newMarca();

                    return true;
                } else {
                    AlertUtil.error("Error en guardado.",
                            "Error interno. Si el problema persiste \ncontacte con el desarrollador.");
                    mensajes.err("No se pudo guardar la marca ->" + marca.getMarca());
                    return false;
                }
            } else {
                AlertUtil.error("Error en guardado.", "La marca que desea guardar \nya existe.");
                mensajes.err("Marca -> " + marca.getMarca() + " ya existe.");
                banderaMarcaExiste = true;
                return false;
            }
        } else {
            AlertUtil.error("Error en guardado.", "Faltan datos.");
            return false;
        }
    }

    public boolean delete(){
        ServiceModelo serviceModelo = new ServiceModelo();
        List<Modelo> listaModelos;
        //Verificar que la marca no tenga más modelos.
        if((listaModelos = getModelosForMarca(marca)).size() > 0){

            if(marca.delete()){
                mensajes.info("Se van a eliminar " + listaModelos.size() + " modelos.");

                for (Modelo m : listaModelos) {
                    serviceModelo.setModelo(m);
                    serviceModelo.delete();
                }

                mensajes.info("Marca: " + marca + " eliminada.");
                Gatillo.newMarca();
                return true;
            }else{
                mensajes.err("Error eliminando marca: " + marca);
                return false;
            }
        }else{
            if(marca.delete()){
                mensajes.info("Marca: " + marca + " eliminada.");
                Gatillo.newMarca();
                return true;
            }else{
                mensajes.err("Error eliminando marca: " + marca);
                return false;
            }
        }
        
    }
    /**
     * Método con el que comprobamos que el objeto marca no esté vacío o sea nulo.
     *
     * @return true en caso de que esté completo, false en caso contrario.
     */
    private boolean isFull() {
        if (this.marca.getMarca().equals("") || this.marca == null) {
            System.err.println("Marca vacía o null.");
            return false;
        }

        return true;
    }

    /**
     * Método que verifica si existe el objeto en la base de datos.
     *
     * @return true si existe, false caso contrario.
     */
    private boolean exist() {
        Map<String, Object> propiedades = new HashMap<>();
        propiedades.put("class", Marca.class);
        propiedades.put("marca", this.marca.getMarca());

        return Marca.existe(propiedades);
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

        listMarca.clear();
        listMarca.addAll(ProxyABaseDeDatos.getListMarcas());// Cargamos los datos desde la base de datos

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

        listMarca.clear();
        // Agregamos el elemento Todos a la lista observable.
        listMarca.add(serviceVista.getMarca());
        listMarca.addAll(ProxyABaseDeDatos.getListMarcas());// Cargamos los datos desde la base de datos

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
            // Seleccionamos Todas
            comboBox.selectItem(serviceVista.getMarca());
        }
    }

    /**
     * Método con el que obtenemos una lista de modelos especificando una <code>marca</code>.
     * 
     * @param <code>marca</code> es a la marca que le vamos a buscar los modelos.
     * @return <code>lista de modelos</code> una lista de modelos, <code>null</code>
     *         en caso de que no se halle una marca.
     */
    public List<Modelo> getModelosForMarca(Marca marca) {
        SessionFactory sessionFactory = UtilsHibernate.getSessionFactory();
        List<Modelo> listaModelos = null;

        mensajes.info("Buscando los modelos para la marca " + marca);
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            String hql = "FROM Modelo m WHERE m.marca = :marca";
            Query<Modelo> query = sessionFactory.getCurrentSession().createQuery(hql, Modelo.class);
            query.setParameter("marca", marca);

            mensajes.info("Modelos encontrados");

            listaModelos = query.list();

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            mensajes.err("Error buscando los modelos para la marca " + marca);
            tx.rollback();
        }
        
        return listaModelos;
    }
}
