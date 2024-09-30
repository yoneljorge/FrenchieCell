package dev.yonel.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.controllers.popup.PopupCelularController;
import dev.yonel.models.Celular;
import dev.yonel.utils.ui.popup.PopupUtil;
import dev.yonel.utils.ui.popup.PopupUtilImp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;


public class PrincipalController implements Initializable {

    @FXML
    private VBox pnItems;
    @FXML
    private Button btnButton;


    private @Setter Stage stage;

    private static PrincipalController instance;

    private PrincipalController() {

    }

    public static PrincipalController getInstance(){
        if(instance == null){
            instance = new PrincipalController();
        }

        return instance;
    }

    public static void restartInstance(){
        instance = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnButton.setOnAction(event -> {
            PopupCelularController controller = new PopupCelularController(Celular.getById(Celular.class, 1));
            PopupUtil popup = new PopupUtilImp();

            controller.onCloseAction(popup.close());

            popup.setController(controller);
            popup.setFxml("popup/popupCelular");

            popup.load();
            popup.show(null, null);
        });

    }
}
