package com.example.lacteosdisanaapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditarPerfilActivity extends AppCompatActivity {
    private EditText etNombre, etCorreo, etContrasena, etCelular, etDireccion;
    private Button btnGuardarCambios;
    private FirebaseService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        etNombre = findViewById(R.id.et_nombre);
        etCorreo = findViewById(R.id.et_correo);
        etContrasena = findViewById(R.id.et_contrasena);
        etCelular = findViewById(R.id.et_celular);
        etDireccion = findViewById(R.id.et_direccion);
        btnGuardarCambios = findViewById(R.id.btn_guardar_cambios);

        firebaseService = new FirebaseService();

        cargarDatosUsuario();

        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
    }

    private void cargarDatosUsuario() {
        FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioActual != null) {
            firebaseService.obtenerDatosUsuario(usuarioActual.getUid(), this, usuario -> {
                if (usuario != null) {
                    etNombre.setText(usuario.getNombre());
                    etCorreo.setText(usuario.getCorreo());
                    etCelular.setText(usuario.getCelular());
                    etDireccion.setText(usuario.getDireccion());
                }
            });
        }
    }

    private void guardarCambios() {
        String nombre = etNombre.getText().toString();
        String correo = etCorreo.getText().toString();
        String contrasena = etContrasena.getText().toString();
        String celular = etCelular.getText().toString();
        String direccion = etDireccion.getText().toString();

        FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioActual != null) {
            firebaseService.actualizarDatosUsuario(usuarioActual.getUid(), nombre, correo, contrasena, celular, direccion, success -> {
                if (success) {
                    Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
