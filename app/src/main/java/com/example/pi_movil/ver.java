// Paquete de la aplicación
package com.example.pi_movil;

// Importaciones necesarias para funcionamiento y diseño
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

// Clase de la Activity "ver"
public class ver extends AppCompatActivity {

    Toolbar toolbar;         // Toolbar de la interfaz
    RecyclerView rv_ver;     // RecyclerView para mostrar la lista de elementos
    Context context;         // Contexto general

    SharedPreferences archivo; // Para manejo de sesión del usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activa el modo edge-to-edge (pantalla completa)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver); // Asocia la activity con su layout

        // Inicializa el archivo de sesión
        archivo = this.getSharedPreferences("sesion", MODE_PRIVATE);

        // Asigna el RecyclerView del layout
        rv_ver = (RecyclerView) findViewById(R.id.rv_ver);

        // Instancia del adaptador personalizado
        adaptadorVer ver = new adaptadorVer();
        ver.context = this; // Asigna el contexto al adaptador

        // Establece el layout del RecyclerView como vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_ver.setLayoutManager(linearLayoutManager); // Aplica el layout al RecyclerView
        rv_ver.setAdapter(ver); // Asocia el adaptador al RecyclerView

        // Configura el toolbar como action bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Carga los datos desde el servidor
        cargarDatos();

        // Aplica los márgenes de sistema (barras de navegación y estado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para cargar los datos del servidor y mostrarlos en el RecyclerView
    private void cargarDatos() {
        RequestQueue queue = Volley.newRequestQueue(this); // Cola de peticiones
        String url = "http://192.168.100.7/bd/ver.php"; // URL del backend

        // Petición GET que recibe un arreglo JSON
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    info.lista.clear(); // Limpia la lista antes de agregar nuevos elementos
                    try {
                        // Recorre cada elemento del JSON recibido
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject objeto = response.getJSONObject(i);
                            datos d = new datos(); // Objeto para almacenar los datos

                            // Asigna valores al objeto "datos" desde el JSON
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

                            // Agrega el objeto a la lista global
                            info.lista.add(d);
                        }

                        // Notifica al adaptador que los datos han cambiado
                        Objects.requireNonNull(rv_ver.getAdapter()).notifyDataSetChanged();
                        Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        Toast.makeText(this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
        );

        queue.add(request); // Se lanza la petición
    }

    // Método para inflar el menú en el Toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Método que gestiona las acciones al seleccionar una opción del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opc1) {
            // Va a la pantalla principal (MainActivity)
            Intent principal = new Intent(this, MainActivity.class);
            startActivity(principal);
        }

        if (item.getItemId() == R.id.opc2) {
            // Ya se encuentra en "ver", solo muestra mensaje
            Toast.makeText(this, "Ya estás en Ver", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.opc3) {
            // Abre pantalla de modificación
            Intent modificar = new Intent(this, Modificar.class);
            startActivity(modificar);
        }

        if (item.getItemId() == R.id.opc4) {
            // Abre pantalla de eliminación
            Intent eliminar = new Intent(this, Eliminar.class);
            startActivity(eliminar);
        }

        if (item.getItemId() == R.id.opc5) {
            // Cierra sesión eliminando el ID guardado en SharedPreferences
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
            // Abre pantalla de creadores
            Intent creadores = new Intent(this, creadores.class);
            startActivity(creadores);
        }

        if (item.getItemId() == R.id.opc7) {
            // Abre pantalla de contactos
            Intent contactos = new Intent(this, contactos.class);
            startActivity(contactos);
        }

        return super.onOptionsItemSelected(item);
    }
}
