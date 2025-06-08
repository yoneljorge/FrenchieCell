package dev.yonel.services.clouds.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropboxFileFetcher {

    private static final String CLIENT_IDENTIFIER = "dropbox/FrenchieCell";
    /**
     * Archivo donde se va a cachear los archivos de dropbox
     */
    private static final String CACHE_FILE_PATH = System.getProperty("user.home") + "/.frenchiecell/backup_cache.json";
    private final DbxClientV2 dropboxClient;
    private Map<String, Map<String, Map<String, List<String>>>> currentData;
    private Map<String, Map<String, Map<String, List<String>>>> cachedData;

    private static DropboxFileFetcher instance;

    private DropboxFileFetcher() {
        currentData = new HashMap<>();
        cachedData = new HashMap<>();

        //Obtener el token de acceso almacenado en SessionStore
        DropboxAuthService service = DropboxAuthService.getInstance();
        String accesToken = null;
        try {
            accesToken = service.getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (accesToken == null || accesToken.isEmpty()) {
            throw new IllegalStateException("No se encontró el token de acceso.Debes autenticar primero.");
        }

        //Crea la configuración y el cliente de Dropbox
        DbxRequestConfig config = DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER).build();
        this.dropboxClient = new DbxClientV2(config, accesToken);
    }

    private static synchronized void createInstance() {
        instance = new DropboxFileFetcher();
    }

    public static DropboxFileFetcher getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    public Map<String, Map<String, Map<String, List<String>>>> fetchBackUps() {

        //Verificar si la caché existe
        if (Files.exists(Paths.get(CACHE_FILE_PATH))) {
            cachedData = loadCache();
            if (cachedData != null && !cachedData.isEmpty()) {
                System.out.println("Cargando datos desde la caché...");
                return cachedData;
            }
        }
        //Si no hay caché o está vacía, obtener los datos desde Dropbox
        // Iniciar el proceso desde la carpeta raíz
        fetchFromDropbox("", currentData);

        //Guardar en caché
        saveCache(currentData);

        return currentData;
    }

    private void fetchFromDropbox(String path, Map<String, Map<String, Map<String, List<String>>>> organizedBackUps) {
        try {
            // Listar el contenido de la carpeta actual
            ListFolderResult result = dropboxClient.files().listFolder(path);

            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    if (metadata instanceof FolderMetadata) {
                        // Si es una carpeta, hacer una llamada recursiva
                        FolderMetadata folderMeta = (FolderMetadata) metadata;
                        String folderPath = folderMeta.getPathLower();

                        // Llamar recursivamente a esta función para explorar el contenido de la carpeta
                        fetchFromDropbox(folderPath, organizedBackUps);

                    } else if (metadata instanceof FileMetadata) {
                        // Si es un archivo, procesarlo
                        FileMetadata fileMeta = (FileMetadata) metadata;
                        String filePath = fileMeta.getPathLower();

                        // Separar la ruta por "/" para obtener año, mes, día y archivo
                        String[] pathParts = filePath.split("/");

                        if (pathParts.length >= 5) {
                            String year = pathParts[1];
                            String month = pathParts[2];
                            String day = pathParts[3];
                            String fileName = pathParts[4];

                            // Organizar la estructura en el mapa (año -> mes -> día -> archivo)
                            organizedBackUps
                                    .computeIfAbsent(year, k -> new HashMap<>())
                                    .computeIfAbsent(month, k -> new HashMap<>())
                                    .computeIfAbsent(day, k -> new ArrayList<>())
                                    .add(fileName);
                        }
                    }
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = dropboxClient.files().listFolderContinue(result.getCursor());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCache(Map<String, Map<String, Map<String, List<String>>>> backups) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(CACHE_FILE_PATH), backups);
            System.out.println("Caché guardada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Map<String, Map<String, List<String>>>> loadCache() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (Files.exists(Paths.get(CACHE_FILE_PATH))) {
                return mapper.readValue(new File(CACHE_FILE_PATH), Map.class);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasChanges() {
        if ((cachedData = loadCache()) != null) {
            fetchFromDropbox("", currentData);

            if (!cachedData.equals(currentData)) {
                saveCache(currentData);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
