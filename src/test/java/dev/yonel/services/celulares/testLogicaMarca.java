package dev.yonel.services.celulares;

import org.junit.jupiter.api.Test;

import dev.yonel.models.Marca;

public class testLogicaMarca {

    @Test
    public void testPrint(){
        LogicaMarca.print();
    }

    @Test
    public void testSaveMarca(){
        Marca marca = new Marca();
        marca.setMarca("XIAOMI");

    LogicaMarca.saveMarca(marca);
        
    }
}
