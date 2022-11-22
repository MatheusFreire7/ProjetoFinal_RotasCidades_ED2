package br.unicamp.approtascidades.Solucao;

import java.util.ArrayList;
import java.util.List;

public class PilhaLista<Dado extends Comparable<? super Dado>> extends ListaSimples<Dado> implements Comparable<Dado>
{
    public Dado Desempilhar() throws Exception {
        if (EstaVazia())
            throw new Exception("pilha vazia!");

        Dado valor = super.getPrimeiro().getInfo();

        NoLista<Dado> pri = super.getPrimeiro();
        NoLista<Dado> ant = null;
        super.RemoverNo( pri,  ant);
        return valor;
    }

    public void Empilhar(Dado elemento)
    {
        super.InserirAntesDoInicio
                (
                        new NoLista<Dado>(elemento, null)
                );
    }

    public boolean EstaVazia()
    {
        return super.EstaVazia();
    }

    public Dado OTopo() throws Exception {
        if (EstaVazia())
            throw new Exception("pilha vazia!");

        return super.getPrimeiro().getInfo();
    }

    public int getTamanho() { return  super.getQuantosNos(); }




    public PilhaLista<Dado> Copia() throws Exception {
        PilhaLista<Dado> copia = new PilhaLista<Dado>();  // nova instância para retorno
        List<Dado> aux = new ArrayList();
        //Dado[] aux = (Dado[]) new Object[this.getTamanho()]; // vetor auxiliar

        for (int i = 0; !this.EstaVazia(); i++)
        {
            aux.add( this.OTopo()); // guarda-se a pilha em um vetor auxiliar na ordem original
            this.Desempilhar();
        }

        for (int i = aux.size() - 1; i >= 0; i--)
        {
            this.Empilhar(aux.get(i)); // vai empilhando de trás para frente na pilha original, para manter a ordem
            copia.Empilhar(aux.get(i));
        }

        return copia;
    }

    public PilhaLista<Dado> CopiaInvertida() throws Exception {
        PilhaLista<Dado> copia = new PilhaLista<Dado>(); // nova instância para retorno
        if(this.getTamanho() > 0)
        {
            List<Dado> aux = new ArrayList();
            //Dado[] aux = (Dado[]) new Object[this.getTamanho()]; // vetor auxiliar

            for (int i = 0; !this.EstaVazia(); i++)
            {
                aux.add(this.OTopo()); //guarda-se a pilha em um vetor auxiliar na ordem original
                this.Desempilhar();
                copia.Empilhar(aux.get(i)); //  empilha-se na cópia, para salvar em ordem invertida
            }

            for (int i = aux.size() - 1; i >= 0; i--)
            {
                this.Empilhar(aux.get(i)); // vai empilhando de trás para frente na pilha original, para manter a ordem
            }
        }
        return copia;
    }

    @Override
    public int compareTo(Dado o) {
        return 0;
    }
}
