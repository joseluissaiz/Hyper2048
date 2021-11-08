package com.adsys.hyper2048;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.BoolRes;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;

import java.util.Random;

public class Ficha {
    private Casilla ref;
    private RectF cajaDeGolpe;
    private RectF cajaDeAnimacion;
    private boolean animAparicion;
    private int numero;
    private int color;
    private int colorNumero;
    private boolean seEstaMoviendo;

    //---------------------------------------------------------------------------------CONSTRUCTORES

    public Ficha(Casilla casillaAparicion){
        this.ref = casillaAparicion;
        this.cajaDeGolpe = new RectF(casillaAparicion.obtenerCajaGolpe());
        this.numero = asignarNumeroAleatorio();
        this.color = obtenerColor();
        this.colorNumero = obtenerColorNumero();
        this.cajaDeAnimacion = new RectF();
        ajustarCajaDeAnimacion();
        this.animAparicion = true;
        seEstaMoviendo = false;
    }

    //---------------------------------------------------------------------------------------GESTION

    public void actualizar(){

    }

    public void moverY(int velocidad){
       this.cajaDeGolpe.top += velocidad;
       this.cajaDeGolpe.bottom += velocidad;
       this.cajaDeAnimacion = cajaDeGolpe;
    }

    public void moverX(int velocidad){
        this.cajaDeGolpe.left += velocidad;
        this.cajaDeGolpe.right += velocidad;
        this.cajaDeAnimacion = cajaDeGolpe;
    }

    public boolean colisionaFicha(Ficha fichaColision) {

        RectF cajaGolpeColision = new RectF(fichaColision.cajaDeGolpe);
        return this.cajaDeGolpe.intersect(new RectF(
                cajaGolpeColision.left+10,
                cajaGolpeColision.top+10,
                cajaGolpeColision.right-10,
                cajaGolpeColision.bottom-10
        ));

    }

