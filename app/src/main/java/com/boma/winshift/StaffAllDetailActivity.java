package com.boma.winshift;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class StaffAllDetailActivity extends AppCompatActivity {
    String imgURi, shiftStr, shiftDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_all_detail);
        Bundle bundle = getIntent().getBundleExtra("itemObj");
        imgURi = bundle.getString("imgUriStr");
        shiftStr = bundle.getString("shiftString");
        shiftDate = bundle.getString("shiftDate");
        ImageView imageView = (ImageView) findViewById(R.id.image_all_detail);
        TextView staffName = (TextView) findViewById(R.id.staff_name_all_detail);
        TextView intercom = (TextView) findViewById(R.id.intercom_all_detail);
        TextView company = (TextView) findViewById(R.id.company_all_detail);
        TextView section = (TextView) findViewById(R.id.section_all_detail);
        Button calcShiftButton = (Button) findViewById(R.id.calculated_shift_all_detail);
        calcShiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpl = new DatePickerDialog(StaffAllDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String calcDateStr = String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth);
                        String currShift = CalculateShifts.shiftForUnknownDateStrings(shiftDate,calcDateStr,shiftStr);
                        MessageClass.message(StaffAllDetailActivity.this, currShift.toUpperCase()+" DUTY", Gravity.CENTER,0,0);
                    }
                }, Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);
                dpl.show();
            }

        });

        if(imgURi!=null && !imgURi.equals("")){
            Picasso.with(this).load(bundle.getString("imgUriStr")).error(R.mipmap.bh).into(imageView);
        }
        staffName.setText(bundle.getString("title"));
        intercom.setText(bundle.getString("intercom"));
        company.setText(bundle.getString("company"));
        section.setText(bundle.getString("section"));

    }

}
