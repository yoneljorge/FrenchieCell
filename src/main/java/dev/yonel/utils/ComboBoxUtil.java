package dev.yonel.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

import java.util.function.Function;

public class ComboBoxUtil<T> {
    private ComboBox<T> comboBox;
    private List<T> items;
    private Function<T, String> displayFunction;
    private ObservableList<T> observableList = FXCollections.observableArrayList();
    private boolean loadingData = false;

    public ComboBoxUtil(ComboBox<T> comboBox, Function<T, String> displayFunction) {
        this.comboBox = comboBox;
        this.displayFunction = displayFunction;
    }

    public void filtrar(List<T> items) {
        if (loadingData)
            return;

        loadingData = true;// Indicar que estamos cargando datos
        observableList.clear();// Limpiar la lista existente

        this.items = items;
        this.observableList = FXCollections.observableArrayList(this.items);
        // Crear el ComboBox con la observableList
        this.comboBox.setItems(observableList);

        // Filtrar elementos mientras se escribe
        TextField editor = (TextField) this.comboBox.getEditor();
        FilteredList<T> filteredList = new FilteredList<>(observableList, p -> true);

        // Agregar un listener al editor de texto del ComboBox
        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(item -> {
                // Si el campo está vacio, mostrar todos los elementos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String upperCaseFilter = newValue.toUpperCase();
                return displayFunction.apply(item).toUpperCase().contains(upperCaseFilter);
            });

            try {
                // Actualizar las opciones del ComboBox con la lista filtrada
                comboBox.setItems(filteredList);
                comboBox.show(); // Mostrar la lista filtrada automáticamente
            } catch (Exception e) {
                e.printStackTrace(); //Manejo básico de excepciones
            } finally{
                loadingData = false; //Restablecer la bandera
            }

        });

        comboBox.setCellFactory(lv -> new javafx.scene.control.ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(displayFunction.apply(item));
                }
            }
        });

        comboBox.setButtonCell(new javafx.scene.control.ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(displayFunction.apply(item));
                }
            }
        });

        //Limpiamos la lista 
        this.items.clear();
    }
}
