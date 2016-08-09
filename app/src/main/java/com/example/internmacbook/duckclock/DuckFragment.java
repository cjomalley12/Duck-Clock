package com.example.internmacbook.duckclock;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by internmacbook on 8/3/16.
 */
public class DuckFragment extends Fragment {
    private static final String TAG = "DuckFragment";
    private static final int REQUEST_CODE = 0;
    private View mDuck;
    private Button mFaster;
    private Button mSlower;
    private Button mNewAlarm;
    private TextView mCurrentTime;
    private TextView mAlarmTime;
    private TextView mAlarmStatus;
    private ProgressBar mProgressBar;

    private BeatBox mBeatBox;

    private int rotateTime;
    private int runCount;
    private String alarmTime;
    private boolean isAlarmSet;



    public static DuckFragment newInstance() {
        return new DuckFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_duck_screen, container, false);

        mDuck = view.findViewById(R.id.duck);
        mFaster = (Button) view.findViewById(R.id.faster_button);
        mSlower = (Button) view.findViewById(R.id.slower_button);
        mNewAlarm = (Button) view.findViewById(R.id.add_alarm_button);
        mCurrentTime = (TextView) view.findViewById(R.id.current_time);
        mAlarmTime = (TextView) view.findViewById(R.id.alarm_time_text_view);
        mAlarmStatus = (TextView) view.findViewById(R.id.alarm_status);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar) ;

        mBeatBox = new BeatBox(getActivity());

        alarmTime = "";
        runCount = 0;
        rotateTime = getActivity().getIntent().getIntExtra("rotateTime", 3000); //5 secs
        updateProgressBar();


        alarmTime = getActivity().getIntent().getStringExtra("time");
        mCurrentTime.setText(getActivity().getIntent().getStringExtra("passedInTime"));
        Log.i(TAG, "Alarm time reads: " + alarmTime);

        if(alarmTime != null){
            mNewAlarm.setText(getResources().getString(R.string.cancel_alarm));
            mAlarmStatus.setText(getResources().getString(R.string.alarm_set));
            mAlarmTime.setText(alarmTime);
        }
        else{
            mAlarmStatus.setText(getResources().getString(R.string.alarm_not_set));
        }

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000); // 1 second
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                updateTimeTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();


        mFaster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(rotateTime > 100) {
                    rotateTime -= 600;
                    if(rotateTime < 100){
                        rotateTime = 100;
                    }
                }
                updateProgressBar();
            }
        });

        mSlower.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                rotateTime += 600;
                if(rotateTime >= 6000) {
                    rotateTime = 6000;
                }
                updateProgressBar();
                Log.i(TAG, "Rotate duration: " + rotateTime);
            }
        });

        mDuck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                animateDuck(rotateTime);
                //play quack noise
                mBeatBox.play(mBeatBox.getSounds().get(0));
            }
        });

        mNewAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mNewAlarm.getText().toString().compareTo(getResources().getString(R.string.add_alarm)) == 0){
                    Intent i = new Intent(getActivity(), AlarmActivity.class);
                    i.putExtra("passedInTime", mCurrentTime.getText());
                    i.putExtra("rotateTime", rotateTime);
                    startActivity(i);
                }
                else{
                    alarmTime = getResources().getString(R.string.default_time);
                    mAlarmTime.setText(alarmTime);
                    mNewAlarm.setText(getResources().getString(R.string.add_alarm));
                    mAlarmStatus.setText(getResources().getString(R.string.alarm_not_set));
                }
            }
        });

        return view;
    }

    private void updateProgressBar() {
        if(mProgressBar.getMax()-rotateTime <= 100){
            mProgressBar.setProgress(mProgressBar.getMax());
        }
        else{
            mProgressBar.setProgress(mProgressBar.getMax() - rotateTime);
        }
    }

    private void updateTimeTextView() {
        CurrentTime time = new CurrentTime();
        String currentTime = time.getCurrentTime();

        boolean alarmActive = false;
        boolean sameTime = currentTime.equals(alarmTime);

        if(sameTime){
            alarmActive = true;
            animateDuck(rotateTime);
            rotateTime -= 100;
            updateProgressBar();
            mBeatBox.play(mBeatBox.getSounds().get(0));
        }

        if(currentTime.compareTo(mCurrentTime.getText().toString()) != 0){
            if(runCount != 0){
                animateDuck(rotateTime);
            }
            mCurrentTime.setText(currentTime);
            runCount++;
        }
    }

//    private void startAnimation(boolean isNight) {
//        if(isNight){
//            animateSunrise();
//        }
//        else{
//            animateSunset();
//        }
//    }

    private void animateDuck(int rotateTime){
        RotateAnimation rotate = new RotateAnimation(0, 360, 256, 256);
        rotate.setDuration(rotateTime);
        rotate.setInterpolator(new LinearInterpolator());

        mDuck.startAnimation(rotate);
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        mBeatBox.release();
    }
}
