// models/Servicio.java
package com.example.lab5_20202335.models;

import java.util.UUID;

public class Servicio {
    private String id;
    private String nombre;
    private double monto;
    private long fechaVencimiento; 
    private String periodicidad;   
    private String importancia;    

    // Constructor para un nuevo servicio
    public Servicio(String nombre, double monto, long fechaVencimiento, String periodicidad, String importancia) {
        this.id = UUID.randomUUID().toString(); 
        this.nombre = nombre;
        this.monto = monto;
        this.fechaVencimiento = fechaVencimiento;
        this.periodicidad = periodicidad;
        this.importancia = importancia;
    }





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

}