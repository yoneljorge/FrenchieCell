package dev.yonel.utils.ui;

import javafx.scene.chart.NumberAxis;
import javafx.util.StringConverter;

public class CustomAxis {

    public static void onlyInteger(NumberAxis customAxis) {
        // Set the axis format to display only integers
        customAxis.setTickLabelFormatter(new StringConverter<Number>() {

            @Override
            public String toString(Number object) {
                return String.format("%d", object.intValue());
            }

            @Override
            public Number fromString(String string) {
                return Integer.parseInt(string);
            }
        });
    }
}