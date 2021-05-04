package com.example.wpossbank.managedb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wpossbank.models.Corresponsal;

public class admincorresponsal {


    public admincorresponsal() {
    }

    //Metodo para verificar usuario y contraseña
    public Corresponsal login(Context context, Corresponsal corresponsal){
        admindb admin = new admindb(context,"wpossbank",null, 1);
        SQLiteDatabase sql = admin.getReadableDatabase();

        //se obtiene cursor que retorna la consulta
        Cursor row = sql.rawQuery("select * from corresponsal " +
                "where email_cor = \'"+corresponsal.getEmail()+"\'" +
                "and password_cor = \'"+corresponsal.getPassword()+"\'",null);

        if(row.moveToFirst()){

            //Se modifica el valor del saldo del objeto a retornar
            corresponsal.setSaldo(row.getDouble(3));
        }

        return corresponsal;
    }
}
