package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class UserPutGoodActivity extends ActionBarActivity {

    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView mback,mexit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_put_good);
        initToolBar();

        if(scanCode())
        {
            Intent intent = new Intent(UserPutGoodActivity.this, UserPutSizeActivity.class);
            startActivity(intent);
        }

    }

    /*扫描二维码，扫描成功返回true ，页面跳转至下一页 选择适当的快件柜*/
    protected Boolean scanCode(){

        return true;
    }

    protected void initToolBar()
    {
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
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
        mback=new TextView(this);
        mback.setTextColor(Color.GRAY);
        mback.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        Drawable drawable=getResources().getDrawable(R.drawable.icon_back);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        mback.setCompoundDrawables(drawable, null, null, null);

        mback.setText(getString(R.string.app_back));
        mToolBar.addView(mback, lp);


        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPutGoodActivity.this, UserMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        mexit=new TextView(this);
        mexit.setTextColor(Color.GRAY);
        mexit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        drawable=getResources().getDrawable(R.drawable.icon_close);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        mexit.setCompoundDrawables(drawable,null,null,null);

        mexit.setText("寄件");
        mToolBar.addView(mexit, lp);


        mexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPutGoodActivity.this, UserPutSizeActivity.class);
                startActivity(intent);
            }
        });


    }
}
