package com.example.wpossbank.contetProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wpossbank.managedb.admindb;

import java.util.HashMap;

public class ProviderBank extends ContentProvider {


    public static final String PROVIDER_NAME = "com.example.wpossbank.contetProvider.ProviderBank";
    public static final String URL = "content://" + PROVIDER_NAME + "/clients";
    public static final Uri CONTENT_URI = Uri.parse(URL);


    static final int ALLROWS = 1;
    static final int SINGLE_ROW = 2;
    static UriMatcher uriMatcher;


    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + PROVIDER_NAME +"/clients";
    public final static String MULTIPLE_MIME ="vnd.android.cursor.dir/vnd." + PROVIDER_NAME + "/clients";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"clients", ALLROWS);
        uriMatcher.addURI(PROVIDER_NAME,"clients/#", SINGLE_ROW);
    }



    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        admindb admin = new admindb(getContext(),"wpossbank",null, 1);
        SQLiteDatabase sql = admin.getReadableDatabase();
        Cursor rows = sql.rawQuery("select * from clients",null);

        rows.setNotificationUri(
                getContext().getContentResolver(),
                ProviderBank.CONTENT_URI);

        return rows;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (ProviderBank.uriMatcher.match(uri)) {
            case ProviderBank.ALLROWS:
                return ProviderBank.MULTIPLE_MIME;
            case ProviderBank.SINGLE_ROW:
                return ProviderBank.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Tipo de actividad desconocida: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        if(values != null){

            admindb admin = new admindb(getContext(),"wpossbank",null, 1);
            SQLiteDatabase sql = admin.getWritableDatabase();

            long rows = sql.insert("clients", null, values);

            if(rows >0){
                Uri new_uri = ContentUris.withAppendedId(
                                ProviderBank.CONTENT_URI, rows);

                getContext().getContentResolver().
                        notifyChange(new_uri, null);
                return new_uri;
            }
        }
        throw new SQLException("Falla al insertar fila en : " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
