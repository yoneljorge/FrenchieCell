package dev.yonel.services.celulares;

import java.time.LocalDate;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;

public class AgregarCelular {

    private Marca marca = new Marca();
    private Modelo modelo = new Modelo();
    private Long imei_Uno;
    private Long imei_Dos;
    private Double precio;
    private LocalDate fechaInventario;
    private String observaciones;

    private Celular celular = new Celular();

    public AgregarCelular() {
    }

    public void setMarca(String marca) {
        this.marca.setMarca(marca);
    }

    public void setModelo(String modelo) {
        this.modelo.setModelo(modelo);
        this.modelo.setMarca(this.marca);
    }

    public void setImei_Uno(Long imei_Uno) {
        this.imei_Uno = imei_Uno;
    }

    public void setImei_Dos(Long imei_Dos) {
        this.imei_Dos = imei_Dos;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setFechaInventario(LocalDate fechaInventario) {
        this.fechaInventario = fechaInventario;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

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

    public boolean saveCelular() {
        updateCelular();

        if (celular.save()) {
            return true;
        } else {
            return false;
        }
    }
}
