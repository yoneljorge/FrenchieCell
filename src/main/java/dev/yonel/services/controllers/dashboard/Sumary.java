package dev.yonel.services.controllers.dashboard;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;

public class Sumary {

    public static void get() {
        Celular celular;
        Map<Marca, List<Map.Entry<Modelo, CelularInfo>>> mapSumary = new HashMap<>();

        while ((celular = Celular.getAllOneToOne(Celular.class)) != null) {
            if (!celular.getVendido()) {
                Marca marca = celular.getMarca();
                Modelo modelo = celular.getModelo();
                Double precio = celular.getPrecio();
                String comentario = celular.getObservaciones(); // Asumiendo que existe un método getComentario()

                // Obtener la lista de modelos para la marca, o crearla si no existe
                List<Map.Entry<Modelo, CelularInfo>> modeloList = mapSumary.getOrDefault(marca, new ArrayList<>());

                // Verificar si el modelo ya existe en la lista
                boolean modeloEncontrado = false;
                for (Map.Entry<Modelo, CelularInfo> entry : modeloList) {
                    if (entry.getKey().equals(modelo)) {
                        // Si el modelo ya existe, actualizar la información (cantidad, precios,
                        // comentarios)
                        entry.getValue().addCelular(precio, comentario);
                        modeloEncontrado = true;
                        break;
                    }
                }

                // Si el modelo no está en la lista, agregarlo con la información inicial
                if (!modeloEncontrado) {
                    CelularInfo celularInfo = new CelularInfo(1, precio, comentario);
                    modeloList.add(new AbstractMap.SimpleEntry<>(modelo, celularInfo));
                }

                // Actualizar el mapa con la nueva lista de modelos
                mapSumary.put(marca, modeloList);
            }
        }
    }

    public static String getContentCelulares() {
        Celular celular;
        StringBuilder stringBuilder = new StringBuilder();
        while ((celular = Celular.getAllOneToOne(Celular.class)) != null) {
            stringBuilder.append("Marca: " + celular.getMarca());
            stringBuilder.append("-Modelo: " + celular.getModelo());
            stringBuilder.append("-Precio: " + celular.getPrecio());
            stringBuilder.append("-Observaciones: " + celular.getObservaciones());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


    /******************* PRUEBA *********************** */

    public static String getSumary() {
        // Mapa que almacenará la información de los celulares
        Map<Marca, List<Map.Entry<Modelo, List<Celular>>>> mapSummary = new HashMap<>();

        // Obtener todos los celulares
        List<Celular> celulares = Celular.getAll(Celular.class);

        for (Celular celular : celulares) {
            if (!celular.getVendido()) {
                // Si la marca no existe en el mapa, la agregamos
                mapSummary.putIfAbsent(celular.getMarca(), new ArrayList<>());

                // Buscamos si el modelo ya está en la lista
                List<Map.Entry<Modelo, List<Celular>>> modelos = mapSummary.get(celular.getMarca());
                Optional<Map.Entry<Modelo, List<Celular>>> entryOptional = modelos.stream()
                        .filter(entry -> entry.getKey().equals(celular.getModelo()))
                        .findFirst();

                if (entryOptional.isPresent()) {
                    // Si el modelo ya existe, agregamos el celular a la lista
                    entryOptional.get().getValue().add(celular);
                } else {
                    // Si no existe el modelo, lo agregamos junto con el celular
                    List<Celular> listaCelulares = new ArrayList<>();
                    listaCelulares.add(celular);
                    modelos.add(new AbstractMap.SimpleEntry<>(celular.getModelo(), listaCelulares));
                }
            }
        }

        // Generamos el reporte en formato similar al solicitado
        return generateReport(mapSummary);
    }

    // Método que genera el reporte en formato de texto
    private static String generateReport(Map<Marca, List<Map.Entry<Modelo, List<Celular>>>> mapSummary) {
        StringBuilder report = new StringBuilder();

        for (Map.Entry<Marca, List<Map.Entry<Modelo, List<Celular>>>> entryMarca : mapSummary.entrySet()) {
            Marca marca = entryMarca.getKey();
            report.append("🌠 *").append(marca).append("* 🌠\n");

            for (Map.Entry<Modelo, List<Celular>> entryModelo : entryMarca.getValue()) {
                Modelo modelo = entryModelo.getKey();
                List<Celular> celulares = entryModelo.getValue();

                // Agrupamos los celulares por precio para generar las observaciones
                Map<Double, List<String>> preciosObservaciones = new HashMap<>();

                for (Celular celular : celulares) {
                    preciosObservaciones.putIfAbsent(celular.getPrecio(), new ArrayList<>());
                    preciosObservaciones.get(celular.getPrecio()).add(celular.getObservaciones());
                }

                // Generamos el formato de salida por modelo y precio
                for (Map.Entry<Double, List<String>> entryPrecio : preciosObservaciones.entrySet()) {
                    Double precio = entryPrecio.getKey();
                    List<String> observaciones = entryPrecio.getValue();

                    report.append("📲 ").append(modelo)
                            .append(": ").append(celulares.size()).append(" unidades ")
                            .append("💸 *").append(precio).append("USD*\n");

                    // Agregar observaciones si hay
                    if (!observaciones.isEmpty()) {
                        report.append(" Observaciones: ");
                        report.append(String.join(", ", observaciones)).append("\n");
                    }
                }
            }
            report.append("\n");
        }

        return report.toString();
    }
}