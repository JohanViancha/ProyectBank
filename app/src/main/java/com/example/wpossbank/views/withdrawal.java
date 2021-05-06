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

public class withdrawal extends AppCompatActivity {

    TextInputEditText identification, pin, repeatpin, amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        identification = findViewById(R.id.txt_withdrawalidentification);
        pin = findViewById(R.id.txt_withdrawalpin);
        repeatpin = findViewById(R.id.txt_withdrawalrepeatpin);
        amount = findViewById(R.id.txt_withdrawalamount);
    }


    //Función que se ejecuta al dar clic en el botón de realizar retiro
    public void makewithdrawal(View view){
        final int commission = 2000;
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
                if(admintra.registertransaction(this,transaction,commission,id)){
                    if(admincli.setBalanceClient(this, identificacion,newamount+commission,false)){
                        message mes = new message();
                        mes.getMessage(this, "¡Correcto!", "El retiro ha sido exitoso");

                    }

                }
            }
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
            mes.getMessage(this, "¡Error!", message);
        }


        return validate;
    }

}