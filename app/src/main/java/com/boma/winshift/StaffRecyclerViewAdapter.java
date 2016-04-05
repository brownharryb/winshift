package com.boma.winshift;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by BOMA on 11/8/2015.
 */
public class StaffRecyclerViewAdapter extends RecyclerView.Adapter<StaffRecyclerViewAdapter.StaffViewHolder>implements View.OnClickListener{

    private LayoutInflater inflater;
    private List<StaffListItem> data = Collections.emptyList();
    private Context context;

    public StaffRecyclerViewAdapter(Context context, List<StaffListItem> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }


    @Override
    public StaffViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new StaffViewHolder(inflater.inflate(R.layout.staff_detail_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(StaffViewHolder staffViewHolder, int position) {

        final StaffListItem currentItem = data.get(position);
        staffViewHolder.textViewName.setText(currentItem.title);
        staffViewHolder.textViewShift.setText("Current Shift: " + currentItem.currentShift + " Duty");

        staffViewHolder.textViewIntercom.setText("Intercom: " + currentItem.intercom);
        staffViewHolder.textViewSection.setText("Dept: "+currentItem.department);
        staffViewHolder.textViewCompany.setText("Company: "+currentItem.company);
        staffViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle b = currentItem.getAsBundle();
//                Intent intent = new Intent(StaffRecyclerViewAdapter.this.context, StaffAllDetailActivity.class);
//                intent.putExtra("itemObj",b);
//                context.startActivity(intent);

                int todayYear = Calendar.getInstance().get(Calendar.YEAR);
                int todayMonth = Calendar.getInstance().get(Calendar.MONTH);
                int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                final String todayStr = String.valueOf(todayYear)+"-"+String.valueOf(todayMonth+1)+
                        "-"+String.valueOf(todayDay);


                DatePickerDialog dpl = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String calcDateStr = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);




                        String currShift = CalculateShifts.shiftForUnknownDateStrings(todayStr, calcDateStr, currentItem.currentShift);
                        String msg = currentItem.title + "\n\n\n" + "Date Selected: " + calcDateStr + "\n\n\n" + "Shift: " + currShift + " Duty";


                        View dialog = inflater.inflate(R.layout.shift_dialog_show, null, false);
                        TextView tvDate = (TextView) dialog.findViewById(R.id.hint_calculated_shift_on_dialog);
                        TextView tvShift = (TextView) dialog.findViewById(R.id.calculated_shift_on_dialog);
                        TextView tvName = (TextView) dialog.findViewById(R.id.hint2_calculated_shift_on_dialog);

                        tvName.setText(currentItem.title);
                        tvName.setVisibility(View.VISIBLE);
                        tvDate.setText(calcDateStr);
                        tvShift.setText("Not Known");
                        if (!currentItem.currentShift.equals("Not Known")) {
                            tvShift.setText(currShift + " Duty");
                        }

                        MessageClass.customMessage(context, dialog);
                    }
                }, todayYear, todayMonth, todayDay);
                dpl.show();
            }
        });
        if(currentItem.imgUriStr.equals("")){
            staffViewHolder.imageView.setImageResource(R.mipmap.bh);
        }else {
                //MyImageLoader.setImageOnViewFromNet(context,currentItem.imgUriStr,200,staffViewHolder.imageView);
                //staffViewHolder.imageView.setImageBitmap(b);

            Picasso.with(context).load(currentItem.imgUriStr).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.mipmap.bh).error(R.mipmap.bh).into(staffViewHolder.imageView);
        }


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

            @Override
        public void onClick(View v) {
            Intent intent = new Intent(StaffRecyclerViewAdapter.this.context, StaffAllDetailActivity.class);
            //intent.putExtra("image")
            context.startActivity(intent);
        }

    class StaffViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageView;
        private TextView textViewShift, textViewName,textViewIntercom,textViewSection,textViewCompany;


        public StaffViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.staff_item_image_view);
            textViewName = (TextView) itemView.findViewById(R.id.staff_name_list_item);
            textViewShift = (TextView) itemView.findViewById(R.id.staff_current_shift_list_item);
            textViewIntercom = (TextView)itemView.findViewById(R.id.staff_intercom_list_item);
            textViewSection = (TextView)itemView.findViewById(R.id.staff_section_list_item);
            textViewCompany = (TextView)itemView.findViewById(R.id.staff_company_list_item);


        }


    }

}
