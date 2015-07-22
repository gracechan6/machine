package com.example.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2015/7/22.
 */
public class StaticReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context arg0, Intent arg1) {
        System.out.println("onReceive......");
    }

}