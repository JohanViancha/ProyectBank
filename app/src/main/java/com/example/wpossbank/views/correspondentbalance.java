package com.example.wpossbank.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correspondentbalance);

        email = findViewById(R.id.txt_correspondentbalanceemail);
        password = findViewById(R.id.txt_correspondentbalancepassword);
        passwordrepeat = findViewById(R.id.txt_correspondentbalancepasswordrepat);
        balance = findViewById(R.id.tv_correspondentbalancevalue);
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
                    mes.getMessage(this,"¡Exitoso!","Las contraseña ha sido actualizada");

                }

            }else{
                mes.getMessage(this,"¡Error!","Las contraseñas ingresadas deben ser iguales");

            }
        }else{
            mes.getMessage(this,"¡Error!","Todos los campos son obligatorios");
        }

    }
}