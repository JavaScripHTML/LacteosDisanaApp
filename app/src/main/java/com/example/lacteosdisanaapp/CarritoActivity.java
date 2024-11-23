package com.example.lacteosdisanaapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacteosdisanaapp.Modelo.Pedido;
import com.example.lacteosdisanaapp.Modelo.Producto;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.Timestamp;

public class CarritoActivity extends AppCompatActivity {

    private FirebaseService firebaseService;
    private RecyclerView carritoRecyclerView;
    private TextView totalTextView, carritoVacioTextView;
    private Button finalizarPedidoButton, vaciarCarritoButton;
    private CarritoAdapter carritoAdapter;
    private String clienteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            clienteId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "No se encontró un usuario autenticado", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si no hay usuario autenticado
        }

        CarritoManager.getInstance().cargarCarrito(this); // Cargar productos almacenados

        firebaseService = new FirebaseService();

        carritoRecyclerView = findViewById(R.id.carritoRecyclerView);
        carritoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        carritoAdapter = new CarritoAdapter(this, CarritoManager.getInstance().obtenerProductos(), this::actualizarTotal);
        carritoRecyclerView.setAdapter(carritoAdapter);

        totalTextView = findViewById(R.id.totalTextView);
        carritoVacioTextView = findViewById(R.id.carritoVacioTextView); // TextView para carrito vacío
        finalizarPedidoButton = findViewById(R.id.finalizarPedidoButton);
        vaciarCarritoButton = findViewById(R.id.vaciarCarritoButton);

        actualizarVistaCarrito(); // Actualiza la vista inicial

        vaciarCarritoButton.setOnClickListener(v -> {
            CarritoManager.getInstance().vaciarCarrito();
            CarritoManager.getInstance().guardarCarrito(this); // Guardar estado
            actualizarVistaCarrito();
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
        });

        finalizarPedidoButton.setOnClickListener(v -> {
            if (CarritoManager.getInstance().obtenerProductos().isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener el email del usuario autenticado
            String clienteEmail = null;

            if (auth.getCurrentUser() != null) {
                clienteEmail = auth.getCurrentUser().getEmail();
            } else {
                Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad si no hay usuario autenticado
            }

            // Crear un nuevo pedido
            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setCliente(clienteEmail);
            nuevoPedido.setTotal(CarritoManager.getInstance().calcularTotal());
            nuevoPedido.setFecha(Timestamp.now());
            nuevoPedido.setEstado("en proceso");
            nuevoPedido.setProductos(CarritoManager.getInstance().obtenerProductos());

            // Guardar el pedido
            firebaseService.guardarPedido(clienteEmail, nuevoPedido, this);

            // Vaciar el carrito y actualizar la vista
            CarritoManager.getInstance().vaciarCarrito();
            actualizarVistaCarrito();

            Toast.makeText(this, "Pedido finalizado correctamente", Toast.LENGTH_SHORT).show();
        });
    }

    private void actualizarVistaCarrito() {
        List<Producto> productos = CarritoManager.getInstance().obtenerProductos();
        carritoAdapter.setProductos(productos);

        // Mostrar u ocultar el mensaje de carrito vacío
        if (productos.isEmpty()) {
            carritoRecyclerView.setVisibility(View.GONE);
            carritoVacioTextView.setVisibility(View.VISIBLE);
        } else {
            carritoRecyclerView.setVisibility(View.VISIBLE);
            carritoVacioTextView.setVisibility(View.GONE);
        }

        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = CarritoManager.getInstance().calcularTotal();
        totalTextView.setText("Total: S/ " + total);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Obtén los productos actualizados del carrito
        List<Producto> productosActualizados = CarritoManager.getInstance().obtenerProductos();

        // Actualiza la vista del carrito
        actualizarVistaCarrito();

        if (productosActualizados.isEmpty()) {
            Log.d("CarritoActivity", "El carrito está vacío");
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("CarritoActivity", "Productos en el carrito: " + productosActualizados.size());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Guardar el carrito en SharedPreferences
        CarritoManager.getInstance().guardarCarrito(this);
    }
}
