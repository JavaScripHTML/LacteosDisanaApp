package com.example.lacteosdisanaapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.lacteosdisanaapp.Modelo.Usuario;

public class RegisterActivity extends AppCompatActivity implements AutenticacionService.RegistroCallback {

    private EditText nombreEditText, correoEditText, contrasenaEditText, celularEditText, direccionEditText;
    private Button btnRegistrar;

    // Instancias de los servicios
    private AutenticacionService autenticacionService;
    private FirebaseService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar campos y servicios
        nombreEditText = findViewById(R.id.nombre);
        correoEditText = findViewById(R.id.correo);
        contrasenaEditText = findViewById(R.id.contrasena);
        celularEditText = findViewById(R.id.celular);
        direccionEditText = findViewById(R.id.direccion);

        btnRegistrar = findViewById(R.id.btn_registrar);

        autenticacionService = new AutenticacionService();
        firebaseService = new FirebaseService();

        // Acción al hacer clic en el botón de registro
        btnRegistrar.setOnClickListener(view -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombre = nombreEditText.getText().toString();
        String correo = correoEditText.getText().toString();
        String contrasena = contrasenaEditText.getText().toString();
        String celular = celularEditText.getText().toString();
        String direccion = direccionEditText.getText().toString();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(contrasena) || TextUtils.isEmpty(celular) || TextUtils.isEmpty(direccion)) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto Usuario
        Usuario nuevoUsuario = new Usuario(nombre, correo, "cliente", celular, direccion);

        // Registrar el usuario usando los servicios POO y manejar el resultado con un callback
        autenticacionService.registrarUsuario(correo, contrasena, this, firebaseService, nuevoUsuario, this);
    }

    public void onRegistroExitoso() {
        Intent intent = new Intent(RegisterActivity.this, UsuarioActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegistroFallido() {
        Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show();
    }

    private void irALoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad de Registro
    }
}
