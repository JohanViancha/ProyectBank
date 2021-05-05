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
        db.execSQL("CREATE TABLE correspondent (" +
                "id_correspondent integer primary key," +
                "email_cor text," +
                "password_cor text," +
                "balance_cor money"+
                ")");


        //Se crea el correponsal inicial
        db.execSQL("INSERT INTO correspondent VALUES (1,'johan@wposs.com','123456',100000)");

        //Se crea la tabla cliente
        db.execSQL("CREATE TABLE clients (" +
                "id_cliente integer primary key," +
                "nombre_cli text,"+
                "cedula_cli text," +
                "pin_cli text," +
                "saldo_cli money)");

        //Se crea la tabla transaccion
        db.execSQL("CREATE TABLE transactions (" +
                "id_transacion integer primary key," +
                "type_tra text," +
                "amount_tra money," +
                "date_tra date," +
                "identification_tra)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
