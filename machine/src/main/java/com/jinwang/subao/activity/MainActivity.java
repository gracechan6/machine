package com.jinwang.subao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.delivery.DeliveryMainActivity;
import com.jinwang.subao.activity.user.UserMainActivity;
import com.jinwang.subao.service.OnlineService;
import com.jinwang.yongbao.device.Device;


public class MainActivity extends SubaoBaseActivity {

 //   LinearLayout llyuser,llydelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化单片机
        //Device.uartInit();

        setContentView(R.layout.activity_main);

        /*静态广播,针对直接扫描二维码从服务器获取取件信息*/
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_EDIT);
        sendBroadcast(intent);

        //15/7/24 modified by michael
//        llyuser= (LinearLayout) findViewById(R.id.rb_user);
//        llydelivery= (LinearLayout) findViewById(R.id.rb_delivery);
//
//        llyuser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setClass(MainActivity.this,UserMainActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        llydelivery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*打开快递员登陆提示页面，登陆成功后再操作跳转*/
//
//
//
//
//                /*登陆成功后进入快递员主界面*/
//                Intent intent=new Intent();
//                intent.setClass(MainActivity.this,DeliveryMainActivity.class);
//                startActivity(intent);
//            }
//        });
        //modify end

        sendBroadcast(new Intent("com.android.action.display_navigationbar"));

        //启动在线服务
        startOnlienService();
    }

    /**
     * 显示用户主界面
     * @param view 被点击的view
     */
    public void showUserMainActivity(View view)
    {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,UserMainActivity.class);
        startActivity(intent);
    }

    /**
     * 显示快递员主界面
     * @param view 被点击的view
     */
    public void showDeliveryMainActivity(View view)
    {
        /*打开快递员登陆提示页面，登陆成功后再操作跳转*/
        Intent intent = new Intent(this, DeliveryMainActivity.class);

        startActivity(intent);



                /*登陆成功后进入快递员主界面*/
//        Intent intent=new Intent();
//        intent.setClass(MainActivity.this,DeliveryMainActivity.class);
//        startActivity(intent);
    }

    private void startOnlienService()
    {
        Intent startSrv = new Intent(this, OnlineService.class);
        startSrv.putExtra("CMD", "RESET");
        this.startService(startSrv);
    }

    private StringBuffer loginUUID = new StringBuffer();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            return super.onKeyDown(keyCode, event);
        }

        loginUUID.append((char) event.getNumber());

        Log.i(getClass().getSimpleName(), "Character " + (char)event.getUnicodeChar());

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //关闭单片机
        Device.uartDestroy();
    }
}