    public boolean colisionaCelda(Casilla casilla, String direccion, int velocidad) {
        switch (direccion){
            case "ARRIBA":
                if (this.cajaDeGolpe.top < casilla.obtenerCajaGolpe().top + velocidad){
                    this.cajaDeGolpe = new RectF(casilla.obtenerCajaGolpe());
                    this.cajaDeAnimacion = new RectF(casilla.obtenerCajaGolpe());
                    cambiarReferencia(casilla);
                    seEstaMoviendo = false;
                    return true;
                } else {
                    return false;
                }
            case "ABAJO":
                if (this.cajaDeGolpe.bottom > casilla.obtenerCajaGolpe().bottom - velocidad){
                    this.cajaDeGolpe = new RectF(casilla.obtenerCajaGolpe());
                    this.cajaDeAnimacion = new RectF(casilla.obtenerCajaGolpe());
                    seEstaMoviendo = false;
                    cambiarReferencia(casilla);
                    return true;
                } else {
                    return false;
                }
            case "IZQUIERDA":
                if (this.cajaDeGolpe.left < casilla.obtenerCajaGolpe().left + velocidad){
                    this.cajaDeGolpe = new RectF(casilla.obtenerCajaGolpe());
                    this.cajaDeAnimacion = new RectF(casilla.obtenerCajaGolpe());
                    seEstaMoviendo = false;
                    cambiarReferencia(casilla);
                    return true;
                } else {
                    return false;
                }
            case "DERECHA":
                if (this.cajaDeGolpe.right > casilla.obtenerCajaGolpe().right - velocidad){
                    this.cajaDeGolpe = new RectF(casilla.obtenerCajaGolpe());
                    this.cajaDeAnimacion = new RectF(casilla.obtenerCajaGolpe());
                    seEstaMoviendo = false;
                    cambiarReferencia(casilla);
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }

    }

    //--------------------------------------------------------------------------------------GRAFICOS

    private int asignarNumeroAleatorio(){
        Random aleatorio = new Random();
        int probabilidad = aleatorio.nextInt(10);
        if (probabilidad < 5){
            return 2;
        } else {
            return 4;
        }
    }

    private void ajustarCajaDeAnimacion(){
        cajaDeAnimacion.top = cajaDeGolpe.top + (cajaDeGolpe.height()/2);
        cajaDeAnimacion.left = cajaDeGolpe.left + (cajaDeGolpe.width()/2);
        cajaDeAnimacion.right = cajaDeGolpe.right - (cajaDeGolpe.width()/2);
        cajaDeAnimacion.bottom = cajaDeGolpe.bottom - (cajaDeGolpe.height()/2);
    }

    public void dibujar(Canvas lienzo){
        if (animAparicion){
            animarAparicion();
        }
        Paint pincel = new Paint();
        pincel.setAntiAlias(true);
        //if (this.numero == 2){
        //    pincel.setColor(Color.parseColor("#ffffff"));
        //}else{
        //    pincel.setColor(Color.parseColor("#000000"));
        //}
        pincel.setColor(this.color);
        lienzo.drawRoundRect(this.cajaDeAnimacion, 20, 20, pincel);
        dibujarNumero(lienzo);
    }

    private void dibujarNumero(Canvas lienzo) {
        Paint pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setColor(colorNumero);
        pincel.setTextSize(50);
        //pincel.setAlpha(190);
        pincel.setTextAlign(Paint.Align.CENTER);
        String numeroStr = String.valueOf(numero);
        int caracteres = numeroStr.length();
        float izquierda = cajaDeGolpe.left + (cajaDeGolpe.width()/2) + (caracteres);
        float arriba = cajaDeGolpe.top + (cajaDeGolpe.height()/2) + 15;
        //float derecha = cajaDeGolpe.right - (cajaDeGolpe.width()/2);
        //float abajo = cajaDeGolpe.bottom - (cajaDeGolpe.height()/2);;
        lienzo.drawText(String.valueOf(numero),izquierda,arriba,pincel);
    }

    private void animarAparicion() {
        int velocidadAnimacion = 15; //Cuanto mayor es el numero, mas se tardara en ejecutar
        float velocidadX = cajaDeGolpe.height()/velocidadAnimacion;
        float velocidadY = cajaDeGolpe.width()/velocidadAnimacion;

        if (cajaDeAnimacion.top > cajaDeGolpe.top){
            cajaDeAnimacion.top -= velocidadY;
        } else {
            cajaDeAnimacion.top = cajaDeGolpe.top;
        }
        if (cajaDeAnimacion.bottom < cajaDeGolpe.bottom){
            cajaDeAnimacion.bottom += velocidadY;
        } else {
            cajaDeAnimacion.bottom = cajaDeGolpe.bottom;
        }
        if (cajaDeAnimacion.left > cajaDeGolpe.left){
            cajaDeAnimacion.left -= velocidadX;
        } else {
            cajaDeAnimacion.left = cajaDeGolpe.left;
        }
        if (cajaDeAnimacion.right < cajaDeGolpe.right){
            cajaDeAnimacion.right += velocidadX;
        } else {
            cajaDeAnimacion.right = cajaDeGolpe.right;
        }
        if (cajaDeAnimacion.top == cajaDeGolpe.top &&
                cajaDeAnimacion.left == cajaDeGolpe.left &&
                cajaDeAnimacion.bottom == cajaDeGolpe.bottom &&
                cajaDeAnimacion.right == cajaDeGolpe.right){
            animAparicion = false;
            Log.d("Ficha "+this.ref.obtenerReferenciaX()+"/"+this.ref.obtenerReferenciaY(),
                    "- Animacion de inicio finalizada");
        }
    }

    //-------------------------------------------------------------------------------------------DTO

    private int obtenerColor(){
        int color;
        switch (this.numero){
            case 2: color = Color.parseColor("#C8CED5"); break;
            case 4: color = Color.parseColor("#AFBFCF"); break;
            case 8: color = Color.parseColor("#E4AA56"); break;
            case 16: color = Color.parseColor("#E29561"); break;
            case 32: color = Color.parseColor("#E27B61"); break;
            case 64: color = Color.parseColor("#E26161"); break;
            case 128: color = Color.parseColor("#E2D561"); break;
            case 256: color = Color.parseColor("#E2D561"); break;
            case 512: color = Color.parseColor("#E2D561"); break;
            case 1024: color = Color.parseColor("#457553"); break;
            default: color = Color.parseColor("#000000"); break;
        }
        return color;
    }

    private int obtenerColorNumero(){
        int color;
        if (numero < 8){
            color = Color.parseColor("#35477A");
        } else {
            color = Color.WHITE;
        }
        return color;
    }

    public void cambiarNumero(int numero) {
        this.numero = numero;
        this.color = obtenerColor();
        this.colorNumero = obtenerColorNumero();
    }

    public void seMueve(boolean movimiento){
        this.seEstaMoviendo = movimiento;
    }

    public void establecerCajaDeGolpe(Casilla casilla) {
        cambiarReferencia(casilla);
        this.cajaDeGolpe = new RectF(casilla.obtenerCajaGolpe());
        this.cajaDeAnimacion = cajaDeGolpe;
    }

    public RectF obtenerCajaGolpe(){ return this.cajaDeGolpe; }

    public int obtenerRefX() { return this.ref.obtenerReferenciaX(); }

    public int obtenerRefY() { return this.ref.obtenerReferenciaY(); }

    public boolean seEstaMoviendo() { return seEstaMoviendo; }

    public int obtenerNumero() { return this.numero; }

    public Casilla obtenerReferencia() { return this.ref; }

    private void cambiarReferencia(Casilla ref) {
        Tablero tablero = ref.obtenerTablero();
        Ficha[][] fichas = tablero.obtenerFichas();
        fichas[obtenerRefX()][obtenerRefY()] = null;
        this.ref = ref;
        fichas[obtenerRefX()][obtenerRefY()] = this;
    }

}
