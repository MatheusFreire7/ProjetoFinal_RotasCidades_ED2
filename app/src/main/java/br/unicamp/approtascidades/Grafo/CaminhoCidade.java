//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades.Grafo;

import android.util.Log;

public class CaminhoCidade implements Comparable<CaminhoCidade> {
    private String idOrigem;
    private String idDestino;
    private int distancia;
    private int tempo;
    private int custo;

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

    public CaminhoCidade(String idOrigem, String idDestino, int distancia, int tempo, int custo) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
        this.distancia = distancia;
        this.tempo = tempo;
        this.custo = custo;
    }

    public CaminhoCidade(String idOrigem, String idDestino, int distancia, int tempo) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
        this.distancia = distancia;
        this.tempo = tempo;
    }

    public CaminhoCidade(int distancia, int tempo, int custo) {
        this.distancia = distancia;
        this.tempo = tempo;
        this.custo = custo;
    }

    public CaminhoCidade(String idOrigem, int distancia) {
        this.idOrigem = idOrigem;
        this.distancia = distancia;
    }

    public CaminhoCidade(String linha)
    {
        if(linha != null)
        {
//            Log.d("linha", linha);
            this.idOrigem = linha.substring(0, tamanhoIdOrigem).replace(" ","");
            this.idDestino = linha.substring(iniIdDestino, iniIdDestino+tamanhoIdDestino).replace(" ", "");
            this.distancia = Integer.parseInt(linha.substring(iniDistancia, iniDistancia+tamanhoDistancia));
            this.tempo = Integer.parseInt(linha.substring(iniTempo, iniTempo+ tamanhoTempo));
            this.custo = Integer.parseInt(linha.substring(iniCusto, iniCusto+tamanhoCusto));
        }
    }

    public CaminhoCidade()
    {}


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

    public int getCusto() {
        return custo;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }

    public CaminhoCidade Clone() throws Exception
    {
        CaminhoCidade ret = null;
        try {
            ret = new CaminhoCidade();
            return ret;
        } catch (Exception erro) {
            throw new Exception("Erro no clone");
        }
    }

    @Override
    public int compareTo(CaminhoCidade o)
    {
       if(this.distancia == o.getDistancia())
           return 0;
       else
       if(o.getDistancia() > this.distancia)
           return 1;
       else
           return 2;

    }
}
