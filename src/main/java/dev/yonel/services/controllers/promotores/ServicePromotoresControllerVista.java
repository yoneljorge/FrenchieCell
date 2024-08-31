package dev.yonel.services.controllers.promotores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dev.yonel.App;
import dev.yonel.controllers.PromotoresController;
import dev.yonel.controllers.items.ItemPromotoresController;
import dev.yonel.models.Promotor;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.utils.data_access.UtilsHibernate;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ServicePromotoresControllerVista {

    private static ServicePromotoresControllerVista instance;

    private VBox vBoxItems;
    private CheckBox checkEnGarantia;
    private CheckBox checkPorPagar;

    private final SessionFactory sessionFactory;
    private FiltrarItemsPromotor filterItems;
    private List<ItemPromotoresController> itemPromotorControllers;

    private PromotoresController promotoresController = PromotoresController.getInstance();

    private ServicePromotoresControllerVista() {
        instance = this;

        this.sessionFactory = UtilsHibernate.getSessionFactory();
        Platform.runLater(() -> {

            this.itemPromotorControllers = new ArrayList<>();
            setObjects();
        });
    }

    public static ServicePromotoresControllerVista getInstance() {
        if (instance == null) {
            instance = new ServicePromotoresControllerVista();
        }
        return instance;
    }

    private void setObjects() {
        this.vBoxItems = promotoresController.getVboxLista_Items();
        this.checkEnGarantia = promotoresController.getCheckEnGarantia();
        this.checkPorPagar = promotoresController.getCheckPorPagar();
    }

    public void configure() {
        Platform.runLater(() -> {
            cleanVBox();
            getAllItems();

            checkEnGarantia.selectedProperty().addListener((observable, oldValue, newValue) -> {
                filterItems = new FiltrarItemsPromotor();
                filtrar(filterItems);
                filterItems.getAllItems();
            });

            checkPorPagar.selectedProperty().addListener((observable, oldValue, newValue) -> {
                filterItems = new FiltrarItemsPromotor();
                filtrar(filterItems);
                filterItems.getAllItems();
            });
        });
    }

    private void getAllItems() {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            String hql = "FROM Promotor"; // Tu entidad promotor
            Query<Promotor> query = sessionFactory.getCurrentSession().createQuery(hql, Promotor.class);
            query.setFirstResult(0); // Desde el primer registro
            query.setMaxResults(1); // Solo un registro

            while (true) {
                List<Promotor> resultList = query.list();
                if (resultList.isEmpty()) {
                    break; // Si no hay más resultados, salir
                }
                setItems(resultList.get(0));
                System.out.println("Nombre del promotor -> " + resultList.get(0).getNombre());

                // Incrementa el desplazamiento para el siguiente
                int currentFirstResult = query.getFirstResult();
                query.setFirstResult(currentFirstResult + 1);
            }

            tx.commit(); // Finaliza la transacción.
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();// Deshacer la transacción en caso de error.
            }
            e.printStackTrace();// Maneja o registra la excepción
        }
    }

    public void setItems(Promotor promotor) {
        // Si el promotor no coincide con el filtrado se pasa un null para que no haga
        // nada.
        if (promotor != null) {
            try {
                HBox hbox;
                FXMLLoader loader = App.fxmlLoader("items/itemPromotores");
                ItemPromotoresController controller = new ItemPromotoresController();
                // ----> Todo lo que se le va a agregar al controlador
                controller.setPromotor(new ServicePromotor(promotor));
                loader.setController(controller);
                hbox = loader.load();
                vBoxItems.getChildren().add(hbox);

                itemPromotorControllers.add(controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanVBox() {
        vBoxItems.getChildren().clear();
        itemPromotorControllers.clear();
    }

    public int getIndexItemPromotorController(ItemPromotoresController controller) {
        return itemPromotorControllers.indexOf(controller);
    }

    public void removeItem(int i) {
        vBoxItems.getChildren().remove(i);
    }

    private void filtrar(FiltrarItemsPromotor filter) {
        filter.setEnGarantia(checkEnGarantia.isSelected());
        filter.setPorPagar(checkPorPagar.isSelected());
    }
}
