package dev.yonel.services.celulares;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.models.Vale;
import dev.yonel.services.Gatillo;
import dev.yonel.services.Mensajes;
import dev.yonel.services.ProxyABaseDeDatos;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerAgregar;
import dev.yonel.services.vales.ServiceVales;
import dev.yonel.utils.fechaUtil.FechaUtil;
import dev.yonel.utils.validation.Validator;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

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
    private List<Celular> listImei;
    private ObservableList<Celular> observableListImei;

    // Objeto en el cual vamos a almacenar los mensajes
    Mensajes mensajes = new Mensajes(ServiceCelular.class);

    private ServiceCelularControllerAgregar serviceAgregar = ServiceCelularControllerAgregar.getInstance();

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

    public Celular getCelular() {
        return this.celular;
    }

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
        return FechaUtil.getStringOfLocalDate(this.fechaInventario);
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
     * Método para guardar los datos del celular. Antes de guardar se verifica que
     * tenga todos los campos y que no
     * exista el imei en la base de datos.
     *
     * @return true si se guardan los datos del celular, false en caso de que
     *         presente error, de que el imei exista o
     *         que falten datos.
     */
    public boolean saveCelular() {
        if (isFull()) {
            updateCelular();
            mensajes.info("Actualizando");
            serviceAgregar.setEstadoInformation("Actualizando");
            if (!existImei()) {
                if (celular.save()) {
                    serviceAgregar.setEstadoInformation("Celular guardado.");
                    // Notificamos al ServiceList que hay cambios
                    Gatillo.newCelular();

                    return true;
                } else {
                    serviceAgregar.setEstadoError("Error en conexión con base de datos.");
                    return false;
                }
            } else {
                serviceAgregar.setEstadoError("Celular no guardado, el imei existe.");
                return false;
            }

        } else {
            serviceAgregar.setEstadoError("Celular no guardado, faltan datos.");
            return false;
        }
    }

    public boolean update() {
        if (isFull()) {
            updateCelular();
            if (celular.update()) {
                // Notificamos al ServiceList que hay cambios
                Gatillo.newCelular();
                mensajes.info("Celular actualizado");
                return true;
            } else {
                mensajes.err("Celular no actualizado, errorn en conexión con base de datos.");
                return false;
            }
        } else {
            mensajes.err("Celular no actualizado, faltan datos.");
            return false;
        }
    }

    /**
     * <p>
     * Méotodo con le cual camos a eliminar un celular de la base de datos.
     * </p>
     * 
     * @return
     *         <p>
     *         <code>true</code> si se elimina el celular sin errores.
     *         </p>
     *         <p>
     *         <code>false</code> si presenta errores al eliminar el celular.
     *         </p>
     */
    public boolean delete() {

        if (celular.delete()) {
            // Eliminamos el vale correspondiente
            Vale vale = getValeForCelular();
            ServiceVales serviceVales = new ServiceVales(vale);
            serviceVales.eliminar();
            mensajes.info("Celular " + celular.getImeiUno() + " eliminado.");
            //Informamos de que hay cambios en los celulares
            Gatillo.newCelular();
            return true;
        } else {
            mensajes.err("Error al eliminar el celular.");
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

    public void configureFilterComboBoxImei(MFXFilterComboBox<Celular> comboBox) {
        if (listImei == null) {
            listImei = new ArrayList<>();
        }
        if (observableListImei == null) {
            observableListImei = FXCollections.observableArrayList();
        }

        listImei.clear();
        listImei.addAll(ProxyABaseDeDatos.getListCelulares());

        observableListImei.clear();
        observableListImei = FXCollections.observableArrayList(listImei);

        StringConverter<Celular> converter = FunctionalStringConverter
                .to(celular -> (celular == null) ? "" : String.valueOf(celular.getImeiUno()));
        Function<String, Predicate<Celular>> filteFunction = s -> celular -> StringUtils
                .containsIgnoreCase(converter.toString(celular), s);

        comboBox.setItems(observableListImei);
        comboBox.setConverter(converter);
        comboBox.setFilterFunction(filteFunction);
    }

    public void configureFilterComboBoxImeiForAgregarVales(MFXFilterComboBox<Celular> comboBox) {
        if (listImei == null) {
            listImei = new ArrayList<>();
        }
        if (observableListImei == null) {
            observableListImei = FXCollections.observableArrayList();
        }

        listImei.clear();
        /*
         * En caso de que el celular no esté vendido entonces es que se agrega a la
         * lista de celulares.
         */
        for (Celular c : ProxyABaseDeDatos.getListCelulares()) {
            if (!c.getVendido()) {
                listImei.add(c);
            }
        }

        observableListImei.clear();
        observableListImei = FXCollections.observableArrayList(listImei);

        StringConverter<Celular> converter = FunctionalStringConverter
                .to(celular -> (celular == null) ? "" : String.valueOf(celular.getImeiUno()));
        Function<String, Predicate<Celular>> filteFunction = s -> celular -> StringUtils
                .containsIgnoreCase(converter.toString(celular), s);

        comboBox.setItems(observableListImei);
        comboBox.setConverter(converter);
        comboBox.setFilterFunction(filteFunction);
    }

    /**
     * Método que configura un MFXFilterComboBox de tipo Celular. Selecciona el
     * celular que es pasado como parámetro.
     *
     * @param comboBox    que se desea configurar.
     * @param imeiCelular el imei del celular que se desea seleccionar.
     */
    public void configureFilterComboBoxImeiForEditarVales(MFXFilterComboBox<Celular> comboBox, Celular imeiCelular) {
        if (listImei == null) {
            listImei = new ArrayList<>();
        }
        if (observableListImei == null) {
            observableListImei = FXCollections.observableArrayList();
        }

        listImei.clear();
        /*
         * En caso de que el celular no esté vendido entonces es que se agrega a la
         * lista de celulares.
         */
        for (Celular c : ProxyABaseDeDatos.getListCelulares()) {
            if (!c.getVendido()) {
                listImei.add(c);
            }
            if (c.getImeiUno().equals(imeiCelular.getImeiUno())) {
                listImei.add(c);
            }
        }

        observableListImei.clear();
        observableListImei = FXCollections.observableArrayList(listImei);

        StringConverter<Celular> converter = FunctionalStringConverter
                .to(celular -> (celular == null) ? "" : String.valueOf(celular.getImeiUno()));
        Function<String, Predicate<Celular>> filteFunction = s -> celular -> StringUtils
                .containsIgnoreCase(converter.toString(celular), s);

        comboBox.setItems(observableListImei);
        comboBox.setConverter(converter);
        comboBox.setFilterFunction(filteFunction);

        // Si se encuentra el celular entonces se selecciona.
        if (observableListImei != null) {
            for (Celular c : observableListImei) {
                if (c.getImeiUno().equals(imeiCelular.getImeiUno())) {
                    comboBox.getSelectionModel().selectItem(c);
                    break;
                }
            }
        }
    }

    /**
     * <p>
     * Método con el cual vamos a obtener el vale correspondiente al celular actual.
     * </p>
     * 
     * @return
     *         <p>
     *         <code>vale</code> si hay un vale asociado a este celular.
     *         </p>
     *         <p>
     *         <code>null</code> en caso de que no halla un vale asociado este
     *         celular.
     *         </p>
     */
    public Vale getValeForCelular() {
        mensajes.info("Buscando el vale de este celular");
        Vale vale;
        while ((vale = Vale.getAllOneToOne(Vale.class)) != null) {
            if (vale.getImei().getImeiUno().equals(celular.getImeiUno())) {
                mensajes.info("Vale encontrado");
                return vale;
            }
        }
        mensajes.info("No hay vales asociados a este celular.");
        return null;
    }

    @Override
    public String toString() {
        return "ServiceCelular [idCelular=" + idCelular + ", marca=" + marca + ", modelo=" + modelo + ", imei_Uno="
                + imei_Uno + ", imei_Dos=" + imei_Dos + ", precio=" + precio + ", fechaInventario=" + fechaInventario
                + ", vendido=" + vendido + ", observaciones=" + observaciones + ", dualSim=" + dualSim + ", celular="
                + celular + "]";
    }

    
}
