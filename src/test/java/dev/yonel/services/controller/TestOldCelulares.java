package dev.yonel.services.controller;

import org.junit.jupiter.api.Test;

import dev.yonel.services.controllers.principal.InfoCelulares;

public class TestOldCelulares {

    @Test
    public void testWriteOldCelular(){
        InfoCelulares.getInstance().writeCelulares();
    }

    @Test
    public void testExtractIdsFromFile(){
        for (Integer id  : InfoCelulares.getInstance().extractIdsFromFile()) {
            System.out.println(id);
        }
    }
}
