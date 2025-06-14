package com.example.pi_movil;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Inicio extends AppCompatActivity {

    EditText usuario, contrasenia;
    Button ingresar;
    SharedPreferences archivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);

        usuario = findViewById(R.id.ed_user);
        contrasenia = findViewById(R.id.ed_password);
        archivo = this.getSharedPreferences("sesion", MODE_PRIVATE);

        ingresar = findViewById(R.id.button_ingresar);
        ingresar.setOnClickListener(view -> inicioSesion());

        if (archivo.contains("id_usuario")) {
            String tipo = archivo.getString("tipo_usuario", "mortal"); // Valor por defecto

            Intent ini;
            if (tipo.equals("admin")) {
                ini = new Intent(this, MainActivity.class);
            } else {
                ini = new Intent(this, ver.class);
            }
            startActivity(ini);
            finish();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void inicioSesion() {
        String url = "http://192.168.100.7/bd/ingreso.php?usr=" +
                usuario.getText().toString() +
                "&pass=" +
                contrasenia.getText().toString();
        Log.d("URL", url);

        JsonObjectRequest pet = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getInt("usr") != -1) {
                            String tipo = response.getString("tipo");

                            SharedPreferences.Editor editor = archivo.edit();
                            editor.putInt("id_usuario", response.getInt("usr"));
                            editor.putString("tipo_usuario", tipo); // ← Usa putString aquí
                            editor.apply();

                            Intent i;
                            if (tipo.equals("admin")) {
                                i = new Intent(Inicio.this, MainActivity.class);
                            } else {
                                i = new Intent(Inicio.this, ver.class);
                            }

                            startActivity(i);
                            finish();
                        } else {
                            usuario.setText("");
                            contrasenia.setText("");
                            Toast.makeText(Inicio.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Inicio.this, "Error en respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VolleyError", error.toString());
                    Toast.makeText(Inicio.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        RequestQueue lanzarPeticion = Volley.newRequestQueue(this);
        lanzarPeticion.add(pet);
    }
}
