package dev.yonel.services.controllers.promotores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import dev.yonel.App;
import dev.yonel.controllers.items.ItemPromotoresController;
import dev.yonel.models.Promotor;
import dev.yonel.services.promotores.ServicePromotor;
import dev.yonel.utils.data_access.UtilsHibernate;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ServicePromotoresControllerVista {

    private static final SessionFactory sessionFactory = UtilsHibernate.getSessionFactory();

    private static VBox vBoxItems;
    private CheckBox checkEnGarantia;
    private CheckBox checkPorPagar;

    private FiltrarItemsPromotor filterItems;

    private static List<ItemPromotoresController> itemPromotorControllers = new ArrayList<>();

    @SuppressWarnings("static-access")
    public ServicePromotoresControllerVista(Map<String, Object> list) {
        this.vBoxItems = (VBox) list.get("vbox");
        this.checkEnGarantia = (CheckBox) list.get("checkEnGarantia");
        this.checkPorPagar = (CheckBox) list.get("checkPorPagar");
    }

    /******************************************
     * **********MÉTODOS ESTÁTICO**************
     ******************************************/

    private static void getAllItems() {
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

    public static void setItems(Promotor promotor) {
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

    public static void cleanVBox() {
        vBoxItems.getChildren().clear();
        itemPromotorControllers.clear();
    }

    public static int getIndexItemPromotorController(ItemPromotoresController controller) {
        return itemPromotorControllers.indexOf(controller);
    }

    public static void removeItem(int i) {
        vBoxItems.getChildren().remove(i);
    }

    /******************************************
     * **********MÉTODOS***********************
     ******************************************/

    public void configure(){
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
    }

    private void filtrar(FiltrarItemsPromotor filter){
        filter.setEnGarantia(checkEnGarantia.isSelected());
        filter.setPorPagar(checkPorPagar.isSelected());
    }
}
