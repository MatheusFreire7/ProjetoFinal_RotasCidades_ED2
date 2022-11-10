package br.unicamp.approtascidades.Grafo;

import android.util.Log;

public class CaminhoCidade
{
    private String idOrigem;
    private String idDestino;
    private int distancia;
    private int tempo;
    private String custo;

    final int tamanhoIdOrigem = 2,
              tamanhoIdDestino = 2,
              tamanhoDistancia = 4,
              tamanhoTempo = 2,
              tamanhoCusto = 3;

    final int iniIdOrigem = 0,
              iniIdDestino = iniIdOrigem + tamanhoIdOrigem,
              iniDistancia = iniIdDestino + tamanhoIdDestino,
              iniTempo = iniDistancia + tamanhoDistancia,
              iniCusto = iniTempo + tamanhoTempo;

    public CaminhoCidade(String idOrigem, String idDestino, int distancia, int tempo, String custo) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
        this.distancia = distancia;
        this.tempo = tempo;
        this.custo = custo;
    }

    public CaminhoCidade(String linha)
    {
        if(linha != null)
        {
            Log.d("linha", linha);
            this.idOrigem = linha.substring(0, tamanhoIdOrigem).replace(" ","");
            this.idDestino = linha.substring(iniIdDestino, iniIdDestino+tamanhoIdDestino).replace(" ", "");
            this.distancia = Integer.parseInt(linha.substring(iniDistancia, iniDistancia+tamanhoDistancia));
            this.tempo = Integer.parseInt(linha.substring(iniTempo, iniTempo+ tamanhoTempo));
            this.custo = linha.substring(iniCusto, iniCusto+tamanhoCusto).replace(" ", "");
        }
    }

    public CaminhoCidade(String idOrigem, String idDestino) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
    }

    public String getIdOrigem() {
        return idOrigem;
    }

    public void setIdOrigem(String idOrigem) {
        this.idOrigem = idOrigem;
    }

    public String getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(String idDestino) {
        this.idDestino = idDestino;
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

    public String getCusto() {
        return custo;
    }

    public void setCusto(String custo) {
        this.custo = custo;
    }
}
