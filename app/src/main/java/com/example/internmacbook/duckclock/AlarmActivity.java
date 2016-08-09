package com.example.internmacbook.duckclock;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class AlarmActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return AlarmFragment.newInstance();
    }

}
