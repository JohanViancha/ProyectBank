package com.example.wpossbank.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.wpossbank.R;
import com.example.wpossbank.managedb.admintransaction;
import com.example.wpossbank.models.Transaction;
import com.example.wpossbank.recycleradapter.RecyclerAdapterTransaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class transactionhistory extends AppCompatActivity {

    RecyclerView rvlistransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactionhistory);


        admintransaction admintra = new admintransaction();

        Cursor rows = admintra.list_transaction(this);
        List<Transaction> list_transactions = new ArrayList<>();

        if(rows != null) {
            while (rows.moveToNext()) {
                Transaction transaction = new Transaction(rows.getString(1), rows.getDouble(2), rows.getString(3), rows.getString(4));
                list_transactions.add(transaction);
            }

            RecyclerAdapterTransaction  adapter = new RecyclerAdapterTransaction(list_transactions,this);

            RecyclerView recycler = findViewById(R.id.rv_transactionhistory);
            recycler.setHasFixedSize(true);
            recycler.setLayoutManager(new LinearLayoutManager(this));
            recycler.setAdapter(adapter);

        }else{
            message mes = new message();
            mes.getMessage(this,"¡Información!","No hay transacciones");
        }


    }
}