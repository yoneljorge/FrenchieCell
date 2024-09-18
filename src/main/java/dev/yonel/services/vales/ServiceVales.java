package dev.yonel.services.vales;

import java.time.LocalDate;
import java.util.List;

import dev.yonel.utils.Fecha;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dev.yonel.models.Celular;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.Gatillo;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.utils.data_access.UtilsHibernate;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceVales {

    private Vale vale;

    public ServiceVales(Vale vale) {
        this.vale = vale;
        if (this.vale.getFechaGarantia() == null) {
            this.vale.setFechaGarantia(this.vale.getFechaVenta().plusWeeks(1));
        }

    }
    /********************************************
     **********MÉTODOS CRUD ********************
     */

    /**
     * Método que se utiliza para guardar un vale en la base de datos.
     * Actualiza el celular a vendido.
     * Actualiza los valores del promotor debido a que se agrego un nuevo vale.
     *
     * @param celular  el celular que se va a actualizar.
     * @param promotor el promotor que se va a actualizar.
     * @return true en caso de que se realicen todos los cambios, false en caso contrario.
     */
    public boolean save(Celular celular, Promotor promotor) {
        celular.setVendido(true);
        ServiceCelular serviceCelular = new ServiceCelular(celular);

        ServicePromotor servicePromotor = new ServicePromotor(promotor);

        vale.setDiasGarantia(Fecha.getDiasEntre(vale.getFechaVenta(), LocalDate.now()));

        if (serviceCelular.update()) {
            if (this.vale.save()) {
                Gatillo.newVale();
                Gatillo.newCelular();
                Gatillo.newPromotor();

                servicePromotor.updatePromotor();

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static void updateAllVales() {
        Vale vale;

        while ((vale = Vale.getAllOneToOne(Vale.class)) != null) {
            int dias = Fecha.getDiasEntre(vale.getFechaVenta(), LocalDate.now());
            //Si la cantidad de días es menor e igual a 7 entonces todavía está en garantia
            if (dias <= 7) {
                vale.setGarantia(true);
            } else {
                vale.setGarantia(false);
            }
            vale.setDiasGarantia(dias);
            vale.update();
        }
    }

    /*********************************************
     * **********MÉTODOS ESTÁTICOS****************
     *********************************************/


    /*
     * Variable de tipo SessionFactory que nos permite interactuar con la base de
     * datos.
     */
    private static final SessionFactory sessionFactory = UtilsHibernate.getSessionFactory();

    /**
     * Método para obtener los vales de un promotor específico sabiendo su id.
     *
     * @param idPromotor el id del promotor que queremos obtener los vales.
     * @return la lista de vales que corresponden al promotor. null en caso de error.
     */
    @Deprecated
    public static List<Vale> findValesByPromotor(Long idPromotor) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            String hql = "FROM Vale v WHERE v.promotor.id = :idPromotor"; // HQL para obtener los vales por promotor
            Query<Vale> query = sessionFactory.getCurrentSession().createQuery(hql, Vale.class);
            query.setParameter("idPromotor", idPromotor);

            List<Vale> vales = query.list(); // Obtener la lista de vales para el promotor actual

            tx.commit();
            return vales;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();// Deshace la transacción en caso de error
            }
            e.printStackTrace();
        }
        return null;
    }

    //Variables que van a gurdar el promotor y por que vale se encuentra.
    private static Long promotor;
    private static int indice;

    /**
     * Método que obtiene los vales de uno en uno desde la base de datos de un
     * promotor especificado por su ID.
     *
     * @param idPromotor el id del promotor que se desea buscar los vales.
     * @return un vale perteneciente a ese promotor, en caso de que no hallan null.
     */
    public static Vale findValesByPromotorOneToOne(Long idPromotor) {
        if (promotor == null) {
            promotor = idPromotor;
            indice = 0;
        }
        if (!promotor.equals(idPromotor)) {
            promotor = idPromotor;
            indice = 0;
        }
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            String hql = "FROM Vale v WHERE v.promotor.id = :idPromotor"; // HQL para obtener los vales por promotor
            Query<Vale> query = sessionFactory.getCurrentSession().createQuery(hql, Vale.class);
            query.setParameter("idPromotor", idPromotor);
            query.setFirstResult(indice);
            query.setMaxResults(1);

            List<Vale> vales = query.list(); // Obtener la lista de vales para el promotor actual

            if (!vales.isEmpty()) {
                indice++;
                tx.commit();
                return vales.getFirst();
            } else {
                indice = 0;
                tx.commit();
                return null;
            }

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();// Deshace la transacción en caso de error
            }
            e.printStackTrace();
        }
        return null;
    }

}
