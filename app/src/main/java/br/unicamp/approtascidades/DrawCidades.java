package br.unicamp.approtascidades;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.approtascidades.Grafo.Cidade;

public class DrawCidades extends View
{
    private List<Cidade> listaCidades;
    Context context;
    Paint paint =  new Paint();
    Drawable mapa;

    public DrawCidades(Context context) {
        super(context);
        this.context = context;
    }

    public DrawCidades(Context context, List<Cidade> listaCidades)
    {
        super(context);
        this.context = context;
        this.listaCidades = listaCidades;
        paint.setColor(Color.BLACK);
    }

    public DrawCidades(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public DrawCidades(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    
    @SuppressLint("ResourceType")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int contCidades = 0;
        mapa = context.getResources().getDrawable(R.id.mapa);
        Log.d("TamanhoListaCanvas", listaCidades.size() + " ");
        while(listaCidades.size() > contCidades)
        {
            Log.d("largura", mapa.getIntrinsicWidth() + " ");
            double coordenadaX = listaCidades.get(contCidades).getCordenadaX() * mapa.getIntrinsicWidth();
            double coordenadaY =listaCidades.get(contCidades).getCordenadaY() * mapa.getIntrinsicHeight();
            canvas.drawCircle((float) coordenadaX,(float)coordenadaY,10f,paint);
            mapa.draw(canvas);
            canvas.save();
            contCidades++;
        }
    }

    @SuppressLint("ResourceType")
    public void draw()
    {
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas;
        canvas = new Canvas(bitmap);

        int contCidades = 0;
        mapa = context.getResources().getDrawable(R.id.mapa);
        Log.d("TamanhoListaCanvas", listaCidades.size() + " ");
        while(listaCidades.size() > contCidades)
        {
            Log.d("largura", mapa.getIntrinsicWidth() + " ");
            double coordenadaX = listaCidades.get(contCidades).getCordenadaX() * mapa.getIntrinsicWidth();
            double coordenadaY =listaCidades.get(contCidades).getCordenadaY() * mapa.getIntrinsicHeight();
            canvas.drawCircle((float) coordenadaX,(float)coordenadaY,10f,paint);
            mapa.draw(canvas);
            canvas.save();
            contCidades++;
        }
    }

}
