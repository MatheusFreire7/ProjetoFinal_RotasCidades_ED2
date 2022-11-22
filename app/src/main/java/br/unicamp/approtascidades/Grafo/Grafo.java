//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades.Grafo;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Grafo
{
    private final int NUM_VERTICES = 23;
    private Vertice[] vertices;
    private int[][] adjMatrix;
    private int numVerts;
    private  GridView gridView;
    private int linha, coluna = 0;

    /// DIJKSTRA
    DistOriginal[] percurso;
    int infinity = Integer.MAX_VALUE;
    int verticeAtual; // global que indica o vértice atualmente sendo visitado
    int doInicioAteAtual; // global usada para ajustar menor caminho com Djikstra
    int nTree;

    public int getNUM_VERTICES() {
        return NUM_VERTICES;
    }

    public Vertice[] getVertices() {return vertices;}

    public void setVertices(Vertice[] vertices) {
        this.vertices = vertices;
    }

    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    public void setAdjMatrix(int[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
    }

    public int getNumVerts() {
        return numVerts;
    }

    public void setNumVerts(int numVerts) {
        this.numVerts = numVerts;
    }

    public GridView getGridView() {
        return gridView;
    }

    public void setGridView(GridView gridView) {
        this.gridView = gridView;
    }


    public Grafo(GridView gridView)
    {
        this.gridView = gridView;
        vertices = new Vertice[NUM_VERTICES];
        adjMatrix = new int[NUM_VERTICES][NUM_VERTICES];
        numVerts = 0;
        nTree = 0;

        for (int j = 0; j < NUM_VERTICES; j++) // zera toda a matriz
            for (int k = 0; k < NUM_VERTICES; k++)
                adjMatrix[j][k] = infinity;

        percurso = new DistOriginal[NUM_VERTICES];
    }

    public Grafo(int tamanhoLinhas, int tamanhoColunas)
    {
        adjMatrix = new int[tamanhoLinhas][tamanhoColunas];
        vertices = new Vertice[tamanhoColunas];
        percurso = new DistOriginal[NUM_VERTICES];
    }


    public void NovoVertice(String label)
    {
        if(numVerts == vertices.length)
            numVerts = 0;
        try {
            vertices[numVerts] = new Vertice(label);
            numVerts++;
        }
        catch (Exception e)
        {
            Log.d("Erro:", e.getMessage());
        }
    }

    public void NovaAresta(int start, int end, int distancia)
    {
        adjMatrix[start][end] = distancia; // adjMatrix[eend, start] = 1; ISSO GERA CICLOS!!!
    }
    public String ExibirVertice(int v)
    {
        String vertice = vertices[v].rotulo + " ";
        return vertice;
    }
    public void ExibirVertice(int v, TextView txt)
    {
        txt.setText(vertices[v].rotulo + " ");
    }

    public int SemSucessores() // encontra e retorna a linha de um vértice sem sucessores
    {
        boolean temAresta;
        for (int linha = 0; linha < numVerts; linha++)
        {
            temAresta = false;
            for (int col = 0; col < numVerts; col++)
                if (adjMatrix[linha][col] > 0)
            {
                temAresta= true;
                break;
            }
            if (!temAresta)
                return linha;
        }
        return -1;
    }

    public void RemoverVertice(int vert)
    {
        if (gridView != null)
        {
            //MessageBox.Show($"Matriz de Adjacências antes de remover vértice {vert}");
            ExibirAdjacencias();
        }
        if (vert != numVerts-1)
        {
            for (int j = vert; j < numVerts-1; j++)// remove vértice do vetor
                vertices[j] = vertices[j+1];
                // remove vértice da matriz
            for (int row = vert; row < numVerts; row++)
                MoverLinhas(row, numVerts-1);
            for (int col = vert; col < numVerts; col++)
                MoverColunas(col, numVerts-1);
        }

        numVerts--;
        if (gridView != null)
        {
            //MessageBox.Show($"Matriz de Adjacências após remover vértice {vert}");
            ExibirAdjacencias();
            //MessageBox.Show("Retornando à ordenação");
        }
    }

    private void MoverLinhas(int row, int length)
    {
        if (row != numVerts-1)
            for (int col = 0; col < length; col++)
                adjMatrix[row][col] = adjMatrix[row+1][col]; // desloca para excluir
    }
    private void MoverColunas(int col, int length)
    {
        if (col != numVerts - 1)
            for (int row = 0; row < length; row++)
                adjMatrix[row][col] = adjMatrix[row][ col+1]; // desloca para excluir
    }
    public void ExibirAdjacencias()
    {
//        gridView.RowCount = numVerts+1;
//        gridView.ColumnCount = numVerts+1;
//        for (int j = 0; j < numVerts; j++)
//        {
//            dgv.Rows[j + 1].Cells[0].Value = vertices[j].rotulo;
//            dgv.Rows[0].Cells[j+1].Value = vertices[j].rotulo;
//            for (int k = 0; k < numVerts; k++)
//                dgv.Rows[j + 1].Cells[k + 1].Value = Convert.ToString(adjMatrix[j, k]);
//        }
    }

    private int ObterVerticeAdjacenteNaoVisitado(int v)
    {
        for (int j = 0; j <= numVerts - 1; j++)
            if ((adjMatrix[v][j] == 1) && (!vertices[j].foiVisitado))
        return j;
        return -1;
    }

    public String OrdenacaoTopologica() throws Exception
    {
        PilhaVetor<String> gPilha = new PilhaVetor<String>(); //guarda a sequência de vértices
        while (numVerts > 0)
        {
            int currVertex = SemSucessores();
            if (currVertex == -1)
                return "Erro: grafo possui ciclos.";
            gPilha.Empilhar(vertices[currVertex].rotulo); // empilha vértice
            RemoverVertice(currVertex);
        }
        String resultado = "Sequência da Ordenação Topológica: ";
        while (!gPilha.EstaVazia())
            resultado += gPilha.Desempilhar() + " "; // desempilha para exibir
        return resultado;
    }


    void ProcessarNo(int i)
    {
        System.out.print(vertices[i].rotulo);
    }
    public void PercursoEmProfundidadeRec(int[][]adjMatrix, int numVerts, int part)
    {
        int i;
        ProcessarNo(part);
        vertices[part].foiVisitado = true;
        for (i = 0; i < numVerts; ++i)
            if (adjMatrix[part][i] == 1 && !vertices[i].foiVisitado)
        PercursoEmProfundidadeRec(adjMatrix, numVerts, i);
    }

    public String Caminho(int inicioDoPercurso, int finalDoPercurso, List lista) throws Exception
    {
        lista = new ArrayList();
        for (int j = 0; j < numVerts; j++)
            vertices[j].foiVisitado = false;
        vertices[inicioDoPercurso].foiVisitado = true;
        for (int j = 0; j < NUM_VERTICES; j++)
        {
            // anotamos no vetor percurso a distância entre o inicioDoPercurso e cada vértice
            // se não há ligação direta, o valor da distância será infinity
            int tempDist = adjMatrix[inicioDoPercurso][j];
            percurso[j] = new DistOriginal(inicioDoPercurso, tempDist);
        }
        for (int nTree = 0; nTree < numVerts; nTree++)
        {
            // Procuramos a saída não visitada do vértice inicioDoPercurso com a menor distância
            int indiceDoMenor = ObterMenor();
            // e anotamos essa menor distância
            int distanciaMinima = percurso[indiceDoMenor].distancia;
            // o vértice com a menor distância passa a ser o vértice atual
            // para compararmos com a distância calculada em AjustarMenorCaminho()
            verticeAtual = indiceDoMenor;
            doInicioAteAtual = percurso[indiceDoMenor].distancia;
            // visitamos o vértice com a menor distância desde o inicioDoPercurso
            vertices[verticeAtual].foiVisitado = true;
            AjustarMenorCaminho(lista);
        }
        return ExibirPercursos(inicioDoPercurso, finalDoPercurso, lista);
    }

    public int ObterMenor()
    {
        int distanciaMinima = infinity;
        int indiceDaMinima = 0;
        for (int j = 0; j < numVerts; j++) {
            Log.d("percurso", percurso[j].distancia + "");
            if (!(vertices[j].foiVisitado))
                if(percurso[j].distancia < distanciaMinima && percurso[j].distancia != 0) {
                    distanciaMinima = percurso[j].distancia;
                    indiceDaMinima = j;

            }
        }
            return indiceDaMinima;

    }
    public void AjustarMenorCaminho(List lista)
    {
        lista = new ArrayList();
        for (int coluna = 0; coluna < numVerts; coluna++)
            if (!vertices[coluna].foiVisitado) // para cada vértice ainda não visitado
            {
                // acessamos a distância desde o vértice atual (pode ser infinity)
                int atualAteMargem = adjMatrix[verticeAtual][coluna];
                // calculamos a distância desde inicioDoPercurso passando por vertice atual
                // até esta saída
                int doInicioAteMargem = doInicioAteAtual + atualAteMargem;
                // quando encontra uma distância menor, marca o vértice a partir do
                // qual chegamos no vértice de índice coluna, e a soma da distância
                // percorrida para nele chegar
                int distanciaDoCaminho = percurso[coluna].distancia;
                if (doInicioAteMargem < distanciaDoCaminho)
                {
                    percurso[coluna].verticePai = verticeAtual;
                    percurso[coluna].distancia = doInicioAteMargem;
                    ExibirTabela(lista);
                }
            }
        lista.add( "==================Caminho ajustado==============");
        lista.add(" ");
    }

    public void ExibirTabela(List lista)
    {
        lista = new ArrayList();
        String dist = "";
        lista.add("Vértice\tVisitado?\tPeso\tVindo de");
        for (int i = 0; i < numVerts; i++)
        {
            if (percurso[i].distancia == infinity)
                dist = "inf";
            else
                dist = percurso[i].distancia + "";
            lista.add(vertices[i].rotulo + "\t" + vertices[i].foiVisitado +
                    "\t\t" + dist + "\t" + vertices[percurso[i].verticePai].rotulo);
        }
        lista.add("-----------------------------------------------------");
    }

    public String ExibirPercursos(int inicioDoPercurso, int finalDoPercurso,
                                  List lista) throws Exception {
        lista = new ArrayList();
        String resultado = "";
        for (int j = 0; j < numVerts; j++)
        {
            resultado += vertices[j].rotulo + "=";
            if (percurso[j].distancia == infinity)
                resultado += "inf";
            else
                resultado += percurso[j].distancia+" ";
            String pai = vertices[percurso[j].verticePai].rotulo;
            resultado += "(" + pai + ") ";
        }
        lista.add(resultado);
        lista.add(" ");
        lista.add(" ");
        lista.add("Caminho entre " + vertices[inicioDoPercurso].rotulo +
                " e " + vertices[finalDoPercurso].rotulo);
        lista.add(" ");
        int onde = finalDoPercurso;
        PilhaVetor<String> pilha = new PilhaVetor<String>();
        int cont = 0;
        while (onde != inicioDoPercurso)
        {
            onde = percurso[onde].verticePai;
            pilha.Empilhar(vertices[onde].rotulo);
            cont++;
        }
        resultado = "";
        while (pilha.getTamanho() != 0)
        {
            resultado += pilha.Desempilhar();
            if (pilha.getTamanho() != 0)
                resultado += " --> ";
        }
        if ((cont == 1) && (percurso[finalDoPercurso].distancia == infinity))
            resultado = "Não há caminho";
        else
            resultado += " --> " + vertices[finalDoPercurso].rotulo;
        return resultado;
    }

}
