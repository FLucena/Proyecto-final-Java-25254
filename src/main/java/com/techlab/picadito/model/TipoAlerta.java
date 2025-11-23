package com.techlab.picadito.model;

public enum TipoAlerta {
    CUPOS_BAJOS("Cupos bajos disponibles"),
    PARTIDO_PROXIMO("Partido próximo a jugarse"),
    PARTIDO_CANCELADO("Partido cancelado"),
    RESERVA_CONFIRMADA("Reserva confirmada"),
    PARTIDO_COMPLETO("Partido completo");

    private final String descripcion;

    TipoAlerta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

