package com.example.lacteosdisanaapp.Modelo;

public class Usuario {
    private String nombre;
    private String correo;
    private String rol;
    private String celular;
    private String direccion;

    // Constructor vacío necesario para Firebase
    public Usuario() {
    }

    // Constructor con parámetros para crear un nuevo usuario
    public Usuario(String nombre, String correo, String rol, String celular, String direccion) {
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.celular = celular;
        this.direccion = direccion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
