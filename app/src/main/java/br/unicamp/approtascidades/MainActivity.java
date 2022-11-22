//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
import br.unicamp.approtascidades.Grafo.Vertice;
import br.unicamp.approtascidades.Solucao.PilhaLista;
import br.unicamp.approtascidades.Solucao.SolucaoCaminhos;
import br.unicamp.approtascidades.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    Cidade vetorCidade[];
    int matrizCaminho[][];
    GrafoBactracking grafo;
    Grafo oGrafo;
    List<Cidade> listaCidade = new ArrayList<Cidade>();
    List<CaminhoCidade> listaCaminhos = new ArrayList<CaminhoCidade>();
    PilhaLista<CaminhoCidade> pilhaCaminhos;
    PilhaVetor<CaminhoCidade> caminhoAtual = new PilhaVetor<>();
    List<String> listaNomeCidades = new ArrayList<String>();
    ActivityMainBinding binding;
    List<PilhaLista<CaminhoCidade>> cListaCaminhos;
    int numCidades = 23;
    Cidade Origem;
    Cidade Destino;
    boolean mostrouCidade = false;
    CaminhoCidade matrizBacktracking[][];
    SolucaoCaminhos solucaoCaminhos;
    List<PilhaLista> listaCaminhosEncontrados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        ArrayList<CaminhoCidade> caminhoAdapter = new ArrayList<CaminhoCidade>();


