package com.example.pi_movil;

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

import Global.info;
import POJO.datos;

public class Modificar extends AppCompatActivity {

    Toolbar toolbar;
    EditText nombreAlumno, apPat, apMat, telefono, fecha, horaSalida, horaEntrega, nombreMaestro;
    Button but_anterior, but_guardarCambios, but_siguiente;
    SharedPreferences archivo;
    Spinner herramientas;

    datos persona = new datos();
    int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        archivo = this.getSharedPreferences("sesion", MODE_PRIVATE);

        nombreAlumno = findViewById(R.id.et_nombreAlumno);
        apPat = findViewById(R.id.et_apPat);
        apMat = findViewById(R.id.et_apMat);
        telefono = findViewById(R.id.et_telefono);
        herramientas = (Spinner)findViewById(R.id.spinner_herramientas);
        String [] opciones = {"Multimetro", "Cautin", "Osciloscopio", "Generador de funciones",
                "Punta bnc", "Punta atenuadora", "Fuente de poder", "Punta lógica"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        herramientas.setAdapter(adapter);
        fecha = findViewById(R.id.et_fecha);
        horaSalida = findViewById(R.id.et_horaSalida);
        horaEntrega = findViewById(R.id.et_horaEntrega);
        nombreMaestro = findViewById(R.id.et_nombreMaestro);

        posicion = 0;
        if (info.lista.size() > 0) {
            mostrar();
        } else {
            Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_SHORT).show();
        }

        but_anterior = findViewById(R.id.button_ant);
        but_anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anterior();
            }
        });

        but_guardarCambios = findViewById(R.id.button_gc);
        but_guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarCambios();

            }
        });

        but_siguiente = findViewById(R.id.button_sig);
        but_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguiente();
            }
        });

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        horaSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerSalida();
            }
        });

        horaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerEntrega();
            }
        });

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

    @Override
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
            Toast.makeText(this, "Ya estás en Modificar", Toast.LENGTH_SHORT).show();
        }
        /*if (item.getItemId() == R.id.opc4) {
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

    public void mostrar() {
        if (info.lista.size() > 0) {
            datos actual = info.lista.get(posicion);
            nombreAlumno.setText(actual.getNombreAlumno());
            apPat.setText(actual.getApPat());
            apMat.setText(actual.getApMat());
            telefono.setText(actual.getTelefono());
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) herramientas.getAdapter();
            int index = adapter.getPosition(actual.getHerramienta());
            if (index >= 0) {
                herramientas.setSelection(index);
            }
            fecha.setText(actual.getFecha());
            horaSalida.setText(actual.getHoraSalida());
            horaEntrega.setText(actual.getHoraEntrega());
            nombreMaestro.setText(actual.getNombreMaestro());
        }
    }

    public void anterior(){
        if (info.lista.size() > 0) {
            if (posicion == 0) {
                posicion = info.lista.size() - 1; // Lista circular
            } else {
                posicion--;
            }
            mostrar();
        }
    }

    public void siguiente(){
        if (info.lista.size() > 0) {
            if (posicion == info.lista.size() - 1) {
                posicion = 0; // Lista circular
            } else {
                posicion++;
            }
            mostrar();
        }
    }

    public void guardarCambios() {
        // CORRECTA validación de lista y posición
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

        // Enviar a la base de datos
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.72/bd/modificar.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, response, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error al actualizar: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(actual.getId()));
                params.put("nombreAlumno", actual.getNombreAlumno());
                params.put("apePat", actual.getApPat());
                params.put("apeMat", actual.getApMat());
                params.put("telefono", actual.getTelefono());
                params.put("herramienta", actual.getHerramienta());
                params.put("fecha", actual.getFecha());
                params.put("horaSalida", actual.getHoraSalida());
                params.put("horaEntrega", actual.getHoraEntrega());
                params.put("nombreMaestro", actual.getNombreMaestro());
                return params;
            }
        };
        queue.add(request);
    }


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

    private void timePickerSalida() {
        Calendar actual = Calendar.getInstance();
        int hr = actual.get(Calendar.HOUR_OF_DAY);
        int min = actual.get(Calendar.MINUTE);

        TimePickerDialog timePD = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            horaSalida.setText(String.format("%02d:%02d", hourOfDay, minute));
        }, hr, min, true);
        timePD.show();
    }

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
