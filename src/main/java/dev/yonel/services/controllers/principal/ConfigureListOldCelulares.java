package dev.yonel.services.controllers.principal;

import java.time.LocalDate;
import java.util.List;

import dev.yonel.models.Celular;
import dev.yonel.utils.fechaUtil.FechaUtil;
import javafx.scene.control.ListView;

public class ConfigureListOldCelulares {

    private static ConfigureListOldCelulares instance;

    private ConfigureListOldCelulares(){
        instance = this;
    }

    public static ConfigureListOldCelulares getInstnace(){
        if(instance == null){
            instance = new ConfigureListOldCelulares();
        }

        return instance;
    }

    private ListView<String> listView;

    public void configure(ListView<String> listView){
        this.listView = listView;
        setDataInList();
    }

    private void setDataInList(){
        List<Integer> idCelulares = InfoCelulares.getInstance().extractIdsFromFile();

        if(!idCelulares.isEmpty()){
            listView.getItems().clear();
            for (Integer id : idCelulares) {
                Celular celular = Celular.getById(Celular.class, id);
                StringBuilder info = new StringBuilder();
                info.append("Días: " + FechaUtil.getDiasEntre(celular.getFechaInventario(), LocalDate.now()));
                info.append("-Marca: " + celular.getMarca());
                info.append("-Modelo: " + celular.getModelo());
                info.append("-IMEI" + celular.getImeiUno());
                listView.getItems().add(info.toString());
            }
        }
    }
}
