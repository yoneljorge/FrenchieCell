package dev.yonel.utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ListCell;

import java.util.List;

import java.util.function.Function;

public class ComboBoxUtil<T> {
    private final ComboBox<T> comboBox;
    private final Function<T, String> displayFunction;
    private final List<T> items;
    private final ObservableList<T> observableList = FXCollections.observableArrayList();
    private boolean loadingData = false;

    public ComboBoxUtil(ComboBox<T> comboBox, List<T> items, Function<T, String> displayFunction) {

        if (displayFunction == null) {
            throw new IllegalArgumentException("El displayFunction no puede ser null");
        }

        this.comboBox = comboBox;
        this.displayFunction = displayFunction;
        this.items = items;

        configureComboBox();
        loadData();
    }

    private void configureComboBox() {

        comboBox.setCellFactory(lv -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : displayFunction.apply(item));
            }
        });

        comboBox.setButtonCell(new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : displayFunction.apply(item));
            }
        });

        setupFiltering();
    }

    private void loadData() {

        if (loadingData)
            return;

        loadingData = true;// Indicar que estamos cargando datos

        Platform.runLater(() -> {
            observableList.clear();
            if (this.items != null) {
                observableList.addAll(items);
            }
            comboBox.setItems(observableList);
            loadingData = false;
        });
    }

    private void setupFiltering() {

        FilteredList<T> filteredList = new FilteredList<>(observableList, p -> true);
        TextField editor = (TextField) comboBox.getEditor();

        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            String upperCaseFilter = newValue == null ? "" : newValue.toUpperCase();

            filteredList.setPredicate(item -> {
                if (upperCaseFilter.isEmpty()) {
                    return true; // Mostrar todo si el campo está vacío
                }
                return displayFunction.apply(item).toUpperCase().contains(upperCaseFilter);
            });

            Platform.runLater(() -> {
                comboBox.setItems(filteredList);

                // Si el campo de texto esta vacío, deselecciona cualquier elemento seleccionado
                if (upperCaseFilter.isEmpty()) {
                    comboBox.getSelectionModel().clearSelection();
                }
                comboBox.show(); // Muestra la lista filtrada
            });
        });
    }
}
