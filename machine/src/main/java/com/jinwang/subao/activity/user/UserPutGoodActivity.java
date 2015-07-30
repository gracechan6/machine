package com.jinwang.subao.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.activity.delivery.DeliveryPutSizeActivity;
import com.jinwang.subao.sysconf.SysConfig;
import com.jinwang.subao.thread.CheckSoftInputThread;


public class UserPutGoodActivity extends SubaoBaseActivity {

    public static final String USER_PUT_CODE = "USER_PUT_CODE";

    //7/28/15 add by michael, 寄件码从扫描器读入
    private EditText inputArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_put_good);


        TextView lly_outermost= (TextView) findViewById(R.id.tv_title);
        lly_outermost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserPutGoodActivity.this,UserPutSizeActivity.class);
                startActivity(intent);
            }
        });

        // 15/7/27 add by michael, 输入取件码（从扫码器读入，扫码器就是一个输入设备）
        //寄件码输入域
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

                //因为键盘输入无法知道录入何时完成，设置^为结束符，读到^认为结束
                String lastOne = text.substring(text.length() - 1);
                if (lastOne.equals(SysConfig.LAST_CHAR))
                {
                    //去掉结束符
                    text = text.substring(0, text.length() - 1);
                    Log.i(getClass().getSimpleName(), "End text: " + text);

                    //验证取件码
                    verifyCode(text);
                }

                //          Log.i(getClass().getSimpleName(), "End text: " + inputArea.getText());
            }
        });
        // add end

        initToolBar();
        this.setTitle(getString(R.string.title_user_put));
        this.cancelExit();


        /*静态广播,针对直接扫描二维码从服务器获取寄件信息*/
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_EDIT);
        sendBroadcast(intent);
        /*扫描成功则进入UserPutSizeActivity 选择尺寸*/

/*测试屏幕属性*/
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;//宽度
//        int height = dm.heightPixels ;//高度
//
//        Log.i(getClass().getSimpleName(), "Height: " + height + "Width: " + width);
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
     * 服务端验证取件码，验证通过后打开取件
     * @param code  寄件码
     */
    private void verifyCode(String code)
    {
        //首先去服务端验证取件码，验证通过后打开选择选择箱格界面传递寄件码
        // 然后打开对应的箱子

        //打开选择箱格界面
        Intent intent = new Intent(this, UserPutSizeActivity.class);

        intent.putExtra(USER_PUT_CODE, code);

        startActivity(intent);
    }

}
