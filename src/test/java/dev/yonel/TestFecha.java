package dev.yonel;

import org.junit.jupiter.api.Test;

import dev.yonel.utils.Fecha;

public class TestFecha {

    String fecha = "22-10-2024";

    @Test
    public void testFecha(){
        if(Fecha.esFechaValida(fecha)){
            System.out.println("Es fecha valida");
        }else{
            System.out.println("No es fecha valida");
        }
    }
}
