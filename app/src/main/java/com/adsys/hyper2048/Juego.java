package com.adsys.hyper2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.text.DecimalFormat;

public class Juego extends SurfaceView implements SurfaceHolder.Callback {
    private final AdaptadorVista adaptadorVista;
    private final MotorJuego motorJuego;
    public SurfaceHolder soporteDeSuperficie;
    public LoopJuego loopJuego;
    public Context contexto;
    public Tablero tablero;
    private final Typeface fuenteTitulo;

    //----------------------------------------------------------------------------------------------

    public Juego(Context contexto) {
        super(contexto);
        this.contexto = contexto;
        //Damos valor a diatintas variables
        Typeface familiaFuenteTitulo = ResourcesCompat.getFont(contexto, R.font.titulo);
        fuenteTitulo = Typeface.create(familiaFuenteTitulo, Typeface.BOLD);
        //Añadimos los adaptadores
        adaptadorVista = new AdaptadorVista(contexto);
        //Creamos un tablero nuevo
        tablero = new Tablero(this,4,4);
        //Iniciamos el motor del juego
        motorJuego = new MotorJuego(this);
        //Obtenemos el soporte de la superficie y establecemos la llamada de vuelta
        soporteDeSuperficie = getHolder();
        soporteDeSuperficie.addCallback(this);
        //Creamos un nuevo loop del juego
        loopJuego = new LoopJuego(this, soporteDeSuperficie, "JUEGO");



    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        loopJuego.iniciarLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    //----------------------------------------------------------------------------------------------

    public void dibujar(Canvas lienzo) {
        synchronized (this){
            super.draw(lienzo);
            dibujarEstatico(lienzo);
            tablero.dibujar(lienzo);
            //Tareas de debug
            dibujarUPS(lienzo);
            dibujarFPS(lienzo);
        }

    }

    public void dibujarEstatico(Canvas lienzo){
        synchronized (this){
            dibujarFondo(lienzo);
            dibujarCabecera(lienzo);
        }

    }

    public void dibujarUPS(Canvas lienzo){
        DecimalFormat formatoDecimal = new DecimalFormat("#.##");
        String promedioUPS = formatoDecimal.format(loopJuego.obtenerPromedioUPS());
        Paint pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setColor(Color.DKGRAY);
        pincel.setTextSize(20);
        lienzo.drawText("UPS: "+promedioUPS, 50, 50, pincel);
    }

    public void dibujarFPS(Canvas lienzo){
        DecimalFormat formatoDecimal = new DecimalFormat("#.##");
        String promedioFPS = formatoDecimal.format(loopJuego.obtenerPromedioFPS());
        Paint pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setColor(Color.DKGRAY);
        pincel.setTextSize(20);
        lienzo.drawText("FPS: "+promedioFPS, 50, 80, pincel);
    }

    public void dibujarCabecera(Canvas lienzo){
        //Dibujando el titulo
        //2048
        Paint pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setColor(Color.DKGRAY);
        pincel.setTextSize(70);
        pincel.setTypeface(fuenteTitulo);
        float titulo1Y = tablero.obtenerCajaDeGolpe().top - adaptadorVista.adaptarY(90);
        float titulo1X = tablero.obtenerCajaDeGolpe().left + adaptadorVista.adaptarX(30);
        lienzo.drawText("2048", titulo1X, titulo1Y, pincel);
        //hyper

        pincel = new Paint();
        pincel.setTypeface(fuenteTitulo);
        pincel.setColor(Color.parseColor("#49CAC5"));
        pincel.setTextSize(40);
        float titulo2Y = titulo1Y + adaptadorVista.adaptarY(30);
        lienzo.drawText("hyper", titulo1X, titulo2Y, pincel);

    }

    public void dibujarFondo(Canvas lienzo){
        Paint pincel = new Paint();
        pincel.setColor(Color.parseColor("#FFF6E6"));
        lienzo.drawRect(0,0, lienzo.getWidth(), lienzo.getHeight(), pincel);
    }

    //----------------------------------------------------------------------------------------------

    public void actualizar(){
        this.motorJuego.actualizar();
    }

    //----------------------------------------------------------------------------------------------

    public AdaptadorVista obtenerAdaptador(){
        return this.adaptadorVista;
    }

    public MotorJuego obtenerMotorDelJuego() { return this.motorJuego; }

    public Tablero obtenerTablero() { return  this.tablero; }
}
