package com.jinwang.subao.activity.delivery;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.jinwang.subao.R;
import com.jinwang.subao.RecordVar;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.asyncHttpClient.SubaoHttpClient;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.sysconf.SysConfig;
import com.jinwang.subao.thread.CheckSoftInputThread;
import com.jinwang.subao.util.SharedPreferenceUtil;
import com.loopj.android.http.RequestParams;


public class DeliveryMainActivity extends SubaoBaseActivity {
    private View lly_get,lly_put;


    //登录UUID
    private String mUUID;
    //输入区域，不可见
    private EditText inputArea;
    private TextView mUUIDvalidate;
    private int flag=0;//0：无操作  1：MUUID验证  2：getAllCabinets
    private ProgressBar progress_horizontal;
    private String[] result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_main);

        mUUIDvalidate= (TextView) findViewById(R.id.mUUIDvalidate);
        mUUIDvalidate.addTextChangedListener(new textChanges());
        progress_horizontal= (ProgressBar) findViewById(R.id.progress_horizontal);
        mUUID="1234567890";//仅作为测试
        verifyCode(mUUID);//仅作为测试
        //getAllCabinets(mUUID);
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
                    //验证登陆码
                    verifyCode(mUUID);
                }
                //Log.i(getClass().getSimpleName(), "End text: " + inputArea.getText());
            }
        });
        initToolBar();
        setTitle(getString(R.string.title_delivery));

        lly_get= findViewById(R.id.lly_get);
        lly_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 7/27/15, add by michael, 如果mUUID为空，说明没有登录，提示扫描用户身份二维码进行登录
                if (null == mUUID) {
                    promptUserLogin();
                    return;
                }
                // add end
                /*从服务端获取该快件柜属于操作快递员的所有快件柜编号,并打开相应的快件柜*/
                getAllCabinets(mUUID);
                //while (right);
                //以上操作成功后进入取件提示页面
                /*Thread thread=new Thread(new SleepThread());
                thread.start();
                if(!right)
                {
                    //将数据传到下一个界面，通过下一个界面打开对应箱格
                    Intent intent = new Intent(DeliveryMainActivity.this, DeliveryGetGoodActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putStringArray("result",result);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else return;*/
            }
        });

        lly_put= findViewById(R.id.lly_put);
        lly_put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 7/27/15, add by michael, 如果mUUID为空，说明没有登录，提示扫描用户身份二维码进行登录
                if (null == mUUID) {
                    promptUserLogin();
                    return;
                }
                // add end
                Intent intent = new Intent(DeliveryMainActivity.this, DeliveryPutGoodActivity.class);
                startActivity(intent);
            }
        });
    }

    /* 服务端获取所有可揽件的箱格-->接口24*/
    private void getAllCabinets(String mUUID){
        progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_GET_ALLCABINETS;
        RequestParams param = new RequestParams();
        param.put("Muuid",mUUID);
        //param.put("TerminalMuuid", SharedPreferenceUtil.getStringData(this,SystemConfig.KEY_DEVICE_MUUID));
        String tid= SharedPreferenceUtil.getStringData(this, SystemConfig.KEY_DEVICE_MUUID);
        String id=SharedPreferenceUtil.getStringData(this, SystemConfig.KEY_DEVICE_ID);
        String pwd=SharedPreferenceUtil.getStringData(this, SystemConfig.KEY_DEVICE_PASSWORD);

        param.put("TerminalMuuid","20CD52A9-AD1D-2BFA-B28A-59E5A3B1902C");//test
        flag=2;
        new SubaoHttpClient(url,param).connect(mUUIDvalidate,
                                               progress_horizontal,
                                               getString(R.string.server_link_fail),
                                               "getAllCabinets");
    }


    /* 服务端验证登陆码mUUID，验证通过后才可进行操作*/
    private void verifyCode(String mUUID){
        /*
         * 服务端验证是否正确，正确后提示登录成功，可以进行其它操作
         * 记住，退出按钮是进行退出到主界面，如果有登录，有退出登录
         */
        /*mUUID不为空，去服务器端判定mUUID是否正确->接口23*/
        flag=1;
        progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_MUUID_VALIDATE;
        RequestParams param = new RequestParams();
        param.put("Muuid",mUUID);

        new SubaoHttpClient(url,param).connect(mUUIDvalidate, progress_horizontal, getString(R.string.server_link_fail),"mUUIDvalidate");
    }

    /*监控mUUIDvalidate(TextView)的内容变化情况*/
    protected class textChanges implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(flag==1) {
                /*判定mUUID结束*/
                if (mUUIDvalidate.getText().toString() == null || mUUIDvalidate.getText().toString().length() == 0){
                    Toast.makeText(DeliveryMainActivity.this, getString(R.string.error_noReturn), Toast.LENGTH_SHORT).show();
                    return;
                }
                result = mUUIDvalidate.getText().toString().trim().split(":");
                if (result[0].equals("errMsg") || result[0].equals("success") && result[1].equals("false"))
                    Toast.makeText(DeliveryMainActivity.this, result[1], Toast.LENGTH_SHORT).show();
                else {
                    progress_horizontal.setProgress(progress_horizontal.getProgress() - progress_horizontal.getProgress());
                    progress_horizontal.setVisibility(View.INVISIBLE);
                    SystemConfig.VALUE_MuuidValue=mUUID;
                    if (BeCancelLogin()) {
                        mexit.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (s.toString().trim().equals(getString(R.string.app_exit))) {
                                    if (new RecordVar().isShowUnlogin()) {
                                        mUUID = null;
                                        SystemConfig.VALUE_MuuidValue=mUUID;
                                        new RecordVar().setShowUnlogin(false);
                                    }
                                }
                            }
                        });
                    }
                }
            }
            if(flag==2)
            {

                if (mUUIDvalidate.getText().toString() == null || mUUIDvalidate.getText().toString().length() == 0) {
                    Toast.makeText(DeliveryMainActivity.this, getString(R.string.error_noReturn), Toast.LENGTH_SHORT).show();
                    return;
                }
                result = mUUIDvalidate.getText().toString().trim().split(";");
                String success[]=result[0].split(":");
                if(success[0].equals("success") && success[1].equals("false") ) {
                    String errMsg[]=result[1].split(":");
                    Toast.makeText(DeliveryMainActivity.this, errMsg[1], Toast.LENGTH_SHORT).show();
                }
                else{
                    progress_horizontal.setProgress(progress_horizontal.getProgress() - progress_horizontal.getProgress());
                    progress_horizontal.setVisibility(View.INVISIBLE);

                    //result=new String[]{"sucfs","boardId:","cabintNo:","cs:33","boardId:","cabintNo:","cs:33","boardId:1","cabintNo:3","cs:33"};
                    //result=new String[]{"sucfs","boardId:1","cabintNo:2","cs:33","boardId:1","cabintNo:1","cs:33","boardId:1","cabintNo:3","cs,33"};
                    if(result.length>3) {
                        //将数据传到下一个界面，通过下一个界面打开对应箱格
                        Intent intent = new Intent(DeliveryMainActivity.this, DeliveryGetGoodActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putStringArray("result",result);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(DeliveryMainActivity.this, getString(R.string.error_Nogood), Toast.LENGTH_SHORT).show();


                }
            }
        }
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

    /*提示用户登录*/
    private void promptUserLogin(){
        //
        Dialog dialog = new Dialog(this);
        TextView tv = new TextView(this);
        tv.setText(R.string.text_prompt_login);
        tv.setTextSize(40);

        dialog.setTitle(R.string.text_error);

        dialog.setContentView(tv);
        dialog.show();
    }

}
