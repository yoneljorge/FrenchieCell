package dev.yonel.services.celulares;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.ServiceLista;
import dev.yonel.services.controllers.celulares.ServiceCelularControllerVista;
import dev.yonel.utils.Fecha;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

public class ServiceFecha {

    private List<Celular> listCelulares;
    private List<LocalDate> listFechas;
    private ObservableList<LocalDate> observableList;

    private MFXFilterComboBox<LocalDate> comboFecha;
    private MFXFilterComboBox<Marca> comboMarca;
    private MFXFilterComboBox<Modelo> comboModelo;

    public ServiceFecha(MFXFilterComboBox<LocalDate> comboFecha, MFXFilterComboBox<Marca> comboMarca,
            MFXFilterComboBox<Modelo> comboModelo) {
        this.comboFecha = comboFecha;
        this.comboMarca = comboMarca;
        this.comboModelo = comboModelo;
    }

    public void configureComboBox() {
        StringConverter<LocalDate> converter = FunctionalStringConverter
                .to(fecha -> (fecha == null) ? "" : Fecha.getStringOfLocalDate(fecha));
        Function<String, Predicate<LocalDate>> filterFunction = s -> fecha -> StringUtils
                .containsIgnoreCase(converter.toString(fecha), s);

        getCelulares();
        observableList = FXCollections.observableArrayList(listFechas);

        comboFecha.setItems(observableList);
        comboFecha.setConverter(converter);
        comboFecha.setFilterFunction(filterFunction);
        comboFecha.getSelectionModel().clearSelection();
    }

    private void getCelulares() {
        /*
         * En cada caso verificamos si está inicializada la lista y si no entonces se
         * inicializa, luego se limpia (esto es en caso de que esté inicializada y de
         * que contenga información) y luego se llenan con los datos correspondientes.
         */
        if (listCelulares == null) {
            listCelulares = new ArrayList<>();
        }
        listCelulares.clear();
        listCelulares.addAll(ServiceLista.getListCelulares());

        // Se inicializa y se limpia la listFecha
        if (listFechas == null) {
            listFechas = new ArrayList<>();
        }
        listFechas.clear();

        /*
         * En caso de que esté seleccionado TODAS en el comboMarca entonces la lista de
         * fechas se llena con todas las fechas de todos los celulares.
         * En caso contrario entonces se recorre la lista y se le asigna solo los
         * celulares que cumplen la condición.
         * Al iniciar la aplicación la selección del ComboBox es nula, por lo que si
         * verificamos que es nula, entonces se llena el ComboBox de la fecha con todos
         * los elementos.
         */

        if (comboMarca.getValue() != null) {
            if (ServiceCelularControllerVista.getMarca().equals(this.comboMarca.getValue())) {
                for (Celular c : listCelulares) {
                    if (!listFechas.contains(c.getFechaInventario())) {
                        listFechas.add(c.getFechaInventario());
                    }
                }
            } else if (this.comboModelo.getValue() != null) {

                if (this.comboModelo.getValue().equals(ServiceCelularControllerVista.getModelo())) {
                    for (Celular c : listCelulares) {
                        if (c.getMarca().equals(this.comboMarca.getValue())) {
                            if (!listFechas.contains(c.getFechaInventario())) {
                                listFechas.add(c.getFechaInventario());
                            }
                        }
                    }
                } else {
                    System.out.println("ComboMarca: " + comboMarca.getValue().getMarca() +
                        "\nComboModelo: " + comboModelo.getValue().getModelo());
                    for (Celular c : listCelulares) {
                        if (c.getMarca().getMarca().equals(this.comboMarca.getValue().getMarca())
                                && c.getModelo().getModelo().equals(this.comboModelo.getValue().getModelo())) {
                            if (!listFechas.contains(c.getFechaInventario())) {
                                listFechas.add(c.getFechaInventario());
                            }
                        }
                    }
                }
            }
        } else {
            for (Celular c : listCelulares) {
                if (!listFechas.contains(c.getFechaInventario())) {
                    listFechas.add(c.getFechaInventario());
                }
            }
        }
    }
}
