package com.example.lab5_20202335.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab5_20202335.R;
import com.example.lab5_20202335.adapters.HistoryAdapter;
import com.example.lab5_20202335.models.PagoHistorial;
import com.example.lab5_20202335.utils.StorageManager;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ArrayList<PagoHistorial> listaHistorial;
    private LinearLayout layoutSinHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Historial de Pagos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerViewHistorial);
        layoutSinHistorial = findViewById(R.id.layoutSinHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaHistorial = StorageManager.cargarHistorial(this);

        if (listaHistorial.isEmpty()) {
            layoutSinHistorial.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            layoutSinHistorial.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new HistoryAdapter(this, listaHistorial);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
