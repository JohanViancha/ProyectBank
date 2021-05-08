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

public class deposit extends AppCompatActivity {

    TextInputEditText send,receive, amount;
    TextView toolbar;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        send = findViewById(R.id.txt_depositsend);
        receive = findViewById(R.id.txt_depositreceive);
        amount = findViewById(R.id.txt_depositamount);
        toolbar = findViewById(R.id.tb_title);
        toolbar.setText("Depositos");

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

                //Se obtiene la fecha actual
                long ahora = System.currentTimeMillis();
                Date fecha = new Date(ahora);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String now = df.format(fecha);

                Transaction transaction = new Transaction("Deposito",newamount+commission, now,send);

                SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                int id = sharedpreferences.getInt("id_correspondent", 0);
                admintransaction admintra = new admintransaction();
                adminclient admincli = new adminclient();


                mes.getConfirm(this,"Confirmación","Esta seguro de realizar el deposito de "+amount + " pesos",getLayoutInflater(), commission)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(admintra.registertransaction(context,transaction, commission, id)){
                                    if(admincli.setBalanceClient(context, receive,newamount,true)) {
                                        mes.getConfirm(context, "Exitoso","El deposito ha sido realizado",getLayoutInflater(),0)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(context, menu.class);
                                                        startActivity(intent);
                                                    }
                                                }).show();;
                                    }

                                }else{
                                    mes.getConfirm(context, "Error","Error al realizar el deposito",getLayoutInflater(),0)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;

                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();;



            }

        }
        else{

            mes.getConfirm(this,"Error","Todos los campos son obligatorios",getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();
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
            mes.getConfirm(this, "Error",message,getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
        }

        return validate;
    }


    public void cancel(View view){

        message mes = new message();
        mes.getConfirm(this,"Confirmación","¿Seguro que desea cancelar el deposito?",getLayoutInflater(),0)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mes.getConfirm(context,"Exitoso","Deposito cancelado",getLayoutInflater(),0).
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