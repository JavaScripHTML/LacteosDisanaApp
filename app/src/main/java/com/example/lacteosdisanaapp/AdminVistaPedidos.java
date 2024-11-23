package com.example.lacteosdisanaapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacteosdisanaapp.Modelo.Pedido;

import java.util.List;

public class AdminVistaPedidos extends AppCompatActivity {
    private FirebaseService firebaseService;
    private RecyclerView pedidosRecyclerView;
    private PedidosAdapter pedidosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pedidos);

        firebaseService = new FirebaseService();
        pedidosRecyclerView = findViewById(R.id.pedidosRecyclerView);
        pedidosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Llamada para obtener todos los pedidos
        firebaseService.obtenerTodosLosPedidos(new FirebaseService.PedidosCallback() {
            @Override
            public void onPedidosRecibidos(List<Pedido> pedidos) {
                if (pedidos != null && !pedidos.isEmpty()) {
                    pedidosAdapter = new PedidosAdapter(AdminVistaPedidos.this, pedidos, true); // Admin es admin
                    pedidosRecyclerView.setAdapter(pedidosAdapter);
                } else {
                    Toast.makeText(AdminVistaPedidos.this, "No hay pedidos disponibles", Toast.LENGTH_SHORT).show();
                    Log.d("AdminVistaPedidos", "No hay pedidos disponibles");
                }
            }
        });
    }
}
