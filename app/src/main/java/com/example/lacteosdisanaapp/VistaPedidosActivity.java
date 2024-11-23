package com.example.lacteosdisanaapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacteosdisanaapp.Modelo.Pedido;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VistaPedidosActivity extends AppCompatActivity {

    private FirebaseService firebaseService;
    private RecyclerView pedidosRecyclerView;
    private PedidosAdapter pedidosAdapter;
    private TextView tituloTextView;
    private String userRole;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        // Inicializar FirebaseService
        firebaseService = new FirebaseService();

        // Vincular vistas
        pedidosRecyclerView = findViewById(R.id.pedidosRecyclerView);
        tituloTextView = findViewById(R.id.tituloTextView);

        // Validar que las vistas no sean null
        if (pedidosRecyclerView == null || tituloTextView == null) {
            throw new NullPointerException("Error al encontrar pedidosRecyclerView o tituloTextView. Verifica los IDs en activity_pedidos.xml");
        }

        // Configurar RecyclerView con un LinearLayoutManager
        pedidosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar el adaptador con una lista vacía por defecto
        pedidosAdapter = new PedidosAdapter(this, new ArrayList<>(), false);
        pedidosRecyclerView.setAdapter(pedidosAdapter);

        // Obtener el rol del usuario desde el intent
        userRole = getIntent().getStringExtra("userRole");
        if (userRole == null) {
            Toast.makeText(this, "Error: No se pudo obtener el rol del usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configurar la vista según el rol del usuario
        configurarVistaPedidos();
    }

    private void configurarVistaPedidos() {
        if ("cliente".equals(userRole)) {
            tituloTextView.setText("Mis Pedidos");
            String clienteId = FirebaseAuth.getInstance().getCurrentUser() != null
                    ? FirebaseAuth.getInstance().getCurrentUser().getEmail()
                    : null;

            if (clienteId == null) {
                Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Obtener pedidos específicos del cliente en orden descendente
            firebaseService.obtenerPedidosCliente(clienteId, pedidos -> {
                if (pedidos != null && !pedidos.isEmpty()) {
                    // Ordenar pedidos por fecha descendente
                    Collections.sort(pedidos, (p1, p2) -> p2.getFecha().compareTo(p1.getFecha()));
                    pedidosAdapter.setPedidos(pedidos);
                } else {
                    Toast.makeText(this, "No tienes pedidos disponibles", Toast.LENGTH_SHORT).show();
                }
            });

        } else if ("admin".equals(userRole)) {
            tituloTextView.setText("Todos los Pedidos");

            // Obtener todos los pedidos (para admin) en orden descendente
            firebaseService.obtenerTodosLosPedidos(pedidos -> {
                if (pedidos != null && !pedidos.isEmpty()) {
                    // Ordenar pedidos por fecha descendente
                    Collections.sort(pedidos, (p1, p2) -> p2.getFecha().compareTo(p1.getFecha()));
                    pedidosAdapter = new PedidosAdapter(this, pedidos, true); // Habilitar opciones de admin
                    pedidosRecyclerView.setAdapter(pedidosAdapter);
                } else {
                    Toast.makeText(this, "No se encontraron pedidos en el sistema", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "Error al obtener el rol del usuario", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
