//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades.Backtracking;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.approtascidades.Grafo.CaminhoCidade;
import br.unicamp.approtascidades.Grafo.PilhaVetor;

public class GrafoBactracking
{
    protected int [][] adjacencia;
    int qtasCidades = 23;
    private int infinity = Integer.MAX_VALUE; // maior int possível, valor tão grande que podemos considerar "inexistente"
    private int total; // valor total do criterio escolhido para efetuar o menor caminho

    public int[][] getAdjacencia() {
        return adjacencia;
    }


    public void setAdjacencia(int[][] adjacencia) {
        this.adjacencia = adjacencia;
    }

    public GrafoBactracking(int tamanhoLinhas, int tamanhoColunas)
    {
        adjacencia = new int[tamanhoLinhas][tamanhoColunas];
    }


//////////////////////////////
    //Backtracking com Recursão//
    ////////////////////////////

    // variáveis utilizadas nos métodos de backtracking por recursão
    private List<PilhaVetor<CaminhoCidade>> listaCaminhos;  // lista de todos os caminhos possíveis
    private PilhaVetor<CaminhoCidade> caminho; // caminho atual
    private int cidadeAtual, saidaAtual;
    private boolean achouCaminho, naoTemSaida;
    private boolean[] passou;

    public List<PilhaVetor<CaminhoCidade>> BuscarCaminhosRec(int origem, int destino, CaminhoCidade matriz[][]) throws Exception {
        // (re)inicia as variáveis
        listaCaminhos = new ArrayList<PilhaVetor<CaminhoCidade>>();
        caminho = new PilhaVetor<CaminhoCidade>();
        cidadeAtual = origem;
        saidaAtual = 0;
        achouCaminho = naoTemSaida = false;
        passou = new boolean[qtasCidades];
        for (int indice = 0; indice < qtasCidades; indice++)   // inicia os valores de “passou” como false
            passou[indice] = false;

        // chama o método recursivo
        BuscarCaminhos(origem, destino, matriz );

        return listaCaminhos;
    }

    public void BuscarCaminhos(int origem, int destino, CaminhoCidade matriz[][] ) throws Exception {
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
                    if (matriz[cidadeAtual][saidaAtual].getDistancia() == infinity)
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
                        if(movim != null)
                        {
                            saidaAtual = Integer.parseInt(movim.getIdDestino().trim() + 1); // a cidade destino anterior não presta, ent vamos para a próxima
                            if(movim.getIdOrigem() == " ")
                                cidadeAtual = Integer.parseInt("0");
                            else
                            {
                                cidadeAtual = Integer.parseInt(movim.getIdOrigem().trim());
                            }
                            if(movim.getIdDestino() == " ")
                                cidadeAtual = Integer.parseInt("0");
                            else
                            {
                                passou[Integer.parseInt(movim.getIdDestino().trim())] = false;
                            }
                            saidaAtual++;
                        }
                    }
                    else
                        naoTemSaida = true;
                }
                else
                {
                    listaCaminhos.add(caminho.Clone());
                    achouCaminho = false;
                    saidaAtual++;
                    caminho.Desempilhar();
                }
            }
        }
        // após achar o caminho reinicia e procura outro
        BuscarCaminhos(origem, destino, matriz);
    }


    public PilhaVetor<CaminhoCidade> MenorCaminhoBacktracking(int linhas, List<PilhaVetor<CaminhoCidade>> cListaCaminhos, CaminhoCidade[][] matrizBacktracking) throws Exception {
        int menorDado = 0;
        PilhaVetor<CaminhoCidade> menorCaminho = null;

        for (int lin = 0; lin < linhas; lin++) // percorre a lista de caminhos
        {
            PilhaVetor<CaminhoCidade> pilhaCam = cListaCaminhos.get(lin); // pega o lin-ésimo caminho
            PilhaVetor<CaminhoCidade> copia = pilhaCam.Clone(); // faz-se cópia para não entragar os dados que serão utilizados posteriormente

            int dado = 0;
            while (!copia.EstaVazia()) // soma a distancia total do caminho atual
            {
                dado += copia.OTopo().Clone().getDistancia();
                copia.Desempilhar();
            }

            if (menorDado == 0 || dado < menorDado) // verifica se este é o menor caminho até então
            {
                menorDado = dado;
                menorCaminho = pilhaCam.Clone();
            }
        }

        total = menorDado;
        return menorCaminho;
    }
}
