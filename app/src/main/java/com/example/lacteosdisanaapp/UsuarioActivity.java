package com.example.lacteosdisanaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioActivity extends AppCompatActivity {

    private TextView usuarioNombreTextView, usuarioCorreoTextView, usuarioCelularTextView, usuarioDireccionTextView;
    private Button btnCerrarSesion, btnVerPedidos, btnVerProductos, btnInfoEmpresa, btnEditarPerfil;
    private AutenticacionService autenticacionService;
    private FirebaseService firebaseService;
    private String userRole; // Variable para almacenar el rol del usuario
    private String userId; // ID del usuario autenticado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        // Inicializar servicios
        firebaseService = new FirebaseService();
        autenticacionService = new AutenticacionService();

        // Vincular vistas
        usuarioNombreTextView = findViewById(R.id.usuario_nombre);
        usuarioCorreoTextView = findViewById(R.id.usuario_correo);
        usuarioCelularTextView = findViewById(R.id.usuario_celular);
        usuarioDireccionTextView = findViewById(R.id.usuario_direccion);
        btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion);
        btnVerPedidos = findViewById(R.id.btn_ver_pedidos);
        btnVerProductos = findViewById(R.id.btn_ver_productos);
        btnInfoEmpresa = findViewById(R.id.btn_info_empresa);
        btnEditarPerfil = findViewById(R.id.btn_editar_perfil);

        // Obtener usuario autenticado
        FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioActual != null) {
            userId = usuarioActual.getUid(); // Inicializa el userId
            mostrarDatosUsuario(userId); // Muestra los datos del usuario autenticado
            cargarRolDesdeFirebase(userId); // Carga el rol del usuario autenticado
        } else {
            Toast.makeText(this, "No se pudo autenticar al usuario", Toast.LENGTH_SHORT).show();
            finish(); // Finaliza la actividad si no hay usuario autenticado
            return;
        }

        // Configurar acciones de los botones
        btnCerrarSesion.setOnClickListener(view -> cerrarSesion());
        btnVerPedidos.setOnClickListener(view -> verPedidos());
        btnVerProductos.setOnClickListener(view -> verCategorias());
        btnInfoEmpresa.setOnClickListener(view -> verInformacionEmpresa());

        Button btnVerCarrito = findViewById(R.id.btn_ver_carrito);
        if (btnVerCarrito != null) {
            btnVerCarrito.setOnClickListener(v -> {
                Intent intent = new Intent(UsuarioActivity.this, CarritoActivity.class);
                startActivity(intent);
            });
        } else {
            Log.e("UsuarioActivity", "btn_ver_carrito no está vinculado correctamente al XML");
        }

        btnEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioActivity.this, EditarPerfilActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Muestra los datos del usuario autenticado.
     */
    private void mostrarDatosUsuario(String userId) {
        firebaseService.obtenerDatosUsuario(userId, this, usuario -> {
            if (usuario != null) {
                usuarioNombreTextView.setText(usuario.getNombre());
                usuarioCorreoTextView.setText(usuario.getCorreo());
                usuarioCelularTextView.setText(usuario.getCelular());
                usuarioDireccionTextView.setText(usuario.getDireccion());
            } else {
                Toast.makeText(this, "No se pudieron cargar los datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Carga el rol del usuario desde Firebase y muestra una notificación del tipo de usuario.
     */
    private void cargarRolDesdeFirebase(String userId) {
        Log.d("cargarRolDesdeFirebase", "Cargando rol para el usuario con ID: " + userId);
        firebaseService.obtenerRolUsuario(userId, this, rol -> {
            if (rol != null) {
                userRole = rol;
                Log.d("cargarRolDesdeFirebase", "Rol obtenido: " + rol);

                // Mostrar notificación con el tipo de usuario
                Toast.makeText(this, "Has iniciado sesión como: " + rol, Toast.LENGTH_SHORT).show();
            } else {
                Log.e("cargarRolDesdeFirebase", "No se pudo obtener el rol para el usuario con ID: " + userId);
                Toast.makeText(this, "No se pudo determinar el tipo de usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    private void cerrarSesion() {
        autenticacionService.cerrarSesion(this);
        Intent intent = new Intent(UsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Método para abrir la pantalla de Pedidos.
     */
    private void verPedidos() {
        if (userRole != null) {
            Intent intent = new Intent(UsuarioActivity.this, VistaPedidosActivity.class);
            intent.putExtra("userRole", userRole);
            startActivity(intent);
        } else {
            Toast.makeText(this, "El rol del usuario no está disponible", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para abrir la pantalla de categorías.
     */
    private void verCategorias() {
        Intent intent = new Intent(UsuarioActivity.this, CategoriasActivity.class);
        startActivity(intent);
    }

    /**
     * Método para abrir la pantalla de información de la empresa.
     */
    private void verInformacionEmpresa() {
        Intent intent = new Intent(UsuarioActivity.this, InfoEmpresaActivity.class);
        startActivity(intent);
    }
}