//        ImageView mapa;
//        mapa = findViewById(R.id.mapa);
//        Picasso.get().load(R.drawable.mapa).into(mapa); //Carregamos a imagem do Mapa de marte com a biblioteca Picasso

        binding.chkRecursao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(!mostrouCidade)
                {
                    MostrarCidades();
                    mostrouCidade = true;
                }

            }
        });

        binding.chkDijkstra.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(!mostrouCidade)
                {
                    MostrarCidades();
                    mostrouCidade = true;
                }

            }
        });

        binding.gvLista.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "Cidade: " + encontrarIdAdapter(position), Toast.LENGTH_LONG).show();
            }
        });


        binding.btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean checkedRecursao = ((CheckBox) binding.chkRecursao).isChecked(); // vericamos se os checkedBox estão clicados
                    boolean checkedDijkstra = ((CheckBox) binding.chkDijkstra).isChecked();
                    if(checkedRecursao == true)
                    {
                        Object nomeCidadeOrigem = binding.numOrigem.getSelectedItem();
                        int idOrigem = buscarId(String.valueOf(nomeCidadeOrigem));
                        Object nomeCidadeDestino = binding.numDestino.getSelectedItem();
                        int idDestino = buscarId(String.valueOf(nomeCidadeDestino));

                        if(idOrigem == idDestino)
                            Toast.makeText(MainActivity.this, "Escolha Cidades Diferentes", Toast.LENGTH_LONG).show();

                         cListaCaminhos = solucaoCaminhos.BuscarCaminhosRec(idOrigem, idDestino);
                         // retorna lista de caminhos possiveis entre as cidades selecionadas
                        // copia da lista de caminhos p / manipulá - la sem estragar os dados, os quais serão utilizados posteriormente
                        listaCaminhosEncontrados = new ArrayList<>();
                        for (int i = 0; i < cListaCaminhos.size(); i++) {
                            listaCaminhosEncontrados.add(cListaCaminhos.get(i).Copia());
                        }
                        int linhas = cListaCaminhos.size();
                        String resultado = " ";

                        for (int lin = 0; lin < linhas; lin++) // percorre a lista de caminhos
                        {
                            PilhaLista<CaminhoCidade> pilhaCam = cListaCaminhos.get(lin); // pega o lin-ésimo caminho
                            PilhaLista<CaminhoCidade> copia = pilhaCam.Copia(); // faz-se cópia para não entragar os dados que serão utilizados posteriormente
                            gvPilhaAdapter gvAdapter = new gvPilhaAdapter(getBaseContext(), copia);
                            binding.gvLista.setAdapter(gvAdapter);
                            CaminhoCidade cidade =new CaminhoCidade(pilhaCam.OTopo().getIdOrigem(), pilhaCam.OTopo().getDistancia()); // exibe apenas a cidade origem
                            if(lin < linhas -1)
                                resultado += "IdCidade: " + cidade.getIdOrigem() + " Distancia: " + cidade.getDistancia() + "\n";
                            else
                                resultado += "IdCidade: " + cidade.getIdOrigem() + " Distancia: " + cidade.getDistancia();

                            pilhaCam.Desempilhar();
                        }
                        if(resultado != " ")
                        {
                            Toast.makeText(MainActivity.this, resultado, Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Não encontramos Caminhos entre estas Cidades", Toast.LENGTH_SHORT).show();

                    }

                    if(checkedDijkstra == true)
                    {
                        Object nomeCidadeOrigem = binding.numOrigem.getSelectedItem();
                        int idOrigem = buscarId(String.valueOf(nomeCidadeOrigem));
                        Object nomeCidadeDestino = binding.numDestino.getSelectedItem();
                        int idDestino = buscarId(String.valueOf(nomeCidadeDestino));
                        if(idOrigem == idDestino)
                            Toast.makeText(MainActivity.this, "Escolha Cidades Diferentes", Toast.LENGTH_LONG).show();

                        pilhaCaminhos = solucaoCaminhos.MenorCaminhoDijkstra(idOrigem,idDestino);

                        int linhas = pilhaCaminhos.getTamanho();
                        String resultado = " ";
                        for (int lin = 0; lin < linhas; lin++) // percorre a lista de caminhos
                        {
                            PilhaLista<CaminhoCidade> pilhaCam = pilhaCaminhos; // pega o lin-ésimo caminho
                            PilhaLista<CaminhoCidade> copia = pilhaCam.Copia(); // faz-se cópia para não entragar os dados que serão utilizados posteriormente
                            CaminhoCidade cidade =new CaminhoCidade(pilhaCam.OTopo().getIdOrigem(), pilhaCam.OTopo().getDistancia()); // exibe apenas a cidade origem
                            resultado += "IdCidade: " + cidade.getIdOrigem() + " Distancia: " + cidade.getDistancia();

                            pilhaCam.Desempilhar();
                        }
                        if(resultado != " ")
                            Toast.makeText(MainActivity.this, resultado, Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Não encontramos Caminhos entre estas Cidades", Toast.LENGTH_SHORT).show();

                    }
                    if(checkedDijkstra == false && checkedRecursao == false)
                        Toast.makeText(MainActivity.this, "Clique em um dos checkedBox para poder Buscar", Toast.LENGTH_LONG).show();


                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Erro na Leitura do Arquivo", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void lerArquivoCidadeJson() throws IOException {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("cidadeCorreto.json"); //abrimos o arquivo na pasta assets
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream); // instanciamos um imputStreamReader
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // instanciamos um BufferedReader
            String linha;
            String idCidade = "";
            int contCidades = 0; // contador de cidades
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine())!= null){
                linhas.add(linha);
               JSONObject objCidade = new JSONObject(linha);
                idCidade = objCidade.getString("IdCidade");
               String nomeCidade = objCidade.getString("NomeCidade");  //declaramos string para receber os campos do arquivo json
               String cordenadaX = objCidade.getString("CordenadaX");
               String cordenadaY = objCidade.getString("CordenadaY");
               Cidade umCid = new Cidade(Integer.parseInt(idCidade.trim()),nomeCidade.trim(),Double.parseDouble(cordenadaX.trim()),Double.parseDouble(cordenadaY.trim()));
               vetorCidade[contCidades] = umCid;
               listaCidade.add(umCid); //adicionamos a cidade na listaCidade
                listaNomeCidades.add(umCid.getNomeCidade()); // adicionamos o nome da cidade na listaNomeCidades
                Log.d("Cidade", umCid.toString());
                contCidades++; //incrementamos +1 ao contador
            }
            inputStream.close();

            //Carregamos os nomes das cidades no sppiner de  cidade origem
            ArrayAdapter<String> adapterString = new ArrayAdapter<>(this,  android.R.layout.simple_spinner_item, listaNomeCidades);
            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_item);
            binding.numOrigem.setAdapter(adapterString);

            //Carregamos os nomes das cidades no sppiner de  cidade Destino
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
            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_item);
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
            InputStream inputStream = assetManager.open("caminhosCidades.json"); //abrimos o arquivo na pasta assets
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream); //instanciamos um inputStreamReader
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            int contCidades = 0;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine())!= null){ // percorremos a linha enquanto ela é diferente de nula
                linhas.add(linha);
                JSONObject objCaminho = new JSONObject(linha);
                String idOrigem = objCaminho.getString("IdCidadeOrigem"); //declaramos string para receber os campos do arquivo json
                String idDestino = objCaminho.getString("IdCidadeDestino");
                String distancia = objCaminho.getString("Distancia");
                String tempo = objCaminho.getString("Tempo");
                String custo = objCaminho.getString("Custo");
                CaminhoCidade umCam = new CaminhoCidade(idOrigem.trim(),idDestino.trim(),Integer.parseInt(distancia.trim()),Integer.parseInt(tempo.trim()),Integer.parseInt(custo.trim())); //Criamos um novo caminho a partir da linha lida
                CaminhoCidade caminhoMatriz = new CaminhoCidade(Integer.parseInt(distancia.trim()),Integer.parseInt(tempo.trim()),Integer.parseInt(custo.trim()));
                listaCaminhos.add(umCam); //adicionamos o caminho a lista de caminhos
                caminhoAtual.Empilhar(umCam); //empilhamos o caminho na pilha
                matrizBacktracking[Integer.parseInt(umCam.getIdOrigem())][Integer.parseInt(umCam.getIdDestino())] = umCam;
                if(contCidades < 23)
                    matrizCaminho[contCidades][contCidades] = caminhoMatriz.getDistancia();
                contCidades++;
            }
            grafo.setAdjacencia(matrizCaminho); //set o grafo com a matrizCaminho
            inputStream.close(); //Fechamos o Arquivo
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
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

    public void MontarGrafodDjikstra()
    {
        for(int i = 0; i < listaCidade.size(); i++ )
        {
            oGrafo.NovoVertice(listaCidade.get(i).getIdCidade() + "");
        }

        for (int i = 0; i < Math.pow(listaCaminhos.size(), 0.5); i++)
        {
            for (int j = 0; j < Math.pow(listaCaminhos.size(), 0.5); j++)
            {
                if (matrizCaminho[i][j] != 0)
                {
                    oGrafo.NovaAresta(i, j, matrizCaminho[i][j]);
                }
            }
        }
    }

    public int buscarId(String nomeCidade)
    {
        int idCidade = 0;
        for(int i = 0; i < listaCidade.size(); i++)
        {
            if(listaCidade.get(i).getNomeCidade() == nomeCidade)
            {
                idCidade = listaCidade.get(i).getIdCidade();
            }
        }
        return idCidade;
    }
