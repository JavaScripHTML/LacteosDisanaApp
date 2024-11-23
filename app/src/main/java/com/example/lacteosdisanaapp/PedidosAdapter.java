package com.example.lacteosdisanaapp;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacteosdisanaapp.Modelo.Pedido;
import com.example.lacteosdisanaapp.Modelo.Producto;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {

    private List<Pedido> pedidos;
    private FirebaseFirestore firestore;
    private Context context; // Contexto para mostrar Toasts y diálogos
    private boolean isAdmin; // Variable para indicar si el usuario es admin

    public PedidosAdapter(Context context, List<Pedido> pedidos, boolean isAdmin) {
        this.context = context;
        this.pedidos = pedidos;
        this.isAdmin = isAdmin;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);

        // Configuración de los datos del pedido
        holder.nombreClienteTextView.setText("Cliente: " + pedido.getCliente());
        holder.estadoTextView.setText("Estado: " + pedido.getEstado());
        holder.totalTextView.setText("Total: S/ " + pedido.getTotal());

        // Manejo de la fecha
        if (pedido.getFecha() instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) pedido.getFecha();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechaFormateada = dateFormat.format(timestamp.toDate());
            holder.fechaTextView.setText("Fecha: " + fechaFormateada);
        } else {
            holder.fechaTextView.setText("Fecha: No disponible");
        }

        // Configuración del botón de descarga de ticket
        holder.descargarTicketButton.setOnClickListener(v -> {
            TicketGenerator ticketGenerator = new TicketGenerator(holder.itemView.getContext());
            List<String> productos = new ArrayList<>();
            for (Producto producto : pedido.getProductos()) {
                productos.add(producto.getNombre() + " x " + producto.getCantidad());
            }
            ticketGenerator.generatePDF(pedido, productos, String.valueOf(pedido.getTotal()));
        });

        // Configuración del botón de actualización de estado (solo para administradores)
        if (isAdmin) { // Verifica si el usuario es administrador
            holder.actualizarEstadoButton.setVisibility(View.VISIBLE);
            holder.actualizarEstadoButton.setOnClickListener(v -> {
                String[] estados = {"pendiente", "en proceso", "completado"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Selecciona el nuevo estado")
                        .setItems(estados, (dialog, which) -> {
                            String nuevoEstado = estados[which]; // Estado seleccionado
                            DocumentReference pedidoRef = firestore.collection("Pedidos").document(pedido.getId());

                            // Actualizar estado en Firestore
                            pedidoRef.update("estado", nuevoEstado)
                                    .addOnSuccessListener(aVoid -> {
                                        holder.estadoTextView.setText("Estado: " + nuevoEstado);
                                        Toast.makeText(context, "Estado actualizado", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("ActualizarPedido", "Error al actualizar el estado", e);
                                        Toast.makeText(context, "Error al actualizar el estado", Toast.LENGTH_SHORT).show();
                                    });
                        }).show();
            });
        } else {
            holder.actualizarEstadoButton.setVisibility(View.GONE); // Oculta el botón si no es admin
        }
    }

    @Override
    public int getItemCount() {
        return pedidos != null ? pedidos.size() : 0;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
        notifyDataSetChanged(); // Actualiza la vista del RecyclerView
    }


    public class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreClienteTextView, fechaTextView, estadoTextView, totalTextView;
        Button actualizarEstadoButton, descargarTicketButton;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreClienteTextView = itemView.findViewById(R.id.nombreClienteTextView);
            fechaTextView = itemView.findViewById(R.id.fechaTextView);
            estadoTextView = itemView.findViewById(R.id.estadoTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
            actualizarEstadoButton = itemView.findViewById(R.id.actualizarEstadoButton);
            descargarTicketButton = itemView.findViewById(R.id.descargarTicketButton);
        }
    }
}
