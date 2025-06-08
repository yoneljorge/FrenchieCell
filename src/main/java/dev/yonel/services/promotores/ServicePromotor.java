package dev.yonel.services.promotores;

import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.Gatillo;
import dev.yonel.services.Mensajes;
import dev.yonel.services.ProxyABaseDeDatos;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerAgregar;
import dev.yonel.services.vales.ServiceVales;
import dev.yonel.utils.AlertUtil;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ServicePromotor {

    private Promotor promotor;

    private static ServicePromotor instance;

    private Mensajes mensajes = new Mensajes(getClass());

    // Listas para los promotores
    private List<Promotor> list;
    private ObservableList<Promotor> observableList;

    public ServicePromotor() {
        this.promotor = new Promotor();
    }

    public ServicePromotor(Promotor promotor) {
        this.promotor = promotor;
    }

    private static ServicePromotor getInstance() {
        if (instance == null) {
            instance = new ServicePromotor();
        }

        return instance;
    }

    /*********************************************
     * **********SETTER AND GETTER****************
     *********************************************/

    public Long getIdPromotor() {
        return this.promotor.getIdPromotor();
    }

    public void setNombre(String nombre) {
        this.promotor.setNombre(nombre);
    }

    public String getNombre() {
        if (promotor.getNombre() != null) {
            return promotor.getNombre();
        } else {
            return "";
        }
    }

    public void setApellidos(String apellidos) {
        this.promotor.setApellidos(apellidos);
    }

    public String getApellidos() {
        if (promotor.getApellidos() != null) {
            return promotor.getApellidos();
        } else {
            return "";
        }
    }

    public void setTelefono(Long telefono) {
        this.promotor.setTelfono(telefono);
    }

    public Long getTelefono() {
        if (promotor.getTelfono() != null) {
            return promotor.getTelfono();
        } else {
            return 0l;
        }
    }

    public Long getValesTotal() {
        if (promotor.getTotalDeVales() != null) {
            return promotor.getTotalDeVales();
        } else {
            return 0l;
        }
    }

    public Long getValesEnGarantia() {
        if (promotor.getValesEnGarantia() != null) {
            return promotor.getValesEnGarantia();
        } else {
            return 0l;
        }
    }

    public Long getValesPorPagar() {
        if (promotor.getValesPorPagar() != null) {
            return promotor.getValesPorPagar();
        } else {
            return 0l;
        }
    }

    public Long getDineroTotalPorPagar() {
        if (promotor.getDineroTotalPorPagar() != null) {
            return promotor.getDineroTotalPorPagar();
        } else {
            return 0l;
        }
    }

    public Long getDineroTotalPagado() {
        if (promotor.getDineroTotalPagado() != null) {
            return promotor.getDineroTotalPagado();
        } else {
            return 0l;
        }
    }

    public void setPromotor(Promotor promotor) {
        this.promotor = promotor;
    }

    public Promotor getPromotor() {
        return this.promotor;
    }

    /*********************************************
     * ********** MÉTODOS PARA OBJETOS ***********
     *********************************************/

    /**
     * Método para configurar un MFXFilterComboBox<Promotor>. Con este método se le
     * asigna el filtro al combo y la lista
     * de promotores.
     *
     * @param comboBox el comboBox que se desea configurar.
     */
    public void configureFilterComboBox(MFXFilterComboBox<Promotor> comboBox) {

        if (list == null) {
            list = new ArrayList<>();
        }

        list.clear();
        /*
         * Cargamos los datos desde ServiceList para una mejor gestión, con esto
         * evitamos cargar datos desde la base de dato in
         * cesariamente.
         */
        list.addAll(ProxyABaseDeDatos.getListPromotores());

        if (observableList == null) {
            observableList = FXCollections.observableArrayList();
        }

        observableList.clear();
        observableList = FXCollections.observableArrayList(list);

        StringConverter<Promotor> converter = FunctionalStringConverter
                .to(promotor -> (promotor == null) ? "" : promotor.getNombre());
        Function<String, Predicate<Promotor>> filterFunction = s -> promotor -> StringUtils
                .containsIgnoreCase(converter.toString(promotor), s);

        comboBox.setItems(observableList);
        comboBox.setConverter(converter);
        comboBox.setFilterFunction(filterFunction);
    }

    /**
     * Método para configurar un MFXFilterComboBox Promotor. Con este método se le
     * asigna el filtro al combo y la lista
     * de promotores. Se selecciona también el promotor.
     *
     * @param comboBox            el combo box que se desea configurar.
     * @param promotorSeleccionar el promotor que se desea seleccionar.
     */
    public void configureFilterComboBox(MFXFilterComboBox<Promotor> comboBox, Promotor promotorSeleccionar) {

        if (list == null) {
            list = new ArrayList<>();
        }

        list.clear();
        /*
         * Cargamos los datos desde ServiceList para una mejor gestión, con esto
         * evitamos cargar datos desde la base de dato in
         * cesariamente.
         */
        list.addAll(ProxyABaseDeDatos.getListPromotores());

        if (observableList == null) {
            observableList = FXCollections.observableArrayList();
        }

        observableList.clear();
        observableList = FXCollections.observableArrayList(list);

        StringConverter<Promotor> converter = FunctionalStringConverter
                .to(promotor -> (promotor == null) ? "" : promotor.getNombre());
        Function<String, Predicate<Promotor>> filterFunction = s -> promotor -> StringUtils
                .containsIgnoreCase(converter.toString(promotor), s);
        if (observableList != null) {
            for (Promotor p : observableList) {
                if (p.getNombre().equals(promotorSeleccionar.getNombre())) {
                    promotorSeleccionar = p;
                }
            }
        }
        comboBox.setItems(observableList);
        comboBox.setConverter(converter);
        comboBox.setFilterFunction(filterFunction);
        comboBox.getSelectionModel().selectItem(promotorSeleccionar);
    }

    /*********************************************
     * ********** MÉTODOS CRUD ******************
     *********************************************/

    /**
     * Método que guarda un promotor en la base de datos.
     *
     * @return true en caso de que se guarde correctamente, false en caso contrario.
     */
    public boolean save() {
        if (!exist()) {
            if (promotor.save()) {
                ServicePromotoresControllerAgregar.getInstance().setEstadoInformativo("Gestor guardado.");
                // Notificamos de que ha sido agregado un nuevo promotor.
                Gatillo.newPromotor();
                return true;
            } else {
                ServicePromotoresControllerAgregar.getInstance().setEstadoError("Error en conexióncon base de datos.");
                AlertUtil.error(null, "Error en base de datos.\nSi el error persiste contacte al desarrolador.");
                return false;
            }
        } else {
            ServicePromotoresControllerAgregar.getInstance().setEstadoError("Gestor no guardado, ya existe.");
            return false;
        }
    }

    /**
     * Método que actualiza un promotor en base a su id.
     *
     * @param idPromotor el id del promotor.
     * @return true en caso de que se actualice, false en caso contrario.
     */
    public boolean updatePromotor(Long idPromotor) {

        long enGarantia = 0L;
        long pagados = 0L;
        long porPagar = 0L;
        long totalVales = 0L;

        long dineroPorPagar = 0L;
        long dineroPagado = 0L;

        Vale vale;
        Promotor promotor = Promotor.getById(Promotor.class, idPromotor);

        while ((vale = ServiceVales.findValesByPromotorOneToOne(promotor.getIdPromotor())) != null) {
            // Iteramos la variable totalVales de uno en uno cada vez que aparezca
            // un vale asociado a ese promotor
            totalVales++;

            if (vale.getLiquidado() != null && vale.getLiquidado()) {
                pagados++;
                dineroPagado = dineroPagado + vale.getComision();
            } else {
                if (vale.getGarantia() != null && vale.getGarantia()) {
                    enGarantia++;
                } else {
                    porPagar++;
                    dineroPorPagar = dineroPorPagar + vale.getComision();
                }
            }
        }

        promotor.setValesPagados(pagados);
        promotor.setValesEnGarantia(enGarantia);
        promotor.setValesPorPagar(porPagar);
        promotor.setTotalDeVales(totalVales);

        promotor.setDineroTotal(dineroPagado + dineroPorPagar);
        promotor.setDineroTotalPorPagar(dineroPorPagar);
        promotor.setDineroTotalPagado(dineroPagado);

        if (promotor.update()) {
            Gatillo.newPromotor();

            return true;
        } else {

            return false;
        }
    }

    public boolean updatePromotor() {

        long enGarantia = 0L;
        long pagados = 0L;
        long porPagar = 0L;
        long totalVales = 0L;

        long dineroPorPagar = 0L;
        long dineroPagado = 0L;

        Vale vale;

        while ((vale = ServiceVales.findValesByPromotorOneToOne(promotor.getIdPromotor())) != null) {
            // Iteramos la variable totalVales de uno en uno cada vez que aparezca
            // un vale asociado a ese promotor
            totalVales++;

            if (vale.getLiquidado() != null && vale.getLiquidado()) {
                pagados++;
                dineroPagado = dineroPagado + vale.getComision();
            } else {
                if (vale.getGarantia() != null && vale.getGarantia()) {
                    enGarantia++;
                } else {
                    porPagar++;
                    dineroPorPagar = dineroPorPagar + vale.getComision();
                }
            }
        }

        promotor.setValesPagados(pagados);
        promotor.setValesEnGarantia(enGarantia);
        promotor.setValesPorPagar(porPagar);
        promotor.setTotalDeVales(totalVales);

        promotor.setDineroTotal(dineroPagado + dineroPorPagar);
        promotor.setDineroTotalPorPagar(dineroPorPagar);
        promotor.setDineroTotalPagado(dineroPagado);

        if (promotor.update()) {
            Gatillo.newPromotor();

            return true;
        } else {

            return false;
        }
    }

    /**
     * Método que actualiza todos los promotores de la base de datos. Este método
     * realiza las transacciones de una en
     * una para evitar sobrecargar la memoria.
     */
    public static void updateAllPromotor() {
        Promotor promotor;

        while ((promotor = Promotor.getAllOneToOne(Promotor.class)) != null) {
            getInstance().updatePromotor(promotor.getIdPromotor());
        }
    }

    public boolean delete() {
        mensajes.info("Eliminando promotor");
        if (promotor.delete()) {
            
            Vale vale;

            while ((vale = ServiceVales.findValesByPromotorOneToOne(promotor.getIdPromotor())) != null) {
                ServiceVales serviceVales = new ServiceVales(vale);
                serviceVales.eliminar(vale.getImei());
            }
            return true;
        }else{
            mensajes.err("Error al eliminar el promotor.");
            return false;
        }
    }

    /*********************************************
     * **************** MÉTODOS *****************
     *********************************************/
    /**
     * Verifica si existe en la base de datos el promotor que se quiere agregar en
     * base a su nombre. En si esta clase
     * utiliza el método existe de promotor que extiende de GenericDAO al cual se le
     * pasa un Map con la clase y nombre
     * del atributo que se quiere verificar si existe.
     *
     * @return true si existe, false en caso contrario.
     */
    private boolean exist() {

        Map<String, Object> propiedades = new HashMap<>();
        propiedades.put("class", Promotor.class);
        propiedades.put("nombre", promotor.getNombre());
        if (Promotor.existe(propiedades)) {
            return true;
        } else {
            propiedades.clear();
            propiedades.put("class", Promotor.class);
            propiedades.put("telfono", promotor.getTelfono());

            if (Promotor.existe(propiedades)) {
                return true;
            }
        }
        return false;
    }

}
