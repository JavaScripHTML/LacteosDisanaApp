package com.example.lacteosdisanaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.lacteosdisanaapp.Modelo.Producto;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CarritoManager {

    private static final String PREFERENCES_NAME = "CarritoPrefs";
    private static final String CARRO_KEY = "carrito";

    private static CarritoManager instance;
    private final List<Producto> carrito;

    private CarritoManager() {
        carrito = new ArrayList<>();
    }

    public static synchronized CarritoManager getInstance() {
        if (instance == null) {
            instance = new CarritoManager();
        }
        return instance;
    }

    public void agregarProducto(Producto producto) {
        if (producto == null) {
            Log.e("CarritoManager", "Producto es null, no se puede agregar.");
            return;
        }


        for (Producto p : carrito) {
            if (p.getNombre().equals(producto.getNombre())) {
                p.setCantidad(p.getCantidad() + 1);
                Log.d("CarritoManager", "Producto ya existente: " + p.getNombre() + ", Cantidad actualizada: " + p.getCantidad());
                return;
            }
        }

        producto.setCantidad(1);
        carrito.add(producto);
        Log.d("CarritoManager", "Producto agregado: " + producto.getNombre() + ", Cantidad inicial: " + producto.getCantidad());
    }


    public List<Producto> obtenerProductos() {
        return new ArrayList<>(carrito); // Retorna una copia para evitar modificaciones externas
    }

    public void guardarCarrito(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CarritoPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String carritoJson = gson.toJson(carrito);
        editor.putString("carrito", carritoJson);
        editor.apply();
        Log.d("CarritoManager", "Carrito guardado: " + carritoJson);
    }

    public void cargarCarrito(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CarritoPref", Context.MODE_PRIVATE);
        String carritoJson = sharedPreferences.getString("carrito", null);
        if (carritoJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Producto>>() {}.getType();
            List<Producto> productosCargados = gson.fromJson(carritoJson, type);
            carrito.clear();
            carrito.addAll(productosCargados);
            Log.d("CarritoManager", "Carrito cargado: " + carrito.toString());
        } else {
            Log.d("CarritoManager", "No se encontraron productos guardados.");
        }
    }
    // Eliminar un producto específico del carrito
    public void eliminarProducto(Producto producto) {
        // Usa un iterador para evitar problemas de ConcurrentModificationException
        for (int i = 0; i < carrito.size(); i++) {
            if (carrito.get(i).getNombre().equals(producto.getNombre())) {
                carrito.remove(i);
                break;
            }
        }
    }


    // Vaciar todo el carrito
    public void vaciarCarrito() {
        carrito.clear();
    }

    // Calcular el total del carrito considerando las cantidades
    public double calcularTotal() {
        double total = 0;
        for (Producto producto : carrito) {
            total += producto.getPrecio() * producto.getCantidad();
        }
        return total;
    }


    // Obtener el número total de productos en el carrito (incluyendo cantidades)
    public int obtenerCantidadTotal() {
        int totalCantidad = 0;
        for (Producto producto : carrito) {
            totalCantidad += producto.getCantidad();
        }
        return totalCantidad;
    }

    // Verificar si el carrito está vacío
    public boolean estaVacio() {
        return carrito.isEmpty();
    }
}
