package dev.yonel.services.promotores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.ServiceLista;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerAgregar;
import dev.yonel.utils.AlertUtil;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

public class ServicePromotor {

    private Promotor promotor;

    // Listas para los promotores
    private List<Promotor> list;
    private ObservableList<Promotor> observableList;

    public ServicePromotor() {
        this.promotor = new Promotor();
    }

    public ServicePromotor(Promotor promotor) {
        this.promotor = promotor;
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

    /*********************************************
     * **********MÉTODOS**************************
     *********************************************/

    /**
     * Método para configurar un MFXFilterComboBox<Promotor>.
     * Con este método se le asigna el filtro al combo y la lista de promotores.
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
        list.addAll(ServiceLista.getListPromotores());

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

    public boolean save() {
        if (!exist()) {
            if (promotor.save()) {
                ServicePromotoresControllerAgregar.getInstance().setEstadoInformativo("Gestor guardado.");
                // Notificamos de que ha sido agregado un nuevo promotor.
                ServiceLista.setCambioPromotor(true);
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

    public boolean update() {

        if (promotor.update()) {
            ServiceLista.setCambioPromotor(true);
            return true;
        } else {

            return false;
        }
    }

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


    public void updateVales() {
        System.out.println("Actualizando vales Promotor: " + promotor.getNombre());
        List<Vale> listVales = ServiceLista.getListValesByPromotor(promotor.getIdPromotor());
        long enGarantia = 0l;
        long liquidados = 0l;
        long porPagar = 0l;

        for (Vale v : listVales) {
            if (v.getLiquidado() != null || v.getLiquidado()) {
                liquidados++;
            } else {
                if (v.getGarantia() == null || v.getGarantia()) {
                    enGarantia++;
                } else {
                    porPagar++;
                }
            }
        }
        promotor.setValesEnGarantia(enGarantia);
        promotor.setValesPagados(liquidados);
        promotor.setValesPorPagar(porPagar);

        promotor.setTotalDeVales(
                promotor.getValesEnGarantia() + promotor.getValesPagados() + promotor.getValesPorPagar());
    }

    private void updateVales(long idPromotor) {
        
        Promotor promotor = Promotor.getById(Promotor.class, idPromotor);

        System.out.println("Actualizando vales Promotor: " + promotor.getNombre());
        List<Vale> listVales = ServiceLista.getListValesByPromotor(promotor.getIdPromotor());
        long enGarantia = 0l;
        long liquidados = 0l;
        long porPagar = 0l;

        for (Vale v : listVales) {
            if (v.getLiquidado()) {
                liquidados++;
            } else {
                if (v.getGarantia()) {
                    enGarantia++;
                } else {
                    porPagar++;
                }
            }
        }
        promotor.setValesEnGarantia(enGarantia);
        promotor.setValesPagados(liquidados);
        promotor.setValesPorPagar(porPagar);

        promotor.setTotalDeVales(
                promotor.getValesEnGarantia() + promotor.getValesPagados() + promotor.getValesPorPagar());
    }

    public void updateValesAllPromotor(){
        list.clear();
        list.addAll(ServiceLista.getListPromotores());
        for(Promotor p : list){
            updateVales(p.getIdPromotor());
        }
    }
}
