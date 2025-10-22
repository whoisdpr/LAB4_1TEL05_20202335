// models/Servicio.java
package com.example.lab5_20202335.models;

import java.util.UUID;

public class Servicio {
    private String id;
    private String nombre;
    private double monto;
    private long fechaVencimiento; // Timestamp en milisegundos
    private String periodicidad;    // "mensual", "bimestral", etc.
    private String importancia;     // "alta", "media", "baja"

    // Constructor para un nuevo servicio
    public Servicio(String nombre, double monto, long fechaVencimiento, String periodicidad, String importancia) {
        this.id = UUID.randomUUID().toString(); // ID único
        this.nombre = nombre;
        this.monto = monto;
        this.fechaVencimiento = fechaVencimiento;
        this.periodicidad = periodicidad;
        this.importancia = importancia;
    }

    // Getters y Setters (Necesarios para GSON y para modificarlos)
    // ... Clic derecho -> Generate -> Getters and Setters -> Select All
    // (A continuación, un ejemplo)

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getMonto() { return monto; }
    public long getFechaVencimiento() { return fechaVencimiento; }
    public String getPeriodicidad() { return periodicidad; }
    public String getImportancia() { return importancia; }

    public void setFechaVencimiento(long fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setMonto(double monto) { this.monto = monto; }
    public void setPeriodicidad(String periodicidad) { this.periodicidad = periodicidad; }
    public void setImportancia(String importancia) { this.importancia = importancia; }
    // ... (Añade los otros setters si los necesitas)
}