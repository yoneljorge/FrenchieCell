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
@Table(name = "marca")
public class Marca extends BaseEntity<Marca, Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long idMarca;

    @Column(name = "marca")
    private String marca;

    public Marca(){}

    public Marca(String marca){
        this.marca = marca;
    }

    public Marca(Long idMarca, String marca) {
        this.idMarca = idMarca;
        this.marca = marca;
    }

    public Long getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Long idMarca) {
        this.idMarca = idMarca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {

        this.marca = marca.toUpperCase();
    }

    @Override
    public String toString() {
        return marca;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Marca other = (Marca) obj;
        if (idMarca == null) {
            if (other.idMarca != null)
                return false;
        } else if (!idMarca.equals(other.idMarca))
            return false;
        if (marca == null) {
            if (other.marca != null)
                return false;
        } else if (!marca.equals(other.marca))
            return false;
        return true;
    }

    
}
