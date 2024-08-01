package dev.yonel.models;

import java.io.Serializable;

import dev.yonel.utils.data_access.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "promotor")
public class Promotor extends BaseEntity<Promotor, Long> implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promotor")
    private Long idPromotor;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "telefono")
    private Long telfono;

    @Column(name = "total_de_vales")
    private Long totalDeVales;

    @Column(name = "vales_en_garantia")
    private Long valesEnGarantia;

    @Column(name = "vales_por_pagar")
    private Long valesPorPagar;

    @Column(name = "dinero_total")
    private Long dineroTotal;


    public Promotor(){}


    public Promotor(Long idPromotor, String nombre, String apellidos, Long telfono, Long totalDeVales,
            Long valesEnGarantia, Long valesPorPagar, Long dineroTotal) {
        this.idPromotor = idPromotor;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telfono = telfono;
        this.totalDeVales = totalDeVales;
        this.valesEnGarantia = valesEnGarantia;
        this.valesPorPagar = valesPorPagar;
        this.dineroTotal = dineroTotal;
    }


    public Long getIdPromotor() {
        return idPromotor;
    }


    public void setIdPromotor(Long idPromotor) {
        this.idPromotor = idPromotor;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getApellidos() {
        return apellidos;
    }


    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }


    public Long getTelfono() {
        return telfono;
    }


    public void setTelfono(Long telfono) {
        this.telfono = telfono;
    }


    public Long getTotalDeVales() {
        return totalDeVales;
    }


    public void setTotalDeVales(Long totalDeVales) {
        this.totalDeVales = totalDeVales;
    }


    public Long getValesEnGarantia() {
        return valesEnGarantia;
    }


    public void setValesEnGarantia(Long valesEnGarantia) {
        this.valesEnGarantia = valesEnGarantia;
    }


    public Long getValesPorPagar() {
        return valesPorPagar;
    }


    public void setValesPorPagar(Long valesPorPagar) {
        this.valesPorPagar = valesPorPagar;
    }


    public Long getDineroTotal() {
        return dineroTotal;
    }


    public void setDineroTotal(Long dineroTotal) {
        this.dineroTotal = dineroTotal;
    }


    @Override
    public String toString() {
        return nombre;
    }
    
}
