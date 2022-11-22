//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades.Solucao;

public class NoLista<Dado>
{
    private Dado info;
    private NoLista<Dado> prox;

    public Dado getInfo() {
        return info;
    }

    public void setInfo(Dado info) {
        this.info = info;
    }

    public NoLista<Dado> getProx() {
        return prox;
    }

    public void setProx(NoLista<Dado> prox) {
        this.prox = prox;
    }

    public NoLista(Dado info, NoLista<Dado> prox) {
        this.info = info;
        this.prox = prox;
    }
}
