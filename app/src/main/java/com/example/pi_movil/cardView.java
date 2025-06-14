package com.example.pi_movil;

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

import Global.info;

public class cardView extends AppCompatActivity {

    // Declaración de los elementos de la interfaz (TextViews para mostrar los datos, botón para llamar)
    TextView nombreAlumno2, apPat2, apMat2, telefono2, herramienta2, fecha2, horaSalida2, horaEntrega2, nombreMaestro2;
    Integer pos; // Variable para almacenar la posición del elemento seleccionado
    Button llamar; // Botón para iniciar la llamada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Permite que la actividad use toda la pantalla, incluso debajo de la barra de estado
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card_view); // Asocia esta clase con el layout activity_card_view.xml

        // Enlaza cada TextView con su correspondiente ID en el XML
        nombreAlumno2 = (TextView)findViewById(R.id.nombreAlumno2);
        apPat2 = (TextView)findViewById(R.id.apPat2);
        apMat2 = (TextView)findViewById(R.id.apMat2);
        telefono2 = (TextView)findViewById(R.id.telefono2);
        herramienta2 = (TextView)findViewById(R.id.herramienta2);
        fecha2 = (TextView)findViewById(R.id.fecha2);
        horaSalida2 = (TextView)findViewById(R.id.et_horaSalida2);
        horaEntrega2 = (TextView)findViewById(R.id.et_horaEntrega2);
        nombreMaestro2 = (TextView)findViewById(R.id.et_nombreMaestro2);

        // Asocia el botón con su ID y define su comportamiento al hacer clic
        llamar = (Button)findViewById(R.id.button_llamar);
        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Se crea un Intent con la acción de llamada telefónica
                Intent llamada = new Intent(Intent.ACTION_CALL);

                // Se establece el número de teléfono a marcar, obtenido del TextView
                llamada.setData(Uri.parse("tel:" + telefono2.getText().toString()));

                // Verifica si el permiso para llamar está concedido
                if(ActivityCompat.checkSelfPermission(cardView.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    // Si no se tiene permiso, se solicita al usuario
                    ActivityCompat.requestPermissions(cardView.this, new String[]{Manifest.permission.CALL_PHONE}, 10);
                    return; // Detiene la ejecución para esperar la respuesta del usuario
                }

                // Si el permiso está concedido, inicia la llamada
                startActivity(llamada);
            }
        });

        // Recupera la posición del elemento seleccionado que se pasó desde el adaptador
        pos = getIntent().getIntExtra("nombreAlumno", 0);

        // Usa la posición para obtener los datos del dueño desde la lista global y mostrarlos en pantalla
        nombreAlumno2.setText(info.lista.get(pos).getNombreAlumno());
        apPat2.setText(info.lista.get(pos).getApPat());
        apMat2.setText(info.lista.get(pos).getApMat());
        telefono2.setText(info.lista.get(pos).getTelefono());
        herramienta2.setText(info.lista.get(pos).getHerramienta());
        fecha2.setText(info.lista.get(pos).getFecha());
        horaSalida2.setText(info.lista.get(pos).getHoraSalida());
        horaEntrega2.setText(info.lista.get(pos).getHoraEntrega());
        nombreMaestro2.setText(info.lista.get(pos).getNombreMaestro());


        // Ajusta los márgenes para evitar que la vista se superponga con las barras del sistema (como la de navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}