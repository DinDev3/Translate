package lk.dinuka.translate.util;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppUtils {
    public static Date getCurrentDateTime(){
        Date currentDate =  Calendar.getInstance().getTime();
        return currentDate;
    }

    public static String getFormattedDateString(Date date) {

        try {

            SimpleDateFormat spf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            String dateString = spf.format(date);

            Date newDate = spf.parse(dateString);
            spf= new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            return spf.format(newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void showMessage(Context context, String message) {
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//    }
}


/*
References:
https://github.com/anitaa1990/RoomDb-Sample/blob/master/app/src/main/java/com/an/room/util/AppUtils.java
 */