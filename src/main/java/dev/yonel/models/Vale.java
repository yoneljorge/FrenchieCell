package dev.yonel.models;

import java.io.Serializable;
import java.time.LocalDate;

import dev.yonel.utils.data_access.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vales")
public class Vale extends BaseEntity<Vale, Long> implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vales")
    private Long idVales;

    @ManyToOne
    @JoinColumn(name = "promotor", referencedColumnName = "nombre")
    private Promotor promotor;

    @ManyToOne
    @JoinColumn(name = "marca", referencedColumnName = "marca")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "modelo", referencedColumnName = "modelo")
    private Modelo modelo;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "moneda")
    private String moneda;

    @Column(name = "mensajeria")
    private Boolean mensajeria;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "costo_mensajeria")
    private Long costoMensajeria;

    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;

    @Column(name = "fecha_garantia")
    private LocalDate fechaGarantia;

    @Column(name = "comision")
    private Long comision;

    public Vale(){}

    public Vale(Long idVales, Promotor promotor, Marca marca, Modelo modelo, Double precio, String moneda,
            Boolean mensajeria, String direccion, Long costoMensajeria, LocalDate fechaVenta, LocalDate fechaGarantia,
            Long comision) {
        this.idVales = idVales;
        this.promotor = promotor;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.moneda = moneda;
        this.mensajeria = mensajeria;
        this.direccion = direccion;
        this.costoMensajeria = costoMensajeria;
        this.fechaVenta = fechaVenta;
        this.fechaGarantia = fechaGarantia;
        this.comision = comision;
    }

    public Long getIdVales() {
        return idVales;
    }

    public void setIdVales(Long idVales) {
        this.idVales = idVales;
    }

    public Promotor getPromotor() {
        return promotor;
    }

    public void setPromotor(Promotor promotor) {
        this.promotor = promotor;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Boolean getMensajeria() {
        return mensajeria;
    }

    public void setMensajeria(Boolean mensajeria) {
        this.mensajeria = mensajeria;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getCostoMensajeria() {
        return costoMensajeria;
    }

    public void setCostoMensajeria(Long costoMensajeria) {
        this.costoMensajeria = costoMensajeria;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public LocalDate getFechaGarantia() {
        return fechaGarantia;
    }

    public void setFechaGarantia(LocalDate fechaGarantia) {
        this.fechaGarantia = fechaGarantia;
    }

    public Long getComision() {
        return comision;
    }

    public void setComision(Long comision) {
        this.comision = comision;
    }
}
