//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades;

import androidx.annotation.NonNull;
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

import br.unicamp.approtascidades.Grafo.CaminhoCidade;
import br.unicamp.approtascidades.Grafo.Cidade;
import br.unicamp.approtascidades.Solucao.PilhaLista;
import br.unicamp.approtascidades.Solucao.SolucaoCaminhos;
import br.unicamp.approtascidades.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    Cidade vetorCidade[]; //Vetor de Cidade
    List<Cidade> listaCidade = new ArrayList<Cidade>(); // ArrayList de listaCidade
    List<CaminhoCidade> listaCaminhos = new ArrayList<CaminhoCidade>(); // ArrayList de listaCaminhos
    PilhaLista<CaminhoCidade> pilhaCaminhos; // PilhaLista de Caminhos
    List<String> listaNomeCidades = new ArrayList<String>(); // ArrayList em String que armazena os nomes da cidades
    ActivityMainBinding binding; //Permite selecionar os componentes do xml sem findViewById()
    List<PilhaLista<CaminhoCidade>> cListaCaminhos; //List de PilhaLista de CaminhoCidade
    int numCidades = 23; // número de cidades de marte que é 23
    Cidade Origem; //Cidade Origem
    Cidade Destino; //Cidade Destino
    boolean mostrouCidade = false; //Boolean para verificar se já mostrou as cidades no Mapa de Marte
    SolucaoCaminhos solucaoCaminhos; // Classe que possui a busca de Caminhos em djikstra e Backtracking recursivo
    List<PilhaLista> listaCaminhosEncontrados; //lista de Caminhos Encontrados

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Utilização da biblioteca Picasso
//        ImageView mapa;
//        mapa = findViewById(R.id.mapa);
//        Picasso.get().load(R.drawable.mapa).into(mapa); //Carregamos a imagem do Mapa de marte com a biblioteca Picasso

        binding.chkRecursao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mostrouCidade) // esse if é feito para não exibir mais de uma vez as cidades do método mostrarCidades()
                {
                    MostrarCidades(); //Mostramos as cidades no mapa
                    mostrouCidade = true; //recebe true para não exibirmos novamente as cidades
                }

            }
        });

        binding.chkDijkstra.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mostrouCidade) // esse if é feito para não exibir mais de uma vez as cidades do método mostrarCidades()
                {
                    MostrarCidades(); //Mostramos as cidades no mapa
                    mostrouCidade = true;  //recebe true para não exibirmos novamente as cidades
                }

            }
        });

        binding.gvLista.setOnItemClickListener(new GridView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(!parent.getItemAtPosition(position).toString().equals("Cidades que passou"))
                     Toast.makeText(getBaseContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                //Exibimos uma mensagem para o usuário do item que ele clicou no GridView
            }
        });


        binding.btnBuscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    boolean checkedRecursao = ((CheckBox) binding.chkRecursao).isChecked(); // vericamos se os checkedBox estão clicados
                    boolean checkedDijkstra = ((CheckBox) binding.chkDijkstra).isChecked();
                    if(checkedRecursao)
                    {
                        Object nomeCidadeOrigem = binding.numOrigem.getSelectedItem();
                        int idOrigem = buscarId(String.valueOf(nomeCidadeOrigem));
                        Object nomeCidadeDestino = binding.numDestino.getSelectedItem();
                        int idDestino = buscarId(String.valueOf(nomeCidadeDestino));

                        if(idOrigem == idDestino) // se verdade Mesma Cidade
                            Toast.makeText(MainActivity.this, "Escolha Cidades Diferentes", Toast.LENGTH_LONG).show();

                         cListaCaminhos = solucaoCaminhos.BuscarCaminhosRec(idOrigem, idDestino);
                         // retorna lista de caminhos possiveis entre as cidades selecionadas
                        // copia da lista de caminhos p / manipulá - la sem estragar os dados, os quais serão utilizados posteriormente
                        listaCaminhosEncontrados = new ArrayList<>();
                        for (int i = 0; i < cListaCaminhos.size(); i++)
                        {
                            listaCaminhosEncontrados.add(cListaCaminhos.get(i).Copia());
                        }
                        int linhas = cListaCaminhos.size();
                        List<String> resultado = new ArrayList<String>();
                        //resultado.add("Cidades que passou");
                        PilhaLista<CaminhoCidade> pilhaCam = null; //Pilhas auxiliares na busca de caminhos
                        PilhaLista<CaminhoCidade> copia = null;
                        for (int lin = 0; lin < linhas; lin++) // percorre a lista de caminhos
                        {
                            pilhaCam = cListaCaminhos.get(lin); // pega o lin-ésimo caminho
                            copia = pilhaCam.Copia(); // faz-se cópia para não entragar os dados que serão utilizados posteriormente
                            CaminhoCidade cidade = new CaminhoCidade(pilhaCam.OTopo().getIdOrigem(), pilhaCam.OTopo().getDistancia()); // exibe apenas a cidade origem
                            Cidade umCid = buscarCidade(Integer.parseInt(pilhaCam.OTopo().getIdOrigem().trim()));
                            resultado.add(umCid.getNomeCidade());
                            resultado.add("Distância: " + cidade.getDistancia());
                            pilhaCam.Desempilhar();
                        }
                        if(resultado.size() > 0)
                        {
                            //Exibimos no GridView o resultado da Busca de Caminhos
                            ArrayAdapter<String> adapterString = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, resultado);
                            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            binding.gvLista.setAdapter(adapterString);
                            DesenharCaminho(copia); //desenhamos o caminho que está armazenado na pilhaLista<CaminhoCidade> copia
                        }
                        else
                            Toast.makeText(MainActivity.this, "Não encontramos Caminhos entre estas Cidades", Toast.LENGTH_SHORT).show();

                    }

                    if(checkedDijkstra)
                    {
                        Object nomeCidadeOrigem = binding.numOrigem.getSelectedItem();
                        int idOrigem = buscarId(String.valueOf(nomeCidadeOrigem));
                        Object nomeCidadeDestino = binding.numDestino.getSelectedItem();
                        int idDestino = buscarId(String.valueOf(nomeCidadeDestino));
                        if(idOrigem == idDestino)
                            Toast.makeText(MainActivity.this, "Escolha Cidades Diferentes", Toast.LENGTH_LONG).show();

                        pilhaCaminhos = solucaoCaminhos.MenorCaminhoDijkstra(idOrigem,idDestino);
                        PilhaLista<CaminhoCidade> pilhaCam = pilhaCaminhos; // pega o lin-ésimo caminho
                        PilhaLista<CaminhoCidade> copia = pilhaCam.Copia(); // faz-se cópia para não entragar os dados que serão utilizados posteriormente
                        gvPilhaAdapter gvAdapter = new gvPilhaAdapter(getBaseContext(), copia);
                        binding.gvLista.setAdapter(gvAdapter);

                        int linhas = pilhaCaminhos.getTamanho();
                        List<String> resultado = new ArrayList<>();
                        resultado.add("Cidades que passou");
                        for (int lin = 0; lin < linhas; lin++) // percorre a lista de caminhos
                        {
                            CaminhoCidade cidade =new CaminhoCidade(pilhaCam.OTopo().getIdOrigem(), pilhaCam.OTopo().getDistancia()); // exibe apenas a cidade origem
                            Cidade umCid = buscarCidade(Integer.parseInt(pilhaCam.OTopo().getIdOrigem().trim()));
                            resultado.add(umCid.getNomeCidade());
                            pilhaCam.Desempilhar(); //Desempilhamos a pilha
                        }
                        if(resultado.size() > 1)
                        {
                            //Exibimos no GridView o resultado da Busca de Caminhos
                            ArrayAdapter<String> adapterString = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, resultado);
                            adapterString.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            binding.gvLista.setAdapter(adapterString);
                            DesenharCaminho(copia); //desenhamos o caminho que está armazenado na pilhaLista<CaminhoCidade> copia
                        }
                        else
                            Toast.makeText(MainActivity.this, "Não encontramos Caminhos entre estas Cidades", Toast.LENGTH_SHORT).show();

                    }
                    if(!checkedDijkstra && !checkedRecursao)
                        Toast.makeText(MainActivity.this, "Clique em um dos checkedBox para poder Buscar", Toast.LENGTH_LONG).show();


                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Falha ao Encontrar Caminhos", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void lerArquivoCidadeJson() throws IOException
    {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("cidadeCorreto.json"); //abrimos o arquivo na pasta assets
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream); // instanciamos um imputStreamReader
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // instanciamos um BufferedReader
            String linha;
            String idCidade = "";
            int contCidades = 0; // contador de cidades
            while ((linha = bufferedReader.readLine())!= null)
            {
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
            inputStream.close(); // Fechamos o arquivo

            //Carregamos os nomes das cidades no sppiner de  cidade origem
            ArrayAdapter<String> adapterString = new ArrayAdapter<>(this,  android.R.layout.simple_spinner_item, listaNomeCidades);
            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_item);
            binding.numOrigem.setAdapter(adapterString);

            //Carregamos os nomes das cidades no sppiner de  cidade Destino
            ArrayAdapter<String> adapterString2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaNomeCidades);
            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.numDestino.setAdapter(adapterString2);

        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void lerArquivoCidadeTexto() throws IOException
    {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("CidadesMarte.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            while ((linha = bufferedReader.readLine())!= null)
            {
                Cidade umCid = new Cidade(linha);
                listaCidade.add(umCid);
                listaNomeCidades.add(umCid.getNomeCidade());
                Log.d("Cidade", umCid.toString());
            }
            inputStream.close(); //Fecha o Arquivo

            //Carrega no spinner de origem os nomes das Cidades
            ArrayAdapter<String> adapterString = new ArrayAdapter<>(this,  android.R.layout.simple_spinner_item, listaNomeCidades);
            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_item);
            binding.numOrigem.setAdapter(adapterString);

            //Carrega no spinner de destino os nomes das Cidades
            ArrayAdapter<String> adapterString2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaNomeCidades);
            adapterString.setDropDownViewResource(android.R.layout.simple_spinner_item);
            binding.numDestino.setAdapter(adapterString2);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void listaCompletaCidades() //Este método exibe todas as cidades no Run do Android Studio
    {
        if(listaCidade != null)
        {
            for(int i = 0; i< listaCidade.size(); i++)
            {
                System.out.println("Nome: "+listaCidade.get(i).getNomeCidade());
                System.out.println("Id: "+listaCidade.get(i).getIdCidade());
                System.out.println("X: "+listaCidade.get(i).getCordenadaX());
                System.out.println("Y: "+listaCidade.get(i).getCordenadaY());
            }
        }
        else
        {
            System.out.println("Lista vazia, necessário cadastrar cidades");
        }
    }

    public void listaCompletacCaminhos() //Este método exibe todas os caminhos no Run do Android Studio
    {
        if(listaCaminhos != null)
        {
            for(int i = 0; i< listaCaminhos.size(); i++)
            {
                System.out.println("Origem: "+listaCaminhos.get(i).getIdOrigem());
                System.out.println("Destino: "+listaCaminhos.get(i).getIdDestino());
                System.out.println("Distancia: "+listaCaminhos.get(i).getDistancia());
                System.out.println("Tempo: "+listaCaminhos.get(i).getTempo());
                System.out.println("Custo: "+listaCaminhos.get(i).getCusto());
            }
        }
        else
        {
            System.out.println("Lista vazia, necessário cadastrar caminhos");
        }
    }


    public void lerArquivoCaminhoJson() throws IOException
    {
        try
        {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("caminhosCidades.json"); //abrimos o arquivo na pasta assets
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream); //instanciamos um inputStreamReader
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            while ((linha = bufferedReader.readLine())!= null)  // percorremos a linha enquanto ela é diferente de nula
            {
                JSONObject objCaminho = new JSONObject(linha);
                String idOrigem = objCaminho.getString("IdCidadeOrigem"); //declaramos string para receber os campos do arquivo json
                String idDestino = objCaminho.getString("IdCidadeDestino");
                String distancia = objCaminho.getString("Distancia");
                String tempo = objCaminho.getString("Tempo");
                String custo = objCaminho.getString("Custo");
                CaminhoCidade umCam = new CaminhoCidade(idOrigem.trim(),idDestino.trim(),Integer.parseInt(distancia.trim()),Integer.parseInt(tempo.trim()),Integer.parseInt(custo.trim())); //Criamos um novo caminho a partir da linha lida
                listaCaminhos.add(umCam); //adicionamos o caminho a lista de caminhos
            }
            inputStream.close(); //Fechamos o Arquivo
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void lerArquivoCaminhoTexto() throws IOException
    {
        try
        {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("CaminhosEntreCidadesMarte.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            while ((linha = bufferedReader.readLine())!= null){
                CaminhoCidade umCaminho = new CaminhoCidade(linha);
                listaCaminhos.add(umCaminho);
            }
            inputStream.close(); //Fecha o Arquivo
        }
        catch (IOException  e)
        {
            e.printStackTrace();
        }
    }

    public int buscarId(String nomeCidade) //Busca o id da Cidade pelo nomeCidade que está no parâmetro do método na listaCidade
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

    public Cidade buscarCidade(int idCidade) //Busca a Cidade pelo idCidade na listaCidade
    {
        Cidade umCid = null;

        for(int i = 0; i < listaCidade.size(); i++)
        {
            if(idCidade == listaCidade.get(i).getIdCidade())
                umCid = new Cidade(listaCidade.get(i).getIdCidade(), listaCidade.get(i).getNomeCidade(), listaCidade.get(i).getCordenadaX(), listaCidade.get(i).getCordenadaY());
        }

        return umCid;
    }

    public void DesenharCaminho(@NonNull PilhaLista<CaminhoCidade> caminho) throws Exception // Método que desenha o caminho encontrado
    {
        Paint paint =  new Paint(); // instanciamos a classe Paint
        paint.setColor(Color.BLACK); //setamos os atributos cor e tamanho de texto
        paint.setTextSize(20);
        binding.mapa.buildDrawingCache();
        int altura = binding.mapa.getDrawingCache().getHeight();  //pegamos a altura e largura do mapa
        int largura = binding.mapa.getDrawingCache().getWidth();
        Bitmap bitmap = Bitmap.createBitmap(largura,altura, Bitmap.Config.ARGB_8888);
        bitmap = bitmap.copy(bitmap.getConfig(),true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(binding.mapa.getDrawingCache(),0,0,null);

        while(!caminho.EstaVazia())
        {
           CaminhoCidade atual =  caminho.OTopo();
           Cidade origem  = buscarCidade(Integer.parseInt(atual.getIdOrigem().trim()));
           Cidade destino = buscarCidade(Integer.parseInt(atual.getIdDestino().trim()));
           canvas.drawLine((float)origem.getCordenadaX() * largura,(float) origem.getCordenadaY() * altura,(float) destino.getCordenadaX() * largura,(float) destino.getCordenadaY() * altura,paint);
           caminho.Desempilhar();
        }
        binding.mapa.setImageBitmap(bitmap);
    }

    public void MostrarCidades() //Método que mostra todas as cidades que estão na listaCidade
    {
            Paint paint =  new Paint(); // instanciamos a classe Paint
            paint.setColor(Color.BLACK); //setamos os atributos cor e tamanho de texto
            paint.setTextSize(20);
            binding.mapa.buildDrawingCache();
            int altura = binding.mapa.getDrawingCache().getHeight();  //pegamos a altura e largura do mapa
            int largura = binding.mapa.getDrawingCache().getWidth();
            Log.d("Altura Mapa: ", altura + "");
            Log.d("Largura Mapa: ", largura + "");
            Bitmap bitmap = Bitmap.createBitmap(largura,altura, Bitmap.Config.ARGB_8888);
            bitmap = bitmap.copy(bitmap.getConfig(),true);
            Canvas canvas = new Canvas(bitmap);
            int contCidades = 0;
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

    public void ExibirTodosCaminhosLidos() // Exibe todos os caminhos que estão na listaCaminhos
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
                    Origem = listaCidade.get(contCidades);
                }
                if(idDestino == id)
                {
                    Destino = listaCidade.get(contCidades);
                }
                contCidades++;
            }
            contCaminhos++;
            canvas.drawLine((float)Origem.getCordenadaX() * largura,(float) Origem.getCordenadaY() * altura,(float) Destino.getCordenadaX() * largura,(float) Destino.getCordenadaY() * altura,paint);
        }
        binding.mapa.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //Instanciação das classes e Estruturas de Dados que utilizaremos na execução do aplicativo
        cListaCaminhos = new ArrayList<>();
        vetorCidade = new Cidade[numCidades];
        listaCaminhos = new ArrayList<CaminhoCidade>();
        pilhaCaminhos = new PilhaLista<>();
        solucaoCaminhos = new SolucaoCaminhos(getBaseContext());

        try
        {
            lerArquivoCidadeJson(); //Lemos o arquivo de Cidade em Json
        }
        catch (IOException e)
        {
            Toast.makeText(getBaseContext(), "Falha na Leitura do Arquivo de Cidades", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        try
        {
            lerArquivoCaminhoJson();  //Lemos o arquivo de Caminhos em Json
        }
        catch (IOException e)
        {
            Toast.makeText(getBaseContext(), "Falha na Leitura do Arquivo de Caminhos", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}