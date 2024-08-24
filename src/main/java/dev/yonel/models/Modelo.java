package dev.yonel.models;

import java.io.Serializable;

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
@Table(name = "modelo")
public class Modelo extends BaseEntity<Modelo, Long> implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Long idModelo;

    @ManyToOne
    @JoinColumn(name = "marca", referencedColumnName = "marca")
    private Marca marca;

    @Column(name = "modelo")
    private String modelo;

    public Modelo(){}

    public Modelo(String modelo, Marca marca){
        this.modelo = modelo;
        this.marca = marca;
    }

    public Modelo(Long idModelo, Marca marca, String modelo) {
        this.idModelo = idModelo;
        this.marca = marca;
        this.modelo = modelo;
    }

    public Long getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Long idModelo) {
        this.idModelo = idModelo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo.toUpperCase();
    }

    @Override
    public String toString() {
        return modelo;
    }

    
}
