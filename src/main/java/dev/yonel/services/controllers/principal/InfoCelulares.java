package dev.yonel.services.controllers.principal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.yonel.models.Celular;
import dev.yonel.models.Vale;
import dev.yonel.services.ConfigManager;
import dev.yonel.services.Mensajes;

public class InfoCelulares {
    private static InfoCelulares instance;

    Mensajes mensajes = new Mensajes(getClass());

    private InfoCelulares(){
        instance = this;    
    }

    private static synchronized void createInstance(){
        instance = new InfoCelulares();
    }
    public static InfoCelulares getInstance(){
        if(instance == null){
            createInstance();
        }

        return instance;
    }

    private String INFO_FILE = ConfigManager.getInstance().getHomeFrenchieCell() + "info";

    public void update(){
        deleteInfo();
        writeCelulares();
        writeVales();
    }
    private void deleteInfo(){
        File file = new File(INFO_FILE);
        if (file.exists()) {
            if (file.delete()) {
                getInstance().mensajes.info("Archivo info borrado.");
            } else {
                getInstance().mensajes.err("No se pudo borrar el archivo info.");
            }
        }
    }

    private void writeCelulares() {
        List<Celular> celulares = new ArrayList<>();
        Celular celular;

        long cantidadCelulares = 0L;
        float importe = 0F;
        while ((celular = Celular.getAllOneToOne(Celular.class)) != null) {
            if (!celular.getVendido()) {
                celulares.add(celular);
                cantidadCelulares ++;
                importe += celular.getPrecio();
            }
        }

        // Ordenar la lista por fecha de inventario
        celulares.sort(Comparator.comparing(Celular::getFechaInventario));

        if (!celulares.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(INFO_FILE))) {
                for (Celular cel : celulares) {
                    StringBuilder string = new StringBuilder();
                    string.append("id: ").append(cel.getIdCelular());
                    string.append(" fecha: ").append(cel.getFechaInventario());
                    writer.write(string.toString());
                    writer.newLine();
                }
                String cantidadCelularesString = "inStock: " + cantidadCelulares;
                String importeString = "importe: " + importe;
                writer.write(cantidadCelularesString);
                writer.newLine();
                writer.write(importeString);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
                getInstance().mensajes.err(e.getMessage());
            }
        }
    }

    private void writeVales(){
        Vale vale;
        long enGarantia = 0L;
        long porPagar = 0L;

        while ((vale = Vale.getAllOneToOne(Vale.class))!= null) {
            if(!vale.getLiquidado()){
                if(vale.getGarantia()){
                    enGarantia ++;
                }else{
                    porPagar ++;
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INFO_FILE, true))) {
            writer.write("enGarantia: " + enGarantia);
            writer.newLine();
            writer.write("porPagar: " + porPagar);
        } catch (IOException e) {
            mensajes.err(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Integer> extractIdsFromFile() {
        List<Integer> ids = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(INFO_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("id: ")) {
                    int id = Integer.parseInt(line.substring(4, line.indexOf(" fecha: ")));
                    ids.add(id);
                }
            }

            return ids;
        } catch (Exception e) {
            getInstance().mensajes.err(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, String> infoStock(){
        Map<String, String> info = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(INFO_FILE))) {
            String line;
            while((line = reader.readLine())!= null){
                if(line.startsWith("inStock: ")){
                    String cantidadCelulares = line.substring(9);
                    info.put("inStock", cantidadCelulares);
                }else if(line.startsWith("importe: ")){
                    String importe = line.substring(9);
                    info.put("importe", importe);
                }else if(line.startsWith("enGarantia: ")){
                    String enGarantia = line.substring(12);
                    info.put("enGarantia", enGarantia);
                }else if(line.startsWith("porPagar: ")){
                    String porPagar = line.substring(10);
                    info.put("porPagar", porPagar);
                }
            }
            return info;
        } catch (IOException e) {
            mensajes.err(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
