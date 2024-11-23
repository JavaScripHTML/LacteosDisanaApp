package com.example.lacteosdisanaapp.Modelo;

public class Producto {
    private String nombre;
    private double precio;
    private int cantidad;
    private String descripcion;

    // Constructor vacío para Firebase
    public Producto() {
        this.cantidad = 0; // Cantidad inicial predeterminada
    }


    // Constructor con parámetros
    public Producto(String nombre, double precio, int cantidad, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.cantidad = (cantidad > 0) ? cantidad : 0; // Asegura que la cantidad inicial sea 1
    }



    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
