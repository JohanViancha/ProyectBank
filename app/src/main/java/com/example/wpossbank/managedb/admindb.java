package com.example.wpossbank.managedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class admindb extends SQLiteOpenHelper {



    public admindb(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //Creación de base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Se crea la tabla de corresponsal
        db.execSQL("CREATE TABLE corresponsal (" +
                "id_corresponsal integer primary key," +
                "email_cor text," +
                "password_cor text," +
                "saldo money"+
                ")");


        //Se crea el correponsal inicial
        db.execSQL("INSERT INTO corresponsal VALUES (1,'johan@wposs.com','123456',100000)");

        //Se crea la tabla cliente
        db.execSQL("CREATE TABLE cliente(" +
                "id_cliente integer primary key," +
                "nombre_cli text,"+
                "cedula_cli text," +
                "pin_cli text," +
                "saldo_cli money)");

        //Se crea la tabla transaccion
        db.execSQL("CREATE TABLE transaccion(" +
                "id_transacion integer primary key," +
                "tipo_tra text," +
                "monto_tra money," +
                "fecha_tra date," +
                "cedula_tra)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
