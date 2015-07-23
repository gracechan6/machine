package com.jinwang.subao;

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


public class UserMainActivity extends ActionBarActivity {

    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView mback;

    private LinearLayout llyUserPut,llyUserGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        initToolBar();
        llyUserPut= (LinearLayout) findViewById(R.id.lly_put);
        llyUserPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, UserPutGoodActivity.class);
                startActivity(intent);
            }
        });

        llyUserGet= (LinearLayout) findViewById(R.id.lly_get);
        llyUserGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, UserGetGoodActivity.class);
                startActivity(intent);
            }
        });
    }


    protected void initToolBar()
    {
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        mToolBar.setBackgroundColor(Color.parseColor("#00000000"));
        //标题初始化
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        mTitle = new TextView(this);
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        mTitle.setText(getString(R.string.app_name));
        mToolBar.addView(mTitle, lp);
        lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.LEFT;
        mback=new TextView(this);
        mback.setTextColor(Color.WHITE);
        mback.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        Drawable drawable=getResources().getDrawable(R.drawable.icon_back_white);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mback.setCompoundDrawables(drawable, null, null, null);

        mback.setText(getString(R.string.app_back));
        mToolBar.addView(mback, lp);


        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }
}
