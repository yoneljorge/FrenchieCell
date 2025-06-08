package dev.yonel.controllers.settings;

import java.net.URL;
import java.util.ResourceBundle;

import dev.yonel.services.ConfigManager;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class ConfigBackUpController implements Initializable {

    @FXML
    private MFXButton btn_Aplicar;

    @FXML
    private Button btn_Down;

    @FXML
    private Button btn_Up;

    @FXML
    private CheckBox checkBox_BackUp;

    @FXML
    private TextField txt_Time;

    // ***********************CONSTRUCTOR PRIVADO **************** */
    private static ConfigBackUpController instance;

    private ConfigBackUpController() {
        instance = this;
    }

    private static synchronized void createInstance() {
        instance = new ConfigBackUpController();
    }

    public static ConfigBackUpController getInstance() {
        if (instance == null) {
            createInstance();
        }

        return instance;
    }

    // *********************VARIABLES ********************* */

    private boolean isUploadActive = ConfigManager.getInstance().getBoolean("app.backup.upload");
    private int time = ConfigManager.getInstance().getInt("app.backup.upload.time");

    // *********************MÉTODOS *********************** */
    @Override
    public void initialize(URL location, ResourceBundle resource) {
        // En dependencia de lo que se halla establecido en la configuración se
        // selecciona o no la syncronización.
        checkBox_BackUp.setSelected(isUploadActive);

        // Ponemos un listener para que escuche cuando sea seleccionado el checkBox.
        checkBox_BackUp.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (checkBox_BackUp.isSelected()) {
                ConfigManager.getInstance().setProperty("app.backup.upload", "true");
                // TODO borrar este sout
                System.out.println("app.backup.upload --> true");
            } else {
                ConfigManager.getInstance().setProperty("app.backup.upload", "false");
                System.out.println("app.backup.upload --> false");
            }
        });

        btn_Aplicar.setOnAction(event -> guardarTiempo());

        btn_Up.setOnAction( event -> sumarTiempo("up"));

        btn_Down.setOnAction(event -> sumarTiempo("down"));

        // Igualamos el TextField con el valor del tiempo
        txt_Time.setText(String.valueOf(time));

    }

    /*
     * public void setDisableControls(boolean value){
     * checkBox_BackUp.setDisable(value);
     * txt_Time.setDisable(value);
     * btn_Aplicar.setDisable(value);
     * btn_Down.setDisable(value);
     * btn_Up.setDisable(value);
     * }
     */

    private void sumarTiempo(String value) {

        int text = Integer.parseInt(txt_Time.getText());

        if (value.equals("up")) {
            text = text + 1;
            txt_Time.setText(String.valueOf(text));
        } else {
            text = text - 1;
            if(text >= 1){
                txt_Time.setText(String.valueOf(text));
            }
        }
    }

    private void guardarTiempo(){
        ConfigManager.getInstance().setProperty("app.backup.upload.time", txt_Time.getText());
    }
}
