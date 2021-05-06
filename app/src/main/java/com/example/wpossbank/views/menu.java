package com.example.wpossbank.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.wpossbank.R;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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


    public void exit(View view){
        Intent inte = new Intent(this, login.class);
        startActivity(inte);
    }
}