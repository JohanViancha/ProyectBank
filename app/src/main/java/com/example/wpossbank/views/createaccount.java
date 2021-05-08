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
    TextView toolbar;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        name = findViewById(R.id.txt_createaccountname);
        identification = findViewById(R.id.txt_createaccountidentification);
        pin = findViewById(R.id.txt_createaccountpin);
        pinrepeat = findViewById(R.id.txt_createaccountpinrepeat);
        balance = findViewById(R.id.txt_createaccountbalance);
        toolbar = findViewById(R.id.tb_title);
        toolbar.setText("Crear cuenta");
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
                        mes.getConfirm(this, "Exitoso","El cliente ha sido creado correctamente",getLayoutInflater(),0)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, menu.class);
                                        startActivity(intent);
                                    }
                                }).show();;
                    }
                }else{
                    mes.getConfirm(this,"Error", "Error al crear el cliente",getLayoutInflater(),0)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
                }
            }else{
                mes.getConfirm(this,"Error", "Los pines deben ser iguales",getLayoutInflater(),0)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;

            }

        }else{
            mes.getConfirm(this, "Error","Todos los campos son obligatorios",getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
        }
    }


    public void cancel(View view){

        message mes = new message();
        mes.getConfirm(this,"Confirmación","¿Seguro que desea cancelar la creación de la cuenta?",getLayoutInflater(),0)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mes.getConfirm(context,"Exitoso","Se canceló la creación de la cuenta",getLayoutInflater(),0).
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