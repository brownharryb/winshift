package com.boma.winshift;

import android.os.Environment;

/**
 * Created by BOMA on 11/8/2015.
 */
public class AllStrings {
    static final String APP_NAME = "WinShift";
    //static final String WEBSITE_URL = "http://10.0.3.2/winshift/";
    //static final String WEBSITE_URL = "http://winshift.eu5.org/";
    static final String WEBSITE_URL = "http://winshift.comxa.com/";
    static final int RANDOM_KEY_LENGTH = 24;
    static final String INFO_UPLOAD_PATH = WEBSITE_URL+"collectinfo.php";
    static  final String ALLOWED_LETTERS = "abcdefghijklmnopqrstuvwxyz- ";
    static final String PIC_LOCATION_PAGE_LINK = WEBSITE_URL+"save_pic.php";
    static final String PIC_LOCATION_PATH = WEBSITE_URL+"pictures/";
    static final String DEFAULT_DATE_FORMAT_STRING = "";
    static final String DELETE_MY_INFO_LINK = WEBSITE_URL+"delete_row.php?owner_key=";
     //static final String UPDATE_DB_LINK = WEBSITE_URL+"?getwhat=all";






//***********************************************************************DbAdapter.java**********************************************
    static final String DB_NAME = "winshift_db";
    static final String MY_TABLE_NAME = "winshift_staff_data";
    static final String MY_TABLE_NAME_NET = "winshift_staff_data_net";
    static final String COLUMN_1 = "person_name";
    static final String COLUMN_2 = "phone_number";
    static final String COLUMN_3 = "shift";
    static final String COLUMN_4 = "date";
    static final String COLUMN_5 = "intercom";
    static final String COLUMN_6 = "department";
    static final String COLUMN_7 = "owner_data";
    static final String COLUMN_8 = "pic_location";
    static final String COLUMN_9 = "company";
    static final String COLUMN_10 = "date_added";
    static final String COLUMN_11 = "date_modified";
    static final String UID = "_id";
    static final String CREATE_MY_TABLE = "CREATE TABLE " + MY_TABLE_NAME + " (" + UID + " INTEGER" +
            " PRIMARY KEY AUTOINCREMENT, " + COLUMN_1 + " VARCHAR(255), "
            + COLUMN_2 + " VARCHAR(255), " + COLUMN_3 + " VARCHAR(255), " + COLUMN_4 +" VARCHAR(255), "+
            COLUMN_5+" VARCHAR(255), "+COLUMN_6+" VARCHAR(255), "+COLUMN_7+" VARCHAR(255), "+COLUMN_8+" VARCHAR(255));";

    static final String DROP_MY_TABLE = "DROP TABLE IF EXISTS" + MY_TABLE_NAME + ";";

    static final String CREATE_TABLE_NET = "CREATE TABLE " + MY_TABLE_NAME_NET + " (" + UID + " INTEGER" +
            " PRIMARY KEY AUTOINCREMENT, " + COLUMN_1 + " VARCHAR(255), "
            + COLUMN_2 + " VARCHAR(255), " + COLUMN_3 + " VARCHAR(255), " + COLUMN_4 +" VARCHAR(255), "+
            COLUMN_5+" VARCHAR(255), "+COLUMN_6+" VARCHAR(255), "+COLUMN_8+" VARCHAR(255), "+COLUMN_9+" VARCHAR(255), "+COLUMN_10+
            " VARCHAR(255), "+COLUMN_11+" VARCHAR(255));";
    static final String DROP_TABLE_NET = "DROP TABLE IF EXISTS" + MY_TABLE_NAME_NET + ";";
//******************************************************************************************************************************************

    static final String THUMBNAILS = "thumbnails";


    static final String APP_PATH_SD_CARD = "/"+AllStrings.APP_NAME+"/";
    static final String APP_THUMBNAIL_PATH_SD_CARD = AllStrings.THUMBNAILS;
    static final String FULL_PATH_TO_IMAGES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;






    public static boolean charIsOk(char a){
        boolean isOk = false;
        for(int i=0;i<AllStrings.ALLOWED_LETTERS.length();i++){
            if(AllStrings.ALLOWED_LETTERS.charAt(i)==a){
                isOk = true;
            }
        }
        return isOk;
    }


    public static String getProperCaseString(String fullName){

        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        for(int i=0;i<fullName.length();i++) {
            fullName = fullName.toLowerCase();

            char eachLetter = fullName.charAt(i);
            if (charIsOk(eachLetter)) {
                sb.append(eachLetter);
            }
        }

            fullName = sb.toString();
            fullName = fullName.trim();
            if(fullName.contains("  ")) {
                fullName = fullName.replace("  ", " ");
            }
            String[] nameStr = fullName.split(" ");
            for(String a:nameStr){
                a = String.valueOf(a.charAt(0)).toUpperCase()+a.substring(1, a.length()).toLowerCase()+" ";
                sb2.append(a);
            }
            fullName = sb2.toString().trim();
        return fullName;
    }
}
