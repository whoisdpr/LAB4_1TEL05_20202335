// utils/StorageManager.java
package com.example.lab5_20202335.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.lab5_20202335.models.Servicio;
import com.example.lab5_20202335.models.PagoHistorial;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {

    private static final String PREFS_NAME = "ServiciosPrefs";
    private static final String KEY_SERVICIOS = "lista_servicios";
    private static final String KEY_HISTORIAL = "lista_historial";
    private static final Gson gson = new Gson();

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // --- Gestión de Servicios ---

    public static ArrayList<Servicio> cargarServicios(Context context) {
        String json = getPrefs(context).getString(KEY_SERVICIOS, null);
        if (json == null) {
            return new ArrayList<>(); // Retorna lista vacía si no hay nada
        }
        Type type = new TypeToken<ArrayList<Servicio>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void guardarServicios(Context context, List<Servicio> servicios) {
        String json = gson.toJson(servicios);
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(KEY_SERVICIOS, json);
        editor.apply();
    }

    // --- Gestión del Historial ---

    public static ArrayList<PagoHistorial> cargarHistorial(Context context) {
        String json = getPrefs(context).getString(KEY_HISTORIAL, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<PagoHistorial>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void guardarHistorial(Context context, List<PagoHistorial> historial) {
        String json = gson.toJson(historial);
        getPrefs(context).edit().putString(KEY_HISTORIAL, json).apply();
    }
}