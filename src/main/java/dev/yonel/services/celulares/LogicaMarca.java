package dev.yonel.services.celulares;

import java.util.List;

import dev.yonel.models.Marca;
import javafx.scene.control.ComboBox;

public class LogicaMarca {
    private static List<Marca> listaMarcas = Marca.getAll(Marca.class);

    public static boolean exist(Marca marcaBuscar){
        for (Marca marca : listaMarcas) {
            if (marca.getMarca().equals(marcaBuscar.getMarca())) {
                return true;
            }
        }
        return false;
    }

    public static void print(){
        for (Marca marca : listaMarcas) {
            System.out.println(marca.getMarca());
        }
    }

    public static String saveMarca(Marca marca){
        if (!exist(marca)) {
            if(marca.save()){
                System.out.println("save");
                return "save";
            }else{
                System.out.println("no save");
                return "no save";
            }
        }else{
            System.out.println("exist");
            return "exist";
        }
    }

    public static void llenarComboBox(ComboBox<Marca> cmb){
        
    }
}
