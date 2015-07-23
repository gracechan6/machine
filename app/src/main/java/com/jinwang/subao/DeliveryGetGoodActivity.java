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
import android.widget.TextView;


public class DeliveryGetGoodActivity extends ActionBarActivity {

    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView mback,mexit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_get_good);
        initToolBar();
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
        mTitle.setText("我要取件");
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
                Intent intent = new Intent(DeliveryGetGoodActivity.this, DeliveryMainActivity.class);
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