//    public void MostrarCaminhos() throws Exception
//    {
//        for (PilhaVetor<CaminhoCidade> caminho : pilhaCaminhos)
//        {
//            int posição = 0;
//            PilhaVetor<CaminhoCidade> aux = caminho.Clone();
//            aux.Inverter();
//
//            while (!aux.EstaVazia())
//            {
//                CaminhoCidade c = aux.Desempilhar();
//                listaCaminhos.add(c);
//            }
//        }
//
//    }

    public void MostrarCidades()
    {
            Paint paint =  new Paint(); // instanciamos a classe Paint
            paint.setColor(Color.BLACK); //setamos os atributos cor e tamanho de texto
            paint.setTextSize(20);
            binding.mapa.buildDrawingCache();
            int altura = binding.mapa.getDrawingCache().getHeight();  //pegamos a altura e largura do mapa
            int largura = binding.mapa.getDrawingCache().getWidth();
            Log.d("altura", altura + "");
            Log.d("largura", largura + "");
            Bitmap bitmap = Bitmap.createBitmap(largura,altura, Bitmap.Config.ARGB_8888);
            bitmap = bitmap.copy(bitmap.getConfig(),true);
            Canvas canvas = new Canvas(bitmap);
            int contCidades = 0;
            Log.d("TamanhoLista", listaCidade.size() + " ");
            canvas.drawBitmap(binding.mapa.getDrawingCache(),0,0,null);
            while(listaCidade.size() > contCidades)
            {
                double coordenadaX = listaCidade.get(contCidades).getCordenadaX() *largura;
                double coordenadaY =listaCidade.get(contCidades).getCordenadaY() * altura;
                String nomeCidade = listaCidade.get(contCidades).getNomeCidade();
                canvas.drawText(nomeCidade,(float) coordenadaX+8,(float)coordenadaY, paint);
                canvas.drawCircle((float) coordenadaX,(float)coordenadaY,7f,paint);
                binding.mapa.setImageBitmap(bitmap);
                contCidades++;
            }
            binding.mapa.setImageBitmap(bitmap);


    }

    public void ExibirTodosCaminhos()
    {
        Paint paint =  new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        binding.mapa.buildDrawingCache();
        int altura = binding.mapa.getDrawingCache().getHeight();
        int largura = binding.mapa.getDrawingCache().getWidth();
        Log.d("altura", altura + "");
        Log.d("largura", largura + "");
        Bitmap bitmap = Bitmap.createBitmap(largura,altura, Bitmap.Config.ARGB_8888);
        bitmap = bitmap.copy(bitmap.getConfig(),true);
        Canvas canvas = new Canvas(bitmap);
        int contCidades = 0;
        Log.d("TamanhoListaCaminhos", listaCaminhos.size() + " ");
        canvas.drawBitmap(binding.mapa.getDrawingCache(),0,0,null);
        while(listaCidade.size() > contCidades)
        {
            double coordenadaX = listaCidade.get(contCidades).getCordenadaX() *largura;
            double coordenadaY =listaCidade.get(contCidades).getCordenadaY() * altura;
            String nomeCidade = listaCidade.get(contCidades).getNomeCidade();
            canvas.drawText(nomeCidade,(float) coordenadaX+8,(float)coordenadaY, paint);
            canvas.drawCircle((float) coordenadaX,(float)coordenadaY,7f,paint);
            binding.mapa.setImageBitmap(bitmap);
            contCidades++;
        }
        binding.mapa.setImageBitmap(bitmap);

        int contCaminhos = 0;
        while(listaCaminhos.size() > contCaminhos)
        {
            contCidades = 0;
            int idOrigem = Integer.parseInt(listaCaminhos.get(contCaminhos).getIdOrigem());
            int idDestino =Integer.parseInt(listaCaminhos.get(contCaminhos).getIdDestino());
            while(listaCidade.size() > contCidades)
            {
                int id  = listaCidade.get(contCidades).getIdCidade();
                if(idOrigem == id)
                {
                    Log.d("entrouOrigem","entrou");
                    Origem = listaCidade.get(contCidades);
                }
                if(idDestino == id)
                {
                    Log.d("entrouDestino","entrou");
                    Destino = listaCidade.get(contCidades);
                }
                contCidades++;
            }
            contCaminhos++;
            canvas.drawLine((float)Origem.getCordenadaX() * largura,(float) Origem.getCordenadaY() * altura,(float) Destino.getCordenadaX() * largura,(float) Destino.getCordenadaY() * altura,paint);
        }
        binding.mapa.setImageBitmap(bitmap);

    }

    public void ExibirMenorCaminho() throws Exception
    {

    }

    public void BuscarDijkstra()
    {

    }
    public String encontrarIdAdapter(int position)
    {
        String id = " ";

        for(int i = 0; i<= position; i++)
        {
            if(i == position)
                id = listaCaminhos.get(i).getIdDestino();
        }
        return id;
    }

    public String encontrarNomeAdapter(int position)
    {
        String id = " ";

        for(int i = 0; i<= position; i++)
        {
            if(i == position)
                id = listaCaminhos.get(i).getIdDestino();
        }

        String nome = " ";

        for(int i = 0; i< listaCidade.size(); i++)
        {
            if(listaCidade.get(i).getIdCidade() == Integer.parseInt(id))
                nome = listaCidade.get(i).getNomeCidade();
        }

        return nome;
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

    public void preencherBacktracking()
    {
        for(int i = 0; i < matrizBacktracking.length; i++)
        {
            for (int j = 0; j < matrizBacktracking.length; j++)
            {
                if(matrizBacktracking[i][j] == null)
                {
                    CaminhoCidade caminhoCidade = new CaminhoCidade(" "," ", 0,0,0);
                    matrizBacktracking[i][j] = caminhoCidade;
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        cListaCaminhos = new ArrayList<>();
        oGrafo = new Grafo(numCidades,numCidades);
        vetorCidade = new Cidade[numCidades];
        listaCaminhos = new ArrayList<CaminhoCidade>();
        matrizCaminho = new int[numCidades][numCidades];
        grafo = new GrafoBactracking(numCidades, numCidades);
        pilhaCaminhos = new PilhaLista<>();
        matrizBacktracking = new CaminhoCidade[numCidades][numCidades];
        solucaoCaminhos = new SolucaoCaminhos(getBaseContext());

        try
        {
            lerArquivoCidadeJson();
            //listaCompletaCidades();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            lerArquivoCaminhoJson();
            preencherBacktracking();
            //listaCompletacCaminhos();
            //ExibirMatrizAdjacencia();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}