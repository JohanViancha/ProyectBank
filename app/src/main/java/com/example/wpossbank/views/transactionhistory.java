package com.example.wpossbank.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.wpossbank.MainActivity;
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
    TextView toolbar;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactionhistory);

        toolbar = findViewById(R.id.tb_title);
        toolbar.setText("Historial de transacciones");
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
            Intent intent = new Intent(this, menu.class);
            mes.getConfirm(this,"Información","No hay transacciones",getLayoutInflater(),0)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, menu.class);
                            startActivity(intent);
                        }
                    }).show();;
        }


    }


    public void cancel(View view){

        Intent inte = new Intent(this, menu.class);
        startActivity(inte);
    }





}