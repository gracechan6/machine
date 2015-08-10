package com.jinwang.subao;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.jinwang.yongbao.device.Device;
import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by michael on 15/7/30.
 */
public class SubaoApplication extends Application
{
    /**
     * Async http client
     */
    private AsyncHttpClient sharedHttpClient;

    public AsyncHttpClient getSharedHttpClient()
    {
        return sharedHttpClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedHttpClient = new AsyncHttpClient();

        //初始化单片机
        Log.i(getClass().getSimpleName(), "Init uart");
        Device.uartInit();

        sendBroadcast(new Intent("com.android.action.display_navigationbar"));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        //关闭单片机
        Log.i(getClass().getSimpleName(), "Destroy uart");
        Device.uartDestroy();
    }
}
