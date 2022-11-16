//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades.Grafo;

public class Vertice {

    public boolean foiVisitado;
    public String rotulo;
    double x, y;// coordenadas cartesianas do vértice

    public Vertice(String label, double x, double y)
    {
        rotulo = label;
        foiVisitado = false;
        this.x = x;
        this.y = y;
    }

    public Vertice(String label)
    {
        rotulo = label;
        foiVisitado = false;
        this.x = 0;
        this.y = 0;
    }

    public boolean isFoiVisitado() {
        return foiVisitado;
    }

    public void setFoiVisitado(boolean foiVisitado) {
        this.foiVisitado = foiVisitado;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
