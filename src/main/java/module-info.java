module dev.yonel {
    requires MaterialFX;

    requires javafx.controls;
    requires transitive javafx.fxml;
    requires javafx.base;
    requires transitive javafx.graphics;

    requires java.sql;

    requires org.hibernate.orm.core;
    requires org.hibernate.orm.community.dialects;
    requires jakarta.persistence;
    requires static lombok;

    //Con esto se soulciona el error cannot access javax.naming.Referenceable.
    requires transitive java.naming;

    opens dev.yonel to javafx.fxml;
    opens dev.yonel.controllers to javafx.fxml;
    opens dev.yonel.controllers.items to javafx.fxml;
    opens dev.yonel.controllers.popup to javafx.fxml;
    exports dev.yonel;


    //Abriendo los modelos hacia Hibernate
    opens dev.yonel.models to org.hibernate.orm.core;
}
