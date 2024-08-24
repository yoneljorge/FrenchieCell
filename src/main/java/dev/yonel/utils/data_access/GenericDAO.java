package dev.yonel.utils.data_access;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class GenericDAO<T, Id extends Serializable> {

    private final static SessionFactory sessionFactory = UtilsHibernate.getSessionFactory();

    private static Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Método que busca y devuelve un objeto de tipo T en función del identificador
     * <id> proporcionado como parámetro. Este método es útil cuando necesitamos
     * buscar y recuperar un objeto específico basado en su identificador único en
     * la base de datos. Al proporcionar el identificador, puedes obtener
     * directamente el objeto correspondiente de la base de datos sin tener que
     * recuperar una lista completa y filtrar los resultados.
     * 
     * @param clazz - la entidad de la cual se quiere obtener el objeto.
     * @param id    - del objeto que queremos recuperar.
     * @return
     */
    public static <T, Id extends Serializable> T getObject(Class<T> clazz, Id id) {
        Transaction tx = getSession().beginTransaction();
        try {
            T object = getSession().get(clazz, id);
            tx.commit();
            return object;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Deshacer la transacción en caso de error
            }
            e.printStackTrace(); // Manejar o registrar la excepción según sea necesario
            return null; // Devolver un valor nulo en caso de error
        }
    }

    /**
     * Método que recupera todos los objetos de la clase genérica T en la
     * base de datos.
     * 
     * Explicacion:
     * 1- Se crea un objeto CriteriaBuilder utilizando el método
     * getCriteriaBuilder() de la sesión actual. El CriteriaBuilder se utiliza para
     * construir consultas.
     * 2- Se crea un objeto CriteriaQuery de tipo T(genérico) utilizando el método
     * createQuery(persistentClass) del CriteriaBuilder. Esto se utiliza para
     * construir la consulta y especificar los criterios de búsqueda.
     * 3- Se crea un objeto Root de tipo T(genérico), que representa una entidad
     * raíz en la consutla. Se utiliza para especificar qué entidad se está
     * consultando.
     * 4- Se especifica que la consulta selecciona todos los campos de la entidad
     * raíz.
     * 5- Se crea una consulta utilizando el objeto query y se ejecuta utilizando
     * getResultList() para obtener una lista de resultados. La sesión actual se
     * utiliza para crear la consulta.
     * 
     * Este método a la hora de utlizarlo hay que implementarlo dentro de un
     * try-catch de la siguiente forma
     * 
     * <// Obtener la lista de usuarios
     * List<Usuarios> listUsuarios = null;
     * 
     * Transaction tx = null;
     * try {
     * Session session = dao.getSession();
     * tx = session.beginTransaction();
     * listUsuarios = dao.getAllObject();
     * tx.commit();
     * } catch (Exception e) {
     * if (tx != null) {
     * tx.rollback();
     * }
     * e.printStackTrace();
     * }/>
     * 
     * @return
     */
    public static <T> List<T> getAllObject(Class<T> clazz) {
        Transaction tx = getSession().beginTransaction();
        try {
            HibernateCriteriaBuilder builder = getSession().getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(clazz);
            Root<T> root = query.from(clazz);
            query.select(root);

            List<T> resultList = getSession().createQuery(query).getResultList();

            tx.commit(); // Finalizar la transacción después de la operación exitosa

            return resultList;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Deshacer la transacción en caso de error
            }
            e.printStackTrace(); // Manejar o registrar la excepción según sea necesario
            return null; // Devolver un valor nulo en caso de error
        }
    }

    /**
     * Método que guarda un objeto en la base de datos.
     * 
     * @param entity - el objeto que se desea guardar.
     * 
     * @return true en caso de que no de ningún error.
     */
    public static <T> boolean save(T entity) {
        Transaction tx = getSession().beginTransaction();
        try {
            getSession().persist(entity);
            tx.commit();
            getSession().close();
            return true;
        } catch (Exception e) {
            tx.rollback(); // en caso de que falle la transacción se deshace la transacción.
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Método que crea si no existe, actualiza si existe o agrega en caso de pasar
     * solo algunos datos un objeto en la base de datos.
     * 
     * @param entity - objeto que se desea actualizar.
     * 
     */
    public static <T> boolean update(T entity) {
        Transaction tx = getSession().beginTransaction();
        try {
            getSession().merge(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Método que elimina un objeto en la base de datos.
     * 
     * @param entity - el objeto que se desea actualizar.
     */
    public static <T> boolean delete(T entity) {
        Transaction tx = getSession().beginTransaction();
        try {
            getSession().remove(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            System.err.println(e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean existe(Map<String, Object> propiedades) {
        if (propiedades == null) {
            throw new IllegalArgumentException("El mapa de propiedades no puede estar vacío.");
        }

        Transaction tx = getSession().beginTransaction();
        try {
            StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM ");

            // Asumimos que el nombre de la clase está presente en el mapa bajo la clave
            // "class"
            Class<T> clase = (Class<T>) propiedades.get("class");
            if (clase == null) {
                throw new IllegalArgumentException("Debe proporcionar una clase en el mapa de propiedades");
            }

            hql.append(clase.getName()).append(" WHERE ");

            Set<Map.Entry<String, Object>> entrySet = propiedades.entrySet();
            boolean first = true;

            for (Map.Entry<String, Object> entry : entrySet) {
                if ("class".equals(entry.getKey())) {
                    continue;
                }
                if (!first) {
                    hql.append(" AND ");
                }
                hql.append(entry.getKey()).append(" = :").append(entry.getKey());
                first = false;
            }

            Query<Long> query = getSession().createQuery(hql.toString(), Long.class);
            for (Map.Entry<String, Object> entry : entrySet) {
                if (!"class".equals(entry.getKey())) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }

            long count = query.uniqueResult();
            tx.commit();
            return count > 0;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
