package dev.yonel.services.controllers.celulares;

import java.time.LocalDate;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import javafx.application.Platform;
import lombok.Setter;

@Setter
public class FilterItemsCelular {

    private Marca marca;
    private Modelo modelo;
    private LocalDate fecha;
    private String imei;
    private boolean dual = false;
    private boolean vendido = false;
    private boolean noVendido = false;

    private ServiceCelularControllerVista serviceVista = ServiceCelularControllerVista.getInstance();
    public FilterItemsCelular() {
        serviceVista.cleanVBox();
    }


    /**
     * Método con el cual se obtienen todos los celulares desde la Base de Datos.
     * Éste método obtiene los datos de uno en uno para evitar sobrecargar la
     * aplicación.
     */
    public void getAllItems() {
        Platform.runLater(() -> {
            // Quitamos todos los items del VBox para que no se repitan
            serviceVista.cleanVBox();

            Celular celular;
            while ((celular = Celular.getAllOneToOne(Celular.class)) != null){
                serviceVista.setItems(filtrar(celular));
            }

            // Invertimos el orden de los celulares
            serviceVista.invertirOrden();
        });

    }

    private Celular filtrar(Celular celular) {

        if (dual) {
            if (vendido) {
                if (celular.getImeiDos() != 0) {
                    if (celular.getVendido()) {
                        return verificarCelular(celular);
                    }
                }
            } else if(noVendido){
                if (celular.getImeiDos() != 0) {
                    if(!celular.getVendido()){
                        return verificarCelular(celular);
                    }  
                }
            }else{
                if(celular.getImeiDos() != 0){
                    return verificarCelular(celular);
                }
            }
        } else if (vendido) {
            if (celular.getVendido()) {
                return verificarCelular(celular);
            }
        } else if(noVendido){
            if(!celular.getVendido()){
                return verificarCelular(celular);
            }
            
        }else{
            return verificarCelular(celular);
        }

        return null;
    }

    private Celular verificarCelular(Celular celular) {
        if (marca != null) {
            if (modelo != null) {
                if (fecha != null) {
                    if (imei != null) {
                        // se filtra marca, modelo, fecha e imei.
                        if (celular.getMarca().getMarca().equals(marca.getMarca())) {
                            if (celular.getModelo().getModelo().equals(modelo.getModelo())) {
                                if (celular.getFechaInventario().equals(fecha)) {
                                    if (String.valueOf(celular.getImeiUno()).contains(imei)) {
                                        return celular;
                                    }
                                }
                            }
                        }
                    } else {
                        // se filtra marca, modelo y fecha
                        if (celular.getMarca().getMarca().equals(marca.getMarca())) {
                            if (celular.getModelo().getModelo().equals(modelo.getModelo())) {
                                if (celular.getFechaInventario().equals(fecha)) {
                                    return celular;
                                }
                            }
                        }
                    }
                } else if (imei != null) {
                    // se filtra marca, modelo e imei.
                    if (celular.getMarca().getMarca().equals(marca.getMarca())) {
                        if (celular.getModelo().getModelo().equals(modelo.getModelo())) {
                            if (String.valueOf(celular.getImeiUno()).contains(imei)) {
                                return celular;
                            }
                        }
                    }
                } else {
                    // se filtra marca y modelo
                    if (celular.getMarca().getMarca().equals(marca.getMarca())) {
                        if (celular.getModelo().getModelo().equals(modelo.getModelo())) {
                            return celular;
                        }
                    }
                }
            } else {
                if (fecha != null) {
                    if (imei != null) {
                        // filtra marca, fecha e imei
                        if (celular.getMarca().getMarca().equals(marca.getMarca())) {
                            if (celular.getFechaInventario().equals(fecha)) {
                                if (String.valueOf(celular.getImeiUno()).contains(imei)) {
                                    return celular;
                                }
                            }
                        }
                    } else {
                        // filtra marca y fecha
                        if (celular.getMarca().getMarca().equals(marca.getMarca())) {
                            if (celular.getFechaInventario().equals(fecha)) {
                                return celular;
                            }
                        }
                    }
                } else {
                    // filtra marca e imei
                    if (imei != null) {
                        if (celular.getMarca().getMarca().equals(marca.getMarca())) {
                            if (String.valueOf(celular.getImeiUno()).contains(imei)) {
                                return celular;
                            }
                        }
                    } else {
                        // filtra marca
                        if (celular.getMarca().getMarca().equals(marca.getMarca())) {
                            return celular;
                        }
                    }

                }
            }
        } else if (fecha != null) {
            if (imei != null) {
                // se filtra fecha e imei
                if (celular.getFechaInventario().equals(fecha)) {
                    if (String.valueOf(celular.getImeiUno()).contains(imei)) {
                        return celular;
                    }
                }
            } else {
                // se filtra la fecha
                if (celular.getFechaInventario().equals(fecha)) {
                    return celular;
                }
            }
        } else {
            // se filtra el imei
            if (imei != null) {
                if (String.valueOf(celular.getImeiUno()).contains(imei)) {
                    return celular;
                }
            } else {
                return celular;
            }
        }
        /*
         * En caso de que no coincida con el filtrado se pasa null para que no sea
         * agregado.
         */
        return null;
    }
}
