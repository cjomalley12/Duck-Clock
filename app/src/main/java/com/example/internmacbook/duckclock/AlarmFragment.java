package com.example.internmacbook.duckclock;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by internmacbook on 8/4/16.
 */
public class AlarmFragment extends Fragment {
    private static final String TAG = "AlarmFragment";

    private TimePicker mTimePicker;
    private Button mSaveButton;

    private int hour;
    private int minute;
    private String passedInTime;
    private int rotateTime;

    public static AlarmFragment newInstance() {
        return new AlarmFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);

        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);
        mSaveButton = (Button) view.findViewById(R.id.alarm_fragment_save_button);

        passedInTime = getActivity().getIntent().getStringExtra("passedInTime");
        rotateTime = getActivity().getIntent().getIntExtra("rotateTime", 5000);

        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MainActivity.class);

                Date date = new Date();
                date.setHours(hour);
                date.setMinutes(minute);

                CurrentTime currentTime = new CurrentTime();

                if(currentTime.getDate().equals(date)){
                    Toast.makeText(getContext(), "Your need to select a different time", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    i.putExtra("time", currentTime.formatTime(date));
                    i.putExtra("passedInTime", passedInTime);
                    i.putExtra("rotateTime", rotateTime);
                    startActivity(i);
                }
            }
        });

        return view;
    }
}
