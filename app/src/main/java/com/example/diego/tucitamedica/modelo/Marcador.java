package com.example.diego.tucitamedica.modelo;

/**
 * Created by Diego on 25/10/2017.
 */

public class Marcador {

    private String nombre;
    private Double latitud;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    private Double longitud;

}
