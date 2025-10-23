package com.example.lab5_20202335.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;             
import android.view.MenuItem;        
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_20202335.R;
import com.example.lab5_20202335.adapters.ServicioAdapter;
import com.example.lab5_20202335.models.PagoHistorial;
import com.example.lab5_20202335.models.Servicio;
import com.example.lab5_20202335.utils.NotificationHelper;
import com.example.lab5_20202335.utils.NotificationScheduler;
import com.example.lab5_20202335.utils.StorageManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements ServicioAdapter.OnServicioClickListener {

    private static final int REQ_CODE_NOTIF = 1001;

    private RecyclerView recyclerView;
    private ServicioAdapter adapter;
    private ArrayList<Servicio> listaServicios;
    private TextView textViewSinServicios, textViewTotalServicios, textViewProximoPago;
    private FloatingActionButton fabAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔹 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 🔹 Crear canales de notificación
        NotificationHelper.createNotificationChannels(this);

        // 🔹 Permiso para notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQ_CODE_NOTIF);
        }

        // 🔹 Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewServicios);
        textViewSinServicios = findViewById(R.id.textViewSinServicios);
        textViewTotalServicios = findViewById(R.id.textViewTotalServicios);
        textViewProximoPago = findViewById(R.id.textViewProximoPago);
        fabAgregar = findViewById(R.id.fabAgregar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAgregar.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, FormActivity.class))
        );

        // 🔹 Cargar servicios guardados
        cargarDatos();

        // 🔹 Actualizar estadísticas
        actualizarEstadisticas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
        actualizarEstadisticas();
    }

    private void cargarDatos() {
        listaServicios = StorageManager.cargarServicios(this);
        if (listaServicios == null) listaServicios = new ArrayList<>();

        if (listaServicios.isEmpty()) {
            textViewSinServicios.setVisibility(android.view.View.VISIBLE);
            recyclerView.setVisibility(android.view.View.GONE);
        } else {
            textViewSinServicios.setVisibility(android.view.View.GONE);
            recyclerView.setVisibility(android.view.View.VISIBLE);
        }

        adapter = new ServicioAdapter(this, listaServicios, this);
        recyclerView.setAdapter(adapter);
    }

    private void actualizarEstadisticas() {
        textViewTotalServicios.setText(String.valueOf(listaServicios.size()));

        long ahora = System.currentTimeMillis();
        long proximoVencimiento = Long.MAX_VALUE;

        for (Servicio servicio : listaServicios) {
            if (servicio.getFechaVencimiento() > ahora &&
                    servicio.getFechaVencimiento() < proximoVencimiento) {
                proximoVencimiento = servicio.getFechaVencimiento();
            }
        }

        if (proximoVencimiento != Long.MAX_VALUE) {
            long diff = proximoVencimiento - ahora;
            long dias = TimeUnit.MILLISECONDS.toDays(diff);
            textViewProximoPago.setText(String.valueOf(dias));
        } else {
            textViewProximoPago.setText("--");
        }
    }

    @Override
    public void onEditClick(Servicio servicio) {
        Intent intent = new Intent(MainActivity.this, FormActivity.class);
        intent.putExtra("servicio_id", servicio.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Servicio servicio) {
        NotificationScheduler.cancelarNotificacion(this, servicio);
        listaServicios.remove(servicio);
        StorageManager.guardarServicios(this, listaServicios);
        cargarDatos();
        actualizarEstadisticas();
    }

    @Override
    public void onPaidClick(Servicio servicio) {
        long ahora = System.currentTimeMillis();
        PagoHistorial itemHistorial = new PagoHistorial(
                servicio.getNombre(),
                servicio.getMonto(),
                ahora,
                servicio.getFechaVencimiento()
        );
        ArrayList<PagoHistorial> historial = StorageManager.cargarHistorial(this);
        historial.add(itemHistorial);
        StorageManager.guardarHistorial(this, historial);

        NotificationScheduler.cancelarNotificacion(this, servicio);

        if ("una vez".equals(servicio.getPeriodicidad())) {
            listaServicios.remove(servicio);
        } else {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(servicio.getFechaVencimiento());
            switch (servicio.getPeriodicidad()) {
                case "mensual": c.add(Calendar.MONTH, 1); break;
                case "bimestral": c.add(Calendar.MONTH, 2); break;
                case "trimestral": c.add(Calendar.MONTH, 3); break;
                case "anual": c.add(Calendar.YEAR, 1); break;
            }
            servicio.setFechaVencimiento(c.getTimeInMillis());
            NotificationScheduler.programarNotificacion(this, servicio);
        }

        StorageManager.guardarServicios(this, listaServicios);
        cargarDatos();
        actualizarEstadisticas();
    }

    // 🔹 Menú principal
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_historial) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}