package com.jinwang.subao.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.jinwang.subao.R;
import com.jinwang.subao.RecordVar;

/**
 * Created by michael on 15/7/27.
 */
public class SubaoBaseActivity extends AppCompatActivity
{
    protected Toolbar mToolBar;
    protected TextView mTitle;
    protected TextView mback;
    protected TextView mexit;

    /**
     * 条理进度对话框
     */
    protected ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);

        //不显示键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * 隐藏软键盘
     * @param view
     */
    public void hideSoftKeyborad(View view)
    {
        InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            IBinder token;
            if (null != view)
            {
               token = view.getWindowToken();
            }
            else {
                token = getCurrentFocus().getWindowToken();
            }

            if (null != token)
            {
                m.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题
     * @param title
     */
    protected void setTitle(String title)
    {
        this.mTitle.setText(title);
    }

    /**
     * 取消右标题
     */
    protected void cancelExit()
    {
        this.mToolBar.removeView(mexit);
    }

    protected boolean BeCancelLogin()
    {
        mexit.setText(getString(R.string.app_exit_login));
        mexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToExit();
            }
        });
        return true;
    }

    protected void BackToExit()
    {
        new RecordVar().setShowUnlogin(true);
        mexit.setText(getString(R.string.app_exit));
        mexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMainActivity();
            }
        });
    }
    /**
     * 初始化导航栏
     */
    protected void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        //mToolBar.setBackgroundColor(Color.parseColor("#F1F1F1"));

        //设置标题
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        mTitle = new TextView(this);
        mTitle.setTextColor(Color.GRAY);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        //mTitle.setText("我要取件");
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
                //退出到主页面
                gotoMainActivity();
            }
        });
    }

    /**
     * 回退到主界面
     */
    protected void gotoMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
