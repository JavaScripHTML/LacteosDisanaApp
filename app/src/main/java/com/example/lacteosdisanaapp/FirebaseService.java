package com.example.lacteosdisanaapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lacteosdisanaapp.Modelo.Pedido;
import com.example.lacteosdisanaapp.Modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseService {
    private FirebaseFirestore firestore;
    private CollectionReference pedidosCollection;
    private DatabaseReference usuariosDatabase;

    public FirebaseService() {
        firestore = FirebaseFirestore.getInstance();
        pedidosCollection = firestore.collection("Pedidos");
        usuariosDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
    }

    public void obtenerRolUsuario(String userId, Context context, RolCallback callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onRolRecibido(null);
            return;
        }

        usuariosDatabase.child(userId).child("rol").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String rol = snapshot.getValue(String.class);
                    callback.onRolRecibido(rol);
                } else {
                    callback.onRolRecibido(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onRolRecibido(null);
            }
        });
    }

    public void obtenerDatosUsuario(String userId, Context context, OnUsuarioRecibidoCallback callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onUsuarioRecibido(null);
            return;
        }

        usuariosDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    callback.onUsuarioRecibido(usuario);
                } else {
                    callback.onUsuarioRecibido(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onUsuarioRecibido(null);
            }
        });
    }

    public void obtenerNombreUsuario(String userId, Context context, OnNombreRecibidoCallback callback) {
        usuariosDatabase.child(userId).child("nombre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre = snapshot.getValue(String.class);
                    callback.onNombreRecibido(nombre);
                } else {
                    Toast.makeText(context, "No se pudo obtener el nombre del usuario", Toast.LENGTH_SHORT).show();
                    callback.onNombreRecibido(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context, "Error en la consulta de nombre: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onNombreRecibido(null);
            }
        });
    }

    public void almacenarUsuarioEnRealtimeDatabase(String userId, Usuario usuario, Context context) {
        usuariosDatabase.child(userId).setValue(usuario)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Usuario registrado en Realtime Database", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al registrar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void guardarPedido(String clienteEmail, Pedido pedido, Context context) {
        Map<String, Object> pedidoMap = new HashMap<>();
        pedidoMap.put("cliente", clienteEmail);
        pedidoMap.put("total", pedido.getTotal());
        pedidoMap.put("fecha", FieldValue.serverTimestamp()); // Usa serverTimestamp para la fecha
        pedidoMap.put("estado", pedido.getEstado());
        pedidoMap.put("productos", pedido.getProductos());

        pedidosCollection.add(pedidoMap)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(context, "Pedido guardado exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al guardar el pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



    public void obtenerPedidosCliente(String clienteId, PedidosCallback callback) {
        pedidosCollection.whereEqualTo("cliente", clienteId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Pedido> pedidos = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Pedido pedido = document.toObject(Pedido.class);
                            if (pedido != null) {
                                pedido.setId(document.getId()); // Asigna el ID del documento
                                pedidos.add(pedido);
                            }
                        }
                        callback.onPedidosRecibidos(pedidos);
                    } else {
                        callback.onPedidosRecibidos(null);
                    }
                });
    }


    public void obtenerTodosLosPedidos(PedidosCallback callback) {
        pedidosCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Pedido> pedidos = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Pedido pedido = document.toObject(Pedido.class);
                            if (pedido != null) {
                                // Establecer el ID del documento
                                pedido.setId(document.getId());
                                pedidos.add(pedido);
                            }
                        }
                        callback.onPedidosRecibidos(pedidos);
                    } else {
                        callback.onPedidosRecibidos(null);
                    }
                });
    }

    public void actualizarEstadoPedido(String pedidoId, String nuevoEstado, Context context) {
        firestore.collection("Pedidos").document(pedidoId)
                .update("estado", nuevoEstado)
                .addOnSuccessListener(aVoid -> {
                    Log.d("ActualizarEstado", "Estado actualizado para: " + pedidoId);
                    Toast.makeText(context, "Estado actualizado correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("ActualizarEstado", "Error al actualizar el estado", e);
                    Toast.makeText(context, "Error al actualizar el estado", Toast.LENGTH_SHORT).show();
                });
    }


    public void actualizarDatosUsuario(String userId, String celular, String direccion, Context context, UpdateCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("celular", celular);
        updates.put("direccion", direccion);

        usuariosDatabase.child(userId).updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    callback.onUpdateCompleted(true);
                })
                .addOnFailureListener(e -> {
                    Log.e("ActualizarUsuario", "Error al actualizar los datos", e);
                    callback.onUpdateCompleted(false);
                });
    }

    public interface UpdateCallback {
        void onUpdateCompleted(boolean success);
    }

    public void actualizarDatosUsuario(String userId, String nombre, String correo, String contrasena, String celular, String direccion, UpdateCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", nombre);
        updates.put("correo", correo);
        updates.put("contrasena", contrasena); // Esto es solo de ejemplo; no almacenes contraseñas en texto plano.
        updates.put("celular", celular);
        updates.put("direccion", direccion);

        usuariosDatabase.child(userId).updateChildren(updates)
                .addOnSuccessListener(aVoid -> callback.onUpdateCompleted(true))
                .addOnFailureListener(e -> callback.onUpdateCompleted(false));
    }

    public void obtenerPedidosOrdenados(PedidosCallback callback) {
        pedidosCollection
                .orderBy("fecha", Query.Direction.DESCENDING) // Ordenar por fecha descendente
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Pedido> pedidos = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Pedido pedido = document.toObject(Pedido.class);
                            if (pedido != null) {
                                pedido.setId(document.getId()); // Asignar el ID del documento
                                pedidos.add(pedido);
                            }
                        }
                        callback.onPedidosRecibidos(pedidos);
                    } else {
                        callback.onPedidosRecibidos(null);
                    }
                });
    }


    public interface RolCallback {
        void onRolRecibido(String rol);
    }

    public interface PedidosCallback {
        void onPedidosRecibidos(List<Pedido> pedidos);
    }

    public interface OnNombreRecibidoCallback {
        void onNombreRecibido(String nombre);
    }

    // Callback para manejar la respuesta
    public interface OnUsuarioRecibidoCallback {
        void onUsuarioRecibido(Usuario usuario);
    }



}
