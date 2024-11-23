package com.example.lacteosdisanaapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lacteosdisanaapp.Modelo.Producto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FinalizarPedidoActivity extends AppCompatActivity {

    private TextView resumenPedidoTextView;
    private Button confirmarPedidoButton;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido);

        resumenPedidoTextView = findViewById(R.id.resumenPedidoTextView);
        confirmarPedidoButton = findViewById(R.id.confirmarPedidoButton);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        List<Producto> productos = CarritoManager.getInstance().obtenerProductos();
        double total = CarritoManager.getInstance().calcularTotal();

        mostrarResumenPedido(productos, total);

        confirmarPedidoButton.setOnClickListener(v -> {
            // Verifica que el usuario esté autenticado
            if (auth.getCurrentUser() != null) {
                String clienteEmail = auth.getCurrentUser().getEmail();

                Map<String, Object> pedido = new HashMap<>();
                pedido.put("cliente", clienteEmail); // Correo del cliente autenticado
                pedido.put("productos", productos); // Lista de productos del carrito
                pedido.put("total", total);
                pedido.put("estado", "pendiente");
                pedido.put("fecha", FieldValue.serverTimestamp()); // Guardar fecha como timestamp

                // Guarda el pedido en Firestore
                firestore.collection("pedidos")
                        .add(pedido)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Pedido realizado con éxito", Toast.LENGTH_SHORT).show();
                            CarritoManager.getInstance().vaciarCarrito(); // Vacía el carrito después de realizar el pedido
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error al realizar el pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            } else {
                // Si no hay un usuario autenticado
                Toast.makeText(this, "Debe iniciar sesión para realizar un pedido", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void mostrarResumenPedido(List<Producto> productos, double total) {
        StringBuilder resumen = new StringBuilder();
        for (Producto producto : productos) {
            resumen.append("- ").append(producto.getNombre())
                    .append(" (S/ ").append(producto.getPrecio()).append(") x ")
                    .append(producto.getCantidad()).append("\n");
        }
        resumen.append("\nTotal: S/ ").append(total);
        resumenPedidoTextView.setText(resumen.toString());
    }
}
