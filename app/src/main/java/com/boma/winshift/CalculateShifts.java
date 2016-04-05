package com.boma.winshift;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by BOMA on 11/15/2015.
 */
public class CalculateShifts {
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static String[] shifts = {"Morning","Afternoon","Night","Off"};
    public static String[] shiftsBackwards = {"Off","Night","Afternoon","Morning"};

    public static String getShiftFromDateStringsForToday(String knownDate,String shiftForKnownDate){
        String currShift="Not Known";

        if (knownDate==null || shiftForKnownDate==null){
            return currShift;
        }

        Date todayDate = new GregorianCalendar().getTime();
        String todayDateString = format.format(todayDate);

        //Return the same shifts for same dates
        if(todayDateString.equals(knownDate)){
            return shiftForKnownDate;
        }


        try {
            currShift = calculateShiftsForFutureDateObj(format.parse(knownDate), todayDate, shiftForKnownDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return currShift;
        }
        return currShift;

    }


    public static String shiftForUnknownDateStrings(String dbDateStr,String dateForCalc, String shiftForKnownDate){
        String currShift="Not Known";

        if (dbDateStr==null || shiftForKnownDate==null){
            return currShift;
        }
        if(dbDateStr.equals(dateForCalc)){
            return shiftForKnownDate;
        }
        try {
            Date dbDate = format.parse(dbDateStr);
            Date currentDate = format.parse(dateForCalc);

            if (dbDate.after(currentDate)) {
                currShift = calculateShiftsForPastDateObj(dbDate,currentDate,shiftForKnownDate);
            } else if (dbDate.before(currentDate)){
               currShift =  calculateShiftsForFutureDateObj(dbDate,currentDate,shiftForKnownDate);
            }
        }catch (ParseException e){
            e.printStackTrace();
            return currShift;
        }
        return currShift;
    }



    public static String forUnknownDate(Date known,Date unknown, String shiftForKnownDate){
        String currShift="Not Known";
        String knownDateStr = format.format(known);
        String unknownDateStr = format.format(unknown);

        if(knownDateStr.equals(unknownDateStr)){
            return shiftForKnownDate;
        }
        if (known.after(unknown)) {
                currShift = calculateShiftsForPastDateObj(known,unknown,shiftForKnownDate);
            } else if (known.before(unknown)){
                currShift =  calculateShiftsForFutureDateObj(known,unknown,shiftForKnownDate);
            }

        return currShift;
    }



    public static String calculateShiftsForFutureDateObj(Date dbDate, Date future, String shift){
        Calendar start = Calendar.getInstance();
        start.setTime(dbDate);
        String dayOfWeek;
        //Always start count from next date
        start.add(Calendar.DATE, 1);

        for(Date date_count = dbDate; date_count.before(future); start.add(Calendar.DATE, 1)){
            date_count = start.getTime();
            if (date_count.after(future))break;
            dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date_count);

            if(dayOfWeek.equals("Monday") || dayOfWeek.equals("Wednesday") || dayOfWeek.equals("Friday") ){
                for (int i=0; i<=shifts.length;i++) {
                    if (shift.equals(shifts[shifts.length - 1])) {
                        shift = shifts[0];
                        break;
                    } else if (shift.equals(shifts[i])) {
                        shift = shifts[i + 1];

                        break;
                    }

                }
            }
        }

        return shift;

    }

    public static String calculateShiftsForPastDateObj(Date known, Date past, String shift){
        Calendar start = Calendar.getInstance();
        start.setTime(known);
        String dayOfWeek;
        //Always start count from next date
        start.add(Calendar.DATE, -1);

        for(Date date_count = known; date_count.after(past); start.add(Calendar.DATE, -1)){
            date_count = start.getTime();
            if(date_count.before(past))break;
            dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date_count);

            if(dayOfWeek.equals("Sunday") || dayOfWeek.equals("Thursday") || dayOfWeek.equals("Tuesday") ){
                for (int i=0; i<=shiftsBackwards.length;i++) {
                    if (shift.equals(shiftsBackwards[shiftsBackwards.length - 1])) {
                        shift = shiftsBackwards[0];
                        break;
                    } else if (shift.equals(shiftsBackwards[i])) {
                        shift = shiftsBackwards[i + 1];

                        break;
                    }

                }
            }
        }

        return shift;


    }

//    public static String convertDateStringToFormat(String str){
//        SimpleDateFormat oldFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss",Locale.ENGLISH);
//
//    }


}
