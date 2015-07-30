package com.jinwang.subao;

import android.app.Application;
import android.util.Log;

import com.jinwang.yongbao.device.Device;

/**
 * Created by michael on 15/7/30.
 */
public class SubaoApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化单片机
        Log.i(getClass().getSimpleName(), "Init uart");
        Device.uartInit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        //关闭单片机
        Log.i(getClass().getSimpleName(), "Destroy uart");
        Device.uartDestroy();
    }
}
