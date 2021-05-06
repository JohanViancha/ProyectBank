package com.example.wpossbank.recycleradapter;

import android.content.Context;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wpossbank.R;
import com.example.wpossbank.models.Transaction;

import java.util.List;
import java.util.zip.Inflater;

public class RecyclerAdapterTransaction extends RecyclerView.Adapter<RecyclerAdapterTransaction.ViewHolder> {

    List<Transaction> list_transaction;
    LayoutInflater inflater;
    Context context;

    public RecyclerAdapterTransaction(List<Transaction> list_transaction, Context context) {
        this.list_transaction = list_transaction;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerAdapterTransaction.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_transaction,null);

        return new RecyclerAdapterTransaction.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterTransaction.ViewHolder holder, int position) {

        holder.showTransactions(this.list_transaction.get(position));
    }

    @Override
    public int getItemCount() {
        return list_transaction.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date,type,identificaction;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.tv_transactionhistorydate);
            type = itemView.findViewById(R.id.tv_transactionhistorytype);
            identificaction = itemView.findViewById(R.id.tv_transactionhistoryidentification);
        }

        public void showTransactions(final Transaction transaction){

            date.setText(String.valueOf(transaction.getDate()));
            type.setText(transaction.getType());
            identificaction.setText(transaction.getIdentification());

        }
    }
}
