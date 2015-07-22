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
                /*打开登陆提示页面，登陆成功后再操作跳转*/




                /*登陆成功后进入快递员主界面*/
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,DeliveryMainActivity.class);
                startActivity(intent);
            }
        });

    }


}
