package com.boma.winshift;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by BOMA on 10/17/2015.
 */
public class ConfirmDialog extends DialogFragment implements View.OnClickListener {

    private String firstName, lastName,fullname, phoneNumber, currentShift, intercom, dept;
    private Button saveBtn, cancelBtn;
    private TextView dataToAdd;
    private AlertDialog.Builder builder;
    private onButtonsClickedListener btnListener; //TODO DELETE THIS IF NOT USED

    public ConfirmDialog() {

    }

    public void getNeededStrings(String firstName, String lastName, String telNumber,
                                 String currentShift, String intercom,String dept){
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullname = firstName+" "+lastName;
        this.phoneNumber = telNumber;
        this.currentShift = currentShift;
        this.intercom=intercom;
        this.dept=dept;
    }

    public interface onButtonsClickedListener{
        public void onSaveButtonClick(DialogFragment dialogFragments);
        //TODO DELETE THIS INTERFACE IF NOT USED

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            btnListener = (onButtonsClickedListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" must implement " +
                    "the necessary interface");
        }
        //TODO DELETE THIS METHOD IF NOT USEDS

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.confirm_dialog, null);

        saveBtn = (Button) v.findViewById(R.id.confirm_save);
        cancelBtn = (Button) v.findViewById(R.id.confirm_cancel);
        dataToAdd = (TextView)v.findViewById(R.id.fullname_to_add);
        
        String feedback = "Hi "+firstName+", I am about to save your name as '"+fullname+"', your Department, '"+dept
                            +"', Intercom Number, '"+intercom+"', mobile, '"+phoneNumber+"' and your current shift is "+currentShift
                            +"..... Can I?";


        dataToAdd.setText(feedback);






        builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v == saveBtn){saveBtnClicked();}
        if(v==cancelBtn){cancelBtnClicked();}
    }

    private void saveBtnClicked(){

        //MessageClass.message(getActivity(),"save clicked");
        btnListener.onSaveButtonClick(this);

    }
    private void cancelBtnClicked(){
        MessageClass.message(getActivity(),"cancel clicked");
        this.dismiss();

    }


}
