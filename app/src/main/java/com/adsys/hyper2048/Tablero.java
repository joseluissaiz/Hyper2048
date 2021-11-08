package com.adsys.hyper2048;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tablero {
    private final Juego juego;
    private Casilla[][] casillas;
    private Ficha[][] fichas;

    //Visual
    private RectF cajaDeGolpe;
    private RectF cajaDeGolpeSombra;
    private Paint pincelSombra;
    private Paint pincelTablero;
    private Paint pincelBordes;

    //---------------------------------------------------------------------------------CONSTRUCTORES

    public Tablero(Juego juego, int filas, int columnas){
        this.juego = juego;
        this.casillas = new Casilla[filas][columnas];
        this.fichas = new Ficha[filas][columnas];
        this.cajaDeGolpe = obtenerCoordenadas();
        this.establecerCajasGraficas();
        //Rellenamos los huecos de las celdas con casillas
        this.establecerCasillas();
        agregarFichaNueva();
        agregarFichaNueva();

    }

    //---------------------------------------------------------------------------------------GESTION

    private RectF obtenerCoordenadas(){
        AdaptadorVista adaptador = juego.obtenerAdaptador();

        //Obtenemos el centro de la pantalla
        float[] centroPantalla = juego.obtenerAdaptador().obtenerCentroDelMonitor();
        float centroX = centroPantalla[0];
        float centroY = centroPantalla[1];
        //Fijamos los margenes para que quede en medio
        int margenX = 50;

        int margenY = 500;
        //Marcamos la altura y anchura del tablero
        float alturaTablero = adaptador.altoMonitor - adaptador.adaptarY(margenY);
        float anchoTablero = adaptador.anchoMonitor - adaptador.adaptarX(margenX);
        //Ajustamos los margenes del centro
        float margenIzquierda = centroX-anchoTablero/2;
        float margenArriba = (centroY-alturaTablero/2) + adaptador.adaptarY(40);
        float margenDerecha = centroX+anchoTablero/2;
        float margenAbajo = centroY + alturaTablero / 2 + adaptador.adaptarY(40);

        return new RectF(
                margenIzquierda,
                margenArriba,
                margenDerecha,
                margenAbajo);
    }

    private void establecerCajasGraficas(){
        AdaptadorVista adaptador = juego.obtenerAdaptador();

        this.cajaDeGolpeSombra = new RectF(
                cajaDeGolpe.left,
                cajaDeGolpe.top,
                cajaDeGolpe.right + adaptador.adaptarX(3),
                cajaDeGolpe.bottom + adaptador.adaptarY(3));


        pincelSombra = new Paint();
        pincelSombra.setAntiAlias(true);
        pincelSombra.setColor(Color.parseColor("#000000"));
        pincelSombra.setAlpha(70);
        pincelSombra.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        pincelTablero = new Paint();
        pincelTablero.setAntiAlias(true);
        pincelTablero.setColor(Color.parseColor("#D5CCC6"));

        pincelBordes = new Paint();
        pincelBordes.setAntiAlias(true);
        pincelBordes.setColor(Color.parseColor("#8E846F"));
        pincelBordes.setStyle(Paint.Style.STROKE);
        pincelBordes.setAlpha(80);
        pincelBordes.setStrokeWidth(adaptador.adaptarY(3));


    }

    private void establecerCasillas(){
        for (int fx = 0; fx < casillas.length; fx++) {
            for (int cy = 0; cy < casillas[0].length; cy++) {
                casillas[fx][cy] = new Casilla(this, fx, cy);
            }
        }
    }

    public void agregarFichaNueva(){
        Random aleatorio = new Random();
        List<Casilla> casillasVacias = obtenerCasillasVacias();
        if (casillasVacias.size() > 0){
            Casilla casillaVacia = casillasVacias.get(aleatorio.nextInt(casillasVacias.size()));
            fichas[casillaVacia.obtenerReferenciaX()][casillaVacia.obtenerReferenciaY()] = new Ficha(casillaVacia);//fallo aqui
        }
    }

    private List<Casilla> obtenerCasillasVacias(){
        //Creamos una lista con las casillas que estan llenas
        List<Casilla> listaCasillasLlenas = new ArrayList<>();
        for (int fx = 0; fx < fichas.length; fx++) {
            for (int cy = 0; cy < fichas[0].length; cy++) {
                if (fichas[fx][cy] != null){
                    listaCasillasLlenas.add(fichas[fx][cy].obtenerReferencia());
                }
            }
        }
        //Creamos una lista con todas las casillas
        List<Casilla> listaCasillasVacias = new ArrayList<Casilla>(){};
        for (int fx = 0; fx < casillas.length; fx++) {
            for (int cy = 0; cy < casillas[0].length; cy++) {
                listaCasillasVacias.add(casillas[fx][cy]);
            }
        }
        //Eliminamos de la lista de todas las casillas aquellas que esten llenas
        for (Casilla casillaLlena:listaCasillasLlenas) {
            listaCasillasVacias.remove(casillaLlena);
        }

        return listaCasillasVacias;
    }

    public void eliminarFicha(Ficha fichaEliminar) {
        for (int fx = 0; fx < fichas.length; fx++) {
            for (int cy = 0; cy < fichas[0].length; cy++) {
                if (fichas[fx][cy] == fichaEliminar){
                    fichas[fx][cy] = null;
                    Log.d("TABLERO-ELIMINAR-FICHA", "Se ha eliminado 1 ficha");
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------------MOTOR

    public void actualizar(){}

    //--------------------------------------------------------------------------------------GRAFICOS

    public void dibujar(Canvas lienzo){
        dibujarSombraTablero(lienzo);
        dibujarTablero(lienzo);
        dibujarBordesTablero(lienzo);
        dibujarCasillas(lienzo);
        dibujarFichas(lienzo);
    }

    private void dibujarTablero(Canvas lienzo){
        lienzo.drawRoundRect(cajaDeGolpe,20,20, pincelTablero);
    }

    private void dibujarSombraTablero(Canvas lienzo){
        lienzo.drawRoundRect(cajaDeGolpeSombra,20,20, pincelSombra);
    }

    private void dibujarBordesTablero(Canvas lienzo){
        lienzo.drawRoundRect(cajaDeGolpe,20,20, pincelBordes);
    }

    private void dibujarCasillas(Canvas lienzo){
        for (int fx = 0; fx < casillas.length; fx++) {
            for (int cy = 0; cy < casillas[0].length; cy++) {
                casillas[fx][cy].dibujar(lienzo);
            }
        }
    }

    private void dibujarFichas(Canvas lienzo){
        for (int fx = 0; fx < fichas.length; fx++) {
            for (int cy = 0; cy < fichas[0].length; cy++) {
                if (fichas[fx][cy] != null){
                    fichas[fx][cy].dibujar(lienzo);
                }
            }
        }
    }

    //-------------------------------------------------------------------------------------------DTO

    public Casilla[][] obtenerCasillas() { return casillas; }

    public Ficha[][] obtenerFichas() { return fichas; }

    public int obtenerFilas(){ return this.casillas.length; }

    public int obtenerColumnas(){ return this.casillas[0].length; }

    public RectF obtenerCajaDeGolpe(){ return this.cajaDeGolpe; }

    public Juego obtenerJuego(){ return this.juego; }

}
