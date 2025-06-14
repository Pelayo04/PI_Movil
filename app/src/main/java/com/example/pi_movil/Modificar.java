// Paquete principal de la app
package com.example.pi_movil;

// Importaciones necesarias para la funcionalidad de la interfaz y red
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Global.info;           // Clase global que contiene la lista de datos
import POJO.datos;            // POJO con atributos del registro

public class Modificar extends AppCompatActivity {

    // Declaración de vistas y variables necesarias
    Toolbar toolbar;
    EditText nombreAlumno, apPat, apMat, telefono, fecha, horaSalida, horaEntrega, nombreMaestro;
    Button but_anterior, but_guardarCambios, but_siguiente;
    SharedPreferences archivo;
    Spinner herramientas;

    datos persona = new datos();   // Objeto datos temporal
    int posicion;                 // Índice de navegación en la lista

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Activa modo pantalla completa sin márgenes
        setContentView(R.layout.activity_modificar); // Asocia layout

        // Configura el Toolbar como barra superior
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Verifica tipo de usuario desde SharedPreferences
        archivo = getSharedPreferences("sesion", MODE_PRIVATE);
        String tipo = archivo.getString("tipo_usuario", "mortal");

        // Si no es admin, cierra la actividad
        if (!tipo.equals("admin")) {
            Toast.makeText(this, "Acceso restringido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Asocia componentes con su respectivo ID
        nombreAlumno = findViewById(R.id.et_nombreAlumno);
        apPat = findViewById(R.id.et_apPat);
        apMat = findViewById(R.id.et_apMat);
        telefono = findViewById(R.id.et_telefono);
        herramientas = findViewById(R.id.spinner_herramientas);

        // Configura el Spinner con opciones de herramientas
        String [] opciones = {"Multimetro", "Cautin", "Osciloscopio", "Generador de funciones",
                "Punta bnc", "Punta atenuadora", "Fuente de poder", "Punta lógica"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        herramientas.setAdapter(adapter);

        // Más campos de texto
        fecha = findViewById(R.id.et_fecha);
        horaSalida = findViewById(R.id.et_horaSalida);
        horaEntrega = findViewById(R.id.et_horaEntrega);
        nombreMaestro = findViewById(R.id.et_nombreMaestro);

        // Inicializa posición en 0 (primer elemento de la lista)
        posicion = 0;

        // Si hay datos en la lista, muestra el primero
        if (info.lista.size() > 0) {
            mostrar();
        } else {
            Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_SHORT).show();
        }

        // Botón anterior
        but_anterior = findViewById(R.id.button_ant);
        but_anterior.setOnClickListener(view -> anterior());

        // Botón guardar cambios
        but_guardarCambios = findViewById(R.id.button_gc);
        but_guardarCambios.setOnClickListener(view -> guardarCambios());

        // Botón siguiente
        but_siguiente = findViewById(R.id.button_sig);
        but_siguiente.setOnClickListener(view -> siguiente());

        // Click en campo fecha
        fecha.setOnClickListener(view -> datePicker());

        // Click en hora de salida
        horaSalida.setOnClickListener(view -> timePickerSalida());

        // Click en hora de entrega
        horaEntrega.setOnClickListener(view -> timePickerEntrega());

        // Ajuste de márgenes para no solaparse con barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Infla el menú superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Acciones al seleccionar opciones del menú
    @Override
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
            Toast.makeText(this, "Ya estás en Modificar", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.opc4) {
            Intent eliminar = new Intent(this, Eliminar.class);
            startActivity(eliminar);
        }
        if (item.getItemId() == R.id.opc5) {
            // Cierra sesión
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

    // Muestra el registro actual en pantalla
    public void mostrar() {
        if (info.lista.size() > 0) {
            datos actual = info.lista.get(posicion);
            nombreAlumno.setText(actual.getNombreAlumno());
            apPat.setText(actual.getApPat());
            apMat.setText(actual.getApMat());
            telefono.setText(actual.getTelefono());

            ArrayAdapter<String> adapter = (ArrayAdapter<String>) herramientas.getAdapter();
            int index = adapter.getPosition(actual.getHerramienta());
            if (index >= 0) herramientas.setSelection(index);

            fecha.setText(actual.getFecha());
            horaSalida.setText(actual.getHoraSalida());
            horaEntrega.setText(actual.getHoraEntrega());
            nombreMaestro.setText(actual.getNombreMaestro());
        }
    }

    // Muestra el registro anterior
    public void anterior(){
        if (info.lista.size() > 0) {
            if (posicion == 0) {
                posicion = info.lista.size() - 1;
            } else {
                posicion--;
            }
            mostrar();
        }
    }

    // Muestra el registro siguiente
    public void siguiente(){
        if (info.lista.size() > 0) {
            if (posicion == info.lista.size() - 1) {
                posicion = 0;
            } else {
                posicion++;
            }
            mostrar();
        }
    }

    // Guarda los cambios modificados del registro actual
    public void guardarCambios() {
        if (info.lista == null || info.lista.size() == 0 || posicion >= info.lista.size()) {
            Toast.makeText(this, "No hay datos para modificar", Toast.LENGTH_SHORT).show();
            return;
        }

        datos actual = info.lista.get(posicion);

        actual.setNombreAlumno(nombreAlumno.getText().toString());
        actual.setApPat(apPat.getText().toString());
        actual.setApMat(apMat.getText().toString());
        actual.setTelefono(telefono.getText().toString());
        actual.setHerramienta(herramientas.getSelectedItem().toString());
        actual.setFecha(fecha.getText().toString());
        actual.setHoraSalida(horaSalida.getText().toString());
        actual.setHoraEntrega(horaEntrega.getText().toString());
        actual.setNombreMaestro(nombreMaestro.getText().toString());

        // Crea petición POST para enviar datos modificados
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.100.7/bd/modificar.php";

        // Empaqueta los datos en JSON
        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("id", actual.getId());
            jsonObject.put("nombreAlumno", actual.getNombreAlumno());
            jsonObject.put("apePat", actual.getApPat());
            jsonObject.put("apeMat", actual.getApMat());
            jsonObject.put("telefono", actual.getTelefono());
            jsonObject.put("herramienta", actual.getHerramienta());
            jsonObject.put("fecha", actual.getFecha());
            jsonObject.put("horaSalida", actual.getHoraSalida());
            jsonObject.put("horaEntrega", actual.getHoraEntrega());
            jsonObject.put("nombreMaestro", actual.getNombreMaestro());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al crear JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        // Envia la petición al servidor
        com.android.volley.toolbox.JsonObjectRequest request = new com.android.volley.toolbox.JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                response -> Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error de conexión con servidor", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    // Selector de fecha
    private void datePicker() {
        Calendar actual = Calendar.getInstance();
        int dia = actual.get(Calendar.DAY_OF_MONTH);
        int mes = actual.get(Calendar.MONTH);
        int anio = actual.get(Calendar.YEAR);

        DatePickerDialog datPD = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            String cadena = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            fecha.setText(cadena);
        }, anio, mes, dia);

        datPD.show();
    }

    // Selector de hora de salida
    private void timePickerSalida() {
        Calendar actual = Calendar.getInstance();
        int hr = actual.get(Calendar.HOUR_OF_DAY);
        int min = actual.get(Calendar.MINUTE);

        TimePickerDialog timePD = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            horaSalida.setText(String.format("%02d:%02d", hourOfDay, minute));
        }, hr, min, true);
        timePD.show();
    }

    // Selector de hora de entrega
    private void timePickerEntrega() {
        Calendar actual = Calendar.getInstance();
        int hr = actual.get(Calendar.HOUR_OF_DAY);
        int min = actual.get(Calendar.MINUTE);

        TimePickerDialog timePD = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            horaEntrega.setText(String.format("%02d:%02d", hourOfDay, minute));
        }, hr, min, true);
        timePD.show();
    }
}
