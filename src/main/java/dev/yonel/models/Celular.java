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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "celulares")
public class Celular extends BaseEntity<Celular, Long> implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_celular")
    private Long idCelular;

    @ManyToOne
    @JoinColumn(name = "marca", referencedColumnName = "marca")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "modelo", referencedColumnName = "modelo")
    private Modelo modelo;

    @Column(name = "imei_uno")
    private Long imeiUno = 0L;

    @Column(name = "imei_dos")
    private Long imeiDos = 0L;

    @Column(name = "fecha_inventario")
    private LocalDate fechaInventario;

    @Column(name = "vendido")
    private Boolean vendido = false;
    
    @Column(name = "precio")
    private Double precio = 0d;

    @Column(name = "observaciones")
    private String observaciones = "";


    //Constructor para un IMEI
    public Celular(Long idCelular, Marca marca, Modelo modelo, Long imeiUno, LocalDate fechaInventario,
            Boolean vendido, Double precio, String observaciones) {
        this.idCelular = idCelular;
        this.marca = marca;
        this.modelo = modelo;
        this.imeiUno = imeiUno;
        this.fechaInventario = fechaInventario;
        this.vendido = vendido;
        this.precio = precio;
        this.observaciones = observaciones;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Celular other = (Celular) obj;
        if (idCelular == null) {
            if (other.idCelular != null)
                return false;
        } else if (!idCelular.equals(other.idCelular))
            return false;
        if (marca == null) {
            if (other.marca != null)
                return false;
        } else if (!marca.equals(other.marca))
            return false;
        if (modelo == null) {
            if (other.modelo != null)
                return false;
        } else if (!modelo.equals(other.modelo))
            return false;
        if (imeiUno == null) {
            if (other.imeiUno != null)
                return false;
        } else if (!imeiUno.equals(other.imeiUno))
            return false;
        if (imeiDos == null) {
            if (other.imeiDos != null)
                return false;
        } else if (!imeiDos.equals(other.imeiDos))
            return false;
        if (fechaInventario == null) {
            if (other.fechaInventario != null)
                return false;
        } else if (!fechaInventario.equals(other.fechaInventario))
            return false;
        if (vendido == null) {
            if (other.vendido != null)
                return false;
        } else if (!vendido.equals(other.vendido))
            return false;
        if (precio != other.precio)
            return false;
        if (observaciones == null) {
            if (other.observaciones != null)
                return false;
        } else if (!observaciones.equals(other.observaciones))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(imeiUno);
    }
}
