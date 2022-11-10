package br.unicamp.approtascidades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.unicamp.approtascidades.Backtracking.GrafoBactracking;
import br.unicamp.approtascidades.Grafo.CaminhoCidade;
import br.unicamp.approtascidades.Grafo.Cidade;
import br.unicamp.approtascidades.Grafo.Grafo;
import br.unicamp.approtascidades.Grafo.PilhaVetor;
import br.unicamp.approtascidades.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    //Cidade vetorCidade[];
    int matrizCaminho[][];
    GrafoBactracking grafo;
    List<Cidade> listaCidade = new ArrayList<Cidade>();
    List<CaminhoCidade> listaCaminhos = new ArrayList<CaminhoCidade>();
    ActivityMainBinding binding;
    int numCidades = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<CaminhoCidade> listaCaminho = new ArrayList<CaminhoCidade>();
        listaCaminho.add(new CaminhoCidade("1","2")); // teste do gvListaAdapter
        listaCaminho.add(new CaminhoCidade("1","3"));
        gvListaAdapter gvAdapter = new gvListaAdapter(this, listaCaminho);
        binding.gvLista.setAdapter(gvAdapter);

        ImageView mapa;
        mapa = findViewById(R.id.mapa);
        Picasso.get().load(R.drawable.mapa).into(mapa); //Carregamos a imagem do Mapa de marte com a biblioteca Picasso


        binding.btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean checkedRecursao = ((CheckBox) binding.chkRecursao).isChecked(); // vericamos se os checkedBox estão clicados
                    boolean checkedDijkstra = ((CheckBox) binding.chkDijkstra).isChecked();

                    if(checkedRecursao == true)
                    {
                        lerArquivoCidadeJson();
                        listaCompletaCidades();
                        lerArquivoCaminhoJson();
                       listaCompletacCaminhos();
                        ExibirMatrizAdjacencia();
//                        Spinner sp =	(Spinner)findViewById(R.id.numOrigem);
//                        String spinnerString = null;
//                        spinnerString = sp.getSelectedItem().toString();
//                        int nPos = sp.getSelectedItemPosition();
//
//
//                        Toast.makeText(getApplicationContext(), "getSelectedItem=" + spinnerString,
//                                Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), "getSelectedItemPosition=" + nPos,
//                                Toast.LENGTH_LONG).show();
                    }

                    if(checkedDijkstra == true)
                    {
                        lerArquivoCidadeJson();
                        listaCompletaCidades();
                        lerArquivoCaminhoJson();
                      listaCompletacCaminhos();
                    }

                    if(checkedDijkstra == false && checkedRecursao == false)
                        Toast.makeText(MainActivity.this, "Clique em um dos checkedBox para poder Buscar", Toast.LENGTH_LONG).show();


                } catch (IOException | NullPointerException e) {
                    Toast.makeText(MainActivity.this, "Erro na Leitura do Arquivo", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void lerArquivoCidadeJson() throws IOException {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("cidadeCorreto.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            String idCidade = "";
            int contCidades = 0;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine())!= null){
                linhas.add(linha);
               JSONObject objCidade = new JSONObject(linha);
               if(contCidades < 10)
               {
                    idCidade = " "+objCidade.getString("IdCidade");
               }
               if(contCidades >= 10)
               {
                   idCidade = objCidade.getString("IdCidade");
               }
               String nomeCidade = objCidade.getString("NomeCidade");
               String cordenadaX = objCidade.getString("CordenadaX");
               String cordenadaY = objCidade.getString("CordenadaY");
               String linhaNova = idCidade+nomeCidade+cordenadaX+cordenadaY;
                Cidade umCid = new Cidade(linhaNova);
                listaCidade.add(umCid);
                Log.d("Cidade", umCid.toString());
                contCidades++;
            }
            inputStream.close();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void lerArquivoCidadeTexto() throws IOException {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("CidadesMarte.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            int contCidades = 0;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine())!= null){
                linhas.add(linha);
                Cidade umCid = new Cidade(linha);
                listaCidade.add(umCid);
                Log.d("Cidade", umCid.toString());
                contCidades++;
            }
            inputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listaCompletaCidades(){
        if(listaCidade != null){
            for(int i = 0; i< listaCidade.size(); i++){
                System.out.println("Nome: "+listaCidade.get(i).getNomeCidade());
                System.out.println("Id: "+listaCidade.get(i).getIdCidade());
                System.out.println("X: "+listaCidade.get(i).getCordenadaX());
                System.out.println("Y: "+listaCidade.get(i).getCordenadaY());
            }

            //Carrega o nome das cidades que estão armazenadas em um vetor em um spinner
            Spinner spinner = (Spinner) findViewById(R.id.numOrigem);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,  R.array.cidades_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

            Spinner spinner2 = (Spinner) findViewById(R.id.numDestino);
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                    this, R.array.cidades_array, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener());

        }else{
            System.out.println("Lista vazia, necessário cadastrar cidades");
        }
    }

    public void listaCompletacCaminhos(){
        if(listaCaminhos != null){
            for(int i = 0; i< listaCaminhos.size(); i++){
                System.out.println("Origem: "+listaCaminhos.get(i).getIdOrigem());
                System.out.println("Destino: "+listaCaminhos.get(i).getIdDestino());
                System.out.println("Distancia: "+listaCaminhos.get(i).getDistancia());
                System.out.println("Tempo: "+listaCaminhos.get(i).getTempo());
                System.out.println("Custo: "+listaCaminhos.get(i).getCusto());
            }

        }else{
            System.out.println("Lista vazia, necessário cadastrar caminhos");
        }
    }

    public void lerArquivoCaminhoJson() throws IOException {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("caminhosCidades.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine())!= null){
                linhas.add(linha);
                JSONObject objCaminho = new JSONObject(linha);
                String idOrigem = objCaminho.getString("IdCidadeOrigem");
                String idDestino = objCaminho.getString("IdCidadeDestino");
                String distancia = objCaminho.getString("Distancia");
                String tempo = objCaminho.getString("Tempo");
                String custo = objCaminho.getString("Custo");


                if(idOrigem.length() < 2)
                {
                    while(idOrigem.length() < 2)
                    {
                        idOrigem = " " + idOrigem;
                    }
                }

                if(idDestino.length() < 2)
                {
                    while (idDestino.length() < 2)
                    {
                        idDestino = " " + idDestino ;
                    }
                }

                if(distancia.length() < 4)
                {
                    while (distancia.length() < 4)
                    {
                        distancia = 0 + distancia ;
                    }
                }

                if(tempo.length() < 2)
                {
                    while (tempo.length() < 2) {
                        tempo = 0 + tempo;
                    }
                }

                if(custo.length() < 3)
                {
                    while (custo.length() < 3) {
                        custo = " " + custo;
                    }
                }

                String linhaNova = idOrigem+idDestino+distancia+tempo+custo;
                CaminhoCidade umCam = new CaminhoCidade(linhaNova);
                Log.d("Linha", linha);
                String idO = idOrigem.replace(" ", "");
                String idD = idDestino.replace(" ", "");
                listaCaminhos.add(umCam);
                matrizCaminho[Integer.parseInt(idO)][Integer.parseInt(idD)] = umCam.getDistancia();
            }
            grafo.setAdjacencia(matrizCaminho);
            inputStream.close();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    public void lerArquivoCaminhoTexto() throws IOException {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("CaminhosEntreCidadesMarte.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine())!= null){
                linhas.add(linha);
                //CaminhoCidade umCam = new CaminhoCidade(linha);
                //matrizCaminho[Integer.parseInt(idOrigem)][Integer.parseInt(idDestino)] = umCam.getDistancia();
            }
            grafo.setAdjacencia(matrizCaminho);
            inputStream.close();
        }
        catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public void ExibirMatrizAdjacencia()
    {
        for(int i = 0; i < matrizCaminho.length; i++)
        {
            for(int j = 0; j< matrizCaminho.length; j++)
            {
                String resultado = String.valueOf(matrizCaminho[i][j]);
                System.out.println(resultado);
            }
        }
    }

    public void checkBox(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.chkRecursao:
                if (checked)
                {
                    Toast.makeText(this, "Clicou em recursão", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.chkDijkstra:
                if (checked)
                {
                    Toast.makeText(this, "Clicou em Dijkstra", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        matrizCaminho = new int[numCidades][numCidades];
        grafo = new GrafoBactracking(numCidades, numCidades);
    }
}