package com.adsys.hyper2048;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Casilla {
    private Tablero tablero;
    private int refX;
    private int refY;
    private RectF cajaDeGolpe;
    private Paint pincel;

    //----------------------------------------------------------------------------------------------

    public Casilla(Tablero tablero, int refX, int refY){
        this.tablero = tablero;
        this.refX = refX;
        this.refY = refY;
        this.cajaDeGolpe = obtenerCoordenadas();

        pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setColor(Color.parseColor("#888888"));
        pincel.setAlpha(80);
    }

    //----------------------------------------------------------------------------------------------

    private RectF obtenerCoordenadas(){
        AdaptadorVista adaptador = tablero.obtenerJuego().obtenerAdaptador();
        RectF tableroCajaGolpe = tablero.obtenerCajaDeGolpe();
        float margenY = 0;
        float margenX = 0;
        if (refY > 0){
            margenY = adaptador.adaptarY(6);
        } else {
            margenY = adaptador.adaptarY(6);
        }
        if (refX == 0){
            margenX = adaptador.adaptarX(12);
        } else {
            margenX = adaptador.adaptarX(12);
        }
        float anchoX = tableroCajaGolpe.width()/tablero.obtenerFilas();
        float altoY = tableroCajaGolpe.height()/tablero.obtenerColumnas();

        float margenIzquierda = (tableroCajaGolpe.left + ((anchoX*(refX+1)-anchoX)))+margenX;
        float margenArriba = tableroCajaGolpe.top + (altoY * refY)+margenY;
        float margenDerecha = tableroCajaGolpe.right - (tableroCajaGolpe.right - (anchoX*(refX+1)))+margenX;
        float margenAbajo = tableroCajaGolpe.bottom - (tableroCajaGolpe.height() - (altoY*refY)-altoY)-margenY;

        return new RectF(margenIzquierda, margenArriba, margenDerecha, margenAbajo);
    }

    //----------------------------------------------------------------------------------------------

    public void dibujar(Canvas lienzo){
        lienzo.drawRoundRect(cajaDeGolpe,20,20, pincel);
    }

    //----------------------------------------------------------------------------------------------

    public int obtenerReferenciaX(){ return refX; }

    public int obtenerReferenciaY(){ return refY; }

    public RectF obtenerCajaGolpe(){ return this.cajaDeGolpe; }

    public Tablero obtenerTablero() { return tablero; }

}
