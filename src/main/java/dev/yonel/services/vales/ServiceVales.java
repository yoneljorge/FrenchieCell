package dev.yonel.services.vales;

import dev.yonel.models.Celular;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.Gatillo;
import dev.yonel.services.Mensajes;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.utils.data_access.UtilsHibernate;
import dev.yonel.utils.fechaUtil.FechaUtil;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
public class ServiceVales {

    private Mensajes mensajes = new Mensajes(ServiceVales.class);

    private Vale vale;

    public ServiceVales(Vale vale) {
        this.vale = vale;
        if (this.vale.getFechaGarantia() == null) {
            this.vale.setFechaGarantia(this.vale.getFechaVenta().plusWeeks(1));
        }

    }

    public Vale getVale(){
        return this.vale;
    }
    /********************************************
     ********** MÉTODOS CRUD ********************
     */

    /**
     * Método que se utiliza para guardar un vale en la base de datos. Actualiza el
     * celular a vendido. Actualiza los
     * valores del promotor debido a que se agrego un nuevo vale.
     *
     * @param celular  el celular que se va a actualizar.
     * @param promotor el promotor que se va a actualizar.
     * @return true en caso de que se realicen todos los cambios, false en caso
     *         contrario.
     */
    public boolean save(Celular celular, Promotor promotor) {
        celular.setVendido(true);
        ServiceCelular serviceCelular = new ServiceCelular(celular);

        ServicePromotor servicePromotor = new ServicePromotor(promotor);

        vale.setDiasGarantia(FechaUtil.getDiasEntre(vale.getFechaVenta(), LocalDate.now()));

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

    /**
     * <p>
     * Método con el que vamos a actualizar un vale.
     * </p>
     * <p>
     * Al acutalizar el vale debemos actualizar el celular y el promotor que
     * conforman este vale
     * </p>
     * 
     * @param oldVale     la información del vale al inicio de los cambios.
     * @param oldCelular  la informacion del celular antes de los cambios.
     * @param newCelular  la informacion del celular después de los cambios.
     * @param oldPromotor la información del promotor antes de los cambios.
     * @param newPromotor la información del pormotor después de los cambios.
     * 
     * @return
     *         <ul>
     *         <li>{@code true} en caso de que se actualice el vale sin
     *         errores.</li>
     *         <li>{@code false} en caso de que no se actualice el vale o se
     *         produzcan errores.</li>
     *         </ul>
     */
    public boolean update(Vale oldVale, Celular oldCelular, Celular newCelular, Promotor oldPromotor,
            Promotor newPromotor) {
        mensajes.info("Actualizando vale");
        Vale newVale = vale;

        // Rectificamos la fecha de la gantia en caso de que halla sido cambiada la
        // fecha de venta.
        if (!oldVale.getFechaVenta().equals(newVale.getFechaVenta())) {
            newVale.setFechaGarantia(this.vale.getFechaVenta().plusWeeks(1));

            int dias = FechaUtil.getDiasEntre(vale.getFechaVenta(), LocalDate.now());
            // Si la cantidad de días es menor e igual a 7 entonces todavía está en garantia
            if (dias <= 7) {
                vale.setGarantia(true);
            } else {
                vale.setGarantia(false);
            }
            vale.setDiasGarantia(dias);
        }

        if (vale.update()) {
            Gatillo.newVale();
            Gatillo.newCelular();
            Gatillo.newPromotor();

            if (!oldCelular.equals(newCelular)) {
                oldCelular.setVendido(false);
                newCelular.setVendido(true);

                ServiceCelular serviceOldCelular = new ServiceCelular(oldCelular);
                serviceOldCelular.update();

                ServiceCelular serviceNewCelular = new ServiceCelular(newCelular);
                serviceNewCelular.update();
            }

            if (!oldPromotor.equals(newPromotor)) {
                ServicePromotor serviceOldPromotor = new ServicePromotor(oldPromotor);
                serviceOldPromotor.updatePromotor();

                ServicePromotor serviceNewPromotor = new ServicePromotor(newPromotor);
                serviceNewPromotor.updatePromotor();
            }

            mensajes.info("Vale actualizado");
            return true;

        } else {
            mensajes.err("Error actualizando vale.");
            return false;
        }
    }

    /**
     * <p>
     * Método con el cual vamos a eliminar un vale y vamos a actualizar al celular
     * correspondiente a no <strong>vendido</strong>.
     * </p>
     * 
     * @param <code>celular</code> el celular que queremos actualizar.
     * @return
     *         <p>
     *         <code>true</code> en caso de que se elimine el vale y se actualice el
     *         celular sin errores.
     *         </p>
     *         <p>
     *         <code>false</code> en caso de que no se elimine el vale o se presente
     *         algún error.
     *         </p>
     */
    public boolean eliminar(Celular celular) {
        mensajes.info("Eliminando vale.");
        celular.setVendido(false);

        if (vale.delete()) {
            celular.update();
            mensajes.info("Vale eliminado");
            Gatillo.newVale();
            Gatillo.newCelular();
            Gatillo.newPromotor();
            return true;
        } else {
            mensajes.info("Error al eliminar el vale,");
            return false;
        }
    }

    /**
     * <p>
     * Método para eliminar el vale sin actualizar el celular.
     * </p>
     * 
     * @return
     *         <p>
     *         <code>true</code> en caso de que se elimine el vale sin errores.
     *         </p>
     *         <p>
     *         <code>false</code> en caso de que se produzca un error y no se
     *         elimine el vale.
     *         </p>
     */
    public boolean eliminar() {
        mensajes.info("Eliminando vale.");

        if (vale.delete()) {
            mensajes.info("Vale eliminado");
            Gatillo.newVale();
            Gatillo.newCelular();
            Gatillo.newPromotor();
            return true;
        } else {
            mensajes.info("Error al eliminar el vale,");
            return false;
        }
    }

    public static void updateAllVales() {
        Vale vale;

        while ((vale = Vale.getAllOneToOne(Vale.class)) != null) {
            int dias = FechaUtil.getDiasEntre(vale.getFechaVenta(), LocalDate.now());
            // Si la cantidad de días es menor e igual a 7 entonces todavía está en garantia
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
     * @return la lista de vales que corresponden al promotor. null en caso de
     *         error.
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

    // Variables que van a gurdar el promotor y por que vale se encuentra.
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
                return vales.get(0);
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
