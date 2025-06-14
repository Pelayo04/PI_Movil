package com.example.pi_movil;

// Importación de clases necesarias de Android
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

// Importación de clases de la librería Volley para peticiones HTTP
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

// Importación para trabajar con objetos JSON
import org.json.JSONException;
import org.json.JSONObject;

public class Inicio extends AppCompatActivity {

    // Declaración de elementos de interfaz y preferencias compartidas
    EditText usuario, contrasenia;
    Button ingresar;
    SharedPreferences archivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilita el modo de diseño Edge-to-Edge
        setContentView(R.layout.activity_inicio); // Establece el layout de la actividad

        // Referencias a los elementos visuales del layout
        usuario = findViewById(R.id.ed_user);
        contrasenia = findViewById(R.id.ed_password);
        archivo = this.getSharedPreferences("sesion", MODE_PRIVATE); // Accede al archivo de sesión

        // Configura el botón para iniciar sesión al hacer clic
        ingresar = findViewById(R.id.button_ingresar);
        ingresar.setOnClickListener(view -> inicioSesion());

        // Si ya hay una sesión iniciada, redirige al usuario correspondiente
        if (archivo.contains("id_usuario")) {
            String tipo = archivo.getString("tipo_usuario", "mortal"); // Recupera tipo de usuario

            Intent ini;
            if (tipo.equals("admin")) {
                ini = new Intent(this, MainActivity.class); // Admin va a MainActivity
            } else {
                ini = new Intent(this, ver.class); // Usuario normal va a ver
            }
            startActivity(ini); // Inicia la nueva actividad
            finish(); // Finaliza la actual
        }

        // Ajusta márgenes del contenido para evitar solaparse con barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método que realiza el inicio de sesión
    public void inicioSesion() {
        // Construye la URL con parámetros GET para enviar usuario y contraseña
        String url = "http://192.168.100.7/bd/ingreso.php?usr=" +
                usuario.getText().toString() +
                "&pass=" +
                contrasenia.getText().toString();
        Log.d("URL", url); // Imprime la URL para depuración

        // Crea una petición JSON al servidor usando Volley
        JsonObjectRequest pet = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Verifica si la respuesta contiene un usuario válido
                        if (response.getInt("usr") != -1) {
                            String tipo = response.getString("tipo"); // Obtiene el tipo de usuario

                            // Guarda el ID y tipo de usuario en SharedPreferences
                            SharedPreferences.Editor editor = archivo.edit();
                            editor.putInt("id_usuario", response.getInt("usr"));
                            editor.putString("tipo_usuario", tipo); // Se guarda como string
                            editor.apply(); // Aplica los cambios

                            // Redirige según el tipo
                            Intent i;
                            if (tipo.equals("admin")) {
                                i = new Intent(Inicio.this, MainActivity.class); // Admin
                            } else {
                                i = new Intent(Inicio.this, ver.class); // Usuario normal
                            }

                            startActivity(i); // Lanza la nueva actividad
                            finish(); // Termina esta actividad
                        } else {
                            // Si el login falla, limpia los campos y muestra mensaje
                            usuario.setText("");
                            contrasenia.setText("");
                            Toast.makeText(Inicio.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // Si ocurre error en la respuesta del servidor
                        Toast.makeText(Inicio.this, "Error en respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Manejo de error de conexión
                    Log.e("VolleyError", error.toString());
                    Toast.makeText(Inicio.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Encola la petición en la cola de Volley
        RequestQueue lanzarPeticion = Volley.newRequestQueue(this);
        lanzarPeticion.add(pet);
    }
}
