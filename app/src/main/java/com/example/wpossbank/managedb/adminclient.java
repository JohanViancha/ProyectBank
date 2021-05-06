package com.example.wpossbank.managedb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wpossbank.models.Client;

public class adminclient {

    public boolean validateDataClient(Context context, Client client,boolean option){

        boolean result = false;
        admindb admin = new admindb(context,"wpossbank",null, 1);
        SQLiteDatabase sql = admin.getReadableDatabase();



        if(option){
            // Se valida la cedula y el pin existan
            //se obtiene cursor que retorna la consulta
            Cursor row = sql.rawQuery("select * from clients " +
                    "where identification_cli = \'"+client.getId()+"\'" +
                    "and pin_cli = \'"+client.getPin()+"\'",null);

            if(row.moveToFirst()){
                result = true;
            }
        }else{

            //Se valida que la cedula exista
            Cursor row = sql.rawQuery("select * from clients " +
                    "where identification_cli = \'"+client.getId()+"\'",null);

            if(row.moveToFirst()){
                result = true;
            }
        }


        return result;
    }

    public boolean validateBalanceClient(Context context, Client client, int commission){

        boolean result = false;
        double balance = client.getBalance() + commission;
        admindb admin = new admindb(context,"wpossbank",null, 1);
        SQLiteDatabase sql = admin.getReadableDatabase();


        //se obtiene cursor que retorna la consulta
        Cursor row = sql.rawQuery("select * from clients " +
                "where identification_cli = \'"+client.getId()+"\'"+
                "and balance_cli >= \'"+balance+"\'",null);


        if(row.moveToFirst()){
            result = true;
        }

        return result;
    }

    public boolean setBalanceClient(Context context,String identification, double amount, boolean action){ //El parametro action se usa para evaluar si se debe sumar o restar al saldo

        boolean result = false;
        admindb admin = new admindb(context,"wpossbank",null, 1);
        SQLiteDatabase sql = admin.getWritableDatabase();

        try {

            if(action) {
                sql.execSQL("update clients set balance_cli = balance_cli + \'" + amount + "\' where identification_cli = \'" + identification + "\'");
            }else{
                sql.execSQL("update clients set balance_cli = balance_cli - \'" + amount + "\' where identification_cli = \'" + identification + "\'");
            }

            result = true;
        }catch (Exception ex){
            System.out.println(ex);
        }
        return result;

    }
}
