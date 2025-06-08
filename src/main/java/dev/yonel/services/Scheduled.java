package dev.yonel.services;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dev.yonel.controllers.DashboardController;
import dev.yonel.controllers.settings.BackUpController;
import dev.yonel.services.clouds.dropbox.DropboxAuthService;
import dev.yonel.services.clouds.dropbox.DropboxUploader;
import dev.yonel.services.controllers.dashboard.TYPE_CLOUD;
import dev.yonel.services.controllers.settings.FillTreeView;
import dev.yonel.utils.AlertUtil;
import javafx.concurrent.Task;

public class Scheduled {

    private static Mensajes mensajes = new Mensajes(Scheduled.class);

    private static int time = ConfigManager.getInstance().getInt("app.conection.cheker.time_in_seconds");

    public static void runTasks() {
        verifyConection();
        autoUpdate();
    }

    /**
     * Método con el que vamos a verificar si hay conexión a internet cada x tiempo.
     */
    private static void verifyConection() {
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

        scheduled.scheduleAtFixedRate(() -> {
            if (ConectionChecker.hasInternetConnection()) {
                DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.CONECTED);
                time = 300; // Establecemos que cada 5 minutos vuelva a revisar si hay conexión
                if (DropboxAuthService.getInstance().isTokenValid()) {
                    DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.CLOUD);
                    // En caso de que la nube este disponible pasamos a poner la vista de BackUp
                    // visible.
                    BackUpController.getInstance().setVisibleViewBackUp();
                    BackUpController.getInstance().setButtonsDisable(false);
                } else {
                    DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.CLOUD_CROSS);
                }
            } else {
                DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.DISCONECTED);
                time = 10; // Establecemos que cada 10 segundos busque a ver si ya hay conexión.
                // Los botones en la vista de BackUp en caso de estar visible los desactivamos
                BackUpController.getInstance().setButtonsDisable(true);
            }
        }, 0, time, TimeUnit.SECONDS);
    }

    /**
     * Método con el cual vamos a ejecutar automáticamente los BackUps
     * 
     */
    private static void autoUpdate() {
        boolean isUpdate = ConfigManager.getInstance().getBoolean("app.backup.upload");
        int time = ConfigManager.getInstance().getInt("app.backup.upload.time");

        //TODO Borrar este sout
        System.out.println("=======>Syncronizando cada: " + time);
        
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
        scheduled.scheduleAtFixedRate(() -> {

            if (isUpdate) {
                if (ConectionChecker.hasInternetConnection()) {
                    if (DropboxAuthService.getInstance().isTokenValid()) {
                        Task<Void> taskUpload = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                DropboxUploader uploader = new DropboxUploader();
                                mensajes.info("Syncronización en proceso...");
                                File localFile = new File(ConfigManager.getInstance().get("app.base_datos"));
                                DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.CLOUD_SYNC);
                                try {
                                    uploader.uploadFile(localFile);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void succeeded() {
                                super.succeeded();
                                DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.CLOUD);
                                FillTreeView.config(BackUpController.getInstance().getTreeView());
                                mensajes.info("Syncronización finalizada.");
                            }

                            @Override
                            protected void failed() {
                                super.failed();
                                DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.CLOUD);
                                AlertUtil.error("Error",
                                        "Mientras se syncronizaba en segundo plano ocurrió un error.");
                            }

                            @Override
                            protected void finalize() throws Throwable {
                                DashboardController.getInstance().setTypeCloud(TYPE_CLOUD.CLOUD);
                            }
                        };
                        new Thread(taskUpload).start();
                    }
                }
            }
        }, 1, time, TimeUnit.MINUTES);
    }

}
