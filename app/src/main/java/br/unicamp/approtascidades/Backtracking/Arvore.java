package br.unicamp.approtascidades.Backtracking;

public class Arvore<Dado extends Comparable<? super Dado>>
{
    private NoArvore<Dado> raiz, atual, antecessor;

    int quantosNos = 0;

    public NoArvore<Dado> getRaiz() {
        return raiz;
    }

    public void setRaiz(NoArvore<Dado> raiz) {
        this.raiz = raiz;
    }

    public NoArvore<Dado> getAtual() {
        return atual;
    }

    public void setAtual(NoArvore<Dado> atual) {
        this.atual = atual;
    }

    public NoArvore<Dado> getAntecessor() {
        return antecessor;
    }

    public void setAntecessor(NoArvore<Dado> antecessor) {
        this.antecessor = antecessor;
    }


    public void Incluir(Dado incluido) throws Exception    // inclusão usando o método de pesquisa binária
    {
        if (raiz == null)
        {
            raiz = new NoArvore<Dado>(incluido);
            return;
        }

        if (Existe(incluido))
            throw new Exception("Informação repetida");
        else
        {
            NoArvore<Dado> novoNo = new NoArvore<Dado>(incluido);

            if (incluido.compareTo(antecessor.info) < 0)
                antecessor.esq = novoNo;
            else
                antecessor.dir = novoNo;
            quantosNos++;
        }
    }

    private int QtosNos(NoArvore<Dado> noAtual)
    {
        if (noAtual == null)
            return 0;
        return 1 +                    // conta o nó atual
                QtosNos(noAtual.esq) + // conta nós da subárvore esquerda
                QtosNos(noAtual.dir);  // conta nós da subárvore direita
    }

    public int getQuantosNos() {
        return quantosNos;
    }

    private int QtasFolhas(NoArvore<Dado> noAtual)
    {
        if (noAtual == null)
            return 0;
        if (noAtual.esq == null && noAtual.dir == null) // noAtual é folha
            return 1;
        // noAtual não é folha, portanto procuramos as folhas de cada ramo e as contamos
        return QtasFolhas(noAtual.esq) + // conta folhas da subárvore esquerda
                QtasFolhas(noAtual.dir);  // conta folhas da subárvore direita
    }
    public int getQuantasFolhas()
    {
        int quantasFolhas = QtasFolhas(raiz);

        return quantasFolhas;
    }

    private int Altura(NoArvore<Dado> noAtual)
    {
        int alturaEsquerda,
                alturaDireita;

        if (noAtual == null)
            return 0;

        alturaEsquerda = Altura(noAtual.esq);
        alturaDireita = Altura(noAtual.dir);

        if (alturaEsquerda >= alturaDireita)
            return 1 + alturaEsquerda;
        return 1 + alturaDireita;
    }

    public int Altura()
    {
        return Altura(raiz);
    }

    public String InOrdem()  // propriedade que gera a string do percurso in-ordem da árvore
    {
        return FazInOrdem(raiz);
    }

    public String PreOrdem()  // propriedade que gera a string do percurso pre-ordem da árvore
    {
         return FazPreOrdem(raiz);
    }

    public String PosOrdem()  // propriedade que gera a string do percurso pos-ordem da árvore
    {
        return FazPosOrdem(raiz);
    }

    private String FazInOrdem(NoArvore<Dado> r)
    {
        if (r == null)
            return "";  // retorna cadeia vazia

        return FazInOrdem(r.esq) + " " +
                r.info + " " +
                FazInOrdem(r.dir);
    }

    private String FazPreOrdem(NoArvore<Dado> r)
    {
        if (r == null)
            return "";  // retorna cadeia vazia

        return r.info + " " +
                FazPreOrdem(r.esq) + " " +
                FazPreOrdem(r.dir);
    }

    private String FazPosOrdem(NoArvore<Dado> r)
    {
        if (r == null)
            return "";  // retorna cadeia vazia

        return FazPosOrdem(r.esq) + " " +
                FazPosOrdem(r.dir) + " " +
                r.info;
    }

    public boolean Existe(Dado procurado)
    {
        antecessor = null;
        atual = raiz;
        while (atual != null)
        {
            if (atual.info.compareTo(procurado) == 0) // atual aponta o nó com o registro procurado, antecessor aponta seu "pai"
                return true;

            antecessor = atual;
            if (procurado.compareTo(atual.info) < 0)
                atual = atual.esq;     // Desloca apontador para o ramo à esquerda
            else
                atual = atual.dir;     // Desloca apontador para o ramo à direita
        }
        return false;       // Se local == null, a chave não existe
    }
}

