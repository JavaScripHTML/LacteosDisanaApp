package com.example.lacteosdisanaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lacteosdisanaapp.Modelo.Categoria;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private Context context;
    private List<Categoria> categorias;

    public CategoriaAdapter(Context context, List<Categoria> categorias) {
        this.context = context;
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño de ítem para las categorías
        View view = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        // Obtén la categoría actual
        Categoria categoria = categorias.get(position);

        // Configura los datos de la categoría en las vistas
        holder.nombreCategoriaTextView.setText(categoria.getNombre());
        holder.descripcionCategoriaTextView.setText(categoria.getDescripcion());
    }

    @Override
    public int getItemCount() {
        // Retorna el número total de categorías
        return categorias.size();
    }

    static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        // Vistas del diseño de ítem para las categorías
        TextView nombreCategoriaTextView, descripcionCategoriaTextView;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreCategoriaTextView = itemView.findViewById(R.id.nombreCategoriaTextView);
            descripcionCategoriaTextView = itemView.findViewById(R.id.descripcionCategoriaTextView);
        }
    }

    // Método para actualizar la lista de categorías
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
        notifyDataSetChanged();
    }
}
