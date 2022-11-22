//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades.Solucao;

import br.unicamp.approtascidades.Grafo.CaminhoCidade;

public class DadosOriginal
{
    // atributos

    private int custo,
            distancia, //para valorar cada peso
            tempo;
    private int verticePai;

    // construtor completo com todos os pesos e o verticePai
    public DadosOriginal(int vp, CaminhoCidade dados)
    {
        this.custo = dados.getCusto();
        this.distancia = dados.getDistancia();
        this.tempo = dados.getTempo();
        this.verticePai = vp;
    }
    // construtor apenas com o valor do criterio e o verticePai
    public DadosOriginal(int vp, int dado)
    {
        this.custo = dado;
        this.distancia = dado;
        this.tempo = dado;
        this.verticePai = vp;
    }

    public int getCusto() {
        return custo;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getVerticePai() {
        return verticePai;
    }

    public void setVerticePai(int verticePai) {
        this.verticePai = verticePai;
    }
}
