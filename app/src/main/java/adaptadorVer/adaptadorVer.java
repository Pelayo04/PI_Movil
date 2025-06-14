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

        // Se asigna el nombre y apellido del alumno desde la lista global
        miniActivity.nombre.setText(info.lista.get(i).getNombreAlumno());
        miniActivity.ap_pat.setText(info.lista.get(i).getApPat());

        // Listener que responde al clic sobre cualquiera de los campos de texto
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Se lanza una nueva actividad (cardView) pasando como dato la posición del elemento
                Intent llamar = new Intent(context, cardView.class);
                llamar.putExtra("nombreAlumno", pos); // Se envía la posición como extra
                context.startActivity(llamar); // Se inicia la nueva actividad
            }
        };

        // Se asigna el mismo listener tanto al TextView del nombre como al del apellido paterno
        miniActivity.nombre.setOnClickListener(listener);
        miniActivity.ap_pat.setOnClickListener(listener);
    }

    // Método que devuelve el número de elementos que se van a mostrar en la lista
    @Override
    public int getItemCount() {
        return info.lista.size(); // El tamaño depende de la lista global info.lista
    }

    // Clase interna que define la estructura de cada tarjeta o fila del RecyclerView
    public class MiniActivity extends RecyclerView.ViewHolder {

        TextView nombre, ap_pat; // Elementos visuales para mostrar nombre y apellido paterno

        public MiniActivity(@NonNull View itemView) {
            super(itemView);

            // Se enlazan los elementos visuales con sus respectivos IDs del XML mi_vista.xml
            nombre = itemView.findViewById(R.id.txt_1);
            ap_pat = itemView.findViewById(R.id.txt_2);
        }
    }
}
