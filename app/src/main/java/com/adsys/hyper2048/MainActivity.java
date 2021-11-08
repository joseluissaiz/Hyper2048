package com.adsys.hyper2048;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private Juego juego;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Poniendo la aplicacion en pantalla completa
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        //Colocamos la vista que contiene nuestro juego
        juego = new Juego(this);
        setContentView(juego);

        //Llamamos al engine para gestionar los eventos obtenidos en la vista
        juego.setOnTouchListener(new OnSwipeListener(this){

            @Override
            public void onSwipeRight() { juego.obtenerMotorDelJuego().deslizarDerecha(); }

            @Override
            public void onSwipeLeft() { juego.obtenerMotorDelJuego().deslizarIzquierda(); }

            @Override
            public void onSwipeTop() { juego.obtenerMotorDelJuego().deslizarArriba(); }

            @Override
            public void onSwipeBottom() { juego.obtenerMotorDelJuego().deslizarAbajo(); }
        });

    }

}