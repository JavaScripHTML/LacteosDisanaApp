package com.example.lacteosdisanaapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lacteosdisanaapp.Modelo.Producto;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    private RecyclerView productosRecyclerView;
    private ProductosAdapter productosAdapter;
    private List<Producto> listaProductos;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        productosRecyclerView = findViewById(R.id.productosRecyclerView);
        productosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaProductos = new ArrayList<>();
        productosAdapter = new ProductosAdapter(this, listaProductos);
        productosRecyclerView.setAdapter(productosAdapter);

        firestore = FirebaseFirestore.getInstance();

        String categoriaSeleccionada = getIntent().getStringExtra("categoriaSeleccionada");

        if (categoriaSeleccionada != null) {
            cargarProductosPorCategoria(categoriaSeleccionada);
        } else {
            Toast.makeText(this, "Categoría no encontrada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void cargarProductosPorCategoria(String categoria) {
        firestore.collection("Categorias").document(categoria).collection("Productos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaProductos.clear(); // Limpia la lista antes de agregar nuevos productos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Producto producto = document.toObject(Producto.class);
                            listaProductos.add(producto);
                        }
                        productosAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("ProductosActivity", "Error al cargar productos: " + task.getException().getMessage());
                    }
                });
    }

}
