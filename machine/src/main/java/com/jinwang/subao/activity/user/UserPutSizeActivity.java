package com.jinwang.subao.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.MainActivity;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.util.DeviceUtil;

import java.util.HashMap;
import java.util.Map;


public class UserPutSizeActivity extends SubaoBaseActivity {

    private LinearLayout lly_large, lly_medium, lly_small;
    private TextView tv_large,tv_medium,tv_small;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_size);
        initToolBar();
        this.setTitle(getString(R.string.title_user_put));

        //7/28/15 add by michael
        // 箱格使用信息同步到客户端，客户端可以直接获取箱格使用情况
        // 获取箱格使用情况，然后显示有界面，有几个大的，几个小的，几个中的

        try {
            Map<Integer, Integer> large = DeviceUtil.getLargeUnusedGridsList(this);
            Map<Integer, Integer> mid = DeviceUtil.getMidUnusedGridsList(this);
            Map<Integer, Integer> small = DeviceUtil.getSmallUnusedGridsList(this);

            /*界面展示各个箱格目前可用情况*/
            tv_large= (TextView) findViewById(R.id.tv_large);
            tv_medium= (TextView) findViewById(R.id.tv_medium);
            tv_small= (TextView) findViewById(R.id.tv_small);

            tv_large.setText(large.size());
            tv_medium.setText(mid.size());
            tv_small.setText(small.size());
            /*界面展示各个箱格目前可用情况end*/

            //显示在界面使用情况
        } catch (Exception e) {
            e.printStackTrace();
            //不会出现此异常
        }

        //





        lly_large = (LinearLayout) findViewById(R.id.lly_large);
        lly_large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取大尺寸快件柜之后，去服务器端获取信息生成二维码，之后打印*/
                getCodetoPrint(2);
            }
        });

        lly_medium = (LinearLayout) findViewById(R.id.lly_medium);
        lly_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取中尺寸快件柜之后，去服务器端获取信息生成二维码，之后打印*/
                getCodetoPrint(1);
            }
        });

        lly_small = (LinearLayout) findViewById(R.id.lly_small);
        lly_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取小尺寸快件柜之后，去服务器端获取信息生成二维码，之后打印*/
                getCodetoPrint(0);

            }
        });


    }

    /*size代表尺寸 2大 1中 0小 服务器端获取信息生成二维码，之后打印*/
    protected void getCodetoPrint(int size) {
        String code = getIntent().getStringExtra(UserPutGoodActivity.USER_PUT_CODE);

        //随机打开一个可用的箱格并打印面单
        //提交服务端更新寄件信息
        //结束该页面，显示提示界面

        /*从服务器获取信息*/



        /*打印*/


        Intent intent = new Intent(UserPutSizeActivity.this, UserPutEndActivity.class);
        intent.putExtra("size", size);
        startActivity(intent);
    }

}
