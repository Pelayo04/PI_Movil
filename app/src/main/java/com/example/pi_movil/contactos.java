package com.example.pi_movil;

import android.content.Intent;
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

public class contactos extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contactos);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opc1) {
            // Si selecciona la opción "Ver", abre la actividad correspondiente
            Intent principal = new Intent(this, MainActivity.class);
            startActivity(principal);
        }
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
            // Si ya está en la pantalla principal, muestra mensaje
            Toast.makeText(this, "Ya estás en Contactos", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}