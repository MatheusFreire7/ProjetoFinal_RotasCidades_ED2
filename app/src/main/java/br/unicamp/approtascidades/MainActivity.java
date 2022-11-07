package br.unicamp.approtascidades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import br.unicamp.approtascidades.Grafo.CaminhoCidade;
import br.unicamp.approtascidades.Grafo.Cidade;
import br.unicamp.approtascidades.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    Cidade[] vetorCidade;
    CaminhoCidade[] caminhoCidades;
    ActivityMainBinding binding;

    public Cidade[] getVetorCidade() {
        return vetorCidade;
    }

    public void setVetorCidade(Cidade[] vetorCidade) {
        this.vetorCidade = vetorCidade;
    }

    public CaminhoCidade[] getCaminhoCidades() {
        return caminhoCidades;
    }

    public void setCaminhoCidades(CaminhoCidade[] caminhoCidades) {
        this.caminhoCidades = caminhoCidades;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding.btnLer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    lerArquivoCidade();
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void lerArquivoCidade() throws IOException {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("CidadesMarte.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine())!= null){
                linhas.add(linha);
                JSONObject objCidade = new JSONObject(linha);
                binding.txtJson.setText(objCidade.toString());
            }
            inputStream.close();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void lerArquivoCaminho() throws IOException {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("Caminho.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine())!= null){
                linhas.add(linha);
                JSONObject objCaminho = new JSONObject(linha);
                binding.txtJson.setText(objCaminho.toString());
            }
            inputStream.close();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}