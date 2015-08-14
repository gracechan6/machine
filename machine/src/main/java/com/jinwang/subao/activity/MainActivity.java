package com.jinwang.subao.activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.delivery.DeliveryMainActivity;
import com.jinwang.subao.activity.user.UserMainActivity;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.service.OnlineService;
import com.jinwang.subao.sysconf.SysConfig;
import com.jinwang.subao.util.DeviceUtil;

import java.util.List;
import java.util.Map;


public class MainActivity extends SubaoBaseActivity {

    //扫描获取的UUID，判端是否管理员登陆
    private String mUUID;
    //输入区域，不可见
    private EditText inputArea;

    ///主页显示箱格使用情况
    private TextView gridUseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化单片机
//        Device.uartInit();
        setContentView(R.layout.activity_main);

        ///Grid use info
        gridUseInfo = (TextView) findViewById(R.id.gridUseInfo);


        inputArea = (EditText) findViewById(R.id.inputArea);

        //始终不显示系统键盘
        inputArea.setShowSoftInputOnFocus(false);

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

                //add by michael, 防止内容为0时空指针异常
                if (text.length() <= 0)
                {
                    return;
                }

                String lastOne = text.substring(text.length() - 1);
                if (lastOne.equals(SysConfig.LAST_CHAR)) {
                    mUUID = text.substring(0, text.length() - 1);
                    Log.i(getClass().getSimpleName(), "End text: " + text);
                    inputArea.setText("");

    //                mUUID = "root";
                    if (mUUID.equals(SystemConfig.SYSTEM_MANAGER_MUUID))
                    {
                        /*如果扫描得到的muuid和管理员的muuid相等 则直接进入管理员界面*/
                        Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
                        startActivity(intent);
                    }

                }
            }
        });
        /*静态广播,针对直接扫描二维码从服务器获取取件信息*/
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_EDIT);
        sendBroadcast(intent);

        //15/7/24 modified by michael
//        llyuser= (LinearLayout) findViewById(R.id.rb_user);
//        llydelivery= (LinearLayout) findViewById(R.id.rb_delivery);
//
//        llyuser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setClass(MainActivity.this,UserMainActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        llydelivery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*打开快递员登陆提示页面，登陆成功后再操作跳转*/
//
//
//
//
//                /*登陆成功后进入快递员主界面*/
//                Intent intent=new Intent();
//                intent.setClass(MainActivity.this,DeliveryMainActivity.class);
//                startActivity(intent);
//            }
//        });
        //modify end

        //启动在线服务
        startOnlienService();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUseInfo();
    }

    /**
     * 更新箱格使用情况
     */
    private void updateUseInfo()
    {
        new AsyncTask<String, Intent, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = "";

                try {
                    List<Map<Integer, Integer>> large = DeviceUtil.getLargeUnusedGridsList(getApplicationContext());
                    List<Map<Integer, Integer>> mid = DeviceUtil.getMidUnusedGridsList(getApplicationContext());
                    List<Map<Integer, Integer>> small = DeviceUtil.getSmallUnusedGridsList(getApplicationContext());

                    ///如果没有箱格可用，显示无空箱格
                    if (large.size() == 0 && mid.size() == 0 && small.size() == 0)
                    {
                        result = "箱子已满";
                    }
                    else {
                        result = "大箱格可用：" + large.size() + ", 中箱格可用：" + mid.size() + ", 小箱格可用：" + small.size();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mDialog.setCancelable(false);
                mDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                gridUseInfo.setText(s);

                mDialog.dismiss();
                mDialog.setCancelable(false);
            }
        }.execute();


    }

    /**
     * 显示用户主界面
     * @param view 被点击的view
     */
    public void showUserMainActivity(View view)
    {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,UserMainActivity.class);
        startActivity(intent);
    }

    /**
     * 显示快递员主界面
     * @param view 被点击的view
     */
    public void showDeliveryMainActivity(View view)
    {
        /*打开快递员登陆提示页面，登陆成功后再操作跳转*/
        Intent intent = new Intent(this, DeliveryMainActivity.class);
        startActivity(intent);



                /*登陆成功后进入快递员主界面*/
//        Intent intent=new Intent();
//        intent.setClass(MainActivity.this,DeliveryMainActivity.class);
//        startActivity(intent);
    }

    private void startOnlienService()
    {
        Intent startSrv = new Intent(this, OnlineService.class);
        startSrv.putExtra("CMD", "RESET");
        this.startService(startSrv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //关闭单片机
//        Device.uartDestroy();
    }

    @Override
    protected void onStart() {

        //输入框请求焦点
        inputArea.requestFocus();
        super.onStart();
    }
}
