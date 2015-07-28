package com.jinwang.subao.activity.delivery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.sysconf.SysConfig;
import com.jinwang.subao.thread.CheckSoftInputThread;


public class DeliveryMainActivity extends SubaoBaseActivity {

    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView mback;

    private View lly_get,lly_put;

    //登录UUID
    private String mUUID;
    //输入区域，不可见
    private EditText inputArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_main);

        //
        inputArea = (EditText) findViewById(R.id.inputArea);

        inputArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = inputArea.getText().toString();
                text = text.trim();

                String lastOne = text.substring(text.length() - 1);
                if (lastOne.equals(SysConfig.LAST_CHAR))
                {
                    mUUID = text.substring(0, text.length() - 1);
                    Log.i(getClass().getSimpleName(), "End text: " + text);

                    //验证取件码
                    verifyCode(text);
                }

                //          Log.i(getClass().getSimpleName(), "End text: " + inputArea.getText());
            }
        });

        initToolBar();

        lly_get= findViewById(R.id.lly_get);
        lly_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 7/27/15, add by michael, 如果mUUID为空，说明没有登录，提示扫描用户身份二维码进行登录
                if (null == mUUID)
                {
                    promptUserLogin();
                    return;
                }
                // add end

                /*从服务端获取该快件柜属于操作快递员的所有快件柜编号,并打开相应的快件柜*/



                //以上操作成功后进入取件提示页面
                Intent intent=new Intent(DeliveryMainActivity.this,DeliveryGetGoodActivity.class);
                startActivity(intent);
            }
        });

        lly_put= findViewById(R.id.lly_put);
        lly_put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 7/27/15, add by michael, 如果mUUID为空，说明没有登录，提示扫描用户身份二维码进行登录
                if (null == mUUID)
                {
                    promptUserLogin();
                    return;
                }
                // add end

                Intent intent=new Intent(DeliveryMainActivity.this,DeliveryPutGoodActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 服务端验证取件码，验证通过后打开取件
     * @param code
     */
    private void verifyCode(String code)
    {
        /*
         * 服务端验证是否正确，正确后提示登录成功，可以进行其它操作
         * 记住，退出按钮是进行退出到主界面，如果有登录，有退出登录
         */
    }

    // 15/7/27 add by michael, 启动禁用系统软件盘线程
    @Override
    protected void onStart() {

        //输入框请求焦点
        inputArea.requestFocus();

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
    // add end

    /**
     * 提示用户登录
     */
    private void promptUserLogin()
    {
        //
        Dialog dialog = new Dialog(this);
        TextView tv = new TextView(this);
        tv.setText(R.string.text_prompt_login);
        tv.setTextSize(40);

        dialog.setTitle(R.string.text_error);

        dialog.setContentView(tv);
        dialog.show();
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
        mTitle.setText("速宝快递员");
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
                //15/7/27 modified by michael, just finish this activity

                finish();
                /*
                Intent intent = new Intent(DeliveryMainActivity.this, UserMainActivity.class);
                startActivity(intent);*/
            }
        });
    }


}
