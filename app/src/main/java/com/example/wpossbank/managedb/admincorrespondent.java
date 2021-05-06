package com.example.wpossbank.managedb;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wpossbank.models.Correspondent;

public class admincorrespondent {


    public admincorrespondent() {
    }

    //Metodo para verificar usuario y contraseña
    public Correspondent login(Context context, Correspondent correspondent){
        admindb admin = new admindb(context,"wpossbank",null, 1);
        SQLiteDatabase sql = admin.getReadableDatabase();

        //se obtiene cursor que retorna la consulta
        Cursor row = sql.rawQuery("select * from correspondent " +
                "where email_cor = \'"+correspondent.getEmail()+"\'" +
                "and password_cor = \'"+correspondent.getPassword()+"\'",null);

        if(row.moveToFirst()){

            //Se modifica el valor del saldo y el id del objeto a retornar
            correspondent.setBalance(row.getDouble(3));
            correspondent.setId(row.getInt(0));
        }
        else{
            correspondent = null;
        }

        return correspondent;
    }


    public boolean setSaldo(Context context, double balance, int id){

        boolean result = false;
        admindb admin = new admindb(context,"wpossbank",null, 1);
        SQLiteDatabase sql = admin.getWritableDatabase();

        try {

            sql.execSQL("update correspondent set balance_cor = balance_cor + \'"+balance+"\' where id_correspondent = \'"+id+"\'");

            result = true;
        }catch (Exception ex){
            System.out.println(ex);
        }
        return result;


    }
}
