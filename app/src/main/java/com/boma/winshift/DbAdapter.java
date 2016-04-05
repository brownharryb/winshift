package com.boma.winshift;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BOMA on 10/13/2015.
 */

public class DbAdapter {
    DbHelper helper;
    SQLiteDatabase db;
    Context context;
    String[] columns = new String[]{DbHelper.COLUMN_1,DbHelper.COLUMN_2, DbHelper.COLUMN_3,
            DbHelper.COLUMN_4,DbHelper.COLUMN_5, DbHelper.COLUMN_6,DbHelper.COLUMN_8};
    String[] columnsNetTable = new String[]{DbHelper.COLUMN_1,DbHelper.COLUMN_2, DbHelper.COLUMN_3,
            DbHelper.COLUMN_4,DbHelper.COLUMN_5, DbHelper.COLUMN_6,DbHelper.COLUMN_8,
            DbHelper.COLUMN_9,DbHelper.COLUMN_10,DbHelper.COLUMN_11};
    public String netTableName = DbHelper.TABLE_NAME_NET;
    public String createTableNetSql = DbHelper.CREATE_TABLE_NET;



    DbAdapter(Context context){
        this.context = context;
        init();


    }
    private void init(){
        helper = new DbHelper(this.context);
    }

    public void insertShiftData(ContentValues cv, boolean forOwner){
//      column names   "person_name","phone_number","shift","date","intercom","department"
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COLUMN_1, cv.getAsString("person_name"));
        contentValues.put(DbHelper.COLUMN_2, cv.getAsString("phone_number"));
        contentValues.put(DbHelper.COLUMN_3, cv.getAsString("shift"));
        contentValues.put(DbHelper.COLUMN_4, cv.getAsString("date"));
        contentValues.put(DbHelper.COLUMN_5, cv.getAsString("intercom"));
        contentValues.put(DbHelper.COLUMN_6, cv.getAsString("department"));
        contentValues.put(DbHelper.COLUMN_8, cv.getAsString("pic_location"));
        if (forOwner){contentValues.put(DbHelper.COLUMN_7, "true");}
        else {contentValues.put(DbHelper.COLUMN_7, "false");}

