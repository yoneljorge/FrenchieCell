package dev.yonel.utils.data_access;

import java.io.Serializable;
import java.util.List;

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
}
