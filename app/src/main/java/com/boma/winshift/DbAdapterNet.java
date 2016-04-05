package com.boma.winshift;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by BOMA on 10/26/2015.
 */
public class DbAdapterNet extends DbAdapter {

    Context context;
    HttpURLConnection conn = null;
    String mainUrl;
    SQLiteDatabase db;
    String[] allColumnsNetTable = columnsNetTable;

    DbAdapterNet(Context context) {
        super(context);
        this.context=context;
    }

    DbAdapterNet(Context context,String homeUrl) {
        super(context);
        this.context=context;
        this.mainUrl = homeUrl;
    }

    public void updateFromNet(String link) throws IOException {
        //rename and recreate table
        db = helper.getWritableDatabase();
        String newTempTableName = "tempNetTable";
        db.execSQL("ALTER TABLE " + netTableName + " RENAME TO " + newTempTableName);
        db.execSQL(createTableNetSql);

        HttpURLConnection conn = null;
        InputStream is = null;

        try {
        URL url = new URL(link);
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();


            if(conn.getResponseCode()==200){
                ContentValues testc = new ContentValues();
                testc.put("_id","1");
                testc.put("person_name","Boma");
                testc.put("shift","morning");

                is = conn.getInputStream();
                DbObjects dbObjects = new DbObjects(is);
                List<ContentValues> l = dbObjects.readData();
                for(int i=0;i<l.size();i++){
                    ContentValues c = l.get(i);
                    //MessageClass.log("from dbadapternet-- "+c.toString());
                    db.insert(netTableName, null, c);
                }
                db.execSQL("DROP TABLE IF EXISTS "+newTempTableName+";");
            }
        }catch (Exception e){
            e.printStackTrace();
            db.execSQL("DROP TABLE IF EXISTS " + netTableName + ";");
            db.execSQL("ALTER TABLE " + newTempTableName + " RENAME TO " + netTableName);

        }

        finally{
            if(conn!=null) {conn.disconnect();}
            if (is != null) {is.close();}
            db.close();
        }
        }

    }

