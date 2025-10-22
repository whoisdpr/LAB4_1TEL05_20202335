package com.example.lab5_20202335.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab5_20202335.R;
import com.example.lab5_20202335.models.PagoHistorial;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<PagoHistorial> listaHistorial;
    private Context context;

    public HistoryAdapter(Context context, List<PagoHistorial> listaHistorial) {
        this.context = context;
        this.listaHistorial = listaHistorial;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_historial, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        PagoHistorial item = listaHistorial.get(position);
        holder.tvNombre.setText(item.getNombreServicio());
        holder.tvMonto.setText(String.format("S/ %.2f", item.getMontoPagado()));

        long diff = item.getFechaVencimientoOriginal() - item.getFechaDePago();
        long diasAntes = TimeUnit.MILLISECONDS.toDays(diff);

        String anticipacion;
        if (diasAntes > 0) {
            anticipacion = "Pagado " + diasAntes + " días antes";
        } else if (diasAntes == 0) {
            anticipacion = "Pagado el mismo día";
        } else {
            anticipacion = "Pagado " + (-diasAntes) + " días después";
        }
        holder.tvAnticipacion.setText(anticipacion);
    }

    @Override
    public int getItemCount() {
        return listaHistorial.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMonto, tvAnticipacion;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.textViewHistNombre);
            tvMonto = itemView.findViewById(R.id.textViewHistMonto);
            tvAnticipacion = itemView.findViewById(R.id.textViewHistAnticipacion);
        }
    }
}
