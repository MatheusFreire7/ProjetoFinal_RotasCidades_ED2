package br.unicamp.approtascidades.Solucao;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.ArrayAdapter;

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
import br.unicamp.approtascidades.Grafo.Vertice;
import br.unicamp.approtascidades.MainActivity;

public class SolucaoCaminhos  extends Activity {
    // atributos utilizada pela classe toda
    private CaminhoCidade[][] matriz;
    // matriz de caminhoCidade em que, onde há caminho entre as cidades de id i e j (tomando matriz[i, j])
    //o objeto é settado com os dados fornecidos pelos txts e, caso contrário, os dados são settados como infinity
    private int qtasCidades;
    private int infinity = Integer.MAX_VALUE; // maior int possível, valor tão grande que podemos considerar "inexistente"
    private int total; // valor total do criterio escolhido para efetuar o menor caminho
    private Context context;

    // propriedades get e set
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    // construtor completo que le os arquivos e setta os vetores e matriz
    public SolucaoCaminhos(Context context) {
        qtasCidades = 23; // nº de cidades do arquivo
        this.context = context;

        vertices = new Vertice[qtasCidades];
        percurso = new DadosOriginal[qtasCidades];
        matriz = new CaminhoCidade[qtasCidades][qtasCidades]; // matriz [23,23]

        for (int l = 0; l < qtasCidades; l++) { // setta todos os campos da matriz com CaminhoEntreCidades com infinity, isto é, "inexistentes"
            for (int c = 0; c < qtasCidades; c++) {
                CaminhoCidade caminho = new CaminhoCidade(l + "", c + "", infinity, infinity, infinity);
                matriz[Integer.parseInt(caminho.getIdOrigem())][Integer.parseInt(caminho.getIdDestino())] = caminho;
            }
        }

        try {
            AssetManager assetManager = context.getResources().getAssets();
            InputStream inputStream = assetManager.open("cidadeCorreto.json"); //abrimos o arquivo na pasta assets
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            int i = 0;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine()) != null) {
                linhas.add(linha);
                JSONObject objCidade = new JSONObject(linha);
                String linhaNova = objCidade.getString("IdCidade");
                vertices[i] = new Vertice(linhaNova.trim()); // add o vertice com o id da cidade
                i++;
            }
            inputStream.close();
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }

        try
        {
            AssetManager assetManager = context.getResources().getAssets();
            InputStream inputStream = assetManager.open("caminhosCidades.json"); //abrimos o arquivo na pasta assets
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream); //instanciamos um inputStreamReader
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            int i = 0;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine()) != null) // percorremos a linha enquanto ela é diferente de nula
            {
                linhas.add(linha);
                JSONObject objCaminho = new JSONObject(linha);
                String idOrigem = objCaminho.getString("IdCidadeOrigem"); //declaramos string para receber os campos do arquivo json
                String idDestino = objCaminho.getString("IdCidadeDestino");
                String distancia = objCaminho.getString("Distancia");
                String tempo = objCaminho.getString("Tempo");
                String custo = objCaminho.getString("Custo");
                CaminhoCidade umCam = new CaminhoCidade(idOrigem.trim(), idDestino.trim(), Integer.parseInt(distancia.trim()), Integer.parseInt(tempo.trim()), Integer.parseInt(custo.trim())); //Criamos um novo caminho a partir da linha lida
                matriz[Integer.parseInt(umCam.getIdOrigem())][Integer.parseInt(umCam.getIdDestino())] = umCam; // add o caminho lido na matriz, com a linha = idCidadeOrigem e coluna = idCidadeDestino
                i++;
            }
            inputStream.close(); //Fechamos o Arquivo
            }
            catch (IOException | JSONException e)
            {
                e.printStackTrace();
            }
        }


    //-----------------------
    // BACKTRACKING RECURSÃO
    //-----------------------

    // variáveis utilizadas nos métodos de backtracking por recursão
    private List<PilhaLista<CaminhoCidade>> listaCaminhos;  // lista de todos os caminhos possíveis
    private PilhaLista<CaminhoCidade> caminho; // caminho atual
    private int cidadeAtual, saidaAtual;
    private boolean achouCaminho, naoTemSaida;
    private boolean[] passou;

    public List<PilhaLista<CaminhoCidade>> BuscarCaminhosRec(int origem, int destino) throws Exception {
        // (re)inicia as variáveis
        listaCaminhos = new ArrayList<PilhaLista<CaminhoCidade>>();
        caminho = new PilhaLista<CaminhoCidade>();
        cidadeAtual = origem;
        saidaAtual = 0;
        achouCaminho = naoTemSaida = false;
        passou = new boolean[qtasCidades];
        for (int indice = 0; indice < qtasCidades; indice++)   // inicia os valores de “passou” como false
            passou[indice] = false;

        // chama o método recursivo
        BuscarCaminhos(origem, destino);

        return listaCaminhos;
    }

    public void BuscarCaminhos(int origem, int destino) throws Exception {
        if (naoTemSaida) // achou todos os caminhos
            return;

        if (!achouCaminho)
        {
            // guarda em boolean se já passamos por todas as cidades do mapa e a pilha está vazia, isto é, não há saída
            naoTemSaida = (cidadeAtual == origem && saidaAtual == qtasCidades && caminho.EstaVazia());
            if (!naoTemSaida)
            {
                while ((saidaAtual < qtasCidades) && !achouCaminho)
                {
                    // se não há saída pela cidade testada, verifica a próxima
                    if (matriz[cidadeAtual][saidaAtual].getCusto() == infinity)
                    saidaAtual++;
                        else // há caminho da cidade atual pra saida
                    if (passou[saidaAtual])
                        saidaAtual++;
                    else // há caminho e ainda não passou por ela
                    {
                        caminho.Empilhar(matriz[cidadeAtual][saidaAtual]);

                        if (saidaAtual == destino) // achamos a cidade destino
                            achouCaminho = true;
                        else
                        {
                            passou[cidadeAtual] = true; // marca a passagem pela cidade atual
                            cidadeAtual = saidaAtual;
                            saidaAtual = 0;
                        }
                    }
                }

                if (!achouCaminho)
                {
                    if (!caminho.EstaVazia())
                    {   // desempilha a configuração atual da pilha
                        // para a pilha da lista de parâmetros
                       CaminhoCidade movim = caminho.Desempilhar();
                        saidaAtual = Integer.parseInt(movim.getIdDestino() + 1); // a cidade destino anterior não presta, ent vamos para a próxima
                        cidadeAtual = Integer.parseInt(movim.getIdOrigem());
                        passou[Integer.parseInt(movim.getIdDestino())] = false;
                        saidaAtual++;
                    }
                    else
                        naoTemSaida = true;
                }
                else
                {
                    listaCaminhos.add(caminho.CopiaInvertida());
                    achouCaminho = false;
                    saidaAtual++;
                    caminho.Desempilhar();
                }
            }
        }
        // após achar o caminho reinicia e procura outro
        BuscarCaminhos(origem, destino);
    }


    public PilhaLista<CaminhoCidade> MenorCaminhoBacktracking(int linhas,  List<PilhaLista<CaminhoCidade>> cListaCaminhos, String criterio) throws Exception {
        int menorDado = 0;
        PilhaLista<CaminhoCidade> menorCaminho = null;

        for (int lin = 0; lin < linhas; lin++) // percorre a lista de caminhos
        {
            PilhaLista<CaminhoCidade> pilhaCam = cListaCaminhos.get(lin); // pega o lin-ésimo caminho
            PilhaLista<CaminhoCidade> copia = pilhaCam.Copia(); // faz-se cópia para não entragar os dados que serão utilizados posteriormente

            int dado = 0;
            while (!copia.EstaVazia()) // soma a distancia total do caminho atual
            {
                dado += copia.OTopo().getDistancia();
                copia.Desempilhar();
            }

            if (menorDado == 0 || dado < menorDado) // verifica se este é o menor caminho até então
            {
                menorDado = dado;
                menorCaminho = pilhaCam.Copia();
            }
        }

        total = menorDado;
        return menorCaminho;
    }


    //------------
    //  DIJKSTRA
    //------------
    private Vertice[] vertices;
    private DadosOriginal[] percurso;
    private int verticeAtual; // global usada para indicar o vértice atualmente sendo visitado
    private int doInicioAteAtual; // global usada para ajustar menor caminho com Djikstra

    public PilhaLista<CaminhoCidade> MenorCaminhoDijkstra(int inicioDoPercurso, int finalDoPercurso)
    {
        for (int j = 0; j < qtasCidades; j++) // setamos o vetor de vertices com o foiVisitado=false
            vertices[j].foiVisitado = false;

        vertices[inicioDoPercurso].foiVisitado = true; // visitamos a inicioDoPercurso

        for (int j = 0; j < qtasCidades; j++)
        {
            // anotamos no vetor percurso o valor do criterio entre o inicioDoPercurso e cada vértice
            // se não há ligação direta, o valor da distância será infinity
            int dado = 0;
            dado = matriz[inicioDoPercurso][j].getDistancia();
            percurso[j] = new DadosOriginal(inicioDoPercurso, dado);
        }

        for (int nTree = 0; nTree < qtasCidades; nTree++)
        {
            // Procuramos a saída não visitada do vértice inicioDoPercurso com o menor dado
            int indiceDoMenor = ObterMenor();
            if (indiceDoMenor < 0)
                break;
            // o vértice com o menor dado passa a ser o vértice atual
            // para compararmos com o dado calculado em AjustarMenorCaminho()
            verticeAtual = indiceDoMenor;
            doInicioAteAtual = percurso[indiceDoMenor].getDistancia();
            // visitamos o vértice com o menor dado desde o inicioDoPercurso
            vertices[verticeAtual].foiVisitado = true;

            AjustarMenorCaminho();
        }

        return MenorCaminho(inicioDoPercurso, finalDoPercurso);
        // após encontrarmos os caminhos no vetor, chamamos o método MenorCaminho para colocá-lo em uma pilha
    }

    private int ObterMenor()
    {
        // retorna o indice da cidade com o menor valor do criterio escolhido entre todas as cidades ainda não visitadas
        int dadoMinimo = infinity;
        int indiceDaMinima = -1;

        for (int j = 0; j < qtasCidades; j++)
        {
            if (!(vertices[j].foiVisitado) && (percurso[j].getDistancia() < dadoMinimo))
            {
                dadoMinimo = percurso[j].getDistancia();
                indiceDaMinima = j;
            }
        }

        return indiceDaMinima;
    }

    private void AjustarMenorCaminho()
    {
        // percorre todas as cidades ainda não visitadas e ajusta o melhor caminho desde a cidadeOrigem até cada uma delas,
        // alterando seus respectivos dados no vetor percurso
        for (int coluna = 0; coluna < qtasCidades; coluna++)
        {
            if (!vertices[coluna].foiVisitado) // para cada vértice ainda não visitado
            {
                // acessamos o valor do criterio desde o vértice atual (pode ser infinity)
                int atualAteMargem = 0;
                atualAteMargem = matriz[verticeAtual][coluna].getDistancia();

                // calculamos o valor do criterio desde inicioDoPercurso passando por vertice atual até esta saída
                int doInicioAteMargem = doInicioAteAtual + atualAteMargem;
                // quando encontra um dado menor, marca o vértice a partir do
                // qual chegamos no vértice de índice coluna, e a soma do valor
                // do caminho até ele
                int dadoDoCaminho = percurso[coluna].getDistancia();
                if (doInicioAteMargem < dadoDoCaminho && doInicioAteMargem>0)
                {
                    percurso[coluna].setVerticePai(verticeAtual);
                    percurso[coluna].setDistancia(doInicioAteMargem);
                }
            }
        }
    }

    private PilhaLista<CaminhoCidade> MenorCaminho(int inicioDoPercurso, int finalDoPercurso)
    {
        // coleta os dados no vetor percurso do melhor caminho encontrado, do fim para o começo
        // e o colocá em uma PilhaLista de CaminhoEntreCidades, retornando
        int onde = finalDoPercurso;
        PilhaLista<CaminhoCidade> pilha = new PilhaLista<CaminhoCidade>();

        //pega o total ate o id da cidadeDestino
        int idDestino = Integer.parseInt(vertices[onde].getRotulo());
        total = percurso[idDestino].getDistancia();

        while (onde != inicioDoPercurso)
        {
            idDestino = Integer.parseInt(vertices[onde].getRotulo());
            int idOrigem = percurso[idDestino].getVerticePai();
            onde = idOrigem;
            pilha.Empilhar(new CaminhoCidade(idOrigem + "", idDestino + ""));
        }

        return pilha;
    }
}



