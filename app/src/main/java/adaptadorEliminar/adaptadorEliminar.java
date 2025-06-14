package adaptadorEliminar;

import static java.util.logging.Logger.global;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_movil.R;

import Global.info;


// Adaptador personalizado para manejar la lista con CheckBoxes
public class adaptadorEliminar extends RecyclerView.Adapter<adaptadorEliminar.EliminarViewHolder> {

    @NonNull
    public Context context;

    //Crea el ViewHolder que representa cada fila en la lista
    @Override
    public adaptadorEliminar.EliminarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Infla el diseño XML que representa una fila (viewholder_eliminar.xml)
        View view = View.inflate(context, R.layout.viewholder_eliminar, null);

        EliminarViewHolder eliminarViewHolder = new EliminarViewHolder(view);
        return eliminarViewHolder;
    }

    // Llena cada elemento de la lista con datos
    @Override
    public void onBindViewHolder(@NonNull adaptadorEliminar.EliminarViewHolder eliminarViewHolder, int i) {
        final int pos = i;

        // Se muestra el nombre del dueño en el TextView
        eliminarViewHolder.nombreDuenio.setText(info.lista.get(i).getNombreAlumno());
        // Al inicio el CheckBox no está seleccionado
        eliminarViewHolder.checkBox.setChecked(false);

        // Evento cuando se marca o desmarca el CheckBox
        eliminarViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckBox checkBox = (CheckBox) view;

                // Si se selecciona, se agrega a la lista temporal para eliminar
                if(checkBox.isChecked()){
                    info.listaEliminar.add(info.lista.get(pos));
                }else{
                    // Si se desmarca, se quita de la lista de eliminación
                    info.listaEliminar.remove(info.lista.get(pos));
                }

            }
        });
    }

    //Devuelve el número total de elementos que se mostrarán
    @Override
    public int getItemCount() {
        return info.lista.size();
    }

    // Clase interna que representa la vista de cada fila en la lista
    public class EliminarViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;  // Casilla para seleccionar si se elimina o no
        TextView nombreDuenio; // Muestra el nombre del dueño
        public EliminarViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_eliminar); //ID en el XML
            nombreDuenio = itemView.findViewById(R.id.txt_3); //ID en el XML


        }
    }
}
