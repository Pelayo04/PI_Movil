package com.example.pi_movil;

// Importaciones necesarias para la funcionalidad general de Android
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import Global.info;                    // Clase global que almacena listas compartidas
import POJO.datos;                    // POJO que representa cada registro de la base de datos
import adaptadorEliminar.adaptadorEliminar; // Adaptador personalizado para eliminar elementos


public class Eliminar extends AppCompatActivity {

    Toolbar toolbar;                 // Barra superior de la pantalla
    RecyclerView rv_eliminar;       // Componente visual para mostrar los registros
    Button but_eliminar;            // Botón que ejecuta la eliminación

    SharedPreferences archivo;      // Objeto para guardar sesión del usuario
    Context context;                // Contexto actual de la aplicación

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);    // Habilita modo pantalla completa
        setContentView(R.layout.activity_eliminar); // Establece el layout

        // Configura el toolbar como barra superior
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtiene preferencias guardadas y verifica tipo de usuario
        archivo = getSharedPreferences("sesion", MODE_PRIVATE);
        String tipo = archivo.getString("tipo_usuario", "mortal");

        // Solo permite el acceso si el usuario es admin
        if (!tipo.equals("admin")) {
            Toast.makeText(this, "Acceso restringido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializa el RecyclerView y su adaptador
        rv_eliminar = (RecyclerView)findViewById(R.id.rv_eliminar);
        adaptadorEliminar eliminar = new adaptadorEliminar();
        eliminar.context = this;

        // Define el layout vertical para mostrar los elementos
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_eliminar.setLayoutManager(linearLayoutManager); // Asigna el layout al RecyclerView
        rv_eliminar.setAdapter(eliminar);                  // Asigna el adaptador

        // Botón que ejecuta la función de eliminar
        but_eliminar = (Button)findViewById(R.id.button_eliminar);
        but_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar(); // Llama a la función de eliminación
            }
        });

        cargarDatos(); // Carga los datos al iniciar la actividad

        // Ajuste para evitar solapamientos con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Infla el menú superior de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Maneja la navegación entre las distintas Activities
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opc1) {
            Intent principal = new Intent(this, MainActivity.class);
            startActivity(principal);
        }
        if (item.getItemId() == R.id.opc2) {
            Intent ver = new Intent(this, ver.class);
            startActivity(ver);
        }
        if (item.getItemId() == R.id.opc3) {
            Intent modificar = new Intent(this, Modificar.class);
            startActivity(modificar);
        }
        if (item.getItemId() == R.id.opc4) {
            Toast.makeText(this, "Ya estás en Eliminar", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.opc5) {
            // Cierra sesión y regresa al login
            if (archivo.contains("id_usuario")) {
                SharedPreferences.Editor editor = archivo.edit();
                editor.remove("id_usuario");
                editor.commit();
                Intent x = new Intent(this, Inicio.class);
                startActivity(x);
                finish();
            }
        }
        if (item.getItemId() == R.id.opc6) {
            Intent creadores = new Intent(this, creadores.class);
            startActivity(creadores);
        }
        if (item.getItemId() == R.id.opc7) {
            Intent contactos = new Intent(this, contactos.class);
            startActivity(contactos);
        }
        return super.onOptionsItemSelected(item);
    }

    // Método que elimina los registros seleccionados
    private void eliminar() {
        // Verifica si hay elementos seleccionados
        if (info.listaEliminar.isEmpty()) {
            Toast.makeText(this, "No hay elementos seleccionados", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.100.7/bd/eliminar.php";

        // Prepara un JSON con los IDs a eliminar
        org.json.JSONArray jsonArray = new org.json.JSONArray();
        try {
            for (datos d : info.listaEliminar) {
                org.json.JSONObject obj = new org.json.JSONObject();
                obj.put("id", d.getId());
                jsonArray.put(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al crear JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        // Envia los datos como un arreglo JSON al servidor
        com.android.volley.toolbox.JsonArrayRequest request = new com.android.volley.toolbox.JsonArrayRequest(
                Request.Method.POST, url, jsonArray,
                response -> {
                    Toast.makeText(this, "Registros eliminados", Toast.LENGTH_SHORT).show();
                    info.listaEliminar.clear(); // Limpia la lista de seleccionados
                    cargarDatos();              // Refresca la vista con nuevos datos
                },
                error -> Toast.makeText(this, "Error al conectar con servidor", Toast.LENGTH_SHORT).show()
        );

        queue.add(request); // Agrega la petición a la cola
    }

    // Carga los registros desde la base de datos
    private void cargarDatos() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.100.7/bd/ver.php";

        com.android.volley.toolbox.JsonArrayRequest request = new com.android.volley.toolbox.JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    // Limpia listas antes de llenar
                    info.lista.clear();
                    info.listaEliminar.clear();

                    try {
                        // Recorre el arreglo JSON y convierte cada objeto en un registro
                        for (int i = 0; i < response.length(); i++) {
                            org.json.JSONObject objeto = response.getJSONObject(i);
                            datos d = new datos();

                            d.setId(objeto.getInt("id"));
                            d.setNombreAlumno(objeto.getString("nombreAlumno"));
                            d.setApPat(objeto.getString("apePat"));
                            d.setApMat(objeto.getString("apeMat"));
                            d.setTelefono(String.valueOf(objeto.getLong("telefono")));
                            d.setHerramienta(objeto.getString("herramienta"));
                            d.setFecha(objeto.getString("fecha"));
                            d.setHoraSalida(objeto.getString("horaSalida"));
                            d.setHoraEntrega(objeto.getString("horaEntrega"));
                            d.setNombreMaestro(objeto.getString("nombreMaestro"));

                            info.lista.add(d); // Agrega el objeto a la lista global
                        }

                        // Notifica al adaptador que los datos cambiaron
                        rv_eliminar.getAdapter().notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
        );

        queue.add(request); // Ejecuta la petición
    }

}
