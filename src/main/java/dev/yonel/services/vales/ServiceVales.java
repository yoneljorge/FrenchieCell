package dev.yonel.services.vales;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dev.yonel.models.Celular;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.ServiceLista;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.utils.data_access.UtilsHibernate;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceVales {

    private static ServiceVales instance;
    private Vale vale;

    public ServiceVales(Vale vale) {
        this.vale = vale;
        if(this.vale.getFechaGarantia() == null){
            this.vale.setFechaGarantia(this.vale.getFechaVenta().plusWeeks(1));
        }
        instance = this;
    }

    public boolean save(Celular celular, Promotor promotor){
        celular.setVendido(true);
        ServiceCelular serviceCelular = new ServiceCelular(celular);
    
        ServicePromotor servicePromotor = new ServicePromotor(promotor);

        
            if(serviceCelular.update()){
                if(this.vale.save()){
                    ServiceLista.setCambioVale(true);
                    
                    servicePromotor.updateVales();
                    servicePromotor.update();

                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        
    }

    /*********************************************
     * **********MÉTODOS ESTÁTICOS****************
     *********************************************/

    private static ServiceVales getInstance(){
        return instance;
    }

    /*
     * Variable de tipo SessionFactory que nos permite interactuar con la base de
     * datos.
     */
    private static final SessionFactory sessionFactory = UtilsHibernate.getSessionFactory();

    /**
     * Método para obtener los vales de un promotor específico sabiendo su id.
     * 
     * @param idPromotor el id del promotor que queremos obtener los vales.
     * @return la lista de vales que corresponden al promotor.
     * @return null en caso de error. 
     */
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

}
