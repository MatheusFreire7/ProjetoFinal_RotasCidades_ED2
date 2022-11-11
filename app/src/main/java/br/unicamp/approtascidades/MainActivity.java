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
    List<String> listaNomeCidades = new ArrayList<String>();
    ActivityMainBinding binding;
    int numCidades = 23;
    int menor;

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

        //Carrega o nome das cidades que estão armazenadas em um vetor em um spinner
//            Spinner spinner = (Spinner) findViewById(R.id.numOrigem);
//            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                    this,  R.array.cidades_array, android.R.layout.simple_spinner_item);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(adapter);
//            spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
//
//            Spinner spinner2 = (Spinner) findViewById(R.id.numDestino);
//            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
//                    this, R.array.cidades_array, android.R.layout.simple_spinner_item);
//            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner2.setAdapter(adapter);
//            spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener());

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
                listaNomeCidades.add(umCid.getNomeCidade());
                Log.d("Cidade", umCid.toString());
                contCidades++;
            }
            inputStream.close();
            ArrayAdapter<String> adapterString = new ArrayAdapter<>(this,  android.R.layout.simple_spinner_item, listaNomeCidades);
            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_item);
            binding.numOrigem.setAdapter(adapterString);

            ArrayAdapter<String> adapterString2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaNomeCidades);
            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.numDestino.setAdapter(adapterString2);
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
                listaNomeCidades.add(umCid.getNomeCidade());
                Log.d("Cidade", umCid.toString());
                contCidades++;
            }
            inputStream.close();
            ArrayAdapter<String> adapterString = new ArrayAdapter<>(this,  android.R.layout.simple_spinner_item, listaNomeCidades);
            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_item);
            binding.numOrigem.setAdapter(adapterString);

            ArrayAdapter<String> adapterString2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaNomeCidades);
            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.numDestino.setAdapter(adapterString2);
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
                        custo = 0 + custo;
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
                if(j != matrizCaminho.length-1)
                    System.out.print(" " + matrizCaminho[i][j]);
                else
                    System.out.print(" " + matrizCaminho[i][j] + "\n");
            }
        }
    }

    private void AcharCaminhos(int origem, int destino) throws Exception
    {
        List<PilhaVetor<CaminhoCidade>> listaCaminhosPilha = new ArrayList<PilhaVetor<CaminhoCidade>>();

        int menorDistancia = 0, disAtual = 0;
        PilhaVetor<CaminhoCidade> caminhoAtual = new PilhaVetor<>();

        PilhaVetor<CaminhoCidade> aux = new PilhaVetor<CaminhoCidade>();

        boolean[] jaPassou = new boolean[23];
        for (int i = 0; i < 23; i++)
            jaPassou[i] = false;

        int atual = origem;

        boolean acabou = false;

        while (!acabou) {
            int tamanhoAnterior = aux.Tamanho();
            for (int i = 0; i < 23; i++)
                if (matrizCaminho[atual][i] != 0 && !jaPassou[i])
                    aux.Empilhar(new CaminhoCidade(String.valueOf(atual),String.valueOf(i), matrizCaminho[atual][i]));
                                        // Caminho criado possui idOrigem, IdDestino e Distancia

            if (!aux.EstaVazia() && tamanhoAnterior == aux.Tamanho()) {
                CaminhoCidade cam = caminhoAtual.Desempilhar();
                disAtual -= cam.getDistancia();
                jaPassou[cam.getDistancia()] = true;
            }

            if (aux.EstaVazia())
                acabou = true;
            else {
                CaminhoCidade c = aux.Desempilhar();

                while (!caminhoAtual.EstaVazia() && caminhoAtual.OTopo().getIdDestino() != c.getIdOrigem()) {
                    CaminhoCidade cam = caminhoAtual.Desempilhar();
                    disAtual -= cam.getDistancia();
                    jaPassou[Integer.parseInt(cam.getIdDestino())] = false;
                }

                caminhoAtual.Empilhar(c);
                disAtual += c.getDistancia();

                if (Integer.parseInt(c.getIdDestino()) != destino) {
                    jaPassou[Integer.parseInt(c.getIdOrigem())] = true;
                    atual = Integer.parseInt(c.getIdDestino());
                } else {
                    listaCaminhosPilha.add(caminhoAtual.Clone());
                    if (disAtual < menorDistancia) {
                        menor = listaCaminhosPilha.size() - 1;
                        menorDistancia = disAtual;
                    }

                    if (aux.EstaVazia())
                        acabou = true;
                    else {
                        CaminhoCidade retorno = aux.Desempilhar();

                        while (!caminhoAtual.EstaVazia() && caminhoAtual.OTopo().getIdDestino() != retorno.getIdOrigem()) {
                            CaminhoCidade cam = caminhoAtual.Desempilhar();
                            disAtual -= cam.getDistancia();
                            jaPassou[Integer.parseInt(cam.getIdDestino())] = false;
                        }

                        caminhoAtual.Empilhar(retorno);
                        jaPassou[Integer.parseInt(retorno.getIdDestino())] = true;
                        disAtual += retorno.getDistancia();

                        while (Integer.parseInt(retorno.getIdDestino()) == destino && !acabou) {
                            listaCaminhosPilha.add(caminhoAtual.Clone());

                            if (disAtual < menorDistancia) {
                                menor = listaCaminhosPilha.size() - 1;
                                menorDistancia = disAtual;
                            }

                            if (!aux.EstaVazia()) {
                                retorno = aux.Desempilhar();
                                while (!caminhoAtual.EstaVazia() && caminhoAtual.OTopo().getIdDestino() != retorno.getIdOrigem()) {
                                    CaminhoCidade cam = caminhoAtual.Desempilhar();
                                    disAtual -= cam.getDistancia();
                                    jaPassou[Integer.parseInt(cam.getIdDestino())] = false;
                                }

                                caminhoAtual.Empilhar(retorno);
                                disAtual += retorno.getDistancia();
                            } else
                                acabou = true;
                        }

                        atual = Integer.parseInt(retorno.getIdDestino());
                    }
                }
            }
        }
    }

    public void MostrarCaminhos()
    {

    }

    public void MostrarCidades()
    {

    }

    public void ExibirMenorCaminho()
    {

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