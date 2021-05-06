package com.example.wpossbank.managedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wpossbank.models.Client;

public class adminclient {


    // Función para validar el pin y la cedula del cliente
    public boolean validateDataClient(Context context, Client client,boolean option){

        boolean result = false;
        admindb admin = new admindb(context,"wpossbank",null, 1);
        SQLiteDatabase sql = admin.getReadableDatabase();



        if(option){
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

    //Función para validar el saldo del cliente
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


    public double getBalanceClient(Context context, String identification){
        double balance = 0;
        admindb admin = new admindb(context,"wpossbank",null, 1);
        SQLiteDatabase sql = admin.getReadableDatabase();


        //se obtiene cursor que retorna la consulta
        Cursor row = sql.rawQuery("select * from clients " +
                "where identification_cli = \'"+identification+"\'",null);

        if(row.moveToFirst()){
            balance =row.getDouble(4);
        }

        return balance;
    }


    public boolean createAccount(Context context, Client client){

        admindb admin = new admindb(context,"wpossbank",null, 1);
        SQLiteDatabase sql = admin.getWritableDatabase();

        ContentValues newclient = new ContentValues();
        //Se hace el llenado del contetvalues
        newclient.put("name_cli", client.getName());
        newclient.put("identification_cli", client.getId());
        newclient.put("pin_cli", client.getPin());
        newclient.put("balance_cli", client.getBalance());

        //Se inserta la transacción
        if(sql.insert("clients",null, newclient) > 0){

            return true;
        }
        else{
            return false;
        }
    }
}
