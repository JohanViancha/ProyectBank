package com.example.wpossbank.views;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.wpossbank.R;

import java.util.zip.Inflater;

public class message extends DialogFragment {



    //Función para mostrar mensaje en forma de AlertDialog
    public AlertDialog.Builder getConfirm(Context context, String title, String message, LayoutInflater inflater, double commission){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = inflater.inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView dialogtitle = view.findViewById(R.id.dialogtitle);
        dialogtitle.setText(title);

        TextView dialogmessage = view.findViewById(R.id.dialogmessage);
        dialogmessage.setText(message);
        ImageView dialogicon = view.findViewById(R.id.dialogicon);
        TextView diaglocommison = view.findViewById(R.id.tv_dialogcommission);
        if(title.equals("Exitoso")){
            dialogicon.setImageResource(R.drawable.ic_baseline_check_circle_24);
        }else if(title.equals("Error")){
            dialogicon.setImageResource(R.drawable.ic_baseline_cancel_24);
        }else{
            if(commission!=0){
                diaglocommison.setText("Comisión por transaccción: $" + String.valueOf(commission));
            }

            dialogicon.setImageResource(R.drawable.ic_baseline_info_24);

        }

        builder.create();

        return  builder;

    }




}
