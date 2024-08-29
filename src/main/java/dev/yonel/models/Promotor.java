package dev.yonel.models;

import java.io.Serializable;

import dev.yonel.utils.data_access.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "dinero_total_por_pagar")
    private Long dineroTotalPorPagar;

    @Column(name = "dinero_total_pagado")
    private Long dineroTotalPagado;
}
