//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades.Backtracking;

public class GrafoBactracking
{
    protected int [][] adjacencia;

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
}
