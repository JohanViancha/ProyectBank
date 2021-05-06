package com.example.wpossbank.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.wpossbank.R;
import com.example.wpossbank.managedb.adminclient;
import com.example.wpossbank.managedb.admintransaction;
import com.example.wpossbank.models.Client;
import com.example.wpossbank.models.Transaction;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class createaccount extends AppCompatActivity {

    TextInputEditText name,identification, pin, pinrepeat,balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        name = findViewById(R.id.txt_createaccountname);
        identification = findViewById(R.id.txt_createaccountidentification);
        pin = findViewById(R.id.txt_createaccountpin);
        pinrepeat = findViewById(R.id.txt_createaccountpinrepeat);
        balance = findViewById(R.id.txt_createaccountbalance);

    }

    public void createAccount(View view){

        final int commission = 10000;
        String name =this.name.getText().toString().toUpperCase();
        String identification = this.identification.getText().toString();
        String pin = this.pin.getText().toString();
        String pinrepeat = this.pinrepeat.getText().toString();
        String balance = this.balance.getText().toString();
        message mes = new message();
        if(!name.isEmpty() && !identification.isEmpty() && !pin.isEmpty() && !pinrepeat.isEmpty() && !balance.isEmpty()){
            if(pin.equals(pinrepeat)){
                double newbalance = Double.parseDouble(balance);

                Client client = new Client(identification,name,pin,newbalance);
                adminclient admincli = new adminclient();

                if(admincli.createAccount(this, client)){

                    long ahora = System.currentTimeMillis();
                    Date fecha = new Date(ahora);
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String now = df.format(fecha);
                    //Se obtiene el id del corresponsal
                    SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                    int id = sharedpreferences.getInt("id_correspondent", 0);


                    Transaction tran = new Transaction("Creación de cuenta",0,now,identification);
                    admintransaction admintra = new admintransaction();

                    if(admintra.registertransaction(this,tran,commission,id)){
                        mes.getMessage(this, "¡Exitoso!","El cliente ha sido creado correctamente");
                    }
                }else{
                    mes.getMessage(this,"¡Error!", "Error al crear el cliente");
                }
            }else{
                mes.getMessage(this,"¡Error!", "Los pines deben ser iguales");

            }

        }else{
            mes.getMessage(this, "¡Error!","Todos los campos son obligatorios");
        }
    }
}