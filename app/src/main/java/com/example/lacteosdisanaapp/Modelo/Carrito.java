package com.example.lacteosdisanaapp.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private static Carrito instancia;
    private List<Producto> productos;
    private double total;

    private Carrito() {
        productos = new ArrayList<>();
        total = 0.0;
    }

    public static Carrito getInstance() {
        if (instancia == null) {
            instancia = new Carrito();
        }
        return instancia;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        total += producto.getPrecio();
    }

    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
        total -= producto.getPrecio();
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public double getTotal() {
        return total;
    }

    public void vaciarCarrito() {
        productos.clear();
        total = 0.0;
    }
}
