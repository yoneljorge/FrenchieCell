package dev.yonel.services.clouds.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;

import dev.yonel.controllers.DashboardController;
import dev.yonel.services.controllers.dashboard.TYPE_CLOUD;
import javafx.concurrent.Task;

public class DropboxUploader {

    private static final String CLIENT_IDENTIFIER = "dropbox/FrenchieCell";

    private final DbxClientV2 dropboxClient;

    public DropboxUploader() {
        // Obtener el token de acceso almacenado en SessionStore
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

        // Crea la configuración y el cliente de Dropbox
        DbxRequestConfig config = DbxRequestConfig.newBuilder(CLIENT_IDENTIFIER).build();
        this.dropboxClient = new DbxClientV2(config, accesToken);
    }

    public boolean uploadFile(File localFile) throws Exception {

        if (!localFile.exists()) {
            // throw new IllegalArgumentException("El archivo local no existe: " +
            // localFile);
            System.out.println("El archivo local no existe");
            return false;
        }

        // Subir el archivo a Dropbox en la ruta especificada
        Task<Void> taskUploadFile = new Task<Void>() {
            @Override
            protected Void call() {

                DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.UPLOAD);
                // Leer el archivo local como InputStream
                try (InputStream in = new FileInputStream(localFile)) {

                    dropboxClient.files().uploadBuilder(getNameFile())
                            .withMode(WriteMode.ADD)
                            .uploadAndFinish(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.CLOUD);
                // System.out.println("Archivo subido exitosamente a Dropbox: " +
                // getValue().getPathLower());
            }
        };

        new Thread(taskUploadFile).start();

        return true;

    }

    private String getNameFile() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");

        // Crear la ruta de carpetas en formato: "/2024/10/22"
        String folderPath = "/" + now.format(dateFormatter) + "/";
        String fileNmae = "backup-" + now.format(timeFormatter) + ".db"; // El archivo será nombrado con la hora

        return folderPath + fileNmae;
    }
}
