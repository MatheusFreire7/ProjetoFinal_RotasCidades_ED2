package br.unicamp.approtascidades.Grafo;

public interface IStack<Dado>{
    void Empilhar(Dado dado) throws Exception;   // empilha o objeto "dado"
    Dado Desempilhar() throws Exception;         // remove e retorna o objeto do topo
    Dado OTopo() throws Exception;               // retorna o objeto do topo sem removê-lo
    int Tamanho();     // informa a quantidade de itens empilhados
    boolean EstaVazia() ;    // informa se a pilha está ou não vazia
}
