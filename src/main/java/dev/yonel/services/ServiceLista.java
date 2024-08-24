package dev.yonel.services;

import java.util.ArrayList;
import java.util.List;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;

public class ServiceLista {
    public static boolean cambioMarca = true;
    public static boolean cambioModelo = true;
    public static boolean cambioCelular = true;
    private static boolean cambioImei = true;

    private static List<Marca> marcas;
    private static List<Modelo> modelos;
    private static List<Celular> celulares;
    private static List<String> imeis;
    

    public static List<Marca> getListMarcas(){
        if(marcas == null){
            marcas = new ArrayList<>();
        }

        if(cambioMarca){
            marcas.clear();
            marcas = Marca.getAll(Marca.class);
            cambioMarca = false;
        }

        return marcas;
    }

    public static List<Modelo> getListModelos(){
        if(modelos == null){
            modelos = new ArrayList<>();
        }

        if(cambioModelo){
            modelos.clear();
            modelos = Modelo.getAll(Modelo.class);
            cambioModelo = false;
        }

        return modelos;
    }

    public static List<Celular> getListCelulares(){

        if(celulares == null){
            celulares = new ArrayList<>();
        }

        if(cambioCelular){
            celulares.clear();
            celulares = Celular.getAll(Celular.class);
            cambioCelular = false;
            cambioImei = true;
        }

        return celulares;
    }

    public static List<String> getListImeis(){
        if(imeis == null){
            imeis = new ArrayList<>();
        }

        if(cambioImei){
            imeis.clear();
            for (Celular celular : celulares) {
                imeis.add(String.valueOf(celular.getImeiUno()));
            }
        }

        return imeis;
    }
}
