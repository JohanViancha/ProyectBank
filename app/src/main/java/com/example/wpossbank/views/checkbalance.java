package com.example.wpossbank.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

public class checkbalance extends AppCompatActivity {


    TextInputEditText identification, pin, pinrepeat;
    TextView balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbalance);

        identification = findViewById(R.id.txt_checkbalanceidentification);
        pin = findViewById(R.id.txt_checkbalancepin);
        pinrepeat = findViewById(R.id.txt_checkbalancepinrepeat);
        balance = findViewById(R.id.txt_checkbalancevalue);
    }


    public void checkBalance(View view){

        String identification = this.identification.getText().toString();
        String pin = this.pin.getText().toString();
        String pinrepeat = this.pinrepeat.getText().toString();
        message mes = new message();
        final int commission = 1000;

        if(!identification.isEmpty() && !pin.isEmpty() && !pinrepeat.isEmpty()){

            Client client = new Client(identification, pin);
            if(validateData(client,pinrepeat,commission)){

                Date dateObj = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formatdate = new SimpleDateFormat("dd/MM/yyyy");
                Date now = null;
                try {
                    now = formatdate.parse(String.valueOf(dateFormat.format(dateObj)));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Se obtiene el id del corresponsal
                SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                int id = sharedpreferences.getInt("id_correspondent", 0);

                Transaction tran = new Transaction("Consulta de Saldo",0,now,identification);
                adminclient admincli = new adminclient();
                admintransaction admintra = new admintransaction();
                if(admintra.registertransaction(this,tran,commission,id)){
                    if(admincli.setBalanceClient(this,identification,commission,false)){
                        balance.setText(String.valueOf(admincli.getBalanceClient(this, identification)));
                    }
                }
            }


        }
        else{

            mes.getMessage(this, "¡Error!", "Todos los datos son obligatorios");
        }
    }

    private boolean validateData(Client client, String pinrepeat,int commission){
        String message = "";
        boolean validate = true;
        adminclient admincli = new adminclient();
        if(!client.getPin().equals(pinrepeat)){
            message = "* Los pines ingresados deben ser iguales \n \n";
            validate = false;
        }else{
            if(!admincli.validateDataClient(this, client,true)){
                message = "* El pin y/o la cedula del cliente que consulta son incorrectos \n \n";
                validate = false;
            }else{
                if(!admincli.validateBalanceClient(this, client,commission)){
                    message = message+"* El cliente no tiene saldo suficiente para hacer la consulta \n \n";
                    validate = false;
                }
            }
        }

        if(!validate){
            message mes = new message();
            mes.getMessage(this,"¡Error!",message );
        }

        return validate;
    }
}