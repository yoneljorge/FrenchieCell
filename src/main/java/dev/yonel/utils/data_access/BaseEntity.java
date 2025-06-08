package dev.yonel.utils.data_access;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BaseEntity<T, Id extends Serializable> {
    
    public boolean save(){
        return GenericDAO.save(this);
    }

    public boolean update(){
        return GenericDAO.update(this);
    }

    public boolean delete(){
        return GenericDAO.delete(this);
    }

    public final static <T, Id extends Serializable> T getById(Class<T> clazz, Id id){
        return GenericDAO.getObject(clazz, id);
    }

    public final static <T> List<T> getAll(Class<T> clazz){
        return GenericDAO.getAllObject(clazz);
    }

    public final static <T> T getAllOneToOne(Class<T> clazz){
        return GenericDAO.getAllObjectOneToOne(clazz);
    }

    public final static <T> T getAllOneToOneInvertido(Class<T> clazz){
        return GenericDAO.getAllObjectOneToOneInvertido(clazz);
    }

    public final static boolean existe(Map<String, Object> propiedades){
        return GenericDAO.existe(propiedades);
    }
}
