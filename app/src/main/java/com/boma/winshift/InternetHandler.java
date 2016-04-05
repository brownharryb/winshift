package com.boma.winshift;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by BOMA on 11/13/2015.
 */
public class InternetHandler {

    public static boolean internetIsAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    public static String getResponseString(Context c, String urlString, ContentValues values, String randKeyForOwner) {

        HttpURLConnection conn = null;
        InputStream is = null;
        String responseString = "";
        String link = buildParamUrl(values, urlString, randKeyForOwner);

        try {

            if (internetIsAvailable(c)) {
                URL url = new URL(link);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(1000);
                conn.connect();
                if (conn.getResponseCode() == 200) {
                    is = conn.getInputStream();
                    responseString = getStringFromInputStream(is);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseString;

    }

    public static String getStringFromInputStream(InputStream is) throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (br != null) {
                br.close();
                is.close();
            }
        }

        return sb.toString();

    }

    private static String buildParamUrl(ContentValues cv, String mainUrl, String randKey) {
        String params = "?";
        for (String st : cv.keySet()) {
            String val = cv.getAsString(st);
            if (val == null) {
                val = "";
            }
            if (st.equals("date")) {
                val = formatDate(val);
            }
            params = params + st + "=" + checkString(val) + "&";
        }

        return mainUrl + params + "random_key=" + randKey;

    }


    private static String checkString(String s) {

        if (s.contains(" ")) {
            s = s.replaceAll(" ", "_");
        }
        return s;
    }

    private static String formatDate(String dateStr) {
        try {
            Date d = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(dateStr);
            dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateStr;
    }

    public static String generateRandomKey(int len) {

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static boolean updateDbFromNet(Context context) {
        String url = AllStrings.WEBSITE_URL;
        boolean updated = false;

        if (internetIsAvailable(context)) {
            try {
                String s = url + "?getwhat=all";
                    if (siteIsActive(s)) {
                        DbAdapterNet dbAdapterNet = new DbAdapterNet(context, s);
                        dbAdapterNet.updateFromNet(s);
                        updated = true;
                    }

            } catch (IOException e) {
                updated = false;
                e.printStackTrace();
            }
        }

            return updated;
        }







    public static boolean siteIsActive(String urlString){
        boolean isActive = false;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.connect();
            if(conn.getResponseCode()==200){
                isActive = true;                }
        }catch (Exception e){
            e.printStackTrace();
            isActive = false;
            //showMsg(2);
        }
        finally {
            if(conn!=null){
                conn.disconnect();
            }

        }

        return isActive;

    }


    public static void deleteInternetInfo(Context context, String ownerKey) throws IOException {

        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            if (internetIsAvailable(context)) {
                String urlStr = AllStrings.DELETE_MY_INFO_LINK + ownerKey;

                URL url = new URL(urlStr);
                MessageClass.log("from internet handler url = "+url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.connect();
                if (conn.getResponseCode() == 200) {
                    is = conn.getInputStream();
                    String responseString = getStringFromInputStream(is);

                }
            }
        }finally {
            if(conn!=null){
                conn.disconnect();
            }
        }
    }




}
