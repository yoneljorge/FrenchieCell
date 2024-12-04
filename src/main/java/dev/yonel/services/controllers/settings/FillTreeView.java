package dev.yonel.services.controllers.settings;

import dev.yonel.services.clouds.dropbox.DropboxAuthService;
import dev.yonel.services.clouds.dropbox.DropboxFileFetcher;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;
import java.util.Map;

public class FillTreeView {
    private static DropboxAuthService dropboxAuthService = DropboxAuthService.getInstance();

    public static void config(TreeView<String> treeView) {
        if (dropboxAuthService.isTokenValid()) {
            //Crear la raíz del TreeView
            TreeItem<String> rootItem = new TreeItem<>("BackUps");

            //Recuperar los backups desde Dropbox (esto simula la lógica del fetch)
            DropboxFileFetcher fileFetcher = DropboxFileFetcher.getInstance();
            //Verifica si hay cambios en la caché y si hay cambios los guarda en el archivo local.
            if (!fileFetcher.hasChanges()) {
                System.out.println("Hay cambios,  actualizando...");
            } else {
                System.out.println("No hay cambios, utilizando la caché.");
            }

            Map<String, Map<String, Map<String, List<String>>>> backups = fileFetcher.fetchBackUps();
            TreeItem<String> yearItem = null;
            TreeItem<String> monthItem = null;
            TreeItem<String> dayItem = null;

            //Agregar los datos de los backups al TreeView
            for (String year : backups.keySet()) {
                yearItem = new TreeItem<>(year);

                Map<String, Map<String, List<String>>> months = backups.get(year);
                for (String month : months.keySet()) {
                    monthItem = new TreeItem<>(month);

                    Map<String, List<String>> days = months.get(month);

                    for (String day : days.keySet()) {
                        dayItem = new TreeItem<>(day);

                        List<String> files = days.get(day);
                        for (String fileName : files) {
                            TreeItem<String> fileItem = new TreeItem<>(fileName);
                            dayItem.getChildren().add(fileItem);
                        }

                        monthItem.getChildren().add(dayItem);
                    }
                    yearItem.getChildren().add(monthItem);
                }
                rootItem.getChildren().add(yearItem);
            }

            rootItem.setExpanded(true); //Expande la raíz por defecto
            treeView.setRoot(rootItem);
        }
    }
}
