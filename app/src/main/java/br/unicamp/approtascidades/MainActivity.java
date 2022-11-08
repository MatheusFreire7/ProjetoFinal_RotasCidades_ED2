package br.unicamp.approtascidades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import br.unicamp.approtascidades.Grafo.CaminhoCidade;
import br.unicamp.approtascidades.Grafo.Cidade;
import br.unicamp.approtascidades.Grafo.PilhaVetor;
import br.unicamp.approtascidades.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    Cidade vetorCidade[];
    CaminhoCidade vetorCaminho[][];
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Spinner spinner = (Spinner) findViewById(R.id.numOrigem);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.cidades_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

        Spinner spinner2 = (Spinner) findViewById(R.id.numDestino);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.cidades_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener());

        ImageView mapa;

        mapa = findViewById(R.id.mapa);
        Picasso.get().load(R.drawable.mapa).into(mapa);
        binding.btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean checkedRecursao = ((CheckBox) binding.chkRecursao).isChecked();
                    boolean checkedDijkstra = ((CheckBox) binding.chkDijkstra).isChecked();

                    if(checkedRecursao == true)
                    {
                        lerCidades();
//                        Spinner sp =	(Spinner)findViewById(R.id.numOrigem);
//                        String spinnerString = null;
//                        spinnerString = sp.getSelectedItem().toString();
//                        int nPos = sp.getSelectedItemPosition();
//
//
//                        Toast.makeText(getApplicationContext(), "getSelectedItem=" + spinnerString,
//                                Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), "getSelectedItemPosition=" + nPos,
//                                Toast.LENGTH_LONG).show();
                    }

                    if(checkedDijkstra == true)
                    {
                        lerArquivoCidade();
                    }

                    if(checkedDijkstra == false && checkedRecursao == false)
                        Toast.makeText(MainActivity.this, "Clique em um dos checkedBox para poder Buscar", Toast.LENGTH_LONG).show();


                } catch (IOException | NullPointerException e) {
                    Toast.makeText(MainActivity.this, "Erro na Leitura do Arquivo", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void lerArquivoCidade() throws IOException {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("CidadesMarte.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine())!= null){
                linhas.add(linha);
                JSONObject objCidade = new JSONObject(linha);
                binding.lista.setText(objCidade.toString());
            }
            inputStream.close();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void lerCidades(){
        String result;
        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.cidades);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            result = new String(b);
            //Cidade umCid= new Cidade(result);
            binding.lista.setText(result);
            //vetorCidade.Empilhar(umCid);
        } catch (Exception e) {
            // e.printStackTrace();
            result = "Error: can't show file.";
        }
    }

    public void lerArquivoCaminho() throws IOException {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("Caminho.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linha;
            LinkedList<String> linhas = new LinkedList<String>();
            while ((linha = bufferedReader.readLine())!= null){
                linhas.add(linha);
                JSONObject objCaminho = new JSONObject(linha);
                binding.lista.setText(objCaminho.toString());
            }
            inputStream.close();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    public void checkBox(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.chkRecursao:
                if (checked)
                {
                    Toast.makeText(this, "Clicou em recursão", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.chkDijkstra:
                if (checked)
                {
                    Toast.makeText(this, "Clicou em Dijkstra", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}