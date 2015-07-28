package com.jinwang.subao.activity.delivery;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.activity.delivery.DeliveryMainActivity;
import com.jinwang.subao.R;
import com.jinwang.subao.thread.CheckSoftInputThread;

public class DeliveryLoginActivity extends SubaoBaseActivity {

    private Toolbar mToolBar;
    private TextView mback;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_login);

        initToolBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delivery_login, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (null == checkSoftInputThread){
            checkSoftInputThread = new CheckSoftInputThread(this);
        }

        checkSoftInputThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        checkSoftInputThread.interrupt();
        checkSoftInputThread = null;
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
        lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.LEFT;
        mback=new TextView(this);
        mback.setTextColor(Color.GRAY);
        mback.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        Drawable drawable=getResources().getDrawable(R.drawable.icon_back);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        mback.setCompoundDrawables(drawable, null, null, null);

        mback.setText(getString(R.string.app_back));
        mback.setGravity(Gravity.CENTER);
        mToolBar.addView(mback, lp);


        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 登录
     * @param view
     */
    public void login(View view)
    {
        Intent intent=new Intent();
        intent.setClass(this,DeliveryMainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
