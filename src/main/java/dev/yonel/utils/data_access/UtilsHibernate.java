package dev.yonel.utils.data_access;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.ConfigManager;

public class UtilsHibernate {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class",
                    "org.sqlite.JDBC");
            configuration.setProperty("hibernate.connection.url",
                    "jdbc:sqlite:" + ConfigManager.getInstance().get("app.base_datos"));

            System.out.println(ConfigManager.getInstance().get("app.base_datos"));

            configuration.setProperty("hibernate.dialect",
                    "org.hibernate.community.dialect.SQLiteDialect");
            configuration.setProperty("hibernate.dbcp.maxActive", "1");
            configuration.setProperty("show_sql", "true");
            configuration.setProperty("format_sql", "true");
            configuration.setProperty("hibernate.current_session_context_class",
                    "thread");
            // Para que hibernate no busque clases relacionadas con javax.naming
            configuration.setProperty("hibernate.jndi.class", "none");
            configuration.setProperty("hibernate.jndi.url", "none");

            // Para que muestre el sql mientras lo ejecuta
            configuration.setProperty("hibernate.show_sql", "false");

            // Actualiza o crea la base de datos si es necesario
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            // Para importar multiples archivos sql utilizamos
            // configuration.setProperty("hibernate.hbm2ddl.import_files",
            // "import.sql,data.sql");

            // Modelos
            configuration.addAnnotatedClass(Celular.class);
            configuration.addAnnotatedClass(Marca.class);
            configuration.addAnnotatedClass(Modelo.class);
            configuration.addAnnotatedClass(Promotor.class);
            configuration.addAnnotatedClass(Vale.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que retorna un objeto de tipo SesionFactory
     * 
     * @return
     */
    public static SessionFactory getSessionFactory() {
        /*
         * if (sessionFactory == null) {
         * try {
         * // Create registry
         * registry = new StandardServiceRegistryBuilder().configure().build();
         * // Create MetadataSources
         * MetadataSources sources = new MetadataSources(registry);
         * // Create Metadata
         * Metadata metadata = sources.getMetadataBuilder().build();
         * // Create SessionFactory
         * sessionFactory = metadata.getSessionFactoryBuilder().build();
         * } catch (Exception e) {
         * e.printStackTrace();
         * if (registry != null) {
         * StandardServiceRegistryBuilder.destroy(registry);
         * }
         * }
         * }
         */
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null)
            StandardServiceRegistryBuilder.destroy(registry);
    }
}
