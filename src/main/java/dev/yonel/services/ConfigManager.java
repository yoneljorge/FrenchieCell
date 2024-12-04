package dev.yonel.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import dev.yonel.utils.AlertUtil;

public class ConfigManager {

    // ************************CONSTRUCTOR PRIVADO******************* */
    private static ConfigManager instance;

    private ConfigManager() {
        this.properties = new Properties();

        File carpetaFrenchieCell = new File(HOME_FRENCHIECELL);
        // Verificamos de que la carpeta exista
        if (!carpetaFrenchieCell.exists()) {
            // Intentar crear la carpeta
            if (carpetaFrenchieCell.mkdir()) {
                mensajes.info("La carpeta fue creda correctamente.");
                loadFileProperties();
            } else {
                mensajes.err("No se puedo crear la carpeta.");
            }
        } else {
            mensajes.info(HOME_FRENCHIECELL + "La carpeta ya existe.");
            loadFileProperties();
        }

    }

    private static synchronized void createInstance() {
        instance = new ConfigManager();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            createInstance();
        }

        return instance;
    }

    // ********************VARIABLES****************** */
    private Mensajes mensajes = new Mensajes(getClass());

    private Properties properties;
    private String HOME_FRENCHIECELL = System.getProperty("user.home")
            + System.getProperty("file.separator") + ".frenchiecell";
    private String FILE_CONFIG_PROPERTIES = HOME_FRENCHIECELL
            + System.getProperty("file.separator") + "config.properties";

    // ********************MÉTODOS******************** */
    private void loadFileProperties() {
        // Verificamos de que el archivo config.properties exista sino lo creamos.
        File fileConfigProperties = new File(FILE_CONFIG_PROPERTIES);
        if (!fileConfigProperties.exists()) {
            try {
                if (fileConfigProperties.createNewFile()) {
                    loadProperties();
                    createProperties();
                } else {
                    AlertUtil.error("ERROR EN EL SISTEMA",
                            "No se tiene acceso a crear los archivos de configuración "
                                    + "\npor lo que la aplicación se va a cerrar.");
                    System.exit(0);
                }
            } catch (Exception e) {
                mensajes.err("Error Constructor config.properties");
                mensajes.err(e.getMessage());
            }
        } else {
            loadProperties();
        }
    }

    private void loadProperties() {
        try (FileInputStream fis = new FileInputStream(FILE_CONFIG_PROPERTIES)) {
            this.properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err("Error cargando config.properties en properties.");
        }
    }

    public String getHomeFrenchieCell() {
        return HOME_FRENCHIECELL + System.getProperty("file.separator");
    }

    // ****************PROPERTIES METODS ***************** */
    private void createProperties() {
        this.properties.setProperty("app.base_datos", getHomeFrenchieCell() + "BaseDatos.db");
        this.properties.setProperty("app.conection.cheker.time_in_seconds", "10");
        this.properties.setProperty("app.backup.update", "true");
        this.properties.setProperty("app.backup.update.time", "60");

        // Escribir de nuevo las propiedades en el archivo
        try (FileOutputStream fos = new FileOutputStream(FILE_CONFIG_PROPERTIES)) {
            this.properties.store(fos, "Configuración");
        } catch (Exception e) {
            e.printStackTrace();
            mensajes.err("Error almacenando la configuración en config.properties");
        }

    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public void setProperty(String key, String value){
        this.properties.setProperty(key, value);

        // Escribir de nuevo las propiedades en el archivo
        try (FileOutputStream fos = new FileOutputStream(FILE_CONFIG_PROPERTIES)) {
            this.properties.store(fos, "Configuración");
        } catch (Exception e) {
            e.printStackTrace();
            mensajes.err("Error almacenando la configuración en config.properties");
        }
    }
}
