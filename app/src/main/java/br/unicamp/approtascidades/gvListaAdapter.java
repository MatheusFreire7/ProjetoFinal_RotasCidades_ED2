//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import br.unicamp.approtascidades.Grafo.CaminhoCidade;

public class gvListaAdapter extends ArrayAdapter<CaminhoCidade> {
    public gvListaAdapter(@NonNull Context context, ArrayList<CaminhoCidade> listCaminhoCidade) {
        super(context,0 ,listCaminhoCidade);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.caminho_item, parent, false);
        }
        CaminhoCidade caminhoCidade = getItem(position);
        TextView txtCaminho = listitemView.findViewById(R.id.tvCaminho);
        txtCaminho.setText(caminhoCidade.getIdDestino());
        return listitemView;
    }
}
