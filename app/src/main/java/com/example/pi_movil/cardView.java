package com.example.pi_movil;

// Importaciones necesarias para funcionalidades de Android
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Global.info; // Importa la clase que contiene la lista global de datos

public class cardView extends AppCompatActivity {

    // Declaración de los elementos de la interfaz (TextViews para mostrar los datos, botón para llamar)
    TextView nombreAlumno2, apPat2, apMat2, telefono2, herramienta2, fecha2, horaSalida2, horaEntrega2, nombreMaestro2;
    Integer pos; // Variable para almacenar la posición del elemento seleccionado
    Button llamar; // Botón para iniciar la llamada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita la visualización sin bordes para una mejor experiencia en pantalla completa
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card_view); // Asocia la actividad con el layout XML correspondiente

        // Enlaza cada TextView con su ID en el XML
        nombreAlumno2 = (TextView)findViewById(R.id.nombreAlumno2);
        apPat2 = (TextView)findViewById(R.id.apPat2);
        apMat2 = (TextView)findViewById(R.id.apMat2);
        telefono2 = (TextView)findViewById(R.id.telefono2);
        herramienta2 = (TextView)findViewById(R.id.herramienta2);
        fecha2 = (TextView)findViewById(R.id.fecha2);
        horaSalida2 = (TextView)findViewById(R.id.et_horaSalida2);
        horaEntrega2 = (TextView)findViewById(R.id.et_horaEntrega2);
        nombreMaestro2 = (TextView)findViewById(R.id.et_nombreMaestro2);

        // Asigna el botón "llamar" y su comportamiento
        llamar = (Button)findViewById(R.id.button_llamar);
        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea un Intent con la acción de hacer una llamada
                Intent llamada = new Intent(Intent.ACTION_CALL);
                // Define el número de teléfono a llamar
                llamada.setData(Uri.parse("tel:" + telefono2.getText().toString()));

                // Verifica si se tiene permiso para realizar llamadas
                if(ActivityCompat.checkSelfPermission(cardView.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    // Si no se tiene, solicita el permiso al usuario
                    ActivityCompat.requestPermissions(cardView.this, new String[]{Manifest.permission.CALL_PHONE}, 10);
                    return; // Sale del método para esperar la respuesta del usuario
                }

                // Si ya se tiene el permiso, se realiza la llamada
                startActivity(llamada);
            }
        });

        // Recupera la posición enviada desde el adaptador (RecyclerView)
        pos = getIntent().getIntExtra("nombreAlumno", 0);

        // Obtiene los datos correspondientes a esa posición desde la lista global
        nombreAlumno2.setText(info.lista.get(pos).getNombreAlumno());
        apPat2.setText(info.lista.get(pos).getApPat());
        apMat2.setText(info.lista.get(pos).getApMat());
        telefono2.setText(info.lista.get(pos).getTelefono());
        herramienta2.setText(info.lista.get(pos).getHerramienta());
        fecha2.setText(info.lista.get(pos).getFecha());
        horaSalida2.setText(info.lista.get(pos).getHoraSalida());
        horaEntrega2.setText(info.lista.get(pos).getHoraEntrega());
        nombreMaestro2.setText(info.lista.get(pos).getNombreMaestro());

        // Ajusta los márgenes automáticamente para evitar que los componentes se oculten bajo las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
