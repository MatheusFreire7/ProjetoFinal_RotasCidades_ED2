package br.unicamp.approtascidades.Backtracking;

public class NoArvore<Dado extends Comparable<? super Dado>>
{
    Dado info;
    NoArvore<Dado> esq;
    NoArvore<Dado> dir;
    int altura;
    private boolean estaMarcadoParaMorrer;

    public NoArvore(Dado informacao)
    {
        this.info = informacao;
        this.esq = null;
        this.dir = null;
        this.altura = 0;
        this.estaMarcadoParaMorrer = false;
    }

    public NoArvore(Dado dados, NoArvore<Dado> esquerdo, NoArvore<Dado> direito, int altura)
    {
        this.info = dados;
        this.esq = esquerdo;
        this.dir = direito;
        this.altura = altura;
    }

    public Dado getInfo() {
        return info;
    }

    public void setInfo(Dado info) {
        this.info = info;
    }

    public NoArvore<Dado> getEsq() {
        return esq;
    }

    public void setEsq(NoArvore<Dado> esq) {
        this.esq = esq;
    }

    public NoArvore<Dado> getDir() {
        return dir;
    }

    public void setDir(NoArvore<Dado> dir) {
        this.dir = dir;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public boolean isEstaMarcadoParaMorrer() {
        return estaMarcadoParaMorrer;
    }

    public void setEstaMarcadoParaMorrer(boolean estaMarcadoParaMorrer) {
        this.estaMarcadoParaMorrer = estaMarcadoParaMorrer;
    }


    public static <Dado extends Comparable<Dado>> int compareTo(Dado rect, Dado outroRect) {
        return outroRect.compareTo(rect);
    }

    public boolean Equals(Dado o)
    {
        return this.info.equals(o);
    }


}
