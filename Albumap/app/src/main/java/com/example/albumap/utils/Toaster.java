package com.example.albumap.utils;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
    private Context context;

    public Toaster (Context context){
        this.context = context;
    };
    public void Short(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void Long(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
