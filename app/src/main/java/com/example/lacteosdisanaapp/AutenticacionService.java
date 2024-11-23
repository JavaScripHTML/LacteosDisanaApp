package com.example.lacteosdisanaapp;

import android.content.Context;
import android.widget.Toast;

import com.example.lacteosdisanaapp.Modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AutenticacionService {
    private FirebaseAuth mAuth;

    // Constructor: Inicializa FirebaseAuth
    public AutenticacionService() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    // Método para registrar un usuario
    public void registrarUsuario(String correo, String contrasena, Context context, FirebaseService firebaseService, Usuario usuario, RegistroCallback callback) {
        mAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            // Almacenar la información del usuario en Firebase Realtime Database
                            firebaseService.almacenarUsuarioEnRealtimeDatabase(userId, usuario, context);
                            callback.onRegistroExitoso();  // Notifica éxito
                        } else {
                            Toast.makeText(context, "No se pudo obtener el usuario después del registro", Toast.LENGTH_SHORT).show();
                            callback.onRegistroFallido();
                        }
                    } else {
                        // Manejar errores específicos
                        if (task.getException() != null) {
                            String errorMessage = task.getException().getMessage();
                            if (errorMessage != null && errorMessage.contains("The email address is already in use")) {
                                Toast.makeText(context, "El correo ya está registrado. Por favor usa otro.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Error en el registro: " + errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                        callback.onRegistroFallido();  // Notifica fallo
                    }
                });
    }

    // Método para iniciar sesión
    public void iniciarSesion(String correo, String contrasena, Context context, IniciarSesionCallback callback) {
        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onInicioSesionExitoso();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                        Toast.makeText(context, "Error en la autenticación: " + errorMessage, Toast.LENGTH_LONG).show();
                        callback.onInicioSesionFallido();
                    }
                });
    }

    // Método para cerrar sesión
    public void cerrarSesion(Context context) {
        mAuth.signOut();
        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show();
    }

    // Método para obtener el usuario autenticado actual
    public FirebaseUser obtenerUsuarioActual() {
        return mAuth.getCurrentUser();
    }

    // Callback para el registro
    public interface RegistroCallback {
        void onRegistroExitoso();

        void onRegistroFallido();
    }

    // Callback para el inicio de sesión
    public interface IniciarSesionCallback {
        void onInicioSesionExitoso();

        void onInicioSesionFallido();
    }
}
