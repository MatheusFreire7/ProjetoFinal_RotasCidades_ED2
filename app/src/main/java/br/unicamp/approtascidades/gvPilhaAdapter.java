package br.unicamp.approtascidades;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.unicamp.approtascidades.Grafo.CaminhoCidade;
import br.unicamp.approtascidades.Solucao.PilhaLista;

public class gvPilhaAdapter extends BaseAdapter
{
    private PilhaLista<CaminhoCidade> listaPilhaListaCaminho;
    Context context;


    public gvPilhaAdapter(Context context, PilhaLista<CaminhoCidade> ListaPilhaListaCaminho)
    {
        this.context = context;
        this.listaPilhaListaCaminho = ListaPilhaListaCaminho;
    }

    @Override
    public int getCount() {
        return  listaPilhaListaCaminho.getTamanho();
    }

    @Override
    public Object getItem(int position) {
      return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.caminho_item,parent,false);
        }

        TextView txtCaminho = convertView.findViewById(R.id.tvCaminho);
        Log.d("pilha", listaPilhaListaCaminho.getTamanho()+ "");

        CaminhoCidade caminhoCidade = null;
        try {
            caminhoCidade = new CaminhoCidade(listaPilhaListaCaminho.OTopo().getIdOrigem(), listaPilhaListaCaminho.OTopo().getDistancia());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Caminho", caminhoCidade.toString());
        txtCaminho.setText("Id: " + caminhoCidade.getIdOrigem() + " Distancia: " + caminhoCidade.getDistancia());

        return convertView;
    }
}
