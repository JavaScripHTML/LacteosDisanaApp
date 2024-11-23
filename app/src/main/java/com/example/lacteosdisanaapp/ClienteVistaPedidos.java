package com.example.lacteosdisanaapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class ClienteVistaPedidos extends AppCompatActivity {
    private FirebaseService firebaseService;
    private RecyclerView pedidosRecyclerView;
    private PedidosAdapter pedidosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_pedidos);

        firebaseService = new FirebaseService();
        pedidosRecyclerView = findViewById(R.id.pedidosRecyclerView);
        pedidosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String clienteEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        firebaseService.obtenerPedidosCliente(clienteEmail, pedidos -> {
            if (pedidos != null && !pedidos.isEmpty()) {
                pedidosAdapter = new PedidosAdapter(ClienteVistaPedidos.this, pedidos, false);
                pedidosRecyclerView.setAdapter(pedidosAdapter);
            } else {
                // Muestra un mensaje si no hay pedidos
                Toast.makeText(ClienteVistaPedidos.this, "No hay pedidos disponibles", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
