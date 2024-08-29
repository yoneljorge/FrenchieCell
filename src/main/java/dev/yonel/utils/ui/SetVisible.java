package dev.yonel.utils.ui;

import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SetVisible {

    public static void This(List<VBox> listVBox, VBox vBox){
        for (VBox vboxElement : listVBox) {
            if (vboxElement != vBox) {
                vboxElement.setVisible(false);
                vboxElement.setDisable(true);
            }else{
                vboxElement.setVisible(true);
                vboxElement.setDisable(false);
            }
        }
    }

    public static void This(List<Pane> listPane, Pane pane){
        for (Pane paneElement : listPane) {
            if (paneElement != pane) {
                paneElement.setVisible(false);
                paneElement.setDisable(true);
            } else {
                paneElement.setVisible(true);
                paneElement.setDisable(false);
            }
        }
    }
}
