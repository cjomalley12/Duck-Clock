package com.example.internmacbook.duckclock;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by internmacbook on 8/4/16.
 */
public class CurrentTime {
    private static final String TAG = "CurrentTime";

    private static Calendar c;
    private int minutes;
    private int hour;

    public CurrentTime(){
        getCurrentTime();
    }

    public String getCurrentTime() {
        c = Calendar.getInstance();
        minutes = c.get(Calendar.MINUTE);
        hour = c.get(Calendar.HOUR);

        //Log.i(TAG, "Current hour: " + hour + "\nCurrent minutes: " + minutes)

        String time = formatTime(c.getTime());

        return time;
    }

    public String formatTime(Date date) {
        DateFormat df = new SimpleDateFormat("h:mm a");
        return df.format(date);
    }

    public Date getDate () {
        return Calendar.getInstance().getTime();
    }

}
