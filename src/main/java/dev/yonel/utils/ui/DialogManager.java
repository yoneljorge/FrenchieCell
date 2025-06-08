package dev.yonel.utils.ui;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPopup;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.Map;

@SuppressWarnings("unused")
public class DialogManager {

   /*
    * 
     private MFXGenericDialog dialogContent;
    private MFXPopup popup;

    // Constructor que inicializa el diálogo genérico y el diálogo de la etapa
    public DialogManager(Node ownerNode) {
        this.dialogContent = MFXGenericDialogBuilder.build()
        .makeScrollable(true)
        .get();

        this.popup = new MFXPopup(ownerNode);
        popup
        

        dialogContent.setMaxSize(400, 200);
    }

    // Método para mostrar un diálogo de información
    public void showInfoDialog(String message) {
        MFXFontIcon infoIcon = new MFXFontIcon("fas-circle-info", 18);
        configureDialog("This is a generic info dialog", message, infoIcon, "mfx-info-dialog");
        dialog.showDialog();
    }

    // Método para mostrar un diálogo de advertencia
    public void showWarningDialog(String message) {
        MFXFontIcon warnIcon = new MFXFontIcon("fas-circle-exclamation", 18);
        configureDialog("This is a warning dialog", message, warnIcon, "mfx-warn-dialog");
        dialog.showDialog();
    }

    // Método para mostrar un diálogo de error
    public void showErrorDialog(String message) {
        MFXFontIcon errorIcon = new MFXFontIcon("fas-circle-xmark", 18);
        configureDialog("This is an error dialog", message, errorIcon, "mfx-error-dialog");
        dialog.showDialog();
    }

    // Método para mostrar un diálogo genérico
    public void showGenericDialog(String title, String message) {
        configureDialog(title, message, null, null);
        dialog.showDialog();
    }

    // Método para configurar el diálogo según el tipo
    private void configureDialog(String headerText, String contentText, MFXFontIcon icon, String styleClass) {
        dialogContent.setHeaderIcon(icon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText(contentText);

        dialogContent.getStyleClass().removeAll("mfx-info-dialog", "mfx-warn-dialog", "mfx-error-dialog");
        if (styleClass != null) {
            dialogContent.getStyleClass().add(styleClass);
        }

        dialogContent.addActions(
            Map.entry(new MFXButton("Confirm"), event -> {}),
            Map.entry(new MFXButton("Cancel"), event -> dialog.close())
        );
    }
    */
}