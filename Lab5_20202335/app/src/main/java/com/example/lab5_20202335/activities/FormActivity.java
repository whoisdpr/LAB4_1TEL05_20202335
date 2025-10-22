package com.example.lab5_20202335.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.lab5_20202335.R;
import com.example.lab5_20202335.models.Servicio;
import com.example.lab5_20202335.utils.StorageManager;
import com.example.lab5_20202335.utils.NotificationScheduler;
import java.util.ArrayList;
import java.util.Calendar;
import androidx.annotation.NonNull;


public class FormActivity extends AppCompatActivity {

    private static final int REQ_CODE_NOTIF = 1002;

    private EditText editTextNombre, editTextMonto;
    private TextView btnFechaVencimiento;
    private Button btnGuardar;
    private Spinner spinnerPeriodicidad, spinnerImportancia;

    private long fechaVencimientoSeleccionada = -1;
    private Calendar calendario = Calendar.getInstance();

    private boolean modoEditar = false;
    private String idServicioEditar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_CODE_NOTIF);
            }
        }
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextMonto = findViewById(R.id.editTextMonto);
        btnFechaVencimiento = findViewById(R.id.btnFechaVencimiento);
        btnGuardar = findViewById(R.id.btnGuardar);
        spinnerPeriodicidad = findViewById(R.id.spinnerPeriodicidad);
        spinnerImportancia = findViewById(R.id.spinnerImportancia);

        btnFechaVencimiento.setOnClickListener(v -> mostrarDatePicker());
        configurarSpinners();
        btnFechaVencimiento.setOnClickListener(v -> mostrarDatePicker());
        btnGuardar.setOnClickListener(v -> guardarServicio());
        if (getIntent().hasExtra("servicio_id")) {
            modoEditar = true;
            idServicioEditar = getIntent().getStringExtra("servicio_id");
            setTitle("Editar Servicio");
            cargarDatosParaEditar();
        } else {
            modoEditar = false;
            setTitle("Agregar Servicio");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_NOTIF) {
        }
    }

    private void configurarSpinners() {
        ArrayAdapter<CharSequence> adapterPer = ArrayAdapter.createFromResource(this,
                R.array.periodicidad_array, android.R.layout.simple_spinner_item);
        adapterPer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodicidad.setAdapter(adapterPer);
        ArrayAdapter<CharSequence> adapterImp = ArrayAdapter.createFromResource(this,
                R.array.importancia_array, android.R.layout.simple_spinner_item);
        adapterImp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImportancia.setAdapter(adapterImp);
    }

    private void mostrarDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, month);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            fechaVencimientoSeleccionada = calendario.getTimeInMillis();
            btnFechaVencimiento.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        };
        new DatePickerDialog(this, dateSetListener,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void cargarDatosParaEditar() {
        ArrayList<Servicio> lista = StorageManager.cargarServicios(this);
        for (Servicio s : lista) {
            if (s.getId().equals(idServicioEditar)) {
                editTextNombre.setText(s.getNombre());
                editTextMonto.setText(String.valueOf(s.getMonto()));
                fechaVencimientoSeleccionada = s.getFechaVencimiento();
                btnFechaVencimiento.setText(android.text.format.DateFormat.format("dd/MM/yyyy", s.getFechaVencimiento()));
                if (s.getPeriodicidad() != null) {
                    String[] per = getResources().getStringArray(R.array.periodicidad_array);
                    for (int i = 0; i < per.length; i++) if (per[i].equals(s.getPeriodicidad())) spinnerPeriodicidad.setSelection(i);
                }
                if (s.getImportancia() != null) {
                    String[] imp = getResources().getStringArray(R.array.importancia_array);
                    for (int i = 0; i < imp.length; i++) if (imp[i].equals(s.getImportancia())) spinnerImportancia.setSelection(i);
                }
                break;
            }
        }
    }

    private void guardarServicio() {
        String nombre = editTextNombre.getText().toString();
        String montoStr = editTextMonto.getText().toString();
        if (nombre.isEmpty() || montoStr.isEmpty() || fechaVencimientoSeleccionada == -1) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        double monto = Double.parseDouble(montoStr);
        String periodicidad = spinnerPeriodicidad.getSelectedItem().toString();
        String importancia = spinnerImportancia.getSelectedItem().toString();
        ArrayList<Servicio> lista = StorageManager.cargarServicios(this);
        if (modoEditar) {
            for (int i = 0; i < lista.size(); i++) {
                Servicio s = lista.get(i);
                if (s.getId().equals(idServicioEditar)) {
                    s.setNombre(nombre);
                    s.setMonto(monto);
                    s.setFechaVencimiento(fechaVencimientoSeleccionada);
                    s.setPeriodicidad(periodicidad);
                    s.setImportancia(importancia);
                    NotificationScheduler.cancelarNotificacion(this, s);
                    NotificationScheduler.programarNotificacion(this, s);
                    break;
                }
            }
        } else {
            Servicio nuevoServicio = new Servicio(nombre, monto, fechaVencimientoSeleccionada, periodicidad, importancia);
            lista.add(nuevoServicio);
            NotificationScheduler.programarNotificacion(this, nuevoServicio);
        }
        StorageManager.guardarServicios(this, lista);
        Toast.makeText(this, "Servicio guardado", Toast.LENGTH_SHORT).show();
        finish();
    }
}
