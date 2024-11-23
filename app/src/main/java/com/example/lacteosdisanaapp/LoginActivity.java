package com.example.lacteosdisanaapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText correoEditText, contrasenaEditText;
    private Button btnIniciarSesion;
    private TextView tvRegistrar;
    private FirebaseAuth mAuth;
    private FirebaseService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase Auth y FirebaseService
        mAuth = FirebaseAuth.getInstance();
        firebaseService = new FirebaseService();

        // Verificar si el usuario ya está autenticado y mantener la sesión activa
        FirebaseUser usuarioActual = mAuth.getCurrentUser();
        if (usuarioActual != null) {
            // Usuario ya autenticado, obtener su rol y redirigir al perfil
            verificarRolYRedirigir(usuarioActual.getUid());
            return;
        }

        // Inicializar las vistas
        correoEditText = findViewById(R.id.correo);
        contrasenaEditText = findViewById(R.id.contrasena);
        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        tvRegistrar = findViewById(R.id.tv_registrar);

        // Configurar el botón de iniciar sesión
        btnIniciarSesion.setOnClickListener(view -> iniciarSesion());

        // Configurar el botón de registro
        tvRegistrar.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion() {
        String correo = correoEditText.getText().toString();
        String contrasena = contrasenaEditText.getText().toString();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        verificarRolYRedirigir(userId);
                    } else {
                        Toast.makeText(this, "Error en la autenticación", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verificarRolYRedirigir(String userId) {
        firebaseService.obtenerRolUsuario(userId, this, rol -> {
            if (rol != null) {
                // Redirigir al perfil (UsuarioActivity) en lugar de directamente a pedidos
                Intent intent = new Intent(LoginActivity.this, UsuarioActivity.class);
                intent.putExtra("userRole", rol);  // Enviamos el rol del usuario
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "No se pudo obtener el rol del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoRestablecerContrasena() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Restablecer Contraseña");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("Ingresa tu correo");
        builder.setView(input);

        builder.setPositiveButton("Enviar", (dialog, which) -> {
            String email = input.getText().toString();
            if (!email.isEmpty()) {
                enviarCorreoRestablecimiento(email);
            } else {
                Toast.makeText(LoginActivity.this, "Por favor ingresa tu correo", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void enviarCorreoRestablecimiento(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Correo de restablecimiento enviado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
