package com.jinwang.subao.activity.delivery;

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
import com.jinwang.subao.util.DeviceUtil;

import java.util.Map;


public class DeliveryPutSizeActivity extends ActionBarActivity {

    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView mback,mexit;

    private LinearLayout lly_large,lly_medium,lly_small;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_put_size);
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
        //--end

        lly_large= (LinearLayout) findViewById(R.id.lly_large);
        lly_large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取大尺寸快件柜*/
                size=1;

                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                getCodetoPrint(size);
            }
        });

        lly_medium= (LinearLayout) findViewById(R.id.lly_medium);
        lly_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取中尺寸快件柜*/
                size = 2;



                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                getCodetoPrint(size);
            }
        });

        lly_small= (LinearLayout) findViewById(R.id.lly_small);
        lly_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取小尺寸快件柜*/
                size = 3;


                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                getCodetoPrint(size);

            }
        });

        // 15/7/28 add by michael, 获取快件柜使用情况

    }


    /*size代表尺寸 1大 2中 3小 服务器随机打开相应尺寸的柜子*/

    /**
     * 从服务端获取适当大小的空柜子编号
     * @param size
     */
    protected void getCodetoPrint(int size){

        String expId = getIntent().getStringExtra(DeliveryPutGoodActivity.GOOD_NUM);
        String tel = getIntent().getStringExtra(DeliveryPutGoodActivity.USER_TEL);

        //服务端提交数据

        /*打开柜子成功后，退出当前页面*/
        finish();
    }

    protected void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setBackgroundColor(Color.parseColor("#00000000"));

        //设置标题
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        mTitle = new TextView(this);
        mTitle.setTextColor(Color.GRAY);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        mTitle.setText("我要投件");
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
                Intent intent = new Intent(DeliveryPutSizeActivity.this, DeliveryPutGoodActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
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
                finish();
            }
        });
    }
}
