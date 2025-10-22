package com.example.lab5_20202335.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.lab5_20202335.R;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String nombre = intent.getStringExtra("NOMBRE_SERVICIO");
        double monto = intent.getDoubleExtra("MONTO_SERVICIO", 0.0);
        String channelId = intent.getStringExtra("CHANNEL_ID");
        int notificationId = intent.getIntExtra("NOTIFICATION_ID", 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Recordatorio de Pago: " + nombre)
                .setContentText(String.format("Monto a pagar: S/ %.2f", monto))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}
