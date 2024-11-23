package com.example.lacteosdisanaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacteosdisanaapp.Modelo.Producto;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private Context context;
    private List<Producto> productos;
    private OnCantidadCambiadaListener onCantidadCambiadaListener; // Interfaz para manejar cambios en cantidades

    public CarritoAdapter(Context context, List<Producto> productos, OnCantidadCambiadaListener listener) {
        this.context = context;
        this.productos = productos;
        this.onCantidadCambiadaListener = listener; // Inicializa el listener
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.nombreProductoTextView.setText(producto.getNombre());
        holder.precioProductoTextView.setText(String.format("Precio: S/ %.2f", producto.getPrecio()));
        holder.cantidadTextView.setText(String.valueOf(producto.getCantidad()));

        // Incrementar la cantidad
        holder.incrementarCantidadButton.setOnClickListener(v -> {
            producto.setCantidad(producto.getCantidad() + 1); // Incrementa la cantidad
            holder.cantidadTextView.setText(String.valueOf(producto.getCantidad()));
            onCantidadCambiadaListener.onCantidadCambiada(); // Notifica el cambio de cantidad
            notifyItemChanged(position); // Actualiza la vista del elemento
        });

        // Decrementar la cantidad
        holder.decrementarCantidadButton.setOnClickListener(v -> {
            if (producto.getCantidad() > 1) {
                producto.setCantidad(producto.getCantidad() - 1); // Disminuye la cantidad
                holder.cantidadTextView.setText(String.valueOf(producto.getCantidad()));
                onCantidadCambiadaListener.onCantidadCambiada(); // Notifica el cambio de cantidad
                notifyItemChanged(position); // Actualiza la vista del elemento
            } else {
                Toast.makeText(context, "La cantidad mínima es 1", Toast.LENGTH_SHORT).show();
            }
        });

        // Eliminar producto
        holder.eliminarProductoButton.setOnClickListener(v -> {
            productos.remove(position); // Elimina el producto de la lista
            CarritoManager.getInstance().eliminarProducto(producto); // Lo elimina del carrito
            onCantidadCambiadaListener.onCantidadCambiada(); // Notifica el cambio para actualizar el total
            notifyItemRemoved(position); // Notifica que el elemento ha sido eliminado
            notifyItemRangeChanged(position, productos.size()); // Actualiza las posiciones restantes
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class CarritoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProductoTextView, precioProductoTextView, cantidadTextView;
        Button incrementarCantidadButton, decrementarCantidadButton;
        ImageView eliminarProductoButton;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProductoTextView = itemView.findViewById(R.id.nombreProductoTextView);
            precioProductoTextView = itemView.findViewById(R.id.precioProductoTextView);
            cantidadTextView = itemView.findViewById(R.id.cantidadTextView);
            incrementarCantidadButton = itemView.findViewById(R.id.incrementarCantidadButton);
            decrementarCantidadButton = itemView.findViewById(R.id.decrementarCantidadButton);
            eliminarProductoButton = itemView.findViewById(R.id.eliminarProductoButton);
        }
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    // Interfaz para manejar los cambios de cantidad
    public interface OnCantidadCambiadaListener {
        void onCantidadCambiada();
    }
}
