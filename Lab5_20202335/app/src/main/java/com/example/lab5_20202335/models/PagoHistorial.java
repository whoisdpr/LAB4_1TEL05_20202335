// models/PagoHistorial.java
package com.example.lab5_20202335.models;

import java.util.UUID;

public class PagoHistorial {
    private String id;
    private String nombreServicio;
    private double montoPagado;
    private long fechaDePago; // Timestamp de "ahora"
    private long fechaVencimientoOriginal; // La fecha que tenía el servicio

    public PagoHistorial(String nombreServicio, double montoPagado, long fechaDePago, long fechaVencimientoOriginal) {
        this.id = UUID.randomUUID().toString();
        this.nombreServicio = nombreServicio;
        this.montoPagado = montoPagado;
        this.fechaDePago = fechaDePago;
        this.fechaVencimientoOriginal = fechaVencimientoOriginal;
    }

    public String getId() { return id; }
    public String getNombreServicio() { return nombreServicio; }
    public double getMontoPagado() { return montoPagado; }
    public long getFechaDePago() { return fechaDePago; }
    public long getFechaVencimientoOriginal() { return fechaVencimientoOriginal; }
}
