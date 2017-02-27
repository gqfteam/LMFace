package com.lmface.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lmface.R;

/**
 * Created by Daniel on 2017/2/27.
 */

public class SponporSignIn extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate_sign);
    }

//    public void onYearMonthDayTimePicker(View view) {
//        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_OF_DAY);
//        picker.setRange(2000, 2030);
//        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
//            @Override
//            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
//                showToast(year + "-" + month + "-" + day + " " + hour + ":" + minute);
//            }
//        });
//        picker.show();
//    }
}
