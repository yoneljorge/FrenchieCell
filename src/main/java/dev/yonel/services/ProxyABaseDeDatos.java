package dev.yonel.services;

import java.util.ArrayList;
import java.util.List;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.models.Promotor;
import dev.yonel.models.Vale;
import dev.yonel.services.vales.ServiceVales;
import lombok.Getter;
import lombok.Setter;

public class ProxyABaseDeDatos {
    private @Setter static boolean cambioMarca = true;
    private @Setter static boolean cambioModelo = true;
    private @Setter @Getter static boolean cambioCelular = true;
    private @Setter static boolean cambioImei = true;
    private @Setter static boolean cambioValeByPromotor = true;
    private @Setter @Getter static boolean cambioVale = true;
    private @Setter @Getter static boolean cambioPromotor = true;

    private static Long idPromotor;

    private static List<Marca> marcas;
    private static List<Modelo> modelos;
    private static List<Celular> celulares;
    private static List<String> imeis;
    private static List<Vale> valesByPromotor;
    private static List<Promotor> promotores;
    private static List<Vale> vales;
    

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

    public static List<Promotor> getListPromotores(){
        if(promotores == null){
            promotores  = new ArrayList<>();
        }

        if(cambioPromotor){
            promotores.clear();
            promotores.addAll(Promotor.getAll(Promotor.class));
            cambioPromotor = false;
        }

        return promotores;
    }

    public static List<Vale> getListValesByPromotor(long i){
        if(valesByPromotor == null){
            valesByPromotor = new ArrayList<>();
        }
        if(idPromotor == null){
            idPromotor = i;
            valesByPromotor.clear();
            valesByPromotor.addAll(ServiceVales.findValesByPromotor(idPromotor));
            cambioVale = false;

        }else if(idPromotor != i){
            idPromotor = i;
            valesByPromotor.clear();
            valesByPromotor.addAll(ServiceVales.findValesByPromotor(idPromotor));
            cambioVale = false;
        }

        if(cambioVale){
            valesByPromotor.clear();
            valesByPromotor.addAll(ServiceVales.findValesByPromotor(idPromotor));
            cambioVale = false;
        }

        return valesByPromotor;
    }

    public static List<Vale> getListVales(){
        if(vales == null){
            vales = new ArrayList<>();
        }

        if(cambioVale){
            vales = Vale.getAll(Vale.class);

            cambioVale = false;
        }

        return vales;
    }   
}
