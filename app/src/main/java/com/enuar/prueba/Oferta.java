package com.enuar.prueba;

public class Oferta {

    private String Titulo, Descripcion, Direccion , Cellphone, IDUsuario;
    private float Precio;
    private String TipoEmergencia;

    public Oferta(String titulo, String descripcion, String direccion, String cellphone, String IDUsuario, float precio, String tipoEmergencia) {
        Titulo = titulo;
        Descripcion = descripcion;
        Direccion = direccion;
        Cellphone = cellphone;
        this.IDUsuario = IDUsuario;
        Precio = precio;
        TipoEmergencia = tipoEmergencia;
    }

    public Oferta() {
    }

    public String getIDUsuario() {
        return IDUsuario;
    }

    public void setIDUsuario(String IDUsuario) {
        this.IDUsuario = IDUsuario;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getCellphone() {
        return Cellphone;
    }

    public void setCellphone(String cellphone) {
        Cellphone = cellphone;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public float getPrecio() {
        return Precio;
    }

    public void setPrecio(float precio) {
        Precio = precio;
    }

    public String getTipoEmergencia() {
        return TipoEmergencia;
    }

    public void setTipoEmergencia(String tipoEmergencia) {
        TipoEmergencia = tipoEmergencia;
    }
}