        db.insert(DbHelper.TABLE_NAME, null, contentValues);
        db.close();
    }

    public void updateShiftDataUsingName(ContentValues cv, String name){
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COLUMN_1, cv.getAsString("person_name"));
        contentValues.put(DbHelper.COLUMN_2, cv.getAsString("phone_number"));
        contentValues.put(DbHelper.COLUMN_3, cv.getAsString("shift"));
        contentValues.put(DbHelper.COLUMN_4, cv.getAsString("date"));
        contentValues.put(DbHelper.COLUMN_5, cv.getAsString("intercom"));
        contentValues.put(DbHelper.COLUMN_6, cv.getAsString("department"));
        db.update(DbHelper.TABLE_NAME, contentValues, DbHelper.COLUMN_1 + "=?", new String[]{name});

        db.close();

    }


    public void updateShiftDataUsingId(ContentValues cv, int id){
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COLUMN_1, cv.getAsString("person_name"));
        contentValues.put(DbHelper.COLUMN_2, cv.getAsString("phone_number"));
        contentValues.put(DbHelper.COLUMN_3, cv.getAsString("shift"));
        contentValues.put(DbHelper.COLUMN_4, cv.getAsString("date"));
        contentValues.put(DbHelper.COLUMN_5, cv.getAsString("intercom"));
        contentValues.put(DbHelper.COLUMN_6, cv.getAsString("department"));
        contentValues.put(DbHelper.COLUMN_8,cv.getAsString("pic_location"));
        db.update(DbHelper.TABLE_NAME, contentValues, DbHelper.UID+"=?",new String[]{String.valueOf(id)});
        db.close();

    }


    public Cursor readAllData(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(false, DbHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        c.moveToFirst();
        db.close();
        //c.close();
        return c;
    }
    public HashMap getOwnerRecord(){
        HashMap<String, String> map = new HashMap<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(false,DbHelper.TABLE_NAME,columns,DbHelper.COLUMN_7+"=?",new String[]{"true"},null,null,
                null, null);
        c.moveToFirst();
        map.put("name", c.getString(c.getColumnIndex(DbHelper.COLUMN_1)));
        map.put("mobile",c.getString(c.getColumnIndex(DbHelper.COLUMN_2)));
        map.put("shift",c.getString(c.getColumnIndex(DbHelper.COLUMN_3)));
        map.put("date",c.getString(c.getColumnIndex(DbHelper.COLUMN_4)));
        map.put("intercom",c.getString(c.getColumnIndex(DbHelper.COLUMN_5)));
        map.put("department",c.getString(c.getColumnIndex(DbHelper.COLUMN_6)));
        map.put("pic_location",c.getString(c.getColumnIndex(DbHelper.COLUMN_8)));

        db.close();
        c.close();
        return map;

    }






        static class DbHelper extends SQLiteOpenHelper {

            //NEVER EVER EVER CHANGE THE NAMES OF ANY TABLE OR COLUMNS

            private static final String DB_NAME = AllStrings.DB_NAME;
            private static final int DB_VERSION = 1;
            private static final String TABLE_NAME = AllStrings.MY_TABLE_NAME;
            private static final String TABLE_NAME_NET = AllStrings.MY_TABLE_NAME_NET;
            private static final String COLUMN_1 = AllStrings.COLUMN_1 ;
            private static final String COLUMN_2 = AllStrings.COLUMN_2;
            private static final String COLUMN_3 = AllStrings.COLUMN_3;
            private static final String COLUMN_4 = AllStrings.COLUMN_4;
            private static final String COLUMN_5 = AllStrings.COLUMN_5;
            private static final String COLUMN_6 = AllStrings.COLUMN_6;
            private static final String COLUMN_7 = AllStrings.COLUMN_7;
            private static final String COLUMN_8 = AllStrings.COLUMN_8;
            private static final String COLUMN_9 = AllStrings.COLUMN_9;
            private static final String COLUMN_10 = AllStrings.COLUMN_10;
            private static final String COLUMN_11 = AllStrings.COLUMN_11;
            private static final String UID = AllStrings.UID;


//*********************************ALL CREATE TABLE STATEMENTS*****************************************************
            private static final String[] ALL_TABLE_NAMES = {TABLE_NAME,TABLE_NAME_NET};
            private static final String CREATE_TABLE = AllStrings.CREATE_MY_TABLE;
            private static final String DROP_TABLE = AllStrings.DROP_MY_TABLE;
            private static final String CREATE_TABLE_NET = AllStrings.CREATE_TABLE_NET;
            private static final String DROP_TABLE_NET = AllStrings.DROP_TABLE_NET;
//********************************************************************************************************************



            public DbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE_NET);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                //Rename all tables
                String [] tempTables = new String[ALL_TABLE_NAMES.length];
                for(int i=0;i<ALL_TABLE_NAMES.length;i++){
                    tempTables[i] = "temp"+ALL_TABLE_NAMES[i];
                    db.execSQL("ALTER TABLE " + ALL_TABLE_NAMES[i] + " RENAME TO " + tempTables[i]);
                }
                //CREATE NEW TABLES
                onCreate(db);
                // MOVE OLD DATA TO NEW TABLES
                for(int i=0;i<tempTables.length;i++){
                    moveTables(db,tempTables[i],ALL_TABLE_NAMES[i]);
                }
            }

        }


            public void moveTables(SQLiteDatabase db, String oldTableName,String newTable){

                String[] oldColNames, newColNames, commonColNames;
                List<String> list = new ArrayList<>();
                ContentValues v = new ContentValues();


                Cursor cursor = db.rawQuery("SELECT * FROM " + oldTableName + " LIMIT 0", null);
                oldColNames = cursor.getColumnNames();
                cursor = db.rawQuery("SELECT * FROM " + newTable + " LIMIT 0", null);
                newColNames = cursor.getColumnNames();

                //GET ALL COMMON COLUMN NAMES
                for(String str:oldColNames){
                 for(String str2:newColNames){
                     if(str.equals(str2)){
                         list.add(str);
                     }
                 }
                }
                commonColNames = new String[list.size()];
                list.toArray(commonColNames);
                cursor.close();

                Cursor cursor1 = db.query(oldTableName,commonColNames,null,null,null,null,null);
                while(cursor1.moveToNext()){
                    for(String str:commonColNames){
                        v.put(str, cursor1.getString(cursor1.getColumnIndex(str)));
                    }
                    db.insert(newTable,null,v);
                }

                cursor1.close();
                db.execSQL("DROP TABLE IF EXISTS " + oldTableName + ";");

            }



    }
}
