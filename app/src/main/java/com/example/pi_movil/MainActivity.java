// Paquete principal de la aplicación
package com.example.pi_movil;

// Importaciones necesarias para la funcionalidad de la actividad
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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

import org.json.JSONException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Global.info;
import POJO.datos;

// Clase principal de la actividad MainActivity
public class MainActivity extends AppCompatActivity {

    // Declaración de componentes de interfaz
    private EditText et_nombreAlumno, et_apPat, et_apMat, et_telefono, et_fechaSalida, et_horaSalida, et_horaEntrega, et_nombreMaestro;
    private Button guardar, limpiar;
    private Spinner herramientas;
    Toolbar toolbar;
    datos persona = new datos();  // Objeto para almacenar los datos ingresados
    SharedPreferences archivo;    // Archivo de preferencias para sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Configuración del toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Recuperación de preferencias de sesión
        archivo = getSharedPreferences("sesion", MODE_PRIVATE);
        String tipo = archivo.getString("tipo_usuario", "mortal");

        // Validación de tipo de usuario (solo admin puede acceder)
        if (!tipo.equals("admin")) {
            Toast.makeText(this, "Acceso restringido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Enlace de componentes del layout a variables Java
        et_nombreAlumno = findViewById(R.id.et_nombreAlumno);
        et_apPat = findViewById(R.id.et_apPat);
        et_apMat = findViewById(R.id.et_apMat);
        et_telefono = findViewById(R.id.et_telefono);
        et_fechaSalida = findViewById(R.id.et_fecha);
        et_horaSalida = findViewById(R.id.et_horaSalida);
        et_horaEntrega = findViewById(R.id.et_horaEntrega);
        et_nombreMaestro = findViewById(R.id.et_nombreMaestro);

        // Configuración del Spinner con herramientas disponibles
        herramientas = findViewById(R.id.spinner_herramientas);
        String[] opciones = {"Multimetro", "Cautin", "Osciloscopio", "Generador de funciones",
                "Punta bnc", "Punta atenuadora", "Fuente de poder", "Punta lógica"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        herramientas.setAdapter(adapter);

        // Configuración del botón guardar
        guardar = findViewById(R.id.button_guardar);
        guardar.setOnClickListener(view -> guardarDatos());

        // Configuración del botón limpiar
        limpiar = findViewById(R.id.button_limpiar);
        limpiar.setOnClickListener(view -> limpiarCampos());

        // Listeners para seleccionar fecha y hora
        et_fechaSalida.setOnClickListener(v -> datePicker());
        et_horaSalida.setOnClickListener(v -> horaSalida());
        et_horaEntrega.setOnClickListener(v -> horaEntrega());

        // Ajuste de los insets para el diseño
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método que guarda los datos ingresados en el formulario
    private void guardarDatos() {
        datos persona = new datos();
        persona.setNombreAlumno(et_nombreAlumno.getText().toString());
        persona.setApPat(et_apPat.getText().toString());
        persona.setApMat(et_apMat.getText().toString());
        persona.setTelefono(et_telefono.getText().toString());
        persona.setHerramienta(herramientas.getSelectedItem().toString());
        persona.setFecha(et_fechaSalida.getText().toString());
        persona.setHoraSalida(et_horaSalida.getText().toString());
        persona.setHoraEntrega(et_horaEntrega.getText().toString());
        persona.setNombreMaestro(et_nombreMaestro.getText().toString());

        // Se añade a la lista global
        info.lista.add(persona);

        // Se envían los datos al servidor con Volley
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.100.7/bd/agregar.php";

        // Construcción del objeto JSON con los datos
        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("nombreAlumno", persona.getNombreAlumno());
            jsonObject.put("apePat", persona.getApPat());
            jsonObject.put("apeMat", persona.getApMat());
            jsonObject.put("telefono", persona.getTelefono());
            jsonObject.put("herramienta", persona.getHerramienta());
            jsonObject.put("fecha", persona.getFecha());
            jsonObject.put("horaSalida", persona.getHoraSalida());
            jsonObject.put("horaEntrega", persona.getHoraEntrega());
            jsonObject.put("nombreMaestro", persona.getNombreMaestro());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al crear JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        // Envío del request POST
        com.android.volley.toolbox.JsonObjectRequest request = new com.android.volley.toolbox.JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                response -> Toast.makeText(MainActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show(),
                error -> {
                    Toast.makeText(MainActivity.this, "Error al conectar con servidor", Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", error.toString());
                }
        );

        queue.add(request);
    }

    // Método que limpia todos los campos del formulario
    private void limpiarCampos() {
        et_nombreAlumno.setText("");
        et_apPat.setText("");
        et_apMat.setText("");
        et_telefono.setText("");
        et_fechaSalida.setText("");
        et_horaSalida.setText("");
        et_horaEntrega.setText("");
        et_nombreMaestro.setText("");
        Toast.makeText(this, "Los campos han sido limpiados", Toast.LENGTH_SHORT).show();
    }

    // Método que infla el menú de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Método que maneja la selección de opciones del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opc1)
            Toast.makeText(this, "Ya estás en Principal", Toast.LENGTH_SHORT).show();

        if (item.getItemId() == R.id.opc2) {
            Intent ver = new Intent(this, ver.class);
            startActivity(ver);
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
            // Cierre de sesión
            if(archivo.contains("id_usuario")){
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

    // Método para mostrar selector de fecha
    private void datePicker() {
        int dia, mes, anio;
        Calendar actual = Calendar.getInstance();
        dia = actual.get(Calendar.DAY_OF_MONTH);
        mes = actual.get(Calendar.MONTH);
        anio = actual.get(Calendar.YEAR);

        DatePickerDialog datPD = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            String cadena = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            et_fechaSalida.setText(cadena);
        }, anio, mes, dia);

        datPD.show();
    }

    // Método para seleccionar hora de salida
    private void horaSalida() {
        int hr, min;
        Calendar actual = Calendar.getInstance();
        hr = actual.get(Calendar.HOUR_OF_DAY);
        min = actual.get(Calendar.MINUTE);

        TimePickerDialog timePD = new TimePickerDialog(this, (timePicker, hourOfDay, minute) -> {
            persona.h = hourOfDay;
            persona.min = minute;
            String cadena = " " + persona.h + ":" + persona.min;
            et_horaSalida.setText(cadena);
        }, hr, min, true);
        timePD.show();
    }

    // Método para seleccionar hora de entrega
    private void horaEntrega() {
        int hr, min;
        Calendar actual = Calendar.getInstance();
        hr = actual.get(Calendar.HOUR_OF_DAY);
        min = actual.get(Calendar.MINUTE);

        TimePickerDialog timePD = new TimePickerDialog(this, (timePicker, hourOfDay, minute) -> {
            persona.h2 = hourOfDay;
            persona.min2 = minute;
            String cadena = " " + persona.h2 + ":" + persona.min2;
            et_horaEntrega.setText(cadena);
        }, hr, min, true);
        timePD.show();
    }
}
