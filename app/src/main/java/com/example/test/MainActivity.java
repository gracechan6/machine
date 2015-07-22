package com.example.test;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {

    LinearLayout llyuser,llydelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llyuser= (LinearLayout) findViewById(R.id.rb_user);
        llydelivery= (LinearLayout) findViewById(R.id.rb_delivery);

        llyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,UserMainActivity.class);
                startActivity(intent);
            }
        });

        llydelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,DeliveryMainActivity.class);
                startActivity(intent);
            }
        });

    }

   /* 页面上设计一个二维码扫描，然后用户直接通过这个扫描寄件二维码*/
    protected void scanCode()
    {
        /*扫描二维码后从服务器获取二维码信息，判断正确与否，正确直接进入选取柜子的页面*/


//        成功
//        Intent intent = new Intent(UserPutGoodActivity.this, UserPutSizeActivity.class);
//        startActivity(intent);


    }

}
