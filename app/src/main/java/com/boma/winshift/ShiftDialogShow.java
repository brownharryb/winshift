package com.boma.winshift;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by BOMA on 11/7/2015.
 */
public class ShiftDialogShow extends DialogFragment implements View.OnClickListener {
    private TextView tvMain, tvHint, tvHint2;
    private Button dismissBtn;
    private String calculatedShift, calculatedDate;
    private AlertDialog.Builder builder;


    public ShiftDialogShow(){

    }

    public void setValues(String dateString,String shift){
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd k:m:s zzz yyyy", Locale.ENGLISH);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);

            Date datestr = format.parse(dateString);
            this.calculatedDate = format2.format(datestr);
        }catch(ParseException e){
            e.printStackTrace();
            MessageClass.log("Error at shiftdialog 41 --"+e.getMessage());
        }
        this.calculatedShift = shift;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.shift_dialog_show, null);
        tvHint = (TextView)v.findViewById(R.id.hint_calculated_shift_on_dialog);
        tvHint2 = (TextView)v.findViewById(R.id.hint2_calculated_shift_on_dialog);
        tvMain = (TextView)v.findViewById(R.id.calculated_shift_on_dialog);
        tvHint2.setText("Shift Calculated");
        tvHint.setText(calculatedDate);
        tvMain.setText(calculatedShift);


        builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);

        dismissBtn.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v==dismissBtn){
            dismiss();
        }
    }
}
