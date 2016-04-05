package com.boma.winshift;

import android.content.ContentValues;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BOMA on 11/6/2015.
 */
public class DbObjects {

    private long id;
    private String staffName, mobileNo, shift, shiftDate,
            company, section, picLocation, dateAdded, dateModified;
    private int intercom;
    private InputStream is;
    private InputStreamReader isReader;
    private List<ContentValues> list;


    public DbObjects(InputStream inputStream) {
        this.is = inputStream;

    }

    public List<ContentValues> readData() throws IOException {
        list = new ArrayList<>();
        isReader = new InputStreamReader(is);
        JsonReader reader = new JsonReader(isReader);
        reader.beginArray();

        while (reader.hasNext()) {
            list.add(getObjects(reader));
        }
        reader.endArray();

        return list;
    }

    public ContentValues getObjects(JsonReader reader) throws IOException {
        ContentValues cv = new ContentValues();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextLong();
                cv.put("_id", id);
            } else if (name.equals("staff_name")&& reader.peek()!=JsonToken.NULL) {
                staffName = reader.nextString();
                cv.put("person_name", staffName);
            } else if (name.equals("mobile_no")&& reader.peek()!=JsonToken.NULL) {
                mobileNo = reader.nextString();
                cv.put("phone_number", mobileNo);
            } else if (name.equals("shift")&& reader.peek()!=JsonToken.NULL) {
                shift = reader.nextString();
                cv.put("shift", shift);
            } else if (name.equals("shift_date")&& reader.peek()!=JsonToken.NULL) {
                shiftDate = reader.nextString();
                cv.put("date", shiftDate);
            } else if (name.equals("company")&& reader.peek()!=JsonToken.NULL) {
                company = reader.nextString();
                cv.put("company", company);
            } else if (name.equals("department")&& reader.peek()!=JsonToken.NULL) {
                section = reader.nextString();
                cv.put("department", section);
            }else if (name.equals("intercom")&& reader.peek()!=JsonToken.NULL) {
                section = reader.nextString();
                cv.put("intercom", section);
            } else if (name.equals("pic_location")&& reader.peek()!=JsonToken.NULL) {
                picLocation = reader.nextString();
                cv.put("pic_location", picLocation);
            } else if (name.equals("date_added")&& reader.peek()!=JsonToken.NULL) {
                dateAdded = reader.nextString();
                cv.put("date_added", dateAdded);
            } else if (name.equals("date_modified")&& reader.peek()!=JsonToken.NULL) {
                dateModified = reader.nextString();
                cv.put("date_modified", dateModified);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return cv;

    }
}
