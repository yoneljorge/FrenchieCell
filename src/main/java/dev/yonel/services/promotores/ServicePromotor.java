package dev.yonel.services.promotores;

import java.util.HashMap;
import java.util.Map;

import dev.yonel.models.Promotor;
import dev.yonel.services.ServiceLista;
import dev.yonel.services.controllers.promotores.ServicePromotoresControllerAgregar;
import dev.yonel.utils.AlertUtil;

public class ServicePromotor {

    private Promotor promotor;

    private boolean cambio = false;

    public ServicePromotor() {
        this.promotor = new Promotor();
    }

    public ServicePromotor(Promotor promotor) {
        this.promotor = promotor;
    }

    /*********************************************
     * **********SETTER AND GETTER****************
     *********************************************/

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

    public boolean save() {
        if (!exist()) {
            if (promotor.save()) {
                ServicePromotoresControllerAgregar.setEstadoInformativo("Gestor guardado.");
                //Notificamos de que ha sido agregado un nuevo promotor.
                ServiceLista.setCambioPromotor(true);
                return true;
            } else {
                ServicePromotoresControllerAgregar.setEstadoError("Error en conexióncon base de datos.");
                AlertUtil.error(null, "Error en base de datos.\nSi el error persiste contacte al desarrolador.");
                return false;
            }
        } else {
            ServicePromotoresControllerAgregar.setEstadoError("Gestor no guardado, ya existe.");
            return false;
        }
    }

    private boolean exist() {
        
        Map<String, Object> propiedades = new HashMap<>();
        propiedades.put("class", Promotor.class);
        propiedades.put("nombre", promotor.getNombre());
        if(Promotor.existe(propiedades)){
            return true;
        }else{
            propiedades.clear();
            propiedades.put("class", Promotor.class);
            propiedades.put("telfono", promotor.getTelfono());

            if(Promotor.existe(propiedades)){
                return true;
            }
        }
        

        return false;
    }
}
