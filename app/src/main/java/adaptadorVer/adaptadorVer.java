package adaptadorVer;

// Importación de clases necesarias

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_movil.R;
import com.example.pi_movil.cardView;
import com.example.pi_movil.R;
import com.example.pi_movil.cardView;

import Global.info;


// Adaptador personalizado para mostrar los datos en un RecyclerView
public class adaptadorVer extends RecyclerView.Adapter<adaptadorVer.MiniActivity> {

    @NonNull
    public Context context; // Contexto de la aplicación, necesario para iniciar actividades

    // Método que infla la vista de cada elemento del RecyclerView
    @Override
    public adaptadorVer.MiniActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout de un ítem (mi_vista.xml) y lo convierte en vista usable
        View view = View.inflate(context, R.layout.mi_vista, null);

        // Crea una nueva instancia del ViewHolder con la vista inflada
        MiniActivity miniActivity = new MiniActivity(view);
        return miniActivity;
    }

    // Método que enlaza los datos con la vista para cada posición del RecyclerView
    @Override
    public void onBindViewHolder(@NonNull adaptadorVer.MiniActivity miniActivity, int i) {
        final int pos = i; // Se guarda la posición actual

        // Se asigna el nombre y apellido del dueño desde la lista global
        miniActivity.nombre.setText(info.lista.get(i).getNombreAlumno());
        miniActivity.ap_pat.setText(info.lista.get(i).getApPat());

        // Unico listener por si se presiona nombre o apellido
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llama a la actividad y pasa la posición del elemento
                Intent llamar = new Intent(context, cardView.class);
                llamar.putExtra("nombreDuenio", pos);
                context.startActivity(llamar);
            }
        };

        // Asigna el mismo listener a ambos campos
        miniActivity.nombre.setOnClickListener(listener);
        miniActivity.ap_pat.setOnClickListener(listener);
    }

    // Método que devuelve la cantidad de elementos en la lista (cuántos se deben mostrar)
    @Override
    public int getItemCount() {
        return info.lista.size(); // Se basa en el tamaño de la lista global
    }

    // Clase interna que representa la vista de cada ítem del RecyclerView
    public class MiniActivity extends RecyclerView.ViewHolder {

        TextView nombre, ap_pat; // Elementos de texto que se mostrarán en cada tarjeta

        public MiniActivity(@NonNull View itemView) {
            super(itemView);

            // Se vinculan los TextView con los IDs definidos en mi_vista.xml
            nombre = itemView.findViewById(R.id.txt_1);
            ap_pat = itemView.findViewById(R.id.txt_2);
        }
    }
}
