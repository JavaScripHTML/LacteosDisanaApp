package com.example.lacteosdisanaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.List;

public class CategoriasActivity extends AppCompatActivity {

    private ListView categoriasListView;
    // Asegúrate de que estos nombres coincidan exactamente con los nombres de los documentos en Firestore
    private List<String> categorias = Arrays.asList("quesos", "yogurt_frutado", "manjar_blanco", "roscas", "mantequilla");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        categoriasListView = findViewById(R.id.categoriasListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorias);
        categoriasListView.setAdapter(adapter);

        categoriasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = categorias.get(position);

                // Intent para abrir ProductosActivity y pasar la categoría seleccionada
                Intent intent = new Intent(CategoriasActivity.this, ProductosActivity.class);
                intent.putExtra("categoriaSeleccionada", categoriaSeleccionada);
                startActivity(intent);
            }
        });
    }
}
