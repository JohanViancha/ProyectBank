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

public class transfer extends AppCompatActivity {

    TextInputEditText send, receive, amount, pin, pinrepeat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        send = findViewById(R.id.txt_transfersend);
        receive = findViewById(R.id.txt_transferreceive);
        amount = findViewById(R.id.txt_transferamount);
        pin = findViewById(R.id.txt_transferpin);
        pinrepeat = findViewById(R.id.txt_transferpinrepeat);

    }

    public void transferamount(View view){
        message mes = new message();

        final int commission = 1000;
        String send = this.send.getText().toString();
        String receive = this.receive.getText().toString();
        String amount = this.amount.getText().toString();
        String pin = this.pin.getText().toString();
        String pinrepeat = this.pinrepeat.getText().toString();

        if(!send.isEmpty() && !receive.isEmpty() && !amount.isEmpty() && !pin.isEmpty() && !pinrepeat.isEmpty()){

            double newamount = Double.parseDouble(amount);
            Client client1 = new Client(send,pin,newamount);
            Client client2 = new Client(receive);
            if(validateData(this,client1,client2,pinrepeat,commission)){

                //Se obtiene la fecha actual
                long ahora = System.currentTimeMillis();
                Date fecha = new Date(ahora);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String now = df.format(fecha);
                //Se obtiene el id del corresponsal
                SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                int id = sharedpreferences.getInt("id_correspondent", 0);


                Transaction tran = new Transaction("Transferencia",newamount+commission,now,send);
                adminclient admincli = new adminclient();
                admintransaction admintra = new admintransaction();
                //Se hace el registro de la transacción
                if(admintra.registertransaction(this,tran,commission,id)){
                    if(admincli.setBalanceClient(this,send,newamount+commission, false)){
                        if(admincli.setBalanceClient(this, receive,newamount,true)){
                            mes.getMessage(this, "¡Correcto!", "El transferencia ha sido exitoso");

                        }
                    }
                }
                else{
                    mes.getMessage(this, "¡Error!", "No se pudo realizar la transferencia");

                }
            }else{
                mes.getMessage(this, "¡Error!", "Todos los campos son obligatorios");

            }

        }

    }

    private boolean validateData(Context context, Client client1, Client client2,String pinrepeat, int commission){
        String message = "";
        boolean validate = true;
        adminclient admincli = new adminclient();

        //Se valiad que los pines ingresados sean iguales
        if(pinrepeat.equals(client1.getPin())){
            if(!admincli.validateDataClient(context, client1,true)){
                message = "* El pin y/o la cedula del cliente que transfiere no existe \n \n";
                validate = false;
            }else{

                if(!admincli.validateBalanceClient(context,client1,commission)){
                    message = message + "* El cliente que transfiere no tiene saldo suficiente \n \n";
                    validate = false;
                }
            }
            if(!admincli.validateDataClient(context,client2, false )){
                message = message + "* La cedula del cliente que recibe la transferencia no existe \n \n";
                validate = false;
            }

            if(client1.getId().equals(client2.getId())){
                message = message + "* Las cedulas ingresadas deben ser diferentes \n \n";
                validate = false;
            }
        }else{
            message = message+"* Los pines ingresados deben ser iguales \n \n";
            validate = false;
        }

        if(!validate){
            message mes = new message();
            mes.getMessage(this,"¡Error!", message);
        }

        return validate;
    }
}