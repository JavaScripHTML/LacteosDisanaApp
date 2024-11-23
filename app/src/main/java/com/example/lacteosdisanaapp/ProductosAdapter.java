package com.example.lacteosdisanaapp;

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
import com.example.lacteosdisanaapp.Modelo.Producto;
import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder> {

    private Context context;
    private List<Producto> productos;

    public ProductosAdapter(Context context, List<Producto> productos) {
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.nombreProductoTextView.setText(producto.getNombre());
        holder.precioProductoTextView.setText("Precio: S/ " + producto.getPrecio());
        holder.descripcionProductoTextView.setText(producto.getDescripcion());

        holder.agregarCarritoButton.setOnClickListener(v -> {
            producto.setCantidad(1); // Cantidad inicial
            CarritoManager.getInstance().agregarProducto(producto);
            CarritoManager.getInstance().guardarCarrito(context); // Guardar estado
            Toast.makeText(context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
        });

    }


    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProductoTextView, precioProductoTextView, descripcionProductoTextView;
        Button agregarCarritoButton; // Declaración del botón

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProductoTextView = itemView.findViewById(R.id.nombreProductoTextView);
            precioProductoTextView = itemView.findViewById(R.id.precioProductoTextView);
            descripcionProductoTextView = itemView.findViewById(R.id.descripcionProductoTextView);
            agregarCarritoButton = itemView.findViewById(R.id.agregarCarritoButton); // Enlace con el botón
        }
    }


    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }
}
