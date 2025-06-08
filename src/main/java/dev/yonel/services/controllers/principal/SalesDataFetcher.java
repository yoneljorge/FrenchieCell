package dev.yonel.services.controllers.principal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;

import dev.yonel.models.Vale;
import dev.yonel.utils.data_access.UtilsHibernate;
import dev.yonel.utils.fechaUtil.FechaUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

public class SalesDataFetcher {
    private static final String SERIE_VENTAS_NAME = "Últimas Ventas";
    private static final String SERIE_MARCAS_NAME = "Ventas por Marcas";
    private static final int MAX_FECHAS = 7;

    /**
     * <p>
     * Este método crea un objeto de tipo {@code XYChart.Data<String, Number>} que
     * representa un par de valores, donde el primero es la categoría (eje X) y el
     * segundo
     * es el número (eje Y). Este dato será utilizado en una serie de datos de un
     * gráfico.
     * </p>
     * 
     * <p>
     * Además de crear el dato para la serie, este método también asocia un
     * {@link Tooltip} al nodo gráfico correspondiente. El {@code Tooltip} mostrará
     * información detallada sobre la categoría y el número cuando el usuario pase
     * el
     * puntero del mouse sobre el dato en el gráfico.
     * </p>
     * 
     * <p>
     * El estilo del nodo gráfico se ajusta para darle un diseño personalizado,
     * incluyendo
     * un radio de fondo y un padding. Los eventos del mouse también se configuran
     * para
     * que el tooltip aparezca al mover el puntero sobre el nodo y se oculte al
     * salir.
     * </p>
     * 
     * @param category La categoría que representa el dato en el eje X.
     * @param number   El valor numérico que representa el dato en el eje Y.
     * @return Un objeto {@code XYChart.Data<String, Number>} listo para añadirse
     *         a una serie de datos del gráfico.
     */
    private static XYChart.Data<String, Number> data(String category, Number number) {
        XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(category, number);

        // Configuramos el tooltip para este dato.
        data.nodeProperty().addListener(new ChangeListener<Node>() {

            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldNode, Node newNode) {
                if (newNode != null) {
                    // Creamos un tooltip para mostrar la informacion
                    Tooltip tooltip = new Tooltip();
                    Tooltip.install(newNode, tooltip);

                    // Ajustamos el tamaño del nodo
                    newNode.setStyle("-fx-background-radius: 15px; -fx-padding: 5px;");

                    // Añadir el evento de mouse moved para mostrar el tooltip
                    newNode.setOnMouseMoved(event -> {
                        tooltip.setText("Fecha: " + category + "\nVentas: " + number);
                        tooltip.show(newNode, event.getScreenX() + 10, event.getScreenY() + 10);
                    });

                    // Añadir el evento de mouse exited para ocultar el tooltip
                    newNode.setOnMouseExited(event -> {
                        tooltip.hide();
                    });
                }
            }
        });
        return data;
    }

    private static PieChart.Data dataPie(String category, Integer number) {
        PieChart.Data data = new PieChart.Data(category, number);

        // Configuramos el tooltip para este dato.
        data.nodeProperty().addListener(new ChangeListener<Node>() {

            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldNode, Node newNode) {
                if (newNode != null) {
                    // Creamos un tooltip para mostrar la informacion
                    Tooltip tooltip = new Tooltip();
                    Tooltip.install(newNode, tooltip);

                    // Ajustamos el tamaño del nodo
                    newNode.setStyle("-fx-background-radius: 15px; -fx-padding: 5px;");

                    // Añadir el evento de mouse moved para mostrar el tooltip
                    newNode.setOnMouseMoved(event -> {
                        tooltip.setText(category + "\nVentas: " + number);
                        tooltip.show(newNode, event.getScreenX() + 10, event.getScreenY() + 10);
                    });

                    // Añadir el evento de mouse exited para ocultar el tooltip
                    newNode.setOnMouseExited(event -> {
                        tooltip.hide();
                    });
                }
            }
        });
        return data;
    }

    /**
     * <p>
     * Este método extrae las ventas agrupadas por fecha desde la base de datos y
     * las organiza en una serie de datos {@code XYChart.Series<String, Number>}
     * para su visualización en un gráfico de tipo XYChart.
     * </p>
     *
     * <p>
     * El método utiliza Hibernate para consultar los datos de ventas de la entidad
     * {@link Vale}, procesando los vales de uno en uno mediante una consulta
     * paginada.
     * Se acumulan las ventas por fecha y se detiene el procesamiento cuando se han
     * obtenido los datos de ventas de las últimas 7 fechas distintas.
     * </p>
     * 
     * <p>
     * Los datos se almacenan en un {@link Map} donde la clave es la fecha de la
     * venta y
     * el valor es la cantidad de ventas realizadas en esa fecha. Luego, los datos
     * se
     * transforman en una serie para ser agregados al gráfico.
     * </p>
     *
     * @return Un objeto {@code XYChart.Series<String, Number>} que contiene los
     *         datos de las ventas agrupadas por fecha, listo para ser utilizado en
     *         un gráfico.
     */
    public static XYChart.Series<String, Number> getSerieVentas() {
        // Mapa para almacenar la cantidad de ventas por fecha.
        Map<LocalDate, Integer> ventasPorFecha = new LinkedHashMap<>();

        // Abrir sesión de Hibernate
        Session session = UtilsHibernate.getSessionFactory().openSession();
        try {
            // Consulta paginada para obtener vales de uno en uno
            int batchSize = 1; // Procesar un vale por iteración
            int startPosition = 0;
            boolean seguirProcesando = true;

            while (seguirProcesando) {
                // Realizar la consulta con paginación
                Query<Vale> query = session.createQuery("FROM Vale v ORDER BY v.fechaVenta DESC", Vale.class)
                        .setFirstResult(startPosition)
                        .setMaxResults(batchSize);
                Vale vale = query.uniqueResult();

                // Si no hay más resultados, detener el ciclo
                if (vale == null) {
                    break;
                }

                LocalDate fechaVenta = vale.getFechaVenta();

                // Acumular las ventas por fecha
                ventasPorFecha.put(fechaVenta, ventasPorFecha.getOrDefault(fechaVenta, 0) + 1);

                // Si ya tenemos 7 fechas diferentes, detener el procesamiento
                if (ventasPorFecha.size() >= MAX_FECHAS) {
                    seguirProcesando = false;
                }

                // Incrementar la posición para la próxima iteración
                startPosition += batchSize;
            }
        } finally {
            session.close();
        }

        // Crear la serie para el gráfico
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(SERIE_VENTAS_NAME);

        //Convertir las claves fechas en una lista y ordenarlas en orden inverso
        List<LocalDate> fechas = new ArrayList<>(ventasPorFecha.keySet());
        fechas.sort(Comparator.naturalOrder());

        // Agregar los datos al gráfico (máximo 7 fechas)
        //ventasPorFecha.forEach((fecha, ventas) -> {
         //   serie.getData().add(data(FechaUtil.getMesDia(fecha), ventas));
        //});

        //Recorrer la lista de fechas ordenadas al inverso
        fechas.forEach(fecha -> {
            Integer ventas = ventasPorFecha.get(fecha);
            serie.getData().add(data(FechaUtil.getMesDia(fecha), ventas));
        });

        return serie;
    }

    public static XYChart.Series<String, Number> getSerieVentasPorMarca() {
        // Mapa para almacenar la cantidad de ventas por fecha.
        Map<String, Integer> ventas = new LinkedHashMap<>();

        Vale vale;
        while ((vale = Vale.getAllOneToOne(Vale.class)) != null) {
            /*
             * Si la marca no se encuentra se devuelve el valor por defecto que es 0 y se le
             * suma 1
             * Si la marca se encuentra se aumenta la venta en 1.
             */
            String marca = vale.getMarca().getMarca();// Obtenemos el nombre de la marca
            ventas.put(marca, ventas.getOrDefault(marca, 0) + 1);
        }

        // Crear la serie para el gráfico
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(SERIE_MARCAS_NAME);

        // Agregamos cada par de datos a la serie
        ventas.forEach((marca, venta) -> serie.getData().add(data(marca, venta)));

        return serie;
    }

    public static List<PieChart.Data> getDataVentasPorMarca() {
        // Mapa para almacenar la cantidad de ventas por fecha.
        Map<String, Integer> ventas = new LinkedHashMap<>();

        Vale vale;
        while ((vale = Vale.getAllOneToOne(Vale.class)) != null) {
            /*
             * Si la marca no se encuentra se devuelve el valor por defecto que es 0 y se le
             * suma 1
             * Si la marca se encuentra se aumenta la venta en 1.
             */
            String marca = vale.getMarca().getMarca();// Obtenemos el nombre de la marca
            ventas.put(marca, ventas.getOrDefault(marca, 0) + 1);
        }

        // Crear la serie para el gráfico
        List<PieChart.Data> datas = new ArrayList<>();

        // Agregamos cada par de datos a la serie
        ventas.forEach((marca, venta) -> {
            datas.add(dataPie(marca, venta));
        });

        return datas;
    }

}