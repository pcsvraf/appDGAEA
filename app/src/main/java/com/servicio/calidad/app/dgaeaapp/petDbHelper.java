package com.servicio.calidad.app.dgaeaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class petDbHelper extends SQLiteOpenHelper {

    final String CREAR_TABLA_PETICION= "CREATE TABLE peticion(id INTEGER, institucion TEXT, tipoMoneda TEXT, fechaSolicitud TEXT, " +
            "estado TEXT, tipoSolicitud TEXT, cuentaCheque TEXT, descripcion TEXT, monto INTEGER)";

    public petDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_PETICION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS peticion");
        onCreate(db);
    }
}
