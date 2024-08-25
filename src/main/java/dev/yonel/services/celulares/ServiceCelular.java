package dev.yonel.services.celulares;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.ServiceLista;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerAgregar;
import dev.yonel.utils.Fecha;
import dev.yonel.utils.validation.Validator;

public class ServiceCelular {

    private Long idCelular;
    private Marca marca;
    private Modelo modelo;
    private Long imei_Uno;
    private Long imei_Dos;
    private Double precio;
    private LocalDate fechaInventario;
    private boolean vendido;
    private String observaciones;
    private String dualSim;

    private Celular celular;

    public ServiceCelular() {
        this.celular = new Celular();
        this.marca = new Marca();
        this.modelo = new Modelo();
    }

    public ServiceCelular(Celular celular) {
        this.celular = celular;
        this.idCelular = celular.getIdCelular();
        this.marca = celular.getMarca();
        this.modelo = celular.getModelo();
        this.imei_Uno = celular.getImeiUno();
        this.imei_Dos = celular.getImeiDos();
        this.precio = celular.getPrecio();
        this.fechaInventario = celular.getFechaInventario();
        this.observaciones = celular.getObservaciones();
        this.vendido = celular.getVendido();

        if (this.imei_Dos == 0 || this.imei_Dos == null) {
            this.dualSim = "NO";
        } else {
            this.dualSim = "SI";
        }
    }

    /*****************************************
     * ********SETTERS AND GUETTERS***********
     *****************************************/

    public Long getIdCelular() {
        return this.idCelular;
    }

    public void setMarca(Marca marca) {
        marca.setMarca(marca.getMarca().toUpperCase());
        this.marca = marca;
    }

    public String getMarca() {
        return marca.getMarca();
    }

    public void setModelo(Modelo modelo) {
        modelo.setModelo(modelo.getModelo().toUpperCase());
        this.modelo = modelo;
    }

    public String getModelo() {
        return modelo.getModelo();
    }

    public void setImei_Uno(String imei_Uno) {
        if (Validator.isIMEI(imei_Uno)) {
            this.imei_Uno = Long.parseLong(imei_Uno);
        } else {
            throw new IllegalArgumentException("No es un IMEI válido.");
        }

    }

    public String getImei_Uno() {
        return String.valueOf(this.imei_Uno);
    }

    public void setImei_Dos(String imei_Dos) {
        if (imei_Dos.equals("")) {
            this.imei_Dos = null;
        } else if (Validator.isIMEI(imei_Dos)) {
            this.imei_Dos = Long.parseLong(imei_Dos);
        } else {
            throw new IllegalArgumentException("No es un IMEI válido.");
        }
    }

    public String getImei_Dos() {
        return String.valueOf(this.imei_Dos);
    }

    public void setPrecio(String precio) {
        this.precio = Double.parseDouble(precio);
    }

    public String getPrecio() {
        return String.valueOf(this.precio);
    }

    public void setFechaInventario(LocalDate fechaInventario) {
        this.fechaInventario = fechaInventario;
    }

    public String getFechaInventario() {
        return Fecha.getStringOfLocalDate(this.fechaInventario);
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public String getDualSim() {

        return this.dualSim;
    }

    public boolean getVendido() {
        return this.vendido;

    }

    /*****************************************
     * ***********MÉTODOS*********************
     *****************************************/

    /**
     * Método con el cual vamos a actualizar la información de celuar antes de
     * realizar cualquier acción.
     */
    private void updateCelular() {
        celular.setMarca(marca);
        celular.setModelo(modelo);
        celular.setImeiUno(imei_Uno);
        celular.setImeiDos(imei_Dos);
        celular.setFechaInventario(fechaInventario);
        celular.setPrecio(precio);
        celular.setObservaciones(observaciones);
    }

    /**
     * Método para comprobar que todos los campos importantes están llenos para que
     * no se produzca un error.
     * 
     * @return
     */
    private boolean isFull() {
        if (marca.getMarca().equals("") || marca.getMarca() == null) {
            return false;
        }
        if (modelo.getModelo().equals("") || modelo.getModelo() == null) {
            return false;
        }
        if (imei_Uno == null) {
            return false;
        }
        if (imei_Dos == null) {
            imei_Dos = (long) 0;
        }
        if (fechaInventario == null) {
            return false;
        }
        if (precio == null || precio == 0) {
            return false;
        }
        if (observaciones == null) {
            observaciones = "";
        }

        return true;
    }

    /**
     * Método para guardar los datos del celular.
     * Antes de guardar se verifica que tenga todos los campos y que no exista el
     * imei en la base de datos.
     * 
     * @return true si se guardan los datos del celular, false en caso de que
     *         presente error, de que el imei exista o que falten datos.
     */
    public boolean saveCelular() {
        if (isFull()) {
            updateCelular();
            ServiceCelularControllerAgregar.setEstadoInformation("Actualizando");
            if (!existImei()) {
                if (celular.save()) {
                    ServiceCelularControllerAgregar.setEstadoInformation("Celular guardado.");
                    // Notificamos al ServiceList que hay cambios
                    ServiceLista.setCambioCelular(true);
                    return true;
                } else {
                    ServiceCelularControllerAgregar.setEstadoError("Error en conexión con base de datos.");
                    return false;
                }
            } else {
                ServiceCelularControllerAgregar.setEstadoError("Celular no guardado, el imei existe.");
                return false;
            }

        } else {
            ServiceCelularControllerAgregar.setEstadoError("Celular no guardado, faltan datos.");
            return false;
        }
    }

    /**
     * Método con el que se comprueba si existe en la base de datos el IMEI
     * introducido.
     * 
     * @return true en caso de que exista, false en caso contrario.
     */
    private boolean existImei() {
        Map<String, Object> propiedades = new HashMap<>();
        propiedades.put("class", Celular.class);
        propiedades.put("imeiUno", imei_Uno);

        return Celular.existe(propiedades);
    }

    public boolean delete() {
        return celular.delete();
    }
}
