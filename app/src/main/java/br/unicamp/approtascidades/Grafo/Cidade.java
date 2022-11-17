//Luiz Henrique Parolim Domingues - 21248
//Matheus Henrique de Oliveira Freire - 21251

package br.unicamp.approtascidades.Grafo;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

public class Cidade
{

     final int tamCodigo = 2,
        tamNome = 16,
        tamX = 25,
        tamY = 31;

    final int iniCodigo = 0,
        iniNome = iniCodigo + tamCodigo,
        iniX = iniNome + tamNome,
        iniY = 25;

    public final int tamanhoCidade = 15;

    private int idCidade;
    private String nomeCidade;
    private double cordenadaX;
    private double cordenadaY;


    public int getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(int idCidade) {
        this.idCidade = idCidade;
    }

    public String getNomeCidade() {
        return nomeCidade;
    }

    public void setNomeCidade(String nomeCidade) {
        this.nomeCidade = nomeCidade;
    }

    public double getCordenadaX() {
        return cordenadaX;
    }

    public void setCordenadaX(double cordenadaX) {
        this.cordenadaX = cordenadaX;
    }

    public double getCordenadaY() {
        return cordenadaY;
    }

    public void setCordenadaY(double cordenadaY) {
        this.cordenadaY = cordenadaY;
    }

    public Cidade()  // construtor default (construtor vazio), sem parâmetros
    {
        idCidade = 0;
        nomeCidade = "";
        cordenadaX = 0.0;
        cordenadaY = 0.0;

    }

    public Cidade(int idCidade, String nomeCidade, double cordenadaX, double cordenadaY)
    {
        this.idCidade = idCidade;
        this.nomeCidade = nomeCidade;
        this.cordenadaX = cordenadaX;
        this.cordenadaY = cordenadaY;
    }

    public Cidade(String linha)
    {
        if(linha != null)
        {
//            Log.d("Linha", linha);
//            Log.d("Id", linha.substring(0,tamCodigo));
//            Log.d("x", linha.substring(16,23));
            this.idCidade = Integer.parseInt(linha.substring(0, tamCodigo).replace(" ",""));
            this.nomeCidade = linha.substring(iniNome, tamNome);
            this.cordenadaX = Double.parseDouble(linha.substring(iniX, tamX).replace(",", "."));
            this.cordenadaY = Double.parseDouble(linha.substring(iniY, tamY).replace(",", "."));
        }
    }

    public Cidade(int matricula)
    {
        this.idCidade = idCidade;
    }

    public int CompareTo(Cidade outroCid)
    {
        return idCidade - outroCid.idCidade;
    }

    public String ToString()
    {
        return idCidade + "";
    }

    public void LerArquivoJson()
    {
        Object obj = new JSONObject();
        File arqCidade = new File("Cidade.json");
        obj = arqCidade;


    }
}
