package dev.yonel.services.clouds.dropbox;

import org.junit.jupiter.api.Test;

import java.io.File;

public class TestDropboxAuthService {

    @Test
    public void testAuthService() {
        try {
            DropboxAuthService dropboxAuthService = DropboxAuthService.getInstance();
            String accessToken = dropboxAuthService.getAccessToken();

            System.out.println("Token de accesso: " + accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUploadFile() {
        DropboxUploader uploader = new DropboxUploader();

        //Ruta del archivo local
        File localFile = new File("BaseDatos.db");

        try {
            uploader.uploadFile(localFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
