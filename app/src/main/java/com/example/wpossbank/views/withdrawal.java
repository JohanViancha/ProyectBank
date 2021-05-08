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

public class withdrawal extends AppCompatActivity {

    TextInputEditText identification, pin, repeatpin, amount;
    TextView toolbar;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        identification = findViewById(R.id.txt_withdrawalidentification);
        pin = findViewById(R.id.txt_withdrawalpin);
        repeatpin = findViewById(R.id.txt_withdrawalrepeatpin);
        amount = findViewById(R.id.txt_withdrawalamount);
        toolbar = findViewById(R.id.tb_title);
        toolbar.setText("Retiros");
    }



    //Función que se ejecuta al dar clic en el botón de realizar retiro
    public void makewithdrawal(View view){
        final int commission = 2000;
        message mes = new message();
        String identificacion = this.identification.getText().toString();
        String pin = this.pin.getText().toString();
        String repeatpin = this.repeatpin.getText().toString();
        String amount = this.amount.getText().toString();

        if(!identificacion.isEmpty() && !pin.isEmpty() && !repeatpin.isEmpty() && !amount.isEmpty()){

            long ahora = System.currentTimeMillis();
            Date fecha = new Date(ahora);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String now = df.format(fecha);

            double newamount = Double.parseDouble(amount);
            SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
            int id = sharedpreferences.getInt("id_correspondent", 0);

            if(validateData(identificacion,pin,repeatpin,Double.parseDouble(amount),commission)){
                Transaction transaction = new Transaction("Retiro",newamount+commission, now,identificacion);
                admintransaction admintra = new admintransaction();
                adminclient admincli = new adminclient();


                //Se realiza primero la confirmación del proceso
                mes.getConfirm(this, "Confirmación","¿Seguro que desea hacer el retiro de $ " + amount,getLayoutInflater(),commission)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(admintra.registertransaction(context,transaction,commission,id)){
                                    if(admincli.setBalanceClient(context, identificacion,newamount+commission,false)){
                                        Intent inte = new Intent(context, menu.class);
                                        mes.getConfirm(context, "Exitoso", "El retiro ha sido exitoso",getLayoutInflater(),0)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(context, menu.class);
                                                        startActivity(intent);
                                                    }
                                                }).show();;

                                    }

                                }else{
                                    mes.getConfirm(context, "Error", "No se pudo realizar el retiro",getLayoutInflater(),0)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {dialog.dismiss();} }).show();


            }
        }else{
            mes.getConfirm(this, "Error", "Todos los campos son obligatorios",getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();
        }

    }


    //Función para validar los datos antes de realizar el retiro
    public boolean validateData(String identification, String pin, String pinrepeat, double amount, int commission){

        String message = "";
        boolean validate = true;
        Client client = new Client(identification,pin, amount);
        adminclient admincli = new adminclient();
        //Se valida que los dos pines ingresados sean iguales
        if(!pin.equals(pinrepeat)){
            message = "* Los pines ingresados deben ser iguales \n \n";
            validate = false;
        }else{
            //Se valida que la cedula y el pin pertenezcan a un cliente registrado
            if(!admincli.validateDataClient(this, client,true)){
                message = "Los datos ingresados del cliente no están registrados \n \n";
                validate = false;
            }
            else{
                if(!admincli.validateBalanceClient(this, client, commission)){
                    message = message + "* El cliente no tiene el saldo suficiente para realizar el retiro \n \n";
                    validate = false;
                }
            }

        }


        if(!validate){
            message mes = new message();
            mes.getConfirm(this, "Error", message,getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
        }


        return validate;
    }



    public void cancel(View view){

        message mes = new message();
        mes.getConfirm(this,"Confirmación","¿Seguro que desea cancelar el retiro?",getLayoutInflater(),0)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mes.getConfirm(context,"Exitoso","Retiro cancelado",getLayoutInflater(),0).
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