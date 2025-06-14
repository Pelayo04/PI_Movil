package com.example.pi_movil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        // Ajusta el padding del layout para evitar que se sobreponga con la barra de estado o navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}