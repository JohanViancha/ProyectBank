package com.example.wpossbank.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.wpossbank.MainActivity;
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
    TextView toolbar;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbalance);

        identification = findViewById(R.id.txt_checkbalanceidentification);
        pin = findViewById(R.id.txt_checkbalancepin);
        pinrepeat = findViewById(R.id.txt_checkbalancepinrepeat);
        balance = findViewById(R.id.txt_checkbalancevalue);
        toolbar = findViewById(R.id.tb_title);
        toolbar.setText("Consultar Saldo");

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

                long ahora = System.currentTimeMillis();
                Date fecha = new Date(ahora);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String now = df.format(fecha);

                //Se obtiene el id del corresponsal
                SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                int id = sharedpreferences.getInt("id_correspondent", 0);

                Transaction tran = new Transaction("Consulta de Saldo",0,now,identification);
                adminclient admincli = new adminclient();
                admintransaction admintra = new admintransaction();

                mes.getConfirm(this, "Confirmación","¿Esta seguro que desea consultar el saldo?",getLayoutInflater(),commission)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(admintra.registertransaction(context,tran,commission,id)){
                                    if(admincli.setBalanceClient(context,identification,commission,false)){
                                        balance.setText("$ " +String.valueOf(admincli.getBalanceClient(context, identification)));
                                    }
                                }
                            }

                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
                        }).show();

            }


        }
        else{

            mes.getConfirm(this, "Error", "Todos los datos son obligatorios",getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
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
            mes.getConfirm(this,"Error",message ,getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
        }

        return validate;
    }


    public void cancel(View view){

        message mes = new message();
        mes.getConfirm(this,"Confirmación","¿Seguro que desea cancelar la consulta del saldo?",getLayoutInflater(),0)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mes.getConfirm(context,"Exitoso","Se canceló la consulta del saldo",getLayoutInflater(),0).
                                setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent inte = new Intent(context, menu.class);
                                        startActivity(inte);
                                    }
                                }).show();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}