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
import com.example.wpossbank.managedb.admincorrespondent;
import com.example.wpossbank.models.Transaction;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class correspondentbalance extends AppCompatActivity {

    TextInputEditText email, password,passwordrepeat;
    TextView balance;
    TextView toolbar;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correspondentbalance);

        email = findViewById(R.id.txt_correspondentbalanceemail);
        password = findViewById(R.id.txt_correspondentbalancepassword);
        passwordrepeat = findViewById(R.id.txt_correspondentbalancepasswordrepat);
        balance = findViewById(R.id.tv_correspondentbalancevalue);
        admincorrespondent admincorr = new admincorrespondent();
        toolbar = findViewById(R.id.tb_title);
        toolbar.setText("Saldo del corresponsal");

        SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("id_correspondent", 0);
        balance.setText(String.valueOf(admincorr.getBalanceCorrespondent(this,id)));

    }

    public void updatePassword(View view){

        message mes = new message();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String passwordrepeat = this.passwordrepeat.getText().toString();

        if(!email.isEmpty() && !password.isEmpty() && !passwordrepeat.isEmpty()){
            if(password.equals(passwordrepeat)){

                admincorrespondent admincorr = new admincorrespondent();

                //Se obtiene el id del corresponsal
                SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                int id = sharedpreferences.getInt("id_correspondent", 0);

                if(admincorr.updatePasswordCorrespondent(this,id,password)){
                    mes.getConfirm(this,"Exitoso","Las contraseña ha sido actualizada",getLayoutInflater(),0)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, menu.class);
                                    startActivity(intent);
                                }
                            }).show();;
                }

            }else{
                mes.getConfirm(this,"Error","Las contraseñas ingresadas deben ser iguales",getLayoutInflater(),0)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;

            }
        }else{
            mes.getConfirm(this,"Error","Todos los campos son obligatorios",getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
        }

    }

    public void cancel(View view){

        message mes = new message();
        mes.getConfirm(this,"Confirmación","¿Seguro que desea cancelar el cambio de contraseña?",getLayoutInflater(),0)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mes.getConfirm(context,"Exitoso","Se canceló el cambio de contraseña",getLayoutInflater(),0).
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