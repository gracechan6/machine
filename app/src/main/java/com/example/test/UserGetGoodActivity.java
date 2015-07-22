package com.example.test;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class UserGetGoodActivity extends ActionBarActivity {

    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView mback,mexit;

    private Button get_good;
    private EditText edt_getGoodCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_get_good);
        initToolBar();

        /*静态广播,针对直接扫描二维码从服务器获取取件信息*/
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_EDIT);
        sendBroadcast(intent);


        edt_getGoodCode= (EditText) findViewById(R.id.edt_getGoodCode);
        get_good= (Button) findViewById(R.id.get_good);
        get_good.setOnClickListener(new get_goodListener());
    }


    class get_goodListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String code=edt_getGoodCode.getText().toString();
            if(code==null || code.length()==0) {
                Toast.makeText(UserGetGoodActivity.this, "请正确填写取件码", Toast.LENGTH_SHORT).show();
                return;
            }

            /*从服务器后台查找是否存在该取件码，存在则打开相应柜子，返回true
                                            否则提示不存在该取件码。*/



            /*成功之后操作跳转至成功页面*/
            Intent intent = new Intent(UserGetGoodActivity.this, UserGetGoodByCodeOkActivity.class);
            startActivity(intent);

        }
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
                Intent intent = new Intent(UserGetGoodActivity.this, UserMainActivity.class);
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
//                finish();
                Intent intent = new Intent(UserGetGoodActivity.this, UserGetGoodByCodeOkActivity.class);
                startActivity(intent);
            }
        });
    }


}
