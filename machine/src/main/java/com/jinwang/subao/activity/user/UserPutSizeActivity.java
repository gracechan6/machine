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
import com.jinwang.subao.util.DeviceUtil;

import java.util.Map;


public class UserPutSizeActivity extends ActionBarActivity {


    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView mback, mexit;

    private LinearLayout lly_large, lly_medium, lly_small;

    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_put_size);
        initToolBar();

        //7/28/15 add by michael
        // 箱格使用信息同步到客户端，客户端可以直接获取箱格使用情况
        // 获取箱格使用情况，然后显示有界面，有几个大的，几个小的，几个中的

        try {
            Map<Integer, Integer> large = DeviceUtil.getLargeUnusedGridsList(this);
            Map<Integer, Integer> mid = DeviceUtil.getMidUnusedGridsList(this);
            Map<Integer, Integer> small = DeviceUtil.getSmallUnusedGridsList(this);

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
                size = 1;
                getCodetoPrint(size);
            }
        });

        lly_medium = (LinearLayout) findViewById(R.id.lly_medium);
        lly_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取中尺寸快件柜之后，去服务器端获取信息生成二维码，之后打印*/
                size = 2;
                getCodetoPrint(size);
            }
        });

        lly_small = (LinearLayout) findViewById(R.id.lly_small);
        lly_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取小尺寸快件柜之后，去服务器端获取信息生成二维码，之后打印*/
                size = 3;
                getCodetoPrint(size);

            }
        });


    }

    /*size代表尺寸 1大 2中 3小 服务器端获取信息生成二维码，之后打印*/
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

    protected void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setBackgroundColor(Color.parseColor("#F1F1F1"));

        //设置标题
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        mTitle = new TextView(this);
        mTitle.setTextColor(Color.GRAY);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        mTitle.setText("我要寄件");
        mToolBar.addView(mTitle, lp);
        lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.LEFT;
        mback = new TextView(this);
        mback.setTextColor(Color.GRAY);
        mback.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_back);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mback.setCompoundDrawables(drawable, null, null, null);

        mback.setText(getString(R.string.app_back));
        mToolBar.addView(mback, lp);


        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPutSizeActivity.this, UserPutGoodActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;

        /**
         * 退出指退出此次操作，直接返回到主界面
         */
        mexit = new TextView(this);
        mexit.setTextColor(Color.GRAY);
        mexit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        drawable = getResources().getDrawable(R.drawable.icon_close);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mexit.setCompoundDrawables(drawable, null, null, null);

        mexit.setText(getString(R.string.app_exit));
        mToolBar.addView(mexit, lp);


        mexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * MainActivity启动模式为sigleTask，为了保证Activity只有一个
                 */
                Intent intent = new Intent(UserPutSizeActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });


    }

}
