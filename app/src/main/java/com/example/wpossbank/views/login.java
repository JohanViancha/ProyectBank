package com.example.wpossbank.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.wpossbank.R;
import com.example.wpossbank.managedb.admincorresponsal;
import com.example.wpossbank.models.Corresponsal;
import com.google.android.material.textfield.TextInputEditText;

public class login extends AppCompatActivity {

    TextInputEditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.txt_loginemail);
        password = findViewById(R.id.txt_loginpassword);
    }



    public void login(View view) {
        //se obtiene el valor del email y el password
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){


            Corresponsal corresponsal = new Corresponsal(email, password);
            admincorresponsal admin = new admincorresponsal();

            //se hace el llamado al metodo login para verificar credenciales en BD
            Corresponsal result = admin.login(this, corresponsal);


            if(result != null){

                //Se almacenan datos del corresponsal en el SharedPreferences
                SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putFloat("saldo", Float.parseFloat(String.valueOf(result.getSaldo())));
                editor.putString("email", result.getEmail());
                editor.putString("password", result.getPassword());
                editor.commit();


                Intent inte = new Intent(this, menu.class);
                startActivity(inte);
            }
            else{
                Toast.makeText(this, "Usuario y/o contraseña", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }


    }
}