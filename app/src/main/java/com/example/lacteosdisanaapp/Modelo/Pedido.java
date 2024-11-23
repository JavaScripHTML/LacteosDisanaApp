package com.example.lacteosdisanaapp.Modelo;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable {
    private String id;  // Campo para almacenar el ID del pedido en Firestore
    private String cliente;
    private String estado;
    private Timestamp fecha;  // Cambiado a Timestamp
    private List<Producto> productos;
    private double total;

    // Constructor vacío necesario para Firebase
    public Pedido() {
    }

    // Constructor con parámetros
    public Pedido(String cliente, String estado, Timestamp fecha, List<Producto> productos) {
        this.cliente = cliente;
        this.estado = estado;
        this.fecha = fecha;
        this.productos = productos;
        calcularTotal();  // Calcula el total basado en la lista de productos
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    private void calcularTotal() {
        double totalTemp = 0;
        if (productos != null) {
            for (Producto p : productos) {
                totalTemp += p.getPrecio() * p.getCantidad();
            }
        }
        total = totalTemp;
    }

    @NonNull
    @Override
    public String toString() {
        return "Pedido{" +
                "cliente='" + cliente + '\'' +
                ", productos=" + productos +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", fecha=" + (fecha != null ? fecha.toDate() : "null") +
                '}';
    }
}
