package dev.yonel.utils.ui.popup;

public interface PopupUtil {

    public void load();

    public void load(Runnable loading);

    public void show();

    public void show(Runnable action);

    public void show(Double x, Double y);

    public void show(Double x, Double y, Runnable action);

    public Runnable close();

    public Runnable close(Runnable action);

    public void setFxml(String fxml);

    public void setController(Object controller);

}
