package dev.yonel.services;

public enum TipoMensaje {
    ERROR("ERROR"),
    INFORMACION("INFORMACION");

    private String comentario;

    TipoMensaje(String comentario) {
        this.comentario = comentario;
    }

    public String getTipo() {
        return comentario;
    }

}
