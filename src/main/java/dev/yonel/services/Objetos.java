package dev.yonel.services;

import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;

public class Objetos {
    private static Marca marca;
    private static Modelo modelo;

    public static void setMarca(String nameMarca){
        marca = new Marca();
        marca.setMarca(nameMarca);
    }

    //public static Marca getMarca(){
    //    return marca;
    //}

    public static void setModelo(String nameModelo){
        modelo = new Modelo();
        modelo.setMarca(marca);
        modelo.setModelo(nameModelo);
    }

    public static Modelo getModelo(){
        return modelo;
    }
}
