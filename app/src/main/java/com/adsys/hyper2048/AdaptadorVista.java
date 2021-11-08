package com.adsys.hyper2048;



import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;


public class AdaptadorVista {
    private final DisplayMetrics metricasMonitor;
    public final float anchoMonitor;
    public final float altoMonitor;

    public AdaptadorVista(Context contexto){
        metricasMonitor = contexto.getResources().getDisplayMetrics();
        anchoMonitor = metricasMonitor.widthPixels;
        altoMonitor = metricasMonitor.heightPixels;
    }

    public float[] obtenerCentroDelMonitor(){
        float centroMonitorX = (float) (anchoMonitor/2.0);
        float centroMonitorY = (float) (altoMonitor/2.0);
        return new float[]{centroMonitorX, centroMonitorY};
    }

    public float adaptarX(float rawX){
        float densidad = (float) (anchoMonitor/1000);
        return rawX*densidad;
    }

    public float adaptarY(float rawY){
        float densidad = (float) (altoMonitor/1000);
        return rawY*densidad;
    }

}

