package dev.yonel.services.controllers.celulares;

import java.time.LocalDate;
import java.util.Optional;

import dev.yonel.controllers.CelularesController;
import dev.yonel.models.Marca;
import dev.yonel.models.Modelo;
import dev.yonel.services.celulares.ServiceCelular;
import dev.yonel.services.celulares.ServiceMarca;
import dev.yonel.services.celulares.ServiceModelo;
import dev.yonel.utils.validation.MFXTextFieldUtil;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TextInputDialog;

public class ServiceCelularControllerAgregar {

    private static ServiceCelularControllerAgregar instance;

    private ServiceMarca serviceMarca;
    private ServiceModelo serviceModelo;

    private ServiceCelularControllerAgregar() {
        instance = this;

        Platform.runLater(() -> {
            this.serviceMarca = new ServiceMarca();
            this.serviceModelo = new ServiceModelo();
        });
    }

    public static ServiceCelularControllerAgregar getInstance() {
        if (instance == null) {
            instance = new ServiceCelularControllerAgregar();
        }
        return instance;
    }

    public static void restartInstance() {
        instance = null;
    }

    /*******************************************************
     * ***********CONFIGURACIÓN DE LOS CONTROLES************
     *******************************************************/

    public void configure() {
        Platform.runLater(() -> {
            setEstadoInformation("");

            serviceMarca.configureComboBox(CelularesController.getInstance().getCmbMarcaAgregar());
            serviceModelo.configureComboBox(
                    CelularesController.getInstance().getCmbModeloAgregar(),
                    CelularesController.getInstance().getCmbMarcaAgregar(),
                    CelularesController.getInstance().getBtnAgregarModelo());

            MFXTextFieldUtil.validateIMEI(
                    CelularesController.getInstance().getTxtImeiUno(),
                    CelularesController.getInstance().getValidationLabel_ImeiUno());
            CelularesController.getInstance().getTxtImeiUno().setText("");
            CelularesController.getInstance().getValidationLabel_ImeiUno().setText("");

            MFXTextFieldUtil.validateIMEI(
                    CelularesController.getInstance().getTxtImeiDos(),
                    CelularesController.getInstance().getValidationLabel_ImeiDos());
            CelularesController.getInstance().getTxtImeiDos().setText("");
            CelularesController.getInstance().getValidationLabel_ImeiDos().setText("");

            MFXTextFieldUtil.validatePrecio(
                    CelularesController.getInstance().getTxtPrecio(),
                    CelularesController.getInstance().getValidationLabel_Precio());
            CelularesController.getInstance().getTxtPrecio().setText("");
            CelularesController.getInstance().getValidationLabel_Precio().setText("");
            CelularesController.getInstance().getDateFechaInventario().setValue(null);
            CelularesController.getInstance().getTxtObservaciones().setText("");

            CelularesController.getInstance().getBtnAgregarMarca().setOnAction(event -> {
                guardarMarca();
            });

            CelularesController.getInstance().getBtnAgregarModelo().setOnAction(event -> {
                guardarModelo();
            });

            CelularesController.getInstance().getBtnLimpiar().setOnAction(event -> {
                limpiar();
            });

            CelularesController.getInstance().getBtnGuardar().setOnAction(event -> {
                guardarCelular();
            });

            // Escuchamos los cambios del modeloCombo
            CelularesController.getInstance().getCmbModeloAgregar().selectedItemProperty()
                    .addListener(new ChangeListener<Modelo>() {
                        @Override
                        public void changed(ObservableValue<? extends Modelo> observable, Modelo oldValue,
                                Modelo newValue) {
                            if (newValue != null) {
                                CelularesController.getInstance().getTxtImeiUno().setDisable(false);
                                CelularesController.getInstance().getTxtImeiDos().setDisable(false);
                                CelularesController.getInstance().getTxtPrecio().setDisable(false);
                                CelularesController.getInstance().getDateFechaInventario().setDisable(false);
                                CelularesController.getInstance().getTxtObservaciones().setDisable(false);
                            }
                        }
                    });

            // Escuchamos los cambios de la fecha
            CelularesController.getInstance().getDateFechaInventario().valueProperty()
                    .addListener(new ChangeListener<LocalDate>() {
                        @Override
                        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
                                LocalDate newValue) {
                            if (newValue != null) {
                                CelularesController.getInstance().getValidationLabel_Fecha().setVisible(false);
                                CelularesController.getInstance().getValidationLabel_Fecha().setText("");
                            }
                        }
                    });

        });
    }

    /**************************************************
     ***************** MÉTODOS*************************
     **************************************************/

    /**
     * Método con el cual vamos a pasar mensajes de información al Label de
     * estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoInformation(String estado) {
        CelularesController.getInstance().getLblEstado().setText(estado);
        CelularesController.getInstance().getLblEstado().getStyleClass().add("label");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            CelularesController.getInstance().getLblEstado().setText("");
        });
        pause.play();
    }

    /**
     * Método con el que vamos a pasar mensajes de error al Label de
     * estado.
     *
     * @param estado el mensaje que se desea mostrar.
     */
    public void setEstadoError(String estado) {
        CelularesController.getInstance().getLblEstado().setText(estado);
        CelularesController.getInstance().getLblEstado().getStyleClass().add("label-error");

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(10));
        pause.setOnFinished(event -> {
            CelularesController.getInstance().getLblEstado().setText(null);
        });
        pause.play();
    }

    private void guardarMarca() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Vista Agregar Celular");
        dialog.setHeaderText("Introduzca la marca que desea agregar.");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            serviceMarca.setMarca(new Marca(name));

            if (serviceMarca.save()) {
                // Igualar el modelo existente por el de la lista para evitar el error al
                // seleccionar el item en el combobox
                ObservableList<Marca> newObservableList = CelularesController.getInstance().getCmbMarcaAgregar()
                        .getItems();
                for (Marca m : newObservableList) {
                    if (serviceMarca.getMarca().getMarca().equals(m.getMarca())) {
                        serviceMarca.setMarca(m);
                    }
                }
                CelularesController.getInstance().getCmbMarcaAgregar().selectItem(serviceMarca.getMarca());
                setEstadoInformation("Marca " + serviceMarca.getMarca().getMarca() + " agregada.");
            } else if (ServiceMarca.isBanderaMarcaExiste()) {
                guardarMarca();
                ServiceMarca.setBanderaMarcaExiste(false);
            }
        });
    }

    private void guardarModelo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Modelo");
        dialog.setHeaderText("El modelo se agregará a la marca "
                + CelularesController.getInstance().getCmbMarcaAgregar().getValue().getMarca());
        dialog.setContentText("Introduzca el modelo que desea agregar.");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            Modelo modelo = new Modelo();
            modelo.setMarca(CelularesController.getInstance().getCmbMarcaAgregar().getValue());
            modelo.setModelo(name);

            serviceModelo.setModelo(modelo);

            if (serviceModelo.save()) {

                // Igualar el modelo existente por el de la lista para eviatar el error en
                // selectItem.
                ObservableList<Modelo> pruebaObservable = CelularesController.getInstance().getCmbModeloAgregar()
                        .getItems();
                for (Modelo m : pruebaObservable) {
                    if (m.getModelo().equals(modelo.getModelo())) {
                        modelo = m;
                    }
                }
                CelularesController.getInstance().getCmbModeloAgregar().selectItem(modelo);
                setEstadoInformation("Modelo " + modelo.getModelo() + " agregado.");
            } else if (ServiceModelo.isBanderaModeloExiste()) {
                guardarModelo();
                ServiceModelo.setBanderaModeloExiste(false);
            }
        });
    }

    private void guardarCelular() {
        // Parte de validación
        if (CelularesController.getInstance().getCmbModeloAgregar().getValue() == null) {
            setEstadoError("Seleccione una MARCA");
        } else if (CelularesController.getInstance().getCmbModeloAgregar().getValue() == null) {
            setEstadoError("Seleccione un MODELO");
        } else if (isValid()) {

            // Parte de guardado
            setEstadoInformation("Guardando...");
            ServiceCelular serviceCelular = new ServiceCelular();
            serviceCelular.setMarca(CelularesController.getInstance().getCmbMarcaAgregar().getValue());
            serviceCelular.setModelo(CelularesController.getInstance().getCmbModeloAgregar().getValue());
            serviceCelular.setImei_Uno(CelularesController.getInstance().getTxtImeiUno().getText());
            serviceCelular.setImei_Dos(CelularesController.getInstance().getTxtImeiDos().getText());
            serviceCelular.setPrecio(CelularesController.getInstance().getTxtPrecio().getText());
            serviceCelular.setFechaInventario(CelularesController.getInstance().getDateFechaInventario().getValue());
            serviceCelular.setObservaciones(CelularesController.getInstance().getTxtObservaciones().getText());

            if (serviceCelular.saveCelular()) {
                CelularesController.getInstance().getTxtImeiUno().setText("");
                CelularesController.getInstance().getTxtImeiDos().setText("");
                CelularesController.getInstance().getTxtPrecio().setText("");
                CelularesController.getInstance().getTxtObservaciones().setText("");
                CelularesController.getInstance().getValidationLabel_ImeiUno().setVisible(false);
                CelularesController.getInstance().getValidationLabel_ImeiDos().setVisible(false);
                CelularesController.getInstance().getValidationLabel_Precio().setVisible(false);
                // Establecemos que se ha agregado un nuevo celular para que al pasar a la vista
                // este recargue los celulares
            }
        }
    }

    private void limpiar() {
        CelularesController.getInstance().getCmbMarcaAgregar().getSelectionModel().clearSelection();
        CelularesController.getInstance().getTxtImeiUno().setDisable(true);
        CelularesController.getInstance().getTxtImeiUno().setText("");
        CelularesController.getInstance().getTxtImeiDos().setDisable(true);
        CelularesController.getInstance().getTxtImeiDos().setText("");
        CelularesController.getInstance().getTxtPrecio().setDisable(true);
        CelularesController.getInstance().getTxtPrecio().setText("");
        CelularesController.getInstance().getDateFechaInventario().setDisable(true);
        CelularesController.getInstance().getDateFechaInventario().setValue(null);
        CelularesController.getInstance().getTxtObservaciones().setDisable(true);
        CelularesController.getInstance().getTxtObservaciones().setText("");
        CelularesController.getInstance().getValidationLabel_ImeiUno().setVisible(false);
        CelularesController.getInstance().getValidationLabel_ImeiDos().setVisible(false);
    }

    private boolean isValid() {

        if (CelularesController.getInstance().getTxtImeiUno().getLength() == 0
                || CelularesController.getInstance().getTxtPrecio().getLength() == 0
                || CelularesController.getInstance().getDateFechaInventario().getValue() == null) {
            if (CelularesController.getInstance().getTxtImeiUno().getLength() == 0) {
                CelularesController.getInstance().getValidationLabel_ImeiUno().setText("Campo obligatorio");
                CelularesController.getInstance().getValidationLabel_ImeiUno().setAlignment(Pos.CENTER_RIGHT);
                CelularesController.getInstance().getValidationLabel_ImeiUno().setVisible(true);
            }

            if (CelularesController.getInstance().getTxtPrecio().getLength() == 0) {
                CelularesController.getInstance().getValidationLabel_Precio().setText("Campo obligatorio");
                CelularesController.getInstance().getValidationLabel_Precio().setAlignment(Pos.CENTER_RIGHT);
                CelularesController.getInstance().getValidationLabel_Precio().setVisible(true);
            }

            if (CelularesController.getInstance().getDateFechaInventario().getValue() == null) {
                CelularesController.getInstance().getValidationLabel_Fecha().setText("Campo obligatorio.");
                CelularesController.getInstance().getValidationLabel_Fecha().setAlignment(Pos.CENTER_RIGHT);
                CelularesController.getInstance().getValidationLabel_Fecha().setVisible(true);
            }

            setEstadoError("Verifique los campos.");
            return false;
        } else if (!CelularesController.getInstance().getTxtImeiUno().isValid()
                || !CelularesController.getInstance().getTxtPrecio().isValid()
                || !CelularesController.getInstance().getTxtImeiDos().isValid()
                        && CelularesController.getInstance().getTxtImeiDos().getLength() > 0) {
            setEstadoError("Verifique los campos");
            return false;
        } else {
            return true;
        }
    }
}
