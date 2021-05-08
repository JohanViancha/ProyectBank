package com.example.wpossbank.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wpossbank.MainActivity;
import com.example.wpossbank.R;
import com.example.wpossbank.managedb.admintransaction;
import com.example.wpossbank.models.Transaction;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class cardpayment extends AppCompatActivity implements View.OnFocusChangeListener {

    TextInputEditText card,name,date,cvv,value, fees;
    ImageView franchise;
    TextView toolbar;
    Context context = this;
    menu menu;

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

        toolbar = findViewById(R.id.tb_title);
        toolbar.setText("Pago con tarjeta");

    }




    public void registerpayment(View view){
        final String type = "Venta con tarjeta";
        message mes = new message();
        //Se obtiene los valores de cada input
        String card = this.card.getText().toString();
        String name = this.name.getText().toString().toUpperCase();
        String date = this.date.getText().toString();
        String cvv = this.cvv.getText().toString();
        String val =this.value.getText().toString();
        String fees = this.fees.getText().toString();

        if(!card.isEmpty() && !name.isEmpty() && !date.isEmpty() && !cvv.isEmpty() && !val.isEmpty() && !fees.isEmpty()){


            double value = Double.parseDouble(val);
            Date dateObj = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatdate = new SimpleDateFormat("dd/MM/yyyy");
            Date newdate = null;
            Date now = null;
            try {
                newdate = formatdate.parse(date);
                now = formatdate.parse(String.valueOf(dateFormat.format(dateObj)));

                //Se usa el metodo validateDate verificar que los datos tengas las condiciones necesarias
                if(validateData(card,newdate,now,date,cvv,value,Integer.parseInt(fees))){

                    String messageHead = "Desea hacer el pago a WPOSS por un valor de $ " + value + " \n\n";
                    String messagefedds = "A " + fees + " cuotas \n\n";
                    String oculto ="";
                    if(card.length()==15){
                        oculto = "***********";
                    }else{
                        oculto = "************";
                    }
                    String messacard = "Numero de tarjeta "+card.charAt(0)+card.charAt(1)+card.charAt(2)+card.charAt(4)+oculto+"\n\n";

                    mes.getConfirm(this, "Confirme pago",messageHead+messagefedds+messacard,getLayoutInflater(),0)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    long ahora = System.currentTimeMillis();
                                    Date fecha = new Date(ahora);
                                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    String actual = df.format(fecha);
                                    Transaction transaction = new Transaction(type,value,actual,card);
                                    admintransaction admin = new admintransaction();
                                    SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                                    int id = sharedpreferences.getInt("id_correspondent", 0);


                                    Intent inte = new Intent(context, menu.class);
                                    //Si se realiza con exito la transacción se muestra el siguiente mensaje
                                    if(admin.registertransaction(context,transaction,transaction.getAmount(),id)){

                                        mes.getConfirm(context,"Exitoso", "El pago ha sido exitoso",getLayoutInflater(),0)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(context, menu.class);
                                                        startActivity(intent);
                                                    }
                                                }).show();
                                        clearData();
                                    }
                                    else{
                                        mes.getConfirm(context,"Error", "No se pudo realizar el pago",getLayoutInflater(),0)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();

                                    }
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                    }


            } catch (ParseException  ex) {
                mes.getConfirm(this,"Error", "Han ingresado algun dato invalido",getLayoutInflater(),0)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
            }


        }else{
            mes.getConfirm(this,"Error", "Todos los campos son obligatorios",getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;

        }


    }



    //Función para validar los datos del pago
    public boolean validateData(String card, Date date,Date now,String dateString,String cvv,double value, int fees){
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


        //Se valida el formato de la fecha

        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(dateString);

            //Se valida que la fehca de vencimiento sea mayor a la fecha actual
            if(date.compareTo(now) < 0){
                message = message + "* La fecha de vencimiento debe ser mayor a la fecha actual \n\n";
                result= false;
            }

        } catch (ParseException e) {
            message = message + "* La fecha no tiene formato correcto \n \n";
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

        if(fees < 1 && fees > 12){
            message = message + "* El numero de cuotas permitidas son entre 1 y 12 \n \n";
        }



        //Si se genero algun error se muestra un mensaje de error
        if(!result){
            message mes = new message();
           mes.getConfirm(this,"Error",message,getLayoutInflater(),0)
                   .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
        }

        return result;
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


    public void cancel(View view) {

        message mes = new message();
        mes.getConfirm(this,"Confirmación","¿Seguro que desea cancelar el pago?",getLayoutInflater(),0)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mes.getConfirm(context,"Exitoso","Se canceló el pago",getLayoutInflater(),0).
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

    private void clearData(){
        card.setText("");
        name.setText("");
        date.setText("");
        cvv.setText("");
        value.setText("");
        fees.setText("");
        franchise.setImageResource(0);
    }

}