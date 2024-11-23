package com.example.lacteosdisanaapp;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lacteosdisanaapp.Modelo.Pedido;

import java.util.ArrayList;
import java.util.List;

public class TicketActivity extends AppCompatActivity {

    private TextView ticketTextView;
    private Button generarPDFButton;
    private Pedido pedido;
    private List<String> productos;
    private String total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        ticketTextView = findViewById(R.id.ticketTextView);
        generarPDFButton = findViewById(R.id.generarPDFButton);

        pedido = (Pedido) getIntent().getSerializableExtra("pedido");
        productos = getIntent().getStringArrayListExtra("productos");
        total = getIntent().getStringExtra("total");

        if (pedido == null || productos == null || total == null) {
            Toast.makeText(this, "Error al cargar detalles del pedido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mostrarDetallesDelPedido();

        generarPDFButton.setOnClickListener(v -> {
            TicketGenerator generator = new TicketGenerator(this);
            generator.generatePDF(pedido, productos, total);
        });
    }

    private void mostrarDetallesDelPedido() {
        StringBuilder detalles = new StringBuilder();
        detalles.append("Cliente: ").append(pedido.getCliente()).append("\n");
        detalles.append("Fecha: ").append(pedido.getFecha()).append("\n");
        detalles.append("Productos:\n");

        for (String producto : productos) {
            detalles.append("- ").append(producto).append("\n");
        }

        detalles.append("Total: S/ ").append(total);

        ticketTextView.setText(detalles.toString());
    }
}
