package dev.yonel.services.clouds.dropbox;

import com.dropbox.core.DbxSessionStore;

import dev.yonel.services.ConfigManager;
import dev.yonel.services.Mensajes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SessionStore implements DbxSessionStore {

    private final File sessionFile;
    private static final String SESSION_TOKEN_KEY = "dropbox-session-token";  // Token de sesión (CSRF)
    private static final String ACCESS_TOKEN_KEY = "dropbox-access-token";  // Token de acceso

    private Mensajes mensajes = new Mensajes(getClass());

    public SessionStore() {
        //Crear la ruta del archivo en el directorio del ususario
        this.sessionFile = new File(ConfigManager.getInstance().getHomeFrenchieCell() + "sessionDropbox.properties");

        //Crear el archivo y directorio si no existen
        if (!sessionFile.getParentFile().exists()) {
            sessionFile.getParentFile().mkdirs();
        }

        if (!sessionFile.exists()) {
            try {
                sessionFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                mensajes.err("Error Constructor SessionStore");
                mensajes.err(e.getMessage());
            }
        }
    }

    @Override
    public void set(String token) {
        Properties properties = new Properties();
        //Cargar las propiedades del archivo si existen
        try (FileInputStream fis = new FileInputStream(sessionFile)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err(e.getMessage());
        }

        //Guardar el token con una clave fija
        properties.setProperty(SESSION_TOKEN_KEY, token);

        //Escribir de nuevo las propiedades en el archivo
        try (FileOutputStream fos = new FileOutputStream(sessionFile)) {
            properties.store(fos, "Dropbox Session Store");
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err("Error almacenando el session token: ");
            mensajes.err(e.getMessage());
        }
    }

    public void saveAccessToken(String accessToken) {
        Properties properties = new Properties();
        //Cargar las propiedades del archivo si existen
        try (FileInputStream fis = new FileInputStream(sessionFile)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err(e.getMessage());
        }

        //Guardar el token con una clave fija
        properties.setProperty(ACCESS_TOKEN_KEY, accessToken);

        //Escribir de nuevo las propiedades en el archivo
        try (FileOutputStream fos = new FileOutputStream(sessionFile)) {
            properties.store(fos, "Dropbox Access Store");
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err("Error almacenando el session token: ");
            mensajes.err(e.getMessage());
        }
    }

    @Override
    public String get() {

        Properties properties = new Properties();

        // Cargar las propiedades del archivo
        try (FileInputStream fis = new FileInputStream(sessionFile)) {
            properties.load(fis);
            return properties.getProperty(SESSION_TOKEN_KEY);  // Obtener el token
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err("Error obteniendo el session token: ");
            mensajes.err(e.getMessage());
        }
        return null;
    }

    public String getAccessToken() {
        Properties properties = new Properties();

        // Cargar las propiedades del archivo
        try (FileInputStream fis = new FileInputStream(sessionFile)) {
            properties.load(fis);
            return properties.getProperty(ACCESS_TOKEN_KEY);  // Obtener el token
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err("Error obteniendo el access token: ");
            mensajes.err(e.getMessage());
        }
        return null;
    }

    @Override
    public void clear() {
        Properties properties = new Properties();

        // Cargar las propiedades del archivo
        try (FileInputStream fis = new FileInputStream(sessionFile)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err("Error limpiando el session token");
            mensajes.err(e.getMessage());
        }

        // Remover el token
        properties.remove(ACCESS_TOKEN_KEY);
        properties.remove(SESSION_TOKEN_KEY);

        // Guardar de nuevo las propiedades en el archivo
        try (FileOutputStream fos = new FileOutputStream(sessionFile)) {
            properties.store(fos, "Dropbox Session Store");
        } catch (IOException e) {
            e.printStackTrace();
            mensajes.err("Error limpiando el session token: ");
            mensajes.err(e.getMessage());
        }

    }
}
