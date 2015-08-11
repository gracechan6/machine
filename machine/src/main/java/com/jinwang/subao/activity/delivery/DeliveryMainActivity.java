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


public class DeliveryMainActivity extends SubaoBaseActivity {
    private View lly_get,lly_put;


    //登录UUID
    private static String mUUID;
    //输入区域，不可见
    private EditText inputArea;
    private TextView mUUIDvalidate;

    private ProgressBar progress_horizontal;
    private String[] result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_main);

        progress_horizontal= (ProgressBar) findViewById(R.id.progress_horizontal);

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
                if (text.length() == 0)
                {
                    return;
                }
                text = text.trim();
                String lastOne = text.substring(text.length() - 1);
                if (lastOne.equals(SysConfig.LAST_CHAR))
                {
                    mUUID = text.substring(0, text.length() - 1);
                    Log.i(getClass().getSimpleName(), "End text: " + text);
                    //验证登陆码
                    verifyCode(mUUID);
                    inputArea.setText("");
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
                SystemConfig.VALUE_MuuidValue = mUUID;
                getAllCabinets(mUUID);
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

                //传递快递员登录UUID
                intent.putExtra(SystemConfig.KEY_Muuid, mUUID);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //置mUUID为空
        mUUID = null;
    }

    /* 服务端获取所有可揽件的箱格-->接口24*/
    private void getAllCabinets(String mUUID){
        //progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_GET_ALLCABINETS;
        RequestParams param = new RequestParams();
        param.put(SystemConfig.KEY_Muuid, mUUID);
        param.put(SystemConfig.KEY_TerminalMuuid, SharedPreferenceUtil.getStringData(this,SystemConfig.KEY_DEVICE_MUUID));

        AsyncHttpClient client = ((SubaoApplication)getApplication()).getSharedHttpClient();
        client.post(url, param, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(getClass().getSimpleName(), "Response: " + response.toString());
                try {
                    boolean success = response.getBoolean("success");
                    //成功，打开箱格
                    if (success) {
                        int sum = response.getJSONArray("returnData").length();
                        String result = "";
                        String terminalID = SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_ID);
                        for (int i = 0; i < sum; i++) {
                            JSONObject gridInfo = response.getJSONArray("returnData").getJSONObject(i);
                            String packageEquipment = gridInfo.getString("packageEquipment");

                            if (!packageEquipment.equals(terminalID)) {
                                Log.e(getClass().getSimpleName(), i + "System return error data: terminal id: " + terminalID + ", but return: " + packageEquipment);
                            } else {
                                result += "cabinetNo:" + gridInfo.getString("cabinetNo") + ";";
                                result += "boardId:" + gridInfo.getString("boardId") + ";";
                            }
                        }
                        if (!(result.length() == 0)) {
                            Intent intent = new Intent(DeliveryMainActivity.this, DeliveryGetGoodActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("result", result);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(DeliveryMainActivity.this, getString(R.string.error_Nogood), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.getString("errMsg"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Response error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(getClass().getSimpleName(), "Response: " + errorResponse.toString());
                Toast.makeText(getApplicationContext(), getString(R.string.error_System), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }


    /* 服务端验证登陆码mUUID，验证通过后才可进行操作*/
    private void verifyCode(String muuid){
        /*
         * 服务端验证是否正确，正确后提示登录成功，可以进行其它操作
         * 记住，退出按钮是进行退出到主界面，如果有登录，有退出登录
         */
        /*mUUID不为空，去服务器端判定mUUID是否正确->接口23*/
        //progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_MUUID_VALIDATE;
        RequestParams param = new RequestParams();
        param.put(SystemConfig.KEY_Muuid, muuid);

        AsyncHttpClient client = ((SubaoApplication) getApplication()).getSharedHttpClient();
        client.post(url, param, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(getClass().getSimpleName(), "Response: " + response.toString());
                try {
                    boolean success = response.getBoolean("success");
                    //成功，打开箱格
                    if (success) {
                        /// 8/11/15 add by michael, 是否为快递员账号
                        //不是快递员，提示账号错误
                        if (!response.getString("userRole").equals("Group0003"))
                        {
                            Toast.makeText(getApplicationContext(), "您没有权限进行此操作", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // add --
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
                                            SystemConfig.VALUE_MuuidValue = mUUID;
                                            new RecordVar().setShowUnlogin(false);
                                        }
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_mexitUpdate), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), response.getString("errMsg"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Response error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(getClass().getSimpleName(), "Response: " + errorResponse.toString());
                Toast.makeText(getApplicationContext(), getString(R.string.error_System), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
 //               mUUID = null;
            }
        });
    }




    // 15/7/27 add by michael, 启动禁用系统软件盘线程
    @Override
    protected void onStart() {

        //输入框请求焦点
        inputArea.requestFocus();

        super.onStart();
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
