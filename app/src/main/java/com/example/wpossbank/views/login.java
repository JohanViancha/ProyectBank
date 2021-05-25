package com.example.wpossbank.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.wpossbank.R;
import com.example.wpossbank.contetProvider.ProviderBank;
import com.example.wpossbank.managedb.admincorrespondent;
import com.example.wpossbank.models.Correspondent;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class login extends AppCompatActivity {

    TextInputEditText email, password;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.txt_loginemail);
        password = findViewById(R.id.txt_loginpassword);

        String fields[] = {"name_cli","identification_cli"};
        Cursor cursor = getContentResolver().query(ProviderBank.CONTENT_URI,fields,null,null,null);

        System.out.println(cursor.getCount());
        while(cursor.moveToNext()){
            System.out.println(cursor.getString(0) + " " + cursor.getString(1));
        }

    }



    public void login(View view) {
        //se obtiene el valor del email y el password
        message mes = new message();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){


            Correspondent correspondent = new Correspondent(email, password);
            admincorrespondent admin = new admincorrespondent();

            //se hace el llamado al metodo login para verificar credenciales en BD
            Correspondent result = admin.login(this, correspondent);


            if(result != null){

                //Se almacenan datos del corresponsal en el SharedPreferences
                SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putFloat("balance", Float.parseFloat(String.valueOf(result.getBalance())));
                editor.putString("email", result.getEmail());
                editor.putString("name", "Corresponsal Bancario WPOSS");
                editor.putString("password", result.getPassword());
                editor.putInt("id_correspondent", result.getId());
                editor.commit();

                Bundle extras = new Bundle();

                extras.putString("email",result.getEmail());
                extras.putString("name", "Corresponsal Bancario WPOSS");

                Intent intent = new Intent(this, menu.class);
                intent.putExtras(extras);
                startActivity(intent);

            }
            else{
                mes.getConfirm(this,"Error", "Usuario y/o contraseña incorrectos",getLayoutInflater(),0)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
            }
        }
        else{

            mes.getConfirm(this,"Error", "Todos los campos son obligatorios",getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}}).show();;
        }


    }
}