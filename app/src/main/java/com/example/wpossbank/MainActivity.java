package com.example.wpossbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wpossbank.views.cardpayment;
import com.example.wpossbank.views.checkbalance;
import com.example.wpossbank.views.correspondentbalance;
import com.example.wpossbank.views.createaccount;
import com.example.wpossbank.views.deposit;
import com.example.wpossbank.views.login;
import com.example.wpossbank.views.transactionhistory;
import com.example.wpossbank.views.transfer;
import com.example.wpossbank.views.withdrawal;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Se carga la barra de progress
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent inte = new Intent(MainActivity.this, login.class);
                startActivity(inte);
                finish();
            }
        };

        Timer tiempo = new Timer();
        tiempo.schedule(tarea, 5000);

    }




}