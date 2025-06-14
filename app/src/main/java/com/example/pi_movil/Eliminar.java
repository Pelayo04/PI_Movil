package com.example.pi_movil;

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

import Global.info;
import POJO.datos;
import adaptadorEliminar.adaptadorEliminar;


public class Eliminar extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv_eliminar; // Lista visual que muestra los elementos a eliminar
    Button but_eliminar;

    SharedPreferences archivo;  // Preferencias compartidas (para sesión de usuario)

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eliminar);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        archivo = this.getSharedPreferences("sesion", MODE_PRIVATE); // Sesión de usuario

        rv_eliminar = (RecyclerView)findViewById(R.id.rv_eliminar); // Lista visual (RecyclerView)
        adaptadorEliminar eliminar = new adaptadorEliminar();
        eliminar.context = this;

        // Se define el tipo de layout vertical para la lista
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rv_eliminar.setLayoutManager(linearLayoutManager); //Asignación del layout

        rv_eliminar.setAdapter(eliminar); //Conectar el adaptador con la lista

        but_eliminar = (Button)findViewById(R.id.button_eliminar);
        but_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });

        cargarDatos();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opc1) {
            Intent principal = new Intent(this, MainActivity.class);
            startActivity(principal);
        }
        if (item.getItemId() == R.id.opc2) {
            // Si selecciona la opción "Ver", abre la actividad correspondiente
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
            // Cierre de sesión (borra ID del usuario guardado)
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
            // Si selecciona la opción "Ver", abre la actividad correspondiente
            Intent creadores = new Intent(this, creadores.class);
            startActivity(creadores);
        }
        if (item.getItemId() == R.id.opc7) {
            // Si selecciona la opción "Ver", abre la actividad correspondiente
            Intent contactos = new Intent(this, contactos.class);
            startActivity(contactos);
        }
        return super.onOptionsItemSelected(item);
    }

    private void eliminar() {
        if (info.listaEliminar.isEmpty()) {
            Toast.makeText(this, "No hay elementos seleccionados", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.100.7/bd/eliminar.php";

        // Creamos un JSONArray con solo los IDs a eliminar
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

        com.android.volley.toolbox.JsonArrayRequest request = new com.android.volley.toolbox.JsonArrayRequest(
                Request.Method.POST, url, jsonArray,
                response -> {
                    Toast.makeText(this, "Registros eliminados", Toast.LENGTH_SHORT).show();
                    info.listaEliminar.clear();    // Limpia los seleccionados
                    cargarDatos();                 // Refresca la lista desde la base de datos
                },
                error -> Toast.makeText(this, "Error al conectar con servidor", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }


    private void cargarDatos() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.100.7/bd/ver.php"; // Ajusta si tu IP cambia

        com.android.volley.toolbox.JsonArrayRequest request = new com.android.volley.toolbox.JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    info.lista.clear();
                    info.listaEliminar.clear();

                    try {
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

                            info.lista.add(d);
                        }

                        // Actualiza el RecyclerView
                        rv_eliminar.getAdapter().notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }


}