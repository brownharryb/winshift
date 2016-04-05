package com.boma.winshift;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by BOMA on 10/17/2015.
 */
public class MessageClass {
    private static ViewGroup vg;

    public static void message(Context context, String string){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
    }

    public static void log(String string){
        Log.d("boms", string);
    }
    public static void message(Context context,String message,int gravity, int xPos, int yPos){
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.setGravity(gravity,xPos,yPos);
        toast.show();

    }

    public static void customMessage(Context context,View v){
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(v);
        toast.show();
    }
}
