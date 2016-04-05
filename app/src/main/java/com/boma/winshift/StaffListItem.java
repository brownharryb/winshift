package com.boma.winshift;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BOMA on 11/8/2015.
 */
public class StaffListItem {
    public List<Bitmap> allBitmaps = new ArrayList<>();
    public String title, currentShift,imgUriStr,phoneNumber,shiftString,shiftDate,intercom,
            department,company;
    public List<String> allImgUrls = new ArrayList<>();
    public List<String> allPersonNames = new ArrayList<>();
    public Context context;
    private SQLiteDatabase db;
    private DbAdapterNet adapterNet;


    public StaffListItem(Context context){
        this.context = context;
    }

    public List<StaffListItem> getDataFromDb(){
        List<StaffListItem> list = new ArrayList<>();
        ContentValues cv = new ContentValues();
        DbAdapterNet.DbHelper helper = new DbAdapterNet.DbHelper(context);
        db = helper.getReadableDatabase();
        adapterNet = new DbAdapterNet(context);
        Cursor c = db.query(adapterNet.netTableName,adapterNet.columnsNetTable,null,
                        null,null,null,null);
            while(c.moveToNext()){
                StaffListItem staffListItem =  new StaffListItem(context);
                for (int i = 0; i < adapterNet.columnsNetTable.length; i++) {
                    cv.put(adapterNet.columnsNetTable[i], c.getString(c.getColumnIndex(adapterNet.allColumnsNetTable[i])));
                }
                staffListItem.shiftString = cv.getAsString("shift");
                staffListItem.shiftDate = cv.getAsString("date");
                staffListItem.company = cv.getAsString("company");
                staffListItem.intercom = cv.getAsString("intercom");
                staffListItem.department = cv.getAsString("department");
                staffListItem.phoneNumber = cv.getAsString("phone_number");
                staffListItem.imgUriStr = cv.getAsString("pic_location");

                //TODO CALCULATE CURRENT SHIFT
                staffListItem.currentShift = CalculateShifts.getShiftFromDateStringsForToday(
                                    staffListItem.shiftDate,cv.getAsString("shift"));
                staffListItem.title = cv.getAsString("person_name");
                allPersonNames.add(staffListItem.title);
                allImgUrls.add(staffListItem.imgUriStr);
                list.add(staffListItem);
            }
        db.close();
        c.close();
        new LoadImageBitmap().execute();

        return list;
    }

    class LoadImageBitmap extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }

        InputStream is = null;
        Bitmap bitmap;
        @Override
        protected Void doInBackground(Void... params) {




            try {
                for (String s : allImgUrls) {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    allBitmaps.add(bitmap);

                }
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
            saveToExt(allBitmaps,allPersonNames);
            return null;
        }

        public void saveToExt(List<Bitmap> bitmaps,List<String> personNames){
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                String fullPath = AllStrings.FULL_PATH_TO_IMAGES;
                FileOutputStream fOut = null;

                File folder = new File(fullPath);

                if(!folder.exists()){folder.mkdirs();}

                try {
                    for(int i=0;i<bitmaps.size() && i<personNames.size();i++){

                        File f = new File(fullPath, personNames.get(i)+".png");
                        f.createNewFile();
                        fOut = new FileOutputStream(f);
                        bitmaps.get(i).compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    }

                }catch (IOException e){
                    e.printStackTrace();
                    MessageClass.log("file creation --"+e.toString());
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                finally {
                    try {
                        if(fOut!=null){
                            fOut.close();                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }



        }
    }
}
