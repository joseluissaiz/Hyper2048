package com.adsys.hyper2048;

import static java.lang.Thread.sleep;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class LoopJuego extends Thread {
    private static final double UPS_MAXIMO = 60.0;
    private static final double UPS_PERIODO = 1E+3/UPS_MAXIMO;
    private final Juego juego;
    private final SurfaceHolder soporteDeSuperficie;
    public boolean estaCorriendo = false;
    public double promedioUPS;
    public double promedioFPS;
    public String objetivo;

    public LoopJuego(Juego juego, SurfaceHolder soporteDeSuperficie, String objetivo){
        this.juego = juego;
        this.soporteDeSuperficie = soporteDeSuperficie;
        this.objetivo = objetivo;
    }

    public double obtenerPromedioUPS() {
        return promedioUPS;
    }

    public double obtenerPromedioFPS() {
        return promedioFPS;
    }

    public void iniciarLoop() {
        estaCorriendo = true;
        start();
    }

    @Override
    public void run() {
        super.run();
        estaCorriendo = true;
        //Declaramos las variables de tiempo y ciclo de vida
        int contadorActualizacion = 0;
        int contadorCuadros = 0;
        long tiempoInicio = 0;
        long tiempoTranscurrido = 0;
        long tiempoPausa = 0;

        //El loop del juego
        Canvas lienzo = null;
        tiempoInicio = System.currentTimeMillis();
        while (estaCorriendo){
            //Intentamos actualizar y renderizar los objetos de la clase Juego
            try {
                lienzo = soporteDeSuperficie.lockCanvas();
                synchronized (soporteDeSuperficie){
                    juego.actualizar();
                    contadorActualizacion++;
                    juego.dibujar(lienzo);
                }
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }finally {
                if (lienzo != null){
                    try {
                        soporteDeSuperficie.unlockCanvasAndPost(lienzo);
                        contadorCuadros++;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            //Pausamos el loop para que no exceda los UPS objetivo
            tiempoTranscurrido = System.currentTimeMillis()-tiempoInicio;
            tiempoPausa = (long)(contadorActualizacion*UPS_PERIODO-tiempoTranscurrido);
            if (tiempoPausa > 0){
                try {
                    sleep(tiempoPausa);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Nos saltamos varios frames para mantener los UPS objetivo
            while (tiempoPausa < 0 && contadorActualizacion < UPS_MAXIMO-1){
                juego.actualizar();
                contadorActualizacion++;
                tiempoTranscurrido = System.currentTimeMillis()-tiempoInicio;
                tiempoPausa = (long)(contadorActualizacion*UPS_PERIODO-tiempoTranscurrido);
            }

            //Calculamos el UPS y FPS objetivo
            tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
            if (tiempoTranscurrido >= 1000){
                promedioUPS = contadorActualizacion/(1E-3*tiempoTranscurrido);
                promedioFPS = contadorCuadros/(1E-3*tiempoTranscurrido);
                contadorActualizacion = 0;
                contadorCuadros = 0;
                tiempoInicio = System.currentTimeMillis();
            }
        }
    }
}
