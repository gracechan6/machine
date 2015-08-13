package com.jinwang.subao.activity.user;


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
import com.jinwang.subao.SubaoApplication;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.sysconf.SysConfig;
import com.jinwang.subao.util.SharedPreferenceUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class UserPutGoodActivity extends SubaoBaseActivity {

    public static final String USER_PUT_CODE = "USER_PUT_CODE";

    //7/28/15 add by michael, 寄件码从扫描器读入
    private EditText inputArea;

    private ProgressBar progress_horizontal;
    private String BoxUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_put_good);

        progress_horizontal= (ProgressBar) findViewById(R.id.progress_horizontal);
        //test
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

                //字符太短直接返回
                if (text.length() == 0)
                {
                    return;
                }

                //因为键盘输入无法知道录入何时完成，设置^为结束符，读到^认为结束
                String lastOne = text.substring(text.length() - 1);
                if (lastOne.equals(SysConfig.LAST_CHAR))
                {
                    //去掉结束符
                    text = text.substring(0, text.length() - 1);
                    Log.i(getClass().getSimpleName(), "End text: " + text);
                    //验证取件码
                    verifyCode(text);

                    //删除已经输入的内容
                    inputArea.setText("");
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
    }
    // add end

    /**
     * 服务端验证取件码，验证通过后打开取件->接口27根据寄件码获取寄件信息 判断是否错误
     * @param code  寄件码
     */
    private void verifyCode(String code)
    {
        // 8/10/15 add by michael, 显示处理进度框
        mDialog.show();

        //首先去服务端验证取件码，验证通过后打开选择选择箱格界面传递寄件码
        this.BoxUuid=code;
        progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_PUT_USERCABINET;
        RequestParams param = new RequestParams();
        param.put(SystemConfig.KEY_PackageUuid, code);
        param.put(SystemConfig.KEY_TerminalMuuid, SharedPreferenceUtil.getStringData(this, SystemConfig.KEY_DEVICE_MUUID));

        Log.i(getClass().getSimpleName(), "Request url: " + url + "\nParams: " + param.toString());

        AsyncHttpClient client=((SubaoApplication)getApplication()).getSharedHttpClient();
        client.post(url,param,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(getClass().getSimpleName(), "Response: " + response.toString());
                try {
                    boolean success = response.getBoolean("success");
                    if(success){
                        int packStage=response.getInt("packStage");
                        if(packStage==0){
                            Intent intent = new Intent(getApplicationContext(), UserPutSizeActivity.class);
                            intent.putExtra(USER_PUT_CODE,BoxUuid);

                            //关闭进度条
                            mDialog.dismiss();
                            startActivity(intent);

                        }else {
                            Toast.makeText(getApplicationContext(),getString(R.string.error_packStage), Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),response.getString("errMsg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(getClass().getSimpleName(), "Response: " + errorResponse.toString());
                Toast.makeText(getApplicationContext(), getString(R.string.error_System), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mDialog.dismiss();
            }
        });
    }
}
