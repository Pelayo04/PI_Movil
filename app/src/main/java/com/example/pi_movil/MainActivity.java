package com.example.pi_movil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import Global.info;
import POJO.datos;

public class MainActivity extends AppCompatActivity {

    private EditText et_nombreAlumno, et_apPat, et_apMat, et_telefono, et_fechaSalida, et_horaSalida, et_horaEntrega, et_nombreMaestro;
    private Button guardar, limpiar;
    private Spinner herramientas;
    Toolbar toolbar;
    datos persona = new datos();
    SharedPreferences archivo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        archivo = this.getSharedPreferences("sesion", MODE_PRIVATE);

        et_nombreAlumno = (EditText)findViewById(R.id.et_nombreAlumno);
        et_apPat = (EditText)findViewById(R.id.et_apPat);
        et_apMat = (EditText)findViewById(R.id.et_apMat);
        et_telefono = (EditText)findViewById(R.id.et_telefono);
        et_fechaSalida = (EditText)findViewById(R.id.et_fecha);
        et_horaSalida = (EditText)findViewById(R.id.et_horaSalida);
        et_horaEntrega = (EditText)findViewById(R.id.et_horaEntrega);
        et_nombreMaestro = (EditText)findViewById(R.id.et_nombreMaestro);

        herramientas = (Spinner)findViewById(R.id.spinner_herramientas);
        String [] opciones = {"Multimetro", "Cautin", "Osciloscopio", "Generador de funciones",
                "Punta bnc", "Punta atenuadora", "Fuente de poder", "Punta lógica"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        herramientas.setAdapter(adapter);


        guardar = (Button)findViewById(R.id.button_guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
            }
        });

        limpiar = (Button)findViewById(R.id.button_limpiar);
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Método para guardar los datos
    private void guardarDatos(){

        persona.setNombreAlumno(et_nombreAlumno.getText().toString());
        persona.setApPat(et_apPat.getText().toString());
        persona.setApMat(et_apMat.getText().toString());
        persona.setTelefono(et_telefono.getText().toString());
        persona.setHerramienta(herramientas.getSelectedItem().toString());
        persona.setFecha(et_fechaSalida.getText().toString());
        persona.setHoraSalida(et_horaSalida.getText().toString());
        persona.setHoraEntrega(et_horaEntrega.getText().toString());
        persona.setNombreMaestro(et_nombreMaestro.getText().toString());

        info.lista.add(persona);

        RequestQueue solicitud = Volley.newRequestQueue(this);

        StringRequest sql =new StringRequest(Request.Method.POST, "http://192.168.1.70/bd/agregar.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                Log.d("no paso", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("no paso", error.getMessage());
            }
        }){
            // Aquí se construye el cuerpo de la petición (los datos que se envían)
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> x = new HashMap<>();
                x.put("nombreAlumno", "" + persona.getNombreAlumno());
                x.put("apePat", "" + persona.getApPat());
                x.put("apeMat", "" + persona.getApMat());
                x.put("telefono", "" + persona.getTelefono());
                x.put("herramienta", "" + persona.getHerramienta());
                x.put("fecha", "" + persona.getFecha());
                x.put("horaSalida", "" + persona.getHoraSalida());
                x.put("horaEntrega", "" + persona.getHoraEntrega());
                x.put("nombreMaestro", "" + persona.getNombreMaestro());
                return x;
            }
        };
        solicitud.add(sql);
    }

    //Método para limpiar los campos
    private void limpiarCampos(){
        et_nombreAlumno.setText("");
        et_apPat.setText("");
        et_apMat.setText("");
        et_telefono.setText("");
        et_horaSalida.setText("");
        et_horaEntrega.setText("");
        et_nombreMaestro.setText("");

        // Muestra mensaje al usuario
        Toast.makeText(this, "Los campos han sido limpiados", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opc1)
            // Si ya está en la pantalla principal, muestra mensaje
            Toast.makeText(this, "Ya estás en Principal", Toast.LENGTH_SHORT).show();

        if (item.getItemId() == R.id.opc2) {
            // Si selecciona la opción "Ver", abre la actividad correspondiente
            Intent ver = new Intent(this, ver.class);
            startActivity(ver);
        }
        /*if (item.getItemId() == R.id.opc3) {
            Intent modificar = new Intent(this, Modificar.class);
            startActivity(modificar);
        }
        if (item.getItemId() == R.id.opc4) {
            Intent eliminar = new Intent(this, Eliminar.class);
            startActivity(eliminar);
        }
        if (item.getItemId() == R.id.opc5) {
            // Cierre de sesión (borra ID del usuario guardado)
            if(archivo.contains("id_usuario")){
                SharedPreferences.Editor editor = archivo.edit();
                editor.remove("id_usuario");
                editor.commit();
                Intent x = new Intent(this, Inicio.class);
                startActivity(x);
                finish();
            }*/
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