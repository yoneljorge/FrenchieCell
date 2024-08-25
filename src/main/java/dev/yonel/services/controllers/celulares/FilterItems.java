package dev.yonel.services.controllers.celulares;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.utils.data_access.UtilsHibernate;
import javafx.application.Platform;

public class FilterItems {

    private static final SessionFactory sessionFactory = UtilsHibernate.getSessionFactory();

    private Marca marca;
    private Modelo modelo;
    private LocalDate fecha;
    private String imei;
    private boolean dual = false;
    private boolean vendido = false;

    public FilterItems() {
        ServiceCelularControllerVista.cleanVBox();
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setDual(boolean dual) {
        this.dual = dual;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }

    /**
     * Método con el cual se obtienen todos los celulares desde la Base de Datos.
     * Éste método obtiene los datos de uno en uno para evitar sobrecargar la
     * aplicación.
     */
    public void getAllItems() {
        Platform.runLater(() -> {
            // Quitamos todos los items del VBox para que no se repitan
            ServiceCelularControllerVista.cleanVBox();
            
            Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
            try {
                String hql = "FROM Celular"; // Tu entidad celular
                Query<Celular> query = sessionFactory.getCurrentSession().createQuery(hql, Celular.class);
                query.setFirstResult(0); // Desde el primer registro
                query.setMaxResults(1); // Solo un registro

                while (true) {
                    List<Celular> resultList = query.list();
                    if (resultList.isEmpty()) {
                        break; // Si no hay más resultados, salir
                    }

                    // setItems(resultList.get(0));
                    ServiceCelularControllerVista.setItems(filtrar(resultList.get(0)));

                    // Incrementa el desplazamiento para el siguiente
                    int currentFirstResult = query.getFirstResult();
                    query.setFirstResult(currentFirstResult + 1);
                }

                tx.commit(); // Finaliza la transacción.
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();// Deshacer la transacción en caso de error.
                }
                e.printStackTrace();// Maneja o registra la excepción
            }

            // Invertimos el orden de los celulares
            ServiceCelularControllerVista.invertirOrden();
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
            } else {
                if (celular.getImeiDos() != 0) {
                    return verificarCelular(celular);
                }
            }
        } else if (vendido) {
            if (celular.getVendido()) {
                return verificarCelular(celular);
            }
        } else {
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
