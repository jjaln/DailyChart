package com.jjaln.dailychart;

import android.app.Application;
import android.content.Context;

import com.jjaln.dailychart.contents.setting.Foreground;

public class MyApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        Foreground.init(this);
    }
}