//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import br.unicamp.approtascidades.Grafo.CaminhoCidade;
import br.unicamp.approtascidades.Grafo.Cidade;

public class gvListaAdapter extends ArrayAdapter<CaminhoCidade> {
    List<Cidade> listCidade = null;

    public gvListaAdapter(@NonNull Context context, List<CaminhoCidade> listCaminhoCidade, List<Cidade> ListCidade) {
        super(context,0 ,listCaminhoCidade);
        this.listCidade = ListCidade;

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
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
        if(buscarNome(caminhoCidade.getIdDestino()) != " ")
        {
            txtCaminho.setText(buscarNome(caminhoCidade.getIdDestino()));
        }

        return listitemView;
    }

    public String buscarNome(String idDestino)
    {
        String nomeCidade = " ";
        for(int i = 0; i < listCidade.size(); i++)
        {
            if(listCidade.get(i).getIdCidade() == Integer.parseInt(idDestino))
            {
                nomeCidade = listCidade.get(i).getNomeCidade();
            }
        }
        return nomeCidade;
    }



}
