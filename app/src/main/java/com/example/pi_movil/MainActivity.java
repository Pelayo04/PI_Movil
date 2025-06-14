package com.example.pi_movil;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText et_nombreAlumno, et_apPat, et_apMat, et_telefono, et_horaSalida, et_horaEntrega, et_nombreMaestro;
    private Button guardar, limpiar;
    private Spinner herramientas;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        et_nombreAlumno = (EditText)findViewById(R.id.et_nombreAlumno);
        et_apPat = (EditText)findViewById(R.id.et_apPat);
        et_apMat = (EditText)findViewById(R.id.et_apMat);
        et_telefono = (EditText)findViewById(R.id.et_telefono);
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

}