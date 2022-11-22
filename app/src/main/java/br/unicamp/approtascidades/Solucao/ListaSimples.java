//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades.Solucao;

public class ListaSimples<Dado  extends Comparable<? super Dado>>
{
    NoLista<Dado> primeiro, ultimo, atual, anterior;
    int quantosNos;
    boolean primeiroAcessoDoPercurso;


    public ListaSimples()
    {
        primeiro = ultimo = anterior = atual = null;
        quantosNos = 0;
        primeiroAcessoDoPercurso = false;
    }

    public void PercorrerLista()
    {
        atual = primeiro;
        while (atual != null)
        {
            atual = atual.getProx();
        }
    }

    public void InserirAntesDoInicio(NoLista<Dado> novoNo)
    {
        if (EstaVazia())       // se a lista está vazia, estamos
            ultimo = novoNo; // incluindo o 1o e o último nós!

        novoNo.setProx(primeiro); // faz o novo nó apontar para o nó
        primeiro = novoNo;      // atualmente no início da lista
        quantosNos++;           // (que pode ser null)
    }

    public void InserirAposFim(NoLista<Dado> novoNo)
    {
        if (EstaVazia())
            primeiro = novoNo;
        else
            ultimo.setProx(novoNo);

        quantosNos++;
        ultimo = novoNo;
        ultimo.setProx(null); // garantimos final lógico da lista
    }

    public boolean ExisteDado(Dado outroProcurado)
    {
        anterior = null;
        atual = primeiro;

        //	Em seguida, é verificado se a lista está vazia. Caso esteja, é
        //	retornado false ao local de chamada, indicando que a chave não foi
        //	encontrada, e atual e anterior ficam valendo null

        if (EstaVazia())
            return false;

        // a lista não está vazia, possui nós

        // dado procurado é menor que o primeiro dado da lista:
        // portanto, dado procurado não existe
        if (outroProcurado.compareTo(primeiro.getInfo()) < 0)
            return false;

        // dado procurado é maior que o último dado da lista:
        // portanto, dado procurado não existe
        if (outroProcurado.compareTo(ultimo.getInfo()) > 0)
        {
            anterior = ultimo;
            atual = null;
            return false;
        }

        //	caso não tenha sido definido que a chave está fora dos limites de
        //	chaves da lista, vamos procurar no seu interior

        //	o apontador atual indica o primeiro nó da lista e consideraremos que
        //	ainda não achou a chave procurada nem chegamos ao final da lista

        boolean achou = false;
        boolean fim = false;

        //	repete os comandos abaixo enquanto não achou o RA nem chegou ao
        //	final da lista

        while (!achou && !fim)

            // se o apontador atual vale null, indica final da lista

            if (atual == null)
                fim = true;

                // se não chegou ao final da lista, verifica o valor da chave atual

            else

                // verifica igualdade entre chave procurada e chave do nó atual

                if (outroProcurado.compareTo(atual.getInfo()) == 0)
                    achou = true;
                else

                    // se chave atual é maior que a procurada, significa que
                    // a chave procurada não existe na lista ordenada e, assim,
                    // termina a pesquisa indicando que não achou. Anterior
                    // aponta o anterior ao atual, que foi acessado por
                    // último

                    if (atual.getInfo().compareTo(outroProcurado) > 0)
                        fim = true;
                    else
                    {

                        // se não achou a chave procurada nem uma chave > que ela,
                        // então a pesquisa continua, de maneira que o apontador
                        // anterior deve apontar o nó atual e o apontador atual
                        // deve seguir para o nó seguinte

                        anterior = atual;
                        atual.setInfo(atual.getProx().getInfo());
                    }

        // por fim, caso a pesquisa tenha terminado, o apontador atual
        // aponta o nó onde está a chave procurada, caso ela tenha sido
        // encontrada, ou o nó onde ela deveria estar para manter a
        // ordenação da lista. O apontador anterior aponta o nó anterior
        // ao atual

        return achou;   // devolve o valor da variável achou, que indica
    }           // se a chave procurada foi ou não encontrado

    public void InserirEmOrdem(Dado dados)
    {
        if (!ExisteDado(dados)) // existeChave configura anterior e atual
        {                       // aqui temos certeza de que a chave não existe
            // guarda dados no novo nó
            NoLista<Dado> novo = new NoLista<Dado>(dados, null);
            if (EstaVazia())      // se a lista está vazia, então o
                InserirAntesDoInicio(novo);  // novo nó é o primeiro da lista
            else
                // testa se nova chave < primeira chave
                if (anterior == null && atual != null)
                    InserirAntesDoInicio(novo); // liga novo antes do primeiro
                else
                    // testa se nova chave > última chave
                    if (anterior != null && atual == null)
                        InserirAposFim(novo);
                    else
                        InserirNoMeio(novo);  // insere entre os nós anterior e atual
        }
    }

    private void InserirNoMeio(NoLista<Dado> novo)
    {
        anterior.setProx(novo);   // liga anterior ao novo
        novo.setProx(atual);      // e novo no atual

        if (anterior == ultimo)  // se incluiu ao final da lista,
            ultimo = novo;       // atualiza o apontador ultimo
        quantosNos++;            // incrementa número de nós da lista     	}
    }
    public boolean removerDado(Dado dados)
    {
        if (ExisteDado(dados))
        {
            // existeDado posicionou atual e anterior
            RemoverNo( atual, anterior);
            return true;
        }

        return false;
    }

    public void RemoverNo(NoLista<Dado> atual,  NoLista<Dado> anterior)
    {
        if (!EstaVazia())
        {
            if (atual == primeiro)
            {
                primeiro = primeiro.getProx();
                if (EstaVazia())
                    ultimo = null;
            }
            else
            if (atual == ultimo)
            {
                ultimo = anterior;
                ultimo.setProx(null);
            }
            else
                anterior.setProx( atual.getProx());

            quantosNos--;
        }
    }

    private void IniciarPercursoSequencial()
    {
        primeiroAcessoDoPercurso = true;
        atual = primeiro;
        anterior = null;
    }

    private boolean PodePercorrer()
    {
        if (!primeiroAcessoDoPercurso)
        {
            anterior = atual;
            atual = atual.getProx();
        }
        else
            primeiroAcessoDoPercurso = false;

        return atual != null;
    }


    public NoLista<Dado> getPrimeiro() {
        return primeiro;
    }

    public void setPrimeiro(NoLista<Dado> primeiro) {
        this.primeiro = primeiro;
    }

    public NoLista<Dado> getUltimo() {
        return ultimo;
    }

    public void setUltimo(NoLista<Dado> ultimo) {
        this.ultimo = ultimo;
    }

    public NoLista<Dado> getAtual() {
        return atual;
    }

    public void setAtual(NoLista<Dado> atual) {
        this.atual = atual;
    }

    public NoLista<Dado> getAnterior() {
        return anterior;
    }

    public void setAnterior(NoLista<Dado> anterior) {
        this.anterior = anterior;
    }

    public int getQuantosNos() {
        return quantosNos;
    }

    public void setQuantosNos(int quantosNos) {
        this.quantosNos = quantosNos;
    }

    public boolean EstaVazia()
    {
        if(primeiro == null)
            return true;
        else
            return false;
    }

}
