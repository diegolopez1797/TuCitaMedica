package com.example.diego.tucitamedica.model;

import android.view.View;
import android.widget.Spinner;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Diego on 17/11/2017.
 */

public class Cita {

    private String especialidad;
    private String medico;
    private String fecha;
    private String hora;

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
