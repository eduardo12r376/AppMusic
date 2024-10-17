package com.example.appmusic.entidades;

import java.io.Serializable;

public class Artista implements Serializable{


    private Integer idArtista;
    private String nombreGrupo;
    private String descripcion;
    private String generoMusical;
    private String correoGrupo;
    private String rutaLogo;


    public Artista() {}

    public Artista(Integer idArtista, String nombreGrupo, String descripcion,
                   String generoMusical, String correoGrupo, String rutaLogo) {
        this.idArtista     = idArtista;
        this.nombreGrupo   = nombreGrupo;
        this.descripcion   = descripcion;
        this.generoMusical = generoMusical;
        this.correoGrupo   = correoGrupo;
        this.rutaLogo      = rutaLogo;
    }

    public Integer getIdArtista() {return idArtista;}

    public void setIdArtista(Integer idArtista) {this.idArtista = idArtista;}

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGeneroMusical() {
        return generoMusical;
    }

    public void setGeneroMusical(String generoMusical) {
        this.generoMusical = generoMusical;
    }

    public String getCorreoGrupo() {
        return correoGrupo;
    }

    public void setCorreoGrupo(String correoGrupo) {
        this.correoGrupo = correoGrupo;
    }

    public String getRutaLogo() {
        return rutaLogo;
    }

    public void setRutaLogo(String rutaLogo) {
        this.rutaLogo = rutaLogo;
    }
}
