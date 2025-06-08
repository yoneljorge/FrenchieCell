package dev.yonel.services.controllers.dashboard;

import java.util.ArrayList;
import java.util.List;

public class CelularInfo {

    private int cantidad;
    private List<Double> precios;
    private List<String> comentarios;
    //private Map<Double, String> parPrecioObservaciones;

    public CelularInfo(int cantidadInicial, Double precioInicial, String comentarioInicial) {
        this.cantidad = cantidadInicial;
        this.precios = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        
        //this.parPrecioObservaciones = new HashMap<>();

        this.precios.add(precioInicial);
        if (comentarioInicial != null && !comentarioInicial.isEmpty()) {
            this.comentarios.add(comentarioInicial);
        }
    }

    public void addCelular(Double precio, String comentario) {
        this.cantidad++;
        if (!this.precios.contains(precio)) {
            this.precios.add(precio);
        }
        if (comentario != null && !comentario.isEmpty() && !this.comentarios.contains(comentario)) {
            this.comentarios.add(comentario);
        }
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public List<Double> getPrecios() {
        return this.precios;
    }

    public boolean hayPreciosDistintos() {
        return this.precios.size() > 1;
    }

    public List<String> getComentarios() {
        return this.comentarios;
    }
}
