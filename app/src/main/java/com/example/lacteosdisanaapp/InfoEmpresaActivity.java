package com.example.lacteosdisanaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InfoEmpresaActivity extends AppCompatActivity {

    private TextView tituloTextView, misionTextView, visionTextView, ubicacionTextView, contactoTextView;
    private Button btnAbrirMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_empresa);

        // Vincular vistas
        tituloTextView = findViewById(R.id.tituloTextView);
        misionTextView = findViewById(R.id.misionTextView);
        visionTextView = findViewById(R.id.visionTextView);
        ubicacionTextView = findViewById(R.id.ubicacionTextView);
        contactoTextView = findViewById(R.id.contactoTextView);
        btnAbrirMapa = findViewById(R.id.btnAbrirMapa);

        // Configurar los textos de la información de la empresa
        configurarInformacionEmpresa();

        // Configurar el botón para abrir el mapa
        btnAbrirMapa.setOnClickListener(v -> {
            // Usa tu enlace de Google Maps
            String url = "https://maps.app.goo.gl/JogX2E6ZGGvnqVJF6";

            // Crea un Intent para abrir el enlace
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            // Verifica que haya una app capaz de manejar el Intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent); // Inicia la actividad de Google Maps
            } else {
                // Muestra un mensaje si no hay apps para manejar el Intent
                Toast.makeText(this, "No hay aplicaciones para abrir mapas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarInformacionEmpresa() {
        // Configura el texto de las secciones (puedes cambiar estos valores según la información de la empresa)
        misionTextView.setText("Misión: Ofrecer productos lácteos frescos y nutritivos, elaborados con amor y cuidado, para satisfacer las necesidades de nuestros clientes y contribuir al desarrollo económico y social de nuestra región, mediante una gestión eficiente y sostenible.");
        visionTextView.setText("Visión: Ser una empresa líder en la producción y comercialización de productos lácteos de alta calidad, reconocida por su innovación y compromiso con la satisfacción del cliente y el bienestar de la comunidad");
        ubicacionTextView.setText("Ubicación: Jr. Yahuar huaca N° 1131 - Baños del inca");
        contactoTextView.setText("Contacto: +51 921 787 981");
    }

    private void abrirMapa() {
        // URI para abrir la ubicación en Google Maps
        String geoUri = "geo:0,0?q=Calle+Principal+123,+Cajamarca,+Perú";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        intent.setPackage("com.google.android.apps.maps");

        // Verificar si hay una app de mapas disponible para manejar la solicitud
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Mensaje si no hay ninguna app que pueda abrir el mapa
            contactoTextView.setText("No se pudo abrir el mapa. Verifique que tenga una aplicación de mapas instalada.");
        }
    }
}
