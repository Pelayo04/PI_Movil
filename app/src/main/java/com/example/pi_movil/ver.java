package com.example.pi_movil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import Global.info;
import POJO.datos;
import adaptadorVer.adaptadorVer;

public class ver extends AppCompatActivity {

    Toolbar toolbar;         // Barra superior de opciones
    RecyclerView rv_ver;     // Vista tipo lista para mostrar los elementos
    Context context;         // Contexto de la aplicación, necesario para operaciones como Intents

    SharedPreferences archivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita el modo Edge-to-Edge (pantalla completa sin márgenes)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver); // Asocia la clase con su archivo XML

        archivo = this.getSharedPreferences("sesion", MODE_PRIVATE);

        // Se enlaza el RecyclerView del XML
        rv_ver = (RecyclerView) findViewById(R.id.rv_ver);

        // Se crea una instancia del adaptador personalizado
        adaptadorVer ver = new adaptadorVer();
        ver.context = this; // Se asigna el contexto a la propiedad del adaptador

        // Define la orientación del RecyclerView como vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // Aplica el administrador de diseño al RecyclerView
        rv_ver.setLayoutManager(linearLayoutManager);

        // Conecta el adaptador con el RecyclerView para que se muestren los datos
        rv_ver.setAdapter(ver);

        // Asocia el Toolbar y lo establece como barra de acción
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cargarDatos();

        // Ajusta el padding del layout para evitar que se sobreponga con la barra de estado o navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarDatos(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.100.7/bd/ver.php"; // Ajusta si cambia tu IP

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    info.lista.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject objeto = response.getJSONObject(i);
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
                        Objects.requireNonNull(rv_ver.getAdapter()).notifyDataSetChanged();
                        Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opc1) {
            // Si selecciona la opción "Ver", abre la actividad correspondiente
            Intent principal = new Intent(this, MainActivity.class);
            startActivity(principal);
        }

        if (item.getItemId() == R.id.opc2) {
            // Si ya está en la pantalla principal, muestra mensaje
            Toast.makeText(this, "Ya estás en Ver", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.opc3) {
            Intent modificar = new Intent(this, Modificar.class);
            startActivity(modificar);
        }
        if (item.getItemId() == R.id.opc4) {
            Intent eliminar = new Intent(this, Eliminar.class);
            startActivity(eliminar);
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
}