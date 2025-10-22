package com.example.lab5_20202335.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab5_20202335.R;
import com.example.lab5_20202335.models.Servicio;
import com.google.android.material.chip.Chip;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    private List<Servicio> listaServicios;
    private Context context;
    private OnServicioClickListener listener;

    public interface OnServicioClickListener {
        void onEditClick(Servicio servicio);
        void onDeleteClick(Servicio servicio);
        void onPaidClick(Servicio servicio);
    }

    public ServicioAdapter(Context context, List<Servicio> listaServicios, OnServicioClickListener listener) {
        this.context = context;
        this.listaServicios = listaServicios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = listaServicios.get(position);

        // Nombre del servicio
        holder.tvNombre.setText(servicio.getNombre());

        // Monto
        holder.tvMonto.setText(String.format(Locale.getDefault(), "S/ %.2f", servicio.getMonto()));

        // Periodicidad
        holder.tvPeriodicidad.setText(servicio.getPeriodicidad().toUpperCase());

        // Icono según tipo de servicio
        String icono = getIconoServicio(servicio.getNombre());
        holder.tvIcono.setText(icono);

        // Chip de importancia con color
        holder.chipImportancia.setText(servicio.getImportancia().toUpperCase());
        int colorChip = getColorImportancia(servicio.getImportancia());
        holder.chipImportancia.setChipBackgroundColor(ColorStateList.valueOf(colorChip));

        // Fecha de vencimiento
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = sdf.format(new Date(servicio.getFechaVencimiento()));
        holder.tvFecha.setText(fechaFormateada);

        // Calcular días restantes
        long ahora = System.currentTimeMillis();
        long diff = servicio.getFechaVencimiento() - ahora;
        long diasRestantes = TimeUnit.MILLISECONDS.toDays(diff);

        if (diasRestantes > 0) {
            holder.tvDiasRestantes.setText("Faltan " + diasRestantes + " días");
            holder.tvDiasRestantes.setTextColor(diasRestantes <= 3 ?
                context.getColor(R.color.error_color) : context.getColor(R.color.warning_color));
        } else if (diasRestantes == 0) {
            holder.tvDiasRestantes.setText("¡Vence hoy!");
            holder.tvDiasRestantes.setTextColor(context.getColor(R.color.error_color));
        } else {
            holder.tvDiasRestantes.setText("Vencido hace " + (-diasRestantes) + " días");
            holder.tvDiasRestantes.setTextColor(context.getColor(R.color.error_color));
        }

        // Click listeners
        holder.btnEditar.setOnClickListener(v -> listener.onEditClick(servicio));
        holder.btnBorrar.setOnClickListener(v -> listener.onDeleteClick(servicio));
        holder.btnPagado.setOnClickListener(v -> listener.onPaidClick(servicio));
    }

    private String getIconoServicio(String nombre) {
        String nombreLower = nombre.toLowerCase();
        if (nombreLower.contains("luz") || nombreLower.contains("electricidad")) return "💡";
        if (nombreLower.contains("agua")) return "💧";
        if (nombreLower.contains("internet") || nombreLower.contains("wifi")) return "📡";
        if (nombreLower.contains("gas")) return "🔥";
        if (nombreLower.contains("teléfono") || nombreLower.contains("celular")) return "📱";
        if (nombreLower.contains("cable") || nombreLower.contains("tv")) return "📺";
        if (nombreLower.contains("alquiler") || nombreLower.contains("renta")) return "🏠";
        if (nombreLower.contains("seguro")) return "🛡️";
        return "📋";
    }

    private int getColorImportancia(String importancia) {
        switch (importancia.toLowerCase()) {
            case "alta":
                return context.getColor(R.color.chip_alta);
            case "baja":
                return context.getColor(R.color.chip_baja);
            default:
                return context.getColor(R.color.chip_media);
        }
    }

    @Override
    public int getItemCount() {
        return listaServicios.size();
    }

    public static class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMonto, tvFecha, tvDiasRestantes, tvPeriodicidad, tvIcono;
        Chip chipImportancia;
        Button btnPagado, btnEditar, btnBorrar;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.textViewNombre);
            tvMonto = itemView.findViewById(R.id.textViewMonto);
            tvFecha = itemView.findViewById(R.id.textViewFecha);
            tvDiasRestantes = itemView.findViewById(R.id.textViewDiasRestantes);
            tvPeriodicidad = itemView.findViewById(R.id.textViewPeriodicidad);
            tvIcono = itemView.findViewById(R.id.textViewIcono);
            chipImportancia = itemView.findViewById(R.id.chipImportancia);
            btnPagado = itemView.findViewById(R.id.btnMarcarPagado);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
        }
    }
}
