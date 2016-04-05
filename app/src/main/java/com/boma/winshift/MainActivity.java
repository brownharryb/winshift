package com.boma.winshift;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private CircleImageView cv;
    private TextView imageChangeHint,welcomeGuest;
    private Bitmap frontImageBitmap;
    private EditText fullName,phoneNo, dept,intercom;
    Spinner shiftsSpinner;
    private String myCurrentShift, imageUriLocationString;
    private String[] allInputs;
    private ContentValues myContentValues;
    private ConfirmDialog confirmDialog;
    private int confirmCount = 0;
    private boolean shouldUpdate = false;
    private Button fronBtn,deleteInfoBtn;
    private boolean imageChanged = false;


    int SELECT_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get all views in activity_main
        cv = (CircleImageView) findViewById(R.id.front_image);
        fullName = (EditText) findViewById(R.id.first_name_edit);
        phoneNo = (EditText) findViewById(R.id.phone_number_edit);
        dept = (EditText)findViewById(R.id.dept_edit);
        intercom = (EditText)findViewById(R.id.intercom_number_edit);
        shiftsSpinner = (Spinner) findViewById(R.id.shifts_spinner);
        fronBtn = (Button)findViewById(R.id.front_btn);
        deleteInfoBtn = (Button) findViewById(R.id.delete_account_btn);




        Bundle b = this.getIntent().getExtras();
        if (b!=null){
            HashMap map = (HashMap)b.getSerializable("map");
            fullName.setText((String) map.get("name"));
            phoneNo.setText((String) map.get("mobile"));
            intercom.setText((String) map.get("intercom"));
            dept.setText((String) map.get("department"));
            fronBtn.setText("Update");
            if(map.get("pic_location")!=null){
                imageUriLocationString = (String) map.get("pic_location");
                MessageClass.log("from MainActivity, imageUriLocationString is -- "+imageUriLocationString);
                Picasso.with(this).load(imageUriLocationString).error(R.mipmap.bh).into(cv);
            }
            shouldUpdate = true;
        }
        if(!shouldUpdate){
            deleteInfoBtn.setVisibility(View.INVISIBLE);
        }


        ArrayAdapter sAdapter = ArrayAdapter.createFromResource(this, R.array.shift_names,R.layout.single_spinner_item);
        shiftsSpinner.setAdapter(sAdapter);


        cv.setBorderWidth(4);
        cv.setBorderColor(Color.WHITE);
        imageChangeHint = (TextView) findViewById(R.id.image_change_hint);
        welcomeGuest = (TextView)findViewById(R.id.main_welcome_txt);

        AllAnimations.animateFrontImage(imageChangeHint,welcomeGuest,cv);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgIntent = new Intent(Intent.ACTION_PICK);
                imgIntent.setType("image/*");
                startActivityForResult(imgIntent,SELECT_IMAGE);

            }
        });

}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==SELECT_IMAGE){
            if(resultCode==RESULT_OK){
                imageUriLocationString = data.getDataString();
                try {
                    changeImage(imageUriLocationString);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void changeImage(String dataString)throws IOException{
        if(dataString!=null) {
            Bitmap b = new MyImageHandler(this,dataString).getThumbnail(dataString,200);
//            Picasso.with(this).load(Uri.parse(dataString)).placeholder(R.mipmap.bh)
//                    .error(R.mipmap.bh).into(cv);
            cv.setImageBitmap(b);
            imageChanged = true;
        }else{
            cv.setImageResource(R.mipmap.bh);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pic_uri_location", imageUriLocationString);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageUriLocationString = savedInstanceState.getString("pic_uri_location");
        try {
            changeImage(imageUriLocationString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void getAllInputs(View v){
        boolean allOk = false;

        if (confirmCount<1) {
            TextView shiftTextView = (TextView) shiftsSpinner.getChildAt(0);
            Date date_obj_today = new Date(new GregorianCalendar().getTimeInMillis());
            String today_date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date_obj_today);

            myContentValues = new ContentValues();

            String full_name_str = fullName.getText().toString().trim();
            String phone_number = phoneNo.getText().toString().trim();
            String shift = shiftTextView.getText().toString();

            String intercom_num = intercom.getText().toString().trim();
            String dept_name = dept.getText().toString().trim();
            myCurrentShift = shift;


            shift = myCurrentShift;

            //CHECK FOR EMPTY STRINGS
            for (String i : new String[]{full_name_str, phone_number, shift, today_date, intercom_num, dept_name}) {
                if (i.equals("")) {
                    MessageClass.message(this, "Some Values Missing.");
                    return;
                }
            }

            full_name_str = AllStrings.getProperCaseString(full_name_str);
            dept_name = AllStrings.getProperCaseString(dept_name);

            myContentValues.put("person_name", full_name_str);
            myContentValues.put("phone_number", phone_number);
            myContentValues.put("shift", shift);
            myContentValues.put("date", today_date);
            myContentValues.put("intercom", intercom_num);
            myContentValues.put("department", dept_name);
            myContentValues.put("pic_location",imageUriLocationString);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  confirmCount = 0;
                }
            },2000);

            MessageClass.message(this, "Click again to save.");
            confirmCount+=1;


        }
        else {
            try {
                saveInfo();
            }catch(Exception e){
                MessageClass.log("Exception from MainActivity.java for saveinfo() --"+e.getMessage());
                MessageClass.message(this,"Something went wrong, Your info wasn't saved");
            }
        }

    }



    public void saveInfo() throws Exception{
        SharedPreferences.Editor editor = getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
        String randKey = InternetHandler.generateRandomKey(AllStrings.RANDOM_KEY_LENGTH);
        if(shouldUpdate){
            String storedKey = getSharedPreferences("settings",Context.MODE_PRIVATE).getString("randomKey",randKey);
            if(randKey.equals(storedKey)){
                editor.putString("randomKey",randKey);
            }
            myContentValues.put("pic_location",AllStrings.PIC_LOCATION_PATH+storedKey+".JPG");
            new DbAdapter(this).updateShiftDataUsingId(myContentValues, 1);
            MessageClass.message(this, "Updated!");
        }
        else {
            editor.putString("randomKey",randKey);
            myContentValues.put("pic_location", AllStrings.PIC_LOCATION_PATH + randKey+".JPG");
            new DbAdapter(this).insertShiftData(myContentValues, true);
            MessageClass.message(this, "Saved!");
        }

        editor.putString("myInfo", "available");
        editor.apply();
        randKey = getSharedPreferences("settings",Context.MODE_PRIVATE).getString("randomKey",randKey);

        new UploadMyInfo(myContentValues,randKey).execute();
        if(imageChanged) {
            MyImageHandler imageHandler = new MyImageHandler(this, imageUriLocationString, randKey);
            imageHandler.uploadToServer();
        }
        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);

        finish();
    }

    public void deleteInfo(View v){

        String ownerKey = getSharedPreferences("settings",Context.MODE_PRIVATE).getString("randomKey","");
        new UploadMyInfo(ownerKey).execute();
        MessageClass.message(this,"Info Successfully deleted from database!!");
    }


    @Override
    public void onBackPressed() {
        if (shouldUpdate){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            super.onBackPressed();
        }
    }
    class UploadMyInfo extends AsyncTask<Void,Void,String>{
        private ContentValues contentValues = new ContentValues();
        String randKey;
        String flag;

        public UploadMyInfo(ContentValues cValues, String randKeyForOwner){
            this.contentValues = cValues;
            randKey = randKeyForOwner;
            flag = "upload";
        }
        public UploadMyInfo(String randKey){
            this.randKey = randKey;
            flag = "delete";
        }


        @Override
        protected String doInBackground(Void... params) {
            if(flag.equals("upload")) {
                String st = AllStrings.INFO_UPLOAD_PATH;
                contentValues.put("pic_location", AllStrings.PIC_LOCATION_PATH + randKey + ".JPG");
                return InternetHandler.getResponseString(MainActivity.this, st, contentValues, randKey);
            }
            else if(flag.equals("delete")){

                if(!randKey.equals("")){
                    try {
                        InternetHandler.deleteInternetInfo(MainActivity.this, randKey);

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }

            }
            return "";

        }


    }
}
