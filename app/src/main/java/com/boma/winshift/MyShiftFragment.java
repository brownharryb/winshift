package com.boma.winshift;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by BOMA on 10/19/2015.
 */
public class MyShiftFragment extends Fragment{
    private Button showDateBtn, editInfoBtn;
    private TextView currentShiftLabel;
    private DbAdapter dbAdapter;
    private HashMap map;
    private String storedShift,storedDate;
    private SimpleDateFormat format;
    private LayoutInflater inflater2;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater2 = inflater;
        View v = inflater.inflate(R.layout.my_shift_fragment,container,false);
        showDateBtn = (Button)v.findViewById(R.id.my_shift_show_date_btn);
        currentShiftLabel = (TextView)v.findViewById(R.id.current_shift_label);
        editInfoBtn = (Button)v.findViewById(R.id.edit_my_info_btn);

        format = new SimpleDateFormat("EEE MMM dd k:m:s zzz yyyy", Locale.ENGLISH);
        dbAdapter = new DbAdapter(getActivity());
        map = dbAdapter.getOwnerRecord();
        storedShift = (String)map.get("shift");
        storedDate =(String) map.get("date");


        Calendar today_calendar = Calendar.getInstance();


        final int mnth = today_calendar.get(Calendar.MONTH);
        final int dy = today_calendar.get(Calendar.DAY_OF_MONTH);
        final int yr = today_calendar.get(Calendar.YEAR);


        showDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpl = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fetchShift(year, monthOfYear, dayOfMonth);
                    }
                },yr,mnth,dy);
                dpl.show();
            }
        });

        editInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("map",map);
                intent.putExtras(b);
                startActivity(intent);
                getActivity().finish();
            }
        });
        String todayShift = CalculateShifts.getShiftFromDateStringsForToday(storedDate,storedShift);
        currentShiftLabel.setText(todayShift+" Duty.");
        return v;

    }



    private void fetchShift(int year, int monthOfYear, int dayOfMonth) {
        Date unknownDate = new Date(new GregorianCalendar(year,monthOfYear,dayOfMonth).getTimeInMillis());
        String unknownDateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(unknownDate);

        storedShift = (String)map.get("shift");
        String s_date =  (String)map.get("date");

        String newShift = CalculateShifts.shiftForUnknownDateStrings(s_date,unknownDateStr,storedShift);


        View dialog = inflater2.inflate(R.layout.shift_dialog_show,null,false);
        TextView tvDate = (TextView) dialog.findViewById(R.id.hint_calculated_shift_on_dialog);
        TextView tvShift = (TextView) dialog.findViewById(R.id.calculated_shift_on_dialog);

        tvDate.setText(unknownDateStr);
        if (!newShift.equals("Not Known")) {
            tvShift.setText(newShift + " Duty");
        }

        MessageClass.customMessage(getActivity(),dialog);



    }

}
