package br.unicamp.approtascidades.Grafo;

public class CaminhoCidade
{
    private String idOrigem;
    private String idDestino;
    private int distancia;
    private int tempo;
    private int custo;

    public CaminhoCidade(String idOrigem, String idDestino, int distancia, int tempo, int custo) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
        this.distancia = distancia;
        this.tempo = tempo;
        this.custo = custo;
    }

    public CaminhoCidade(String idOrigem) {
        this.idOrigem = idOrigem;
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
}
