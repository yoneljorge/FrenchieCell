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

/**
 * Clase genérica para operaciones CRUD (Create, Read, Update, Delete)
 * utilizando Hibernate.
 * 
 * @param <T>  el tipo de entidad que se manipulará
 * @param <Id> el tipo de identificador de la entidad
 */
public class GenericDAO<T, Id extends Serializable> {

    private final static SessionFactory sessionFactory = UtilsHibernate.getSessionFactory();

    private static Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Busca y devuelve un objeto de tipo T en función del identificador
     * proporcionado.
     * 
     * <p>
     * Este método es útil para recuperar un objeto específico de la base de datos
     * basado en su identificador único. Al proporcionar el identificador, se
     * obtiene
     * directamente el objeto deseado, sin necesidad de recuperar una lista completa
     * y aplicar filtros adicionales.
     * </p>
     * 
     * @param clazz la clase de la entidad que se quiere obtener
     * @param id    el identificador único del objeto que se desea recuperar
     * @return el objeto de tipo T correspondiente al identificador proporcionado,
     *         o {@code null} si no se encuentra
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
     * Recupera todos los objetos de la clase genérica T en la base de datos.
     * 
     * <p>
     * <strong>Explicación del proceso:</strong>
     * </p>
     * 
     * <ol>
     * <li>Se crea un objeto {@code CriteriaBuilder} utilizando el método
     * {@code getCriteriaBuilder()} de la sesión actual. Este
     * {@code CriteriaBuilder}
     * se utiliza para construir consultas.</li>
     * 
     * <li>Se crea un objeto {@code CriteriaQuery} de tipo T (genérico) usando el
     * método
     * {@code createQuery(persistentClass)} del {@code CriteriaBuilder}. Este objeto
     * se utiliza para construir la consulta y especificar los criterios de
     * búsqueda.</li>
     * 
     * <li>Se crea un objeto {@code Root<T>} que representa una entidad raíz en la
     * consulta.
     * Se usa para definir la entidad que se está consultando.</li>
     * 
     * <li>Se especifica que la consulta selecciona todos los campos de la entidad
     * raíz
     * utilizando {@code query.select(root)}.</li>
     * 
     * <li>La consulta se ejecuta con el método {@code getResultList()} para obtener
     * una lista
     * de resultados. La sesión actual se utiliza para ejecutar la consulta.</li>
     * </ol>
     * 
     * <p>
     * <strong>Uso recomendado:</strong>
     * </p>
     * <p>
     * Este método debe ser implementado dentro de un bloque <i>try-catch</i> de la
     * siguiente manera:
     * </p>
     * 
     * <pre>{@code
     * List<Usuarios> listUsuarios = null;
     * listUsuarios = Usuarios.getAllObject(Usuarios.class)
     * }</pre>
     * 
     * @param <T>   el tipo genérico de la entidad
     * @param clazz la clase de la entidad que se consulta
     * @return una lista de objetos de la clase genérica T obtenidos de la base de
     *         datos
     * @deprecated Este método está obsoleto y puede ser reemplazado por otros
     *             métodos más modernos.
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

    private static int i = 0;
    private static Object clazzGlobal;

    /**
     * Método que recupera todos los objetos de uno en uno de la clase genérica T en
     * la base de datos.
     * <p>
     * Debe de utilizarse en un bulce while debido a que va a ir obteniendo los
     * objetos de uno en uno desde la base de
     * datos y cuando
     * llegue al final va a
     * retornar un null.
     * </p>
     * <p>
     * <strong>Ejemplo:</strong>
     * </p>
     * 
     * <pre>{@code
     * Usuario usuario;
     * while ((usuario = Usuario.getAllObjectOneToOne) != null) {
     *     // lo que se quiera hacer con el objeto.
     * }
     * }</pre>
     * 
     *
     * @param clazz la entidad de la cual que se quieren obtener los objetos.
     * @return un objeto de esa entidad.
     * @param <T>
     */
    public static <T> T getAllObjectOneToOne(Class<T> clazz) {
        // Inicializamos la claseGlobal con la primera clase que utilice este método.
        if (clazzGlobal == null) {
            clazzGlobal = clazz;
        }
        /**
         * En caso de que la claseGlobal sea distinta a la clase que se pasa como
         * argumento, entonces se iguala a la clase
         * y el índice se iguala a 0, porque se sobreentiende que se está buscando
         * objetos de otra clase y no queremos que
         * comience desde otro índice.
         */
        if (clazzGlobal != clazz) {
            i = 0;
            clazzGlobal = clazz;
        }
        Transaction tx = getSession().beginTransaction();
        try {
            HibernateCriteriaBuilder builder = getSession().getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(clazz);
            Root<T> root = query.from(clazz);
            query.select(root);

            // Usamos el contador i para establecer desde que registro empezar.
            Query<T> hibernateQuery = getSession().createQuery(query)
                    .setFirstResult(i) // Desde el índice actual
                    .setMaxResults(1);

            List<T> resultList = hibernateQuery.getResultList();

            if (!resultList.isEmpty()) {
                i++; // Incrementar el índice para la siguiente llamada
                tx.commit(); // Finalizar la transacción después de la operación exitosa
                return resultList.get(0); // Devolver el primer y unico registro
            } else {
                i = 0;// Reiniciar el índice si no hay más resultados.
                tx.commit(); // Finalizar la transacción después de la operación exitosa
                return null;// Devolver null si no hay más registros.
            }

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // Deshacer la transacción en caso de error
            }
            e.printStackTrace(); // Manejar o registrar la excepción según sea necesario
            return null; // Devolver un valor nulo en caso de error
        }
    }

    private static int iInvertido = 0;
    private static Object clazzGlobalInvertido;

    public static <T> T getAllObjectOneToOneInvertido(Class<T> clazz) {
        // Inicializamos la claseGlobal con la primera clase que utilice este método.
        if (clazzGlobalInvertido == null) {
            clazzGlobalInvertido = clazz;
        }
        /**
         * En caso de que la claseGlobal sea distinta a la clase que se pasa como
         * argumento, entonces se iguala a la clase
         * y el índice se iguala a 0, porque se sobreentiende que se está buscando
         * objetos de otra clase y no queremos que
         * comience desde otro índice.
         */
        if (clazzGlobalInvertido != clazz) {
            iInvertido = 0;
            clazzGlobalInvertido = clazz;
        }
        Transaction tx = getSession().beginTransaction();
        try {
            HibernateCriteriaBuilder builder = getSession().getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(clazz);
            Root<T> root = query.from(clazz);
            query.select(root);
            query.orderBy(builder.desc(root.get("id"))); // Orden inverso por ID (ajusta según tu campo)

            // Usamos el contador i para establecer desde qué registro empezar.
            Query<T> hibernateQuery = getSession().createQuery(query)
                    .setFirstResult(iInvertido) // Desde el índice actual
                    .setMaxResults(1);

            List<T> resultList = hibernateQuery.getResultList();

            if (!resultList.isEmpty()) {
                iInvertido++; // Incrementar el índice para la siguiente llamada
                tx.commit(); // Finalizar la transacción después de la operación exitosa
                return resultList.get(0); // Devolver el primer y único registro
            } else {
                iInvertido = 0; // Reiniciar el índice si no hay más resultados.
                tx.commit(); // Finalizar la transacción después de la operación exitosa
                return null; // Devolver null si no hay más registros.
            }

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
