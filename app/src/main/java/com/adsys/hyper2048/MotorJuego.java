package com.adsys.hyper2048;

import android.graphics.RectF;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MotorJuego {
    private Juego juego;
    private Tablero tablero;
    private Ficha[][] fichas;
    private Casilla[][] casillas;
    private boolean animacionActiva;
    private String direccionDeMovimiento;
    private final int velocidadMovimiento = 40;
    private boolean siguienteTurno;
    List<Ficha> fusionesPorTurno;
    private int fichasEnAccion;

    public MotorJuego(Juego juego){
        this.juego = juego;
        this.tablero = juego.obtenerTablero();
        this.fichas = tablero.obtenerFichas();
        this.casillas = tablero.obtenerCasillas();
        animacionActiva = false;
        siguienteTurno = false;
        direccionDeMovimiento = "NULO";
        fusionesPorTurno = new ArrayList<>();
    }


    public void actualizar(){
        fichasEnAccion = 0;

        switch (direccionDeMovimiento){
            case "ARRIBA":
                for (int fx = 0; fx < fichas.length; fx++) {
                    for (int cy = 0; cy < fichas[0].length; cy++) {
                        aplicarFisicas(fichas[fx][cy], "ARRIBA");
                    }
                }
                break;
            case "ABAJO":
                for (int fx = 0; fx < fichas.length; fx++) {
                    for (int cy = fichas[0].length - 1; cy >= 0; cy--) {
                        aplicarFisicas(fichas[fx][cy], "ABAJO");
                    }
                }
                break;
            case "IZQUIERDA":
                for (int fx = 0; fx < fichas.length; fx++) {
                    for (int cy = fichas[0].length - 1; cy >= 0; cy--) {
                        aplicarFisicas(fichas[fx][cy], "IZQUIERDA");
                    }
                }
                break;
            case "DERECHA":
                for (int fx = fichas.length - 1; fx >= 0; fx--) {
                    for (int cy = fichas[0].length - 1; cy >= 0; cy--) {
                        aplicarFisicas(fichas[fx][cy], "DERECHA");
                    }
                }
                break;
        }

        //for (int fx = 0; fx < fichas.length; fx++) {
        //    for (int cy = 0; cy < fichas[0].length; cy++) {
        //        if (fichas[fx][cy] != null){
        //            switch (direccionDeMovimiento){
        //                case "ARRIBA": moverArriba(fichas[fx][cy]); break;
        //                case "ABAJO": moverAbajo(fichas[fx][cy]); break;
        //                case "IZQUIERDA": moverIzquierda(fichas[fx][cy]); break;
        //                case "DERECHA": moverDerecha(fichas[fx][cy]); break;
        //            }
        //            if (fichas[fx][cy] != null){
        //                if (fichas[fx][cy].seEstaMoviendo()){
        //                    fichasEnAccion++;
        //                }
        //            }
        //        }
        //    }
        //}
        if (fichasEnAccion == 0){
            animacionActiva = false;
            if (siguienteTurno){
                tablero.agregarFichaNueva();
                siguienteTurno = false;
                direccionDeMovimiento = "NULO";
                fusionesPorTurno = new ArrayList<>();
            }

        }
    }


    private void aplicarFisicas(Ficha ficha, String direccion) {
        if (ficha != null) {
            switch(direccion) {
                case "ARRIBA":
                    moverArriba(ficha);
                    break;
                case "ABAJO":
                    moverAbajo(ficha);
                    break;
                case "IZQUIERDA":
                    moverIzquierda(ficha);
                    break;
                case "DERECHA":
                    moverDerecha(ficha);
                    break;
            }
            if (ficha.seEstaMoviendo()){
                fichasEnAccion++;
            }
        }
    }


    public void moverArriba(Ficha ficha){
        Casilla casillaArriba = tablero.obtenerCasillas()[ficha.obtenerRefX()][0];
        boolean mover = true;

        if (ficha.obtenerRefY() != 0){
            ficha.seMueve(true);

            //Evitamos que la ficha se mueva si ya esta encima de otra
            mover = noSePrevieneColision(ficha, "ARRIBA");

            //Comprobamos si existe colision con alguna de las fichas del tablero
            for (int fx = 0; fx < fichas.length; fx++) {
                for (int cy = 0; cy < fichas[0].length; cy++) {
                    //Nos saltamos la ficha en cuestion
                    if (fichas[fx][cy] != ficha && fichas[fx][cy] != null){
                        if (ficha.colisionaFicha(fichas[fx][cy])){
                            aplicarColisionFicha(ficha, fichas[fx][cy],"ARRIBA");
                            ficha.seMueve(false);
                            mover = false;
                        }
                    }
                }
            }

            //Comprobamos si hay colision con la casilla del borde
            if (ficha.colisionaCelda(casillaArriba, "ARRIBA", velocidadMovimiento)) {
                ficha.seMueve(false);
                mover = false;
            }

            if (mover){
                ficha.moverY(-velocidadMovimiento);
            }
        }
    }


    public void moverAbajo(Ficha ficha){
        Casilla casillaAbajo = tablero.obtenerCasillas()[ficha.obtenerRefX()][fichas[0].length-1];
        boolean mover = true;

        if (ficha.obtenerRefY() != fichas[0].length-1){
            ficha.seMueve(true);

            //Evitamos que la ficha se mueva si ya esta encima de otra
            mover = noSePrevieneColision(ficha, "ABAJO");

            //Comprobamos si existe colision con alguna de las fichas del tablero
            for (int fx = 0; fx < fichas.length; fx++) {
                for (int cy = 0; cy < fichas[0].length; cy++) {
                    //Nos saltamos la ficha en cuestion
                    if (fichas[fx][cy] != ficha && fichas[fx][cy] != null){
                        if (ficha.colisionaFicha(fichas[fx][cy])){
                            aplicarColisionFicha(ficha, fichas[fx][cy],"ABAJO");
                            ficha.seMueve(false);
                            mover = false;
                        }
                    }
                }
            }

            if (ficha.colisionaCelda(casillaAbajo, "ABAJO", velocidadMovimiento)) {
                ficha.seMueve(false);
                mover = false;
            }

            if (mover){
                ficha.moverY(velocidadMovimiento);
            }
        }
    };


    public void moverIzquierda(Ficha ficha){
        Casilla casillaIzquierda = tablero.obtenerCasillas()[0][ficha.obtenerRefY()];
        boolean mover = true;

        if (ficha.obtenerRefX() != 0){
            ficha.seMueve(true);

            //Evitamos que la ficha se mueva si ya esta al lado de otra
            mover = noSePrevieneColision(ficha, "IZQUIERDA");

            //Comprobamos si existe colision con alguna de las fichas del tablero
            for (int fx = 0; fx < fichas.length; fx++) {
                for (int cy = 0; cy < fichas[0].length; cy++) {
                    //Nos saltamos la ficha en cuestion
                    if (fichas[fx][cy] != ficha && fichas[fx][cy] != null){
                        if (ficha.colisionaFicha(fichas[fx][cy])){
                            aplicarColisionFicha(ficha, fichas[fx][cy],"IZQUIERDA");
                            ficha.seMueve(false);
                            mover = false;
                        }
                    }
                }
            }

            if (ficha.colisionaCelda(casillaIzquierda, "IZQUIERDA", velocidadMovimiento)) {
                ficha.seMueve(false);
                mover = false;
            }

            if (mover){
                ficha.moverX(-velocidadMovimiento);
            }
        }
    };


    public void moverDerecha(Ficha ficha){

        Casilla casillaDerecha = tablero.obtenerCasillas()[fichas.length-1][ficha.obtenerRefY()];
        boolean mover = true;

        if (ficha.obtenerRefX() != fichas.length-1){
            ficha.seMueve(true);

            //Evitamos que la ficha se mueva si ya esta al lado de otra
            mover = noSePrevieneColision(ficha, "DERECHA");

            //Comprobamos si existe colision con alguna de las fichas del tablero
            for (int fx = 0; fx < fichas.length; fx++) {
                for (int cy = 0; cy < fichas[0].length; cy++) {
                    //Nos saltamos la ficha en cuestion
                    if (fichas[fx][cy] != ficha && fichas[fx][cy] != null){
                        if (ficha.colisionaFicha(fichas[fx][cy])){
                            aplicarColisionFicha(ficha, fichas[fx][cy],"DERECHA");
                            ficha.seMueve(false);
                            mover = false;
                        }
                    }
                }
            }

            if (ficha.colisionaCelda(casillaDerecha, "DERECHA", velocidadMovimiento)) {
                ficha.seMueve(false);
                mover = false;
            }

            if (mover && ficha.seEstaMoviendo()){
                ficha.moverX(velocidadMovimiento);
            }
        }
    };


    private boolean noSePrevieneColision(Ficha ficha, String direccion){
        AdaptadorVista adaptador = juego.obtenerAdaptador();
        boolean mover = true;
        RectF cajaVision = null;
        int prevencionX = 0;
        int prevencionY = 0;


        //cajaVision se encarga de detectar colisiones antes de que aparezcan
        switch (direccion){
            case "ARRIBA":
                cajaVision = new RectF(
                        ficha.obtenerCajaGolpe().left,
                        ficha.obtenerCajaGolpe().top - adaptador.adaptarY(26),
                        ficha.obtenerCajaGolpe().right,
                        ficha.obtenerCajaGolpe().bottom
                );
                prevencionY = 1;
                break;
            case "ABAJO":
                cajaVision = new RectF(
                        ficha.obtenerCajaGolpe().left,
                        ficha.obtenerCajaGolpe().top,
                        ficha.obtenerCajaGolpe().right,
                        ficha.obtenerCajaGolpe().bottom + adaptador.adaptarY(26)
                );
                prevencionY = -1;
                break;
            case "IZQUIERDA":
                cajaVision = new RectF(
                        ficha.obtenerCajaGolpe().left - adaptador.adaptarY(26),
                        ficha.obtenerCajaGolpe().top,
                        ficha.obtenerCajaGolpe().right,
                        ficha.obtenerCajaGolpe().bottom
                );
                prevencionX = -1;
                break;
            case "DERECHA":
                cajaVision = new RectF(
                        ficha.obtenerCajaGolpe().left,
                        ficha.obtenerCajaGolpe().top,
                        ficha.obtenerCajaGolpe().right + adaptador.adaptarY(26),
                        ficha.obtenerCajaGolpe().bottom
                );
                prevencionX = 1;
                break;
        }

        //Detectamos si ya se ha producido alguna colision
        for (int fx = 0; fx < fichas.length; fx++) {
            for (int cy = 0; cy < fichas[0].length; cy++) {
                if (fichas[fx][cy] != ficha && fichas[fx][cy] != null){
                    if (cajaVision.intersect(fichas[fx][cy].obtenerCajaGolpe())){
                        if (fichas[fx][cy].obtenerNumero() != ficha.obtenerNumero()){
                            switch (direccion){
                                case "ARRIBA":
                                    ficha.establecerCajaDeGolpe(
                                            casillas[ficha.obtenerRefX()+prevencionX]
                                                    [fichas[fx][cy].obtenerRefY()+prevencionY]
                                    );
                                    break;
                                case "ABAJO":
                                    ficha.establecerCajaDeGolpe(
                                            casillas[ficha.obtenerRefX()+prevencionX]
                                                    [fichas[fx][cy].obtenerRefY()+prevencionY]
                                    );
                                    break;
                                case "IZQUIERDA":
                                    ficha.establecerCajaDeGolpe(
                                            casillas[fichas[fx][cy].obtenerRefX()+1]
                                                    [fichas[fx][cy].obtenerRefY()]
                                    );
                                    break;
                                case "DERECHA":
                                    ficha.establecerCajaDeGolpe(
                                            casillas[fichas[fx][cy].obtenerRefX()-1]
                                                    [fichas[fx][cy].obtenerRefY()]
                                    );
                                    break;
                            }
                            mover = false;
                            ficha.seMueve(false);
                        }
                    }
                }
            }
        }

        return mover;
    }


    private void aplicarColisionFicha(Ficha ficha, Ficha fichaColision, String direccion) {
        Ficha[][] fichas = tablero.obtenerFichas();
        //Si las fichas tienen el mismo numero las fusionamos
        if (ficha.obtenerNumero() == fichaColision.obtenerNumero() && (!fusionesPorTurno.contains(ficha) && !fusionesPorTurno.contains(fichaColision))){
            fichaColision.cambiarNumero(ficha.obtenerNumero()*2);
            tablero.eliminarFicha(ficha);
            fusionesPorTurno.add(fichaColision);
            //fichaColision.seMueve(false);
            //ficha.seMueve(false);
        } else {
            switch (direccion){
                case "ARRIBA":
                    if (fichaColision.obtenerRefY() != 0){
                        ficha.establecerCajaDeGolpe(
                                tablero.obtenerCasillas()[fichaColision.obtenerRefX()]
                                        [fichaColision.obtenerRefY()+1]
                        );
                    }
                    ficha.seMueve(false);
                    fichaColision.seMueve(false);
                    break;

                case "ABAJO":
                    if (fichaColision.obtenerRefY() != 0) {
                        ficha.establecerCajaDeGolpe(
                                tablero.obtenerCasillas()[fichaColision.obtenerRefX()]
                                        [fichaColision.obtenerRefY()-1]
                        );
                    }
                    ficha.seMueve(false);
                    fichaColision.seMueve(false);
                    break;

                case "IZQUIERDA":
                    if (fichaColision.obtenerRefX() != casillas.length-1) {
                        ficha.establecerCajaDeGolpe(
                                tablero.obtenerCasillas()[fichaColision.obtenerRefX()+1]
                                        [fichaColision.obtenerRefY()]
                        );
                    }
                    ficha.seMueve(false);
                    fichaColision.seMueve(false);
                    break;

                case "DERECHA":
                    if (fichaColision.obtenerRefX() != 0) {
                        ficha.establecerCajaDeGolpe(
                                tablero.obtenerCasillas()[fichaColision.obtenerRefX()-1]
                                        [fichaColision.obtenerRefY()]
                        );
                    }
                    ficha.seMueve(false);
                    fichaColision.seMueve(false);
                    break;
            }
        }
    }


    public void deslizarArriba(){
        if (!animacionActiva){
            siguienteTurno = true;
            animacionActiva = true;
            direccionDeMovimiento = "ARRIBA";
        }
    }


    public void deslizarAbajo(){
        if (!animacionActiva){
            siguienteTurno = true;
            animacionActiva = true;
            direccionDeMovimiento = "ABAJO";
        }
    }


    public void deslizarIzquierda(){
        if (!animacionActiva){
            siguienteTurno = true;
            animacionActiva = true;
            direccionDeMovimiento = "IZQUIERDA";
        }
    }


    public void deslizarDerecha(){
        if (!animacionActiva){
            siguienteTurno = true;
            animacionActiva = true;
            direccionDeMovimiento = "DERECHA";
        }
    }

}
