package dev.yonel.dataAccess;

import dev.yonel.models.Promotor;
import org.junit.jupiter.api.Test;


public class TestGenericDAO {

    @Test
    public void TestDAOPromotor(){
        Promotor promotor;

        while ((promotor = Promotor.getAllOneToOne(Promotor.class)) != null){
            System.out.println(promotor.getNombre());
        }
    }
}
