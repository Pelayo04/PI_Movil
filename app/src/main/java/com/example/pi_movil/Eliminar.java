package com.example.pi_movil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import adaptadorEliminar.adaptadorEliminar;


public class Eliminar extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv_eliminar; // Lista visual que muestra los elementos a eliminar
    Button but_eliminar;

    SharedPreferences archivo;  // Preferencias compartidas (para sesión de usuario)

    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eliminar);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        archivo = this.getSharedPreferences("sesion", MODE_PRIVATE); // Sesión de usuario

        rv_eliminar = (RecyclerView)findViewById(R.id.rv_eliminar); // Lista visual (RecyclerView)
        adaptadorEliminar eliminar = new adaptadorEliminar();
        eliminar.context = this;

        // Se define el tipo de layout vertical para la lista
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rv_eliminar.setLayoutManager(linearLayoutManager); //Asignación del layout

        rv_eliminar.setAdapter(eliminar); //Conectar el adaptador con la lista

        but_eliminar = (Button)findViewById(R.id.toolbar);
        but_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
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
            Intent modificar = new Intent(this, Modificar.class);
            startActivity(modificar);
        }
        if (item.getItemId() == R.id.opc4) {
            Toast.makeText(this, "Ya estás en Eliminar", Toast.LENGTH_SHORT).show();
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

    private void eliminar(){

    }
}