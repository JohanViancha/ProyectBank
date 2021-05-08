package com.example.wpossbank.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wpossbank.R;

public class menu extends AppCompatActivity {

    TextView email,name;
    Context context =this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        email = findViewById(R.id.tv_menuemail);
        name = findViewById(R.id.tv_menuname);

        SharedPreferences sharedpreferences = getSharedPreferences("sesion_corresponsal", Context.MODE_PRIVATE);
        email.setText(sharedpreferences.getString("email", ""));
        name.setText(sharedpreferences.getString("name", ""));

    }

    public void cardpayment(View view){
        Intent inte = new Intent(this, cardpayment.class);
        startActivity(inte);
    }

    public void withtdrawal(View view){
        Intent inte = new Intent(this, withdrawal.class);
        startActivity(inte);
    }

    public void deposit(View view){
        Intent inte = new Intent(this, deposit.class);
        startActivity(inte);
    }

    public void transfer(View view){
        Intent inte = new Intent(this, transfer.class);
        startActivity(inte);
    }

    public void checkbalance(View view){
        Intent inte = new Intent(this,checkbalance.class);
        startActivity(inte);
    }

    public void createaccount(View view){
        Intent inte = new Intent(this, createaccount.class);
        startActivity(inte);
    }

    public void transactionhistory(View view){
        Intent inte = new Intent(this, transactionhistory.class);
        startActivity(inte);
    }

    public void correspondentbalance(View view){
        Intent inte = new Intent(this, correspondentbalance.class);
        startActivity(inte);
    }


    public void exit(View view){
        message mes = new message();
        mes.getConfirm(this,"Confirmación","¿Esta seguro que desea salir?",getLayoutInflater(),0)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent inte = new Intent(context, login.class);
                        startActivity(inte);

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