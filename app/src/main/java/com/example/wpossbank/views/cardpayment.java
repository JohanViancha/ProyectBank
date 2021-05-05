package com.example.wpossbank.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wpossbank.R;
import com.example.wpossbank.managedb.admintransaction;
import com.example.wpossbank.models.Transaction;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class cardpayment extends AppCompatActivity implements View.OnFocusChangeListener {

    TextInputEditText card,name,date,cvv,value, fees;
    ImageView franchise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardpayment);

        card = findViewById(R.id.txt_cardpaymentcard);
        card.setOnFocusChangeListener(this);
        name = findViewById(R.id.txt_cardpaymentname);
        date = findViewById(R.id.txt_cardpaymentdate);
        cvv = findViewById(R.id.txt_cardpaymentcvv);
        value = findViewById(R.id.txt_cardpaymentvalue);
        fees = findViewById(R.id.txt_cardpaymentfees);
        franchise = findViewById(R.id.iv_cardpaymentfranchise);
    }

    public void registerpayment(View view){
        final String type = "Venta con tarjeta";

        //Se obtiene los valores de cada input
        String card = this.card.getText().toString();
        String name = this.name.getText().toString().toUpperCase();
        String date = this.date.getText().toString();
        String cvv = this.cvv.getText().toString();
        double value = Double.parseDouble(this.value.getText().toString());
        String fees = this.value.getText().toString();

        Date dateObj = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatdate = new SimpleDateFormat("dd/MM/yyyy");
        Date newdate = null;
        Date now = null;
        try {
            newdate = formatdate.parse(date);
            now = formatdate.parse(String.valueOf(dateFormat.format(dateObj)));

            //Se usa el metodo validateDate verificar que los datos tengas las condiciones necesarias
            if(validateData(card,newdate,now,cvv,value)){
                Transaction transaction = new Transaction(type,value,now,card);
                admintransaction admin = new admintransaction();
                SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                int id = sharedpreferences.getInt("id_correspondent", 0);



                //Si se realiza con exito la transacción se muestra el siguiente mensaje
                if(admin.registertransaction(this,transaction,id)){
                    getMessage("!Exitoso!", "El pago ha sido exitoso");
                    Intent inte = new Intent(this, menu.class);
                    startActivity(inte);
                }
                else{
                    getMessage("!Error!", "No se pudo realizar el pago");

                }
            }
        } catch (ParseException  ex) {
            ex.printStackTrace();
        }


    }



    //Función para validar los datos del pago
    public boolean validateData(String card, Date date,Date now,String cvv,double value){
        String message = "";
        boolean result = true;
        //Se valida el tamaño de la tarjeta
        if(card.length() < 15 || card.length() > 16){
            message = "* El numero de la tarjeta debe tener entre 15 y 16 digitos \n\n";
            result= false;
        }
        //Se valida el primer digito de la tarjeta
        if(card.charAt(0) != '3' && card.charAt(0) != '4' && card.charAt(0) != '5' && card.charAt(0) != '6') {
            message = message + "* El numero de la tajera debe empezar con 3,4,5 o 6 \n\n";
            result= false;
        }

        //Se valida que la fehca de vencimiento sea mayor a la fecha actual
        if(date.compareTo(now) < 0){
            message = message + "* La fecha de vencimiento debe ser mayor a la fecha actual \n\n";
            result= false;
        }

        //Se valida el tamaño del cvv
        if(cvv.length() <3 || cvv.length() > 4){
            message = message + "* El codigo CVV debe tener entre 3 a 4 caracteres \n\n";
            result= false;
        }

        //Se valida el valor a pagar
        if(value <= 10000 || value >= 1000000){
            message = message + "* El valor a pagar debe ser mayor a 10.000 y menor a 1.000.000 \n \n";
            result= false;

        }

        //Si se genero algun error se muestra un mensaje de error
        if(!result){
           getMessage("¡Error!",message);
        }

        return result;
    }


    //Función para mostrar mensaje en forma de AlertDialog
    public void getMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
    }


    //Evento que se ejecuta cada vez que el edit text del numero de la tarjeta modifica su focus
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String card = this.card.getText().toString();

        //Se muestra la franquicia segun el primero digito de la tarjeta
        if(card.length()>= 15 && card.length()<=16){
            switch (card.charAt(0)){
                case '3':
                    franchise.setImageResource(R.drawable.americanexpress);
                    break;

                case '4':
                    franchise.setImageResource(R.drawable.visa);
                    break;

                case '5':
                    franchise.setImageResource(R.drawable.mastercard);
                    break;

                case '6':
                    franchise.setImageResource(R.drawable.unionpay);
                    break;

                default:
                    franchise.setImageResource(R.drawable.ic_baseline_payments_24);
                    break;
            }

        }
        else{
            franchise.setImageResource(R.drawable.ic_baseline_payments_24);
        }

    }
}