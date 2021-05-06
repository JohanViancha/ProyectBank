package com.example.wpossbank.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.wpossbank.R;
import com.example.wpossbank.managedb.adminclient;
import com.example.wpossbank.managedb.admincorrespondent;
import com.example.wpossbank.managedb.admintransaction;
import com.example.wpossbank.models.Client;
import com.example.wpossbank.models.Transaction;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class deposit extends AppCompatActivity {

    TextInputEditText send,receive, amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        send = findViewById(R.id.txt_depositsend);
        receive = findViewById(R.id.txt_depositreceive);
        amount = findViewById(R.id.txt_depositamount);
    }

    public void makeDeposit(View view){

        final double commission = 1000;
        String send = this.send.getText().toString();
        String receive = this.receive.getText().toString();
        String amount = this.amount.getText().toString();
        message mes = new message();

        if(!send.isEmpty() && !receive.isEmpty() && !amount.isEmpty()){

            double newamount = Double.parseDouble(amount);

            Client client1 = new Client(send);
            Client client2 = new Client(receive);

            //Se valida que las dos celulas ingresadas existen en base de datos
            if(validateData(client1,client2)){

                Date dateObj = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formatdate = new SimpleDateFormat("dd/MM/yyyy");
                Date now = null;
                try {
                    now = formatdate.parse(String.valueOf(dateFormat.format(dateObj)));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Transaction transaction = new Transaction("Deposito",newamount+commission, now,send);

                SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                int id = sharedpreferences.getInt("id_correspondent", 0);
                admintransaction admintra = new admintransaction();
                adminclient admincli = new adminclient();
                if(admintra.registertransaction(this,transaction, commission, id)){
                    if(admincli.setBalanceClient(this, receive,newamount,true)) {
                        mes.getMessage(this, "¡Exitoso!","El deposito ha sido realizado");
                    }

                }else{
                    mes.getMessage(this, "¡Error!","Error al realizar el deposito");

                }


            }

        }
        else{

            mes.getMessage(this,"¡Error!","Todos los campos son obligatorios");
        }
    }


    private boolean validateData(Client client1, Client client2){
        adminclient admincli = new adminclient();
        String message = "";
        boolean validate = true;
        if(!admincli.validateDataClient(this,client1,false)){
            message = "* La cedula del cliente que deposita no existe \n\n";
            validate = false;
        }
        if(!admincli.validateDataClient(this,client2,false)){
            message = message+"* La cedula del cliente a quien se le deposita no existe \n\n";
            validate = false;
        }
        if(client1.getId().equals(client2.getId())){
            message = message+"* Las cedulas ingresadas no pueden ser iguales \n\n";
            validate = false;
        }

        if(!validate){
            message mes = new message();
            mes.getMessage(this, "¡Error!",message);
        }

        return validate;
    }
}