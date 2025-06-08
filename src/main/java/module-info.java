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
    //Modulos de google
    requires com.google.api.client.json.gson;
    requires com.google.api.client;
    requires com.google.api.services.drive;
    requires com.google.api.client.auth;
    requires google.api.client;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires dropbox.core.sdk;
    requires undertow.core;
    requires org.json;
    requires com.fasterxml.jackson.databind;


    opens dev.yonel to javafx.fxml;
    opens dev.yonel.controllers to javafx.fxml;
    opens dev.yonel.controllers.items to javafx.fxml;
    opens dev.yonel.controllers.popup to javafx.fxml;
    opens dev.yonel.controllers.settings to javafx.fxml;
    exports dev.yonel;


    //Abriendo los modelos hacia Hibernate
    opens dev.yonel.models to org.hibernate.orm.core;
}
