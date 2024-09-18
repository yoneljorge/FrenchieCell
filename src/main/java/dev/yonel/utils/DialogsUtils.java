package dev.yonel.utils;

import dev.yonel.App;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Map;

public class DialogsUtils {
    private static MFXGenericDialog dialogContent;
    private static MFXStageDialog dialog;

    private static void buildDialog(Stage stage, Pane ownerNode, String contentText, String title) {
        dialogContent = MFXGenericDialogBuilder.build()
                .setContentText(contentText)
                .makeScrollable(true)
                .get();
        dialog = MFXGenericDialogBuilder.build(dialogContent)
                .toStageDialogBuilder()
                .initOwner(stage)
                .setDraggable(true)
                .setTitle(title)
                .setOwnerNode(ownerNode)
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimOwner(true)
                .get();

        dialogContent.addActions(
                Map.entry(new MFXButton("Confirmar"), event -> {
                }),
                Map.entry(new MFXButton("Cancelar"), event -> dialog.close())
        );

        dialogContent.setMaxSize(400, 200);
    }

    public static void openInfo(Pane ownerNode, String contentText, String title) {
        buildDialog(App.getStage(), ownerNode, contentText, title);

        MFXFontIcon infoIcon = new MFXFontIcon("fas-circle-info", 18);
        dialogContent.setHeaderIcon(infoIcon);
        dialogContent.setHeaderText("This is a generic info dialog");
        convertDialogTo("mfx-info-dialog");
        dialog.showDialog();
    }

    private static void convertDialogTo(String styleClass) {
        dialogContent.getStyleClass().removeIf(
                s -> s.equals("mfx-info-dialog") || s.equals("mfx-warn-dialog") || s.equals("mfx-error-dialog")
        );

        if (styleClass != null)
            dialogContent.getStyleClass().add(styleClass);
    }
}
