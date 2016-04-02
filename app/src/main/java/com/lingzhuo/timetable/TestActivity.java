package com.lingzhuo.timetable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Wang on 2016/3/31.
 */
public class TestActivity extends AppCompatActivity {
    private TextView textView;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tableshow);
        textView= (TextView) findViewById(R.id.textView_Monday);
        System.out.println("/*************************************************************************************"+textView.getHeight());    }
}
