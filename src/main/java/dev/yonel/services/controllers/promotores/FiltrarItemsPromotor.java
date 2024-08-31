package dev.yonel.services.controllers.promotores;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dev.yonel.models.Promotor;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerVista;
import dev.yonel.utils.data_access.UtilsHibernate;
import javafx.application.Platform;
import lombok.Setter;

public class FiltrarItemsPromotor {

    private static final SessionFactory sessionFactory = UtilsHibernate.getSessionFactory();

    private @Setter boolean porPagar = false;
    private @Setter boolean enGarantia = false;

    private ServiceCelularControllerVista serviceCelularesVista = ServiceCelularControllerVista.getInstance();
    private ServicePromotoresControllerVista servicePromotoresVista = ServicePromotoresControllerVista.getInstance();

    public void getAllItems() {
        Platform.runLater(() -> {
            // Quitamos todos los items del VBox para que no se repitan
            serviceCelularesVista.cleanVBox();

            Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
            try {
                String hql = "FROM Promotor"; // Tu entidad
                Query<Promotor> query = sessionFactory.getCurrentSession().createQuery(hql, Promotor.class);
                query.setFirstResult(0); // Desde el primer registro
                query.setMaxResults(1); // Solo un registro

                while (true) {
                    List<Promotor> resultList = query.list();
                    if (resultList.isEmpty()) {
                        break; // Si no hay más resultados, salir
                    }

                    // setItems(resultList.get(0));
                    servicePromotoresVista.setItems(filtrar(resultList.get(0)));

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
            serviceCelularesVista.invertirOrden();
        });
    }

    private Promotor filtrar(Promotor promotor) {

        if (enGarantia) {
            if (porPagar) {
                if (promotor.getValesEnGarantia() != null && promotor.getValesEnGarantia() > 0) {
                    if (promotor.getValesPorPagar() != null && promotor.getValesPorPagar() > 0) {
                        return promotor;
                    }
                }
            } else {
                if (promotor.getValesEnGarantia() != null && promotor.getValesEnGarantia() > 0) {
                    return promotor;
                }
            }
        } else if (porPagar) {
            if (promotor.getValesPorPagar() != null && promotor.getValesPorPagar() > 0) {
                return promotor;
            }
        } else {
            return promotor;
        }

        return null;
    }
}
