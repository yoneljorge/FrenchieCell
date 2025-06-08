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
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Accessors(fluent = false, chain = true) //Esto es para el encadenamiento de métodos
@Table(name = "vales")
public class Vale extends BaseEntity<Vale, Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vales")
    private Long idVales;

    @ManyToOne
    @JoinColumn(name = "promotor", referencedColumnName = "nombre")
    private Promotor promotor;

    @Column(name = "comision")
    private Long comision = 0l;


    @ManyToOne
    @JoinColumn(name = "imei", referencedColumnName = "imei_uno")
    private Celular imei;
    @ManyToOne
    @JoinColumn(name = "marca", referencedColumnName = "marca")
    private Marca marca;
    @ManyToOne
    @JoinColumn(name = "modelo", referencedColumnName = "modelo")
    private Modelo modelo;
    @Column(name = "precio")
    private Double precio = 0d;

    @Column(name = "mensajeria")
    private Boolean mensajeria = false;
    @Column(name = "direccion")
    private String direccion = "";
    @Column(name = "costo_mensajeria")
    private Long costoMensajeria = 0l;

    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;
    @Column(name = "fecha_garantia")
    private LocalDate fechaGarantia;
    @Column(name = "garantia")
    private Boolean garantia = true;
    @Column(name = "dias_garantia")
    private Integer diasGarantia;

    @Column(name = "cliente")
    private String cliente = "";
    @Column(name = "cliente_telefono")
    private Long clienteTelefono = 0l;
    @Column(name = "liquidado")
    private Boolean liquidado = false;

    @Override
    public String toString() {
        return "Vale{" +
                "\nidVales=" + idVales +
                "\npromotor=" + promotor +
                "\ncomision=" + comision +
                "\nimei=" + imei +
                "\nmarca=" + marca +
                "\nmodelo=" + modelo +
                "\nprecio=" + precio +
                "\nmensajeria=" + mensajeria +
                "\ndireccion='" + direccion + '\'' +
                "\ncostoMensajeria=" + costoMensajeria +
                "\nfechaVenta=" + fechaVenta +
                "\nfechaGarantia=" + fechaGarantia +
                "\ngarantia=" + garantia +
                "\ncliente='" + cliente + '\'' +
                "\nclienteTelefono=" + clienteTelefono +
                "\nliquidado=" + liquidado +
                '}';
    }
}
