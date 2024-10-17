package com.example.appmusic.entidades;

import java.io.Serializable;

public class Historial implements Serializable{


    private Integer idContrato;
    private String nombre;
    private String apellidoMaterno;
    private String apellidoPaterno;
    private String tipoPaquete;
    private String horaEvento;
    private String fechaEvento;
    private String fechareservada;
    private String apellidopaternoP;
    private String apellidomaternoP;

    public Historial(){}

    public Historial(Integer idContrato, String nombre, String apellidoMaterno, String apellidoPaterno, String tipoPaquete, String horaEvento, String fechaEvento, String fechareservada, String apellidopaternoP, String apellidomaternoP) {
        this.idContrato = idContrato;
        this.nombre = nombre;
        this.apellidoMaterno = apellidoMaterno;
        this.apellidoPaterno = apellidoPaterno;
        this.tipoPaquete = tipoPaquete;
        this.horaEvento = horaEvento;
        this.fechaEvento = fechaEvento;
        this.fechareservada = fechareservada;
        this.apellidopaternoP = apellidopaternoP;
        this.apellidomaternoP = apellidomaternoP;
    }

    public Integer getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getTipoPaquete() {
        return tipoPaquete;
    }

    public void setTipoPaquete(String tipoPaquete) {
        this.tipoPaquete = tipoPaquete;
    }

    public String getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(String horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(String fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getFechareservada() {
        return fechareservada;
    }

    public void setFechareservada(String fechareservada) {
        this.fechareservada = fechareservada;
    }

    public String getApellidopaternoP() {
        return apellidopaternoP;
    }

    public void setApellidopaternoP(String apellidopaternoP) {
        this.apellidopaternoP = apellidopaternoP;
    }

    public String getApellidomaternoP() {
        return apellidomaternoP;
    }

    public void setApellidomaternoP(String apellidomaternoP) {
        this.apellidomaternoP = apellidomaternoP;
    }
}
