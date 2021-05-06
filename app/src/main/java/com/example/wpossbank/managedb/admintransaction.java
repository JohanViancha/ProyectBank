package com.example.wpossbank.managedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.wpossbank.models.Transaction;

public class admintransaction {



    //Metodo para registrar una transacción
    public boolean registertransaction(Context context, Transaction transaction, double valueForCorrespondet, int idcorrespondent){

        boolean result = false;
        try{
            admindb admin = new admindb(context,"wpossbank",null, 1);
            SQLiteDatabase sql = admin.getReadableDatabase();

            ContentValues newtransaction = new ContentValues();
            //Se hace el llenado del contetvalues
            newtransaction.put("type_tra", transaction.getType());
            newtransaction.put("amount_tra", transaction.getAmount());
            newtransaction.put("date_tra", String.valueOf(transaction.getDate()));
            newtransaction.put("identification_tra", transaction.getIdentification());

            //Se inserta la transacción
            if(sql.insert("transactions",null, newtransaction) > 0){

                admincorrespondent admincorr = new admincorrespondent();

                if(admincorr.setSaldo(context, valueForCorrespondet,idcorrespondent)){
                    result =  true;
                }

            }
        }catch (Exception ex){
            System.out.println(ex);
        }

        return result;
    }
}