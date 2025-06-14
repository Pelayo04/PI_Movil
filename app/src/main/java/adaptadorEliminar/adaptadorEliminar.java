package adaptadorEliminar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_movil.R;

import Global.info; // Clase que contiene listas globales como lista y listaEliminar

// Adaptador personalizado para manejar la lista con elementos seleccionables para eliminar
public class adaptadorEliminar extends RecyclerView.Adapter<adaptadorEliminar.EliminarViewHolder> {

    @NonNull
    public Context context; // Contexto para inflar la vista y acceder a recursos

    // Método que se ejecuta cuando se necesita crear un nuevo ViewHolder (una nueva fila visual)
    @Override
    public adaptadorEliminar.EliminarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Infla el diseño XML correspondiente a cada fila (viewholder_eliminar.xml)
        View view = View.inflate(context, R.layout.viewholder_eliminar, null);

        // Crea una instancia del ViewHolder y la devuelve
        EliminarViewHolder eliminarViewHolder = new EliminarViewHolder(view);
        return eliminarViewHolder;
    }

    // Método que llena (bind) cada elemento de la lista con sus respectivos datos
    @Override
    public void onBindViewHolder(@NonNull adaptadorEliminar.EliminarViewHolder eliminarViewHolder, int i) {
        final int pos = i; // Guarda la posición actual

        // Establece el nombre del alumno en el TextView
        eliminarViewHolder.nombreDuenio.setText(info.lista.get(i).getNombreAlumno());

        // Inicialmente el CheckBox aparece desmarcado
        eliminarViewHolder.checkBox.setChecked(false);

        // Listener que detecta cuando el usuario marca o desmarca el CheckBox
        eliminarViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                // Si se marca el CheckBox, el elemento se agrega a la lista de eliminación
                if(checkBox.isChecked()){
                    info.listaEliminar.add(info.lista.get(pos));
                }else{
                    // Si se desmarca, se elimina de la lista de eliminación
                    info.listaEliminar.remove(info.lista.get(pos));
                }
            }
        });
    }

    // Devuelve la cantidad total de elementos a mostrar en el RecyclerView
    @Override
    public int getItemCount() {
        return info.lista.size();
    }

    // Clase interna que representa la estructura visual de cada elemento (fila) de la lista
    public class EliminarViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;         // Casilla para seleccionar si se elimina
        TextView nombreDuenio;     // Texto que muestra el nombre del alumno

        public EliminarViewHolder(@NonNull View itemView) {
            super(itemView);

            // Se enlazan los elementos visuales con sus respectivos IDs del XML
            checkBox = itemView.findViewById(R.id.check_eliminar);
            nombreDuenio = itemView.findViewById(R.id.txt_3);
        }
    }
}
