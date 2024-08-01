package dev.yonel;



import org.junit.jupiter.api.Test;

import dev.yonel.models.Celular;
import dev.yonel.models.Marca;

import dev.yonel.utils.Fecha;


public class TestCelular {
    final Marca marca = new Marca(null, "Samsung");
        

      
    @Test
    public void CrearMarca(){
        //Guardamos la marca y el modelo
        marca.save();
        
    }
    @Test
    public void TestCelularUno(){

        final Celular celular = new Celular();
        celular.setMarca(null);
        celular.setModelo(null);
        celular.setImeiUno((long) 1234567890);
        String fecha = "22-10-2024";
        celular.setFechaInventario(Fecha.getLocalDate(fecha));
        celular.setVendido(false);
        celular.setObservaciones("Tiene una raya en la pantalla");

        if(celular.save()){
            System.out.println("Celular guardado");
        }else{
            System.err.println("Celular no guardado");
        }
    }


    @Test
    public void VerCelularUno(){
        final Celular celular = Celular.getById(Celular.class, 1);

        System.out.println(celular);

    }
    
}
