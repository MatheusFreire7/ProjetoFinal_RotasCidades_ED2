package br.unicamp.approtascidades.Grafo;

import android.os.Message;
import android.widget.GridView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Grafo {
    private final int NUM_VERTICES = 20;
    private Vertice[] vertices;
    private int[][] adjMatrix;
    int numVerts;
    GridView gridView;


    public Grafo(GridView gridView)
    {
        this.gridView = gridView;
        vertices = new Vertice[NUM_VERTICES];
        adjMatrix = new int[NUM_VERTICES][NUM_VERTICES];
        numVerts = 0;
        for (int j = 0; j < NUM_VERTICES; j++) // zera toda a matriz
            for (int k = 0; k < NUM_VERTICES; k++)
                adjMatrix[j][k] = 0;
    }

    public void NovoVertice(String label)
    {
        vertices[numVerts] = new Vertice(label);
        numVerts++;
        if (gridView != null) // se foi passado como parâmetro um dataGridView para exibição
        { // se realiza o seu ajuste para a quantidade de vértices

        }
    }

    public void NovaAresta(int start, int end)
    {
        adjMatrix[start][end] = 1; // adjMatrix[eend, start] = 1; ISSO GERA CICLOS!!!
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



}
