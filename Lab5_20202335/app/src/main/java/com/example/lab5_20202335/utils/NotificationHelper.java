package com.example.lab5_20202335.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationHelper {

    public static final String CANAL_ALTA_ID = "canal_alta_importancia";
    public static final String CANAL_MEDIA_ID = "canal_media_importancia";
    public static final String CANAL_BAJA_ID = "canal_baja_importancia";

    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            NotificationChannel canalAlta = new NotificationChannel(
                    CANAL_ALTA_ID,
                    "Pagos Urgentes",
                    NotificationManager.IMPORTANCE_HIGH
            );
            canalAlta.setDescription("Notificaciones para pagos de alta prioridad.");
            NotificationChannel canalMedia = new NotificationChannel(
                    CANAL_MEDIA_ID,
                    "Pagos Regulares",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            canalMedia.setDescription("Notificaciones para pagos de prioridad media.");
            NotificationChannel canalBaja = new NotificationChannel(
                    CANAL_BAJA_ID,
                    "Pagos Menores",
                    NotificationManager.IMPORTANCE_LOW
            );
            canalBaja.setDescription("Notificaciones para pagos de baja prioridad.");
            manager.createNotificationChannel(canalAlta);
            manager.createNotificationChannel(canalMedia);
            manager.createNotificationChannel(canalBaja);
        }
    }
}
