package com.jinwang.subao;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity {

 //   LinearLayout llyuser,llydelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        sendBroadcast(new Intent("com.android.action.hide_navigationbar"));

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




                /*登陆成功后进入快递员主界面*/
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,DeliveryMainActivity.class);
        startActivity(intent);
    }

}
