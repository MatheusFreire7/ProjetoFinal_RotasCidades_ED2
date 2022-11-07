package br.unicamp.approtascidades.Grafo;

import java.util.ArrayList;

public class PilhaVetor<Dado> implements IStack<Dado> {
    Dado[] p;
    int topo;
    public PilhaVetor(int maximo)
    {
        p =  (Dado[]) new Object[maximo];
        topo = -1;          // pois acabamos de criar a pilha, VAZIA
    }

    public PilhaVetor()
    { }

    public int Tamanho(){ return topo + 1;}

    public boolean EstaVazia(){
        if(topo < 0)
            return true;
        else
            return false;
    }
    public void Empilhar(Dado dado) throws Exception
    {
        if (Tamanho() == p.length)
            throw new Exception("`Pilha cheia (Stack Overflow)!");

        topo = topo + 1;    // ou apenas
        p[topo] = dado;     // p[++topo] = dado;
    }
    public Dado Desempilhar()throws Exception
    {
        if (EstaVazia())
            throw new Exception("Pilha vazia (Stack Underflow)!");
        Dado dadoEmpilhado = p[topo]; // ou
        topo = topo - 1;              // Dado dadoEmpilhado = p[topo--];
        return dadoEmpilhado;
    }
    public Dado OTopo() throws Exception
    {
        if (EstaVazia())
            throw new Exception("Pilha vazia (Stack Underflow)!");

        Dado dadoEmpilhado = p[topo]; // copia o conteúdo da posição topo
        // e não altera o valor do índice topo
        // ou seja, mantém o estado da pilha,
        // apenas consultou o dado empilhado
        // no topo da pilha
        return dadoEmpilhado;
    }

    public ArrayList<Dado> DadosDaPilha()
    {
        ArrayList<Dado> lista = new ArrayList<Dado>();

        for (int indice = 0; indice <= topo; indice++)
            lista.add(p[indice]);

        return lista;
    }
}
