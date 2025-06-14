package com.example.pi_movil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Inicio extends AppCompatActivity {

    EditText usuario, contrasenia;
    Button ingresar;
    // Preferencias compartidas para mantener la sesion del usuario
    SharedPreferences archivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);

        usuario = (EditText)findViewById(R.id.ed_user);
        contrasenia = (EditText)findViewById(R.id.ed_password);
        // Inicializa las SharedPreferences con el nombre "sesion"
        archivo = this.getSharedPreferences("sesion", MODE_PRIVATE);

        ingresar = (Button)findViewById(R.id.button_ingresar);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicioSesion(); // Llama al metodo para iniciar sesion
            }
        });

        // Si ya hay una sesion activa se redirige directamente a MainActivity
        if(archivo.contains("usuario")){
            Intent ini = new Intent(this, MainActivity.class);
            startActivity(ini);
            finish();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Login
    public void inicioSesion() {
        // URL con parámetros GET para verificar usuario en el servidor
        String url = "http://192.168.1.72/bd/ingreso.php?usr=" +
                usuario.getText().toString() +
                "&pass=" +
                contrasenia.getText().toString();
        Log.d("URL", url);
        // Se crea una petición JSON (con Volley) al servidor con la URL construida
        JsonObjectRequest pet = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Si el ID de usuario recibido es distinto de -1, el login fue exitoso
                    if (response.getInt("usr") != -1) {
                        Intent i = new Intent(Inicio.this, MainActivity.class);
                        // Se guarda el ID de usuario en SharedPreferences para mantener la sesión
                        SharedPreferences.Editor editor = archivo.edit();
                        editor.putInt("id_usuario", response.getInt("usr"));
                        editor.apply();
                        startActivity(i);
                        finish();
                    } else {
                        // Si es -1 el usuario o la contraseña no coinciden y se limpian los campos
                        usuario.setText("");
                        contrasenia.setText("");
                        Toast.makeText(Inicio.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // Error en el formato del JSON recibido
                    Toast.makeText(Inicio.this, "Error en respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            // Si la peticion falla
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(Inicio.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Se lanza la petición agregandola a la cola de Volley
        RequestQueue lanzarPeticion = Volley.newRequestQueue(this);
        lanzarPeticion.add(pet);
    }

}