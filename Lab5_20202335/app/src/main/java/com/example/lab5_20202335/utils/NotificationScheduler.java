package com.example.lab5_20202335.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.lab5_20202335.models.Servicio;
import com.example.lab5_20202335.receivers.NotificationReceiver;

public class NotificationScheduler {

    public static void programarNotificacion(Context context, Servicio servicio) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long triggerAtMillis = servicio.getFechaVencimiento() - (24 * 60 * 60 * 1000);
        PendingIntent pendingIntent = crearPendingIntent(context, servicio);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    public static void cancelarNotificacion(Context context, Servicio servicio) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = crearPendingIntent(context, servicio);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private static PendingIntent crearPendingIntent(Context context, Servicio servicio) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("NOMBRE_SERVICIO", servicio.getNombre());
        intent.putExtra("MONTO_SERVICIO", servicio.getMonto());
        intent.putExtra("CHANNEL_ID", getChannelId(servicio.getImportancia()));
        int notificationId = servicio.getId().hashCode();
        intent.putExtra("NOTIFICATION_ID", notificationId);
        return PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    private static String getChannelId(String importancia) {
        if (importancia == null) return NotificationHelper.CANAL_MEDIA_ID;
        switch (importancia.toLowerCase()) {
            case "alta":
                return NotificationHelper.CANAL_ALTA_ID;
            case "baja":
                return NotificationHelper.CANAL_BAJA_ID;
            default:
                return NotificationHelper.CANAL_MEDIA_ID;
        }
    }
}
