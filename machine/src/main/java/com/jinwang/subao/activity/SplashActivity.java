package com.jinwang.subao.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.SubaoApplication;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.db.CabinetGrid;
import com.jinwang.subao.db.CabinetGridDB;
import com.jinwang.subao.util.SharedPreferenceUtil;
import com.jinwang.subao.util.ToastUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    //首次运行标志
    private static final String FIRST_RUN_FLAG = "FIRST_RUN_FLAG";
    //设备编号
    private static final String TERMINAL_NO = "terminalNo";
    //终端密码
    private static final String DEVICE_PASSWORD = "pwd";

    private static CabinetGridDB cabinetGridDB =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (SharedPreferenceUtil.getBooleanData(this, FIRST_RUN_FLAG))
        {
            cabinetGridDB=CabinetGridDB.getInstance();
            List<CabinetGrid> cabinetGrids=cabinetGridDB.FailUpload();
            if (cabinetGrids!=null && cabinetGrids.size()>0) {
                //cabinetGridDB.upLoadLocalData(cabinetGrids);//上传本地数据至服务器

            }

            Log.i(getClass().getSimpleName(), "Terminal MUUID: " + SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_MUUID));
            startMainActivity();

            return;
        }

        String terminalID = SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_ID);
        String terminalPass = SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_PASSWORD);

        final AsyncHttpClient client = ((SubaoApplication)getApplication()).getSharedHttpClient();

        //账号和密码已经保存，直接登录
        if (null != terminalID && null != terminalPass && 0 < terminalID.length() && 0 < terminalPass.length())
        {
            Map<String, String> params = new HashMap<>();
            params.put("Mobilephone", terminalID);
            params.put("Password", terminalPass);
            params.put("OperationType", "A");

            Log.i(getClass().getSimpleName(), "Terminal ID: " + terminalID + " Pass: " + terminalPass);

            login(client, new RequestParams(params));

            return;
        }
        //首次启动服务端注册该设备
        //获取设备编号
        client.post(SystemConfig.URL_GET_CLIENT_ACOUNT, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i(getClass().getSimpleName(), "Response: " + response.toString());

                try {
                    if (response.getBoolean("success"))
                    {
                        //保存设备账号和密码
                        SharedPreferenceUtil.saveData(getApplicationContext(), SystemConfig.KEY_DEVICE_ID, response.getString(TERMINAL_NO));
                        SharedPreferenceUtil.saveData(getApplicationContext(), SystemConfig.KEY_DEVICE_PASSWORD, response.getString(DEVICE_PASSWORD));

                        //登录
                        Map<String, String> params = new HashMap<>();
                        params.put("Mobilephone", SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_ID));
                        params.put("Password", SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_PASSWORD));
                        params.put("OperationType", "A");

                        login(client, new RequestParams(params));
                    }
                    else
                    {
                        String error = response.getString("errMsg");
                        Log.e(getClass().getSimpleName(), "Register error: " +  error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i(getClass().getSimpleName(), "Response: " + statusCode);
            }
        });
    }


    public void login(final AsyncHttpClient client, final RequestParams params)
    {
        //登录
        client.post(SystemConfig.URL_LOGIN, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i(getClass().getSimpleName(), "Response: " + response.toString());

                try {
                    if (response.getBoolean("success")) {
                        //保存登录UUID
                        JSONObject jsonObject = response.getJSONArray("returnData").getJSONObject(0);
                        SharedPreferenceUtil.saveData(getApplicationContext(), SystemConfig.KEY_DEVICE_MUUID, jsonObject.getString("mUuid"));

                        /*chenss  added
                            首次登陆设备，从服务器端获取箱格信息，同步到本地数据库
                            */
                        //创建本地数据库
                        cabinetGridDB = CabinetGridDB.getInstance();
                        //从服务器端获取数据同步到本地数据库
                        Map<String, String> params = new HashMap<>();
                        params.put(SystemConfig.KEY_TerminalMuuid,
                                SharedPreferenceUtil.getStringData(getApplicationContext(),
                                        SystemConfig.KEY_TerminalMuuid));
                        Log.d("DBTest", "init db");
                        cabinetGridDB.syncLocalDBData(client, new RequestParams(params));//从服务器获取数据更新
                        //add  end

                        //保存首次运行标志
                        SharedPreferenceUtil.saveData(getApplicationContext(), FIRST_RUN_FLAG, true);

                        startMainActivity();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Log.i(getClass().getSimpleName(), "Response: " + statusCode);
            }
        });
    }

    /**
     * 启动主界面
     */
    private void startMainActivity()
    {
        //服务器端更新本地新版本
        if(SystemConfig.SYSTEM_VERSION!=null) {
            String url = SystemConfig.URL_UPDATE_TERMINALVERSION;
            RequestParams param = new RequestParams();
            param.put(SystemConfig.KEY_EquipmentMuuid, SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_ID));
            param.put(SystemConfig.KEY_SysVersion, SystemConfig.SYSTEM_VERSION);
            param.put(SystemConfig.KEY_TerminalMuuid, SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_MUUID));

            AsyncHttpClient client=((SubaoApplication)getApplication()).getSharedHttpClient();
            client.post(url, param, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(getClass().getSimpleName(), "Response: " + response.toString());
                    try {
                        boolean success = response.getBoolean("success");
                        //成功，跳转至相应页面
                        if (success){
                            Log.i(getClass().getSimpleName(), getString(R.string.succ_operate) );
                        } else {
                            ToastUtil.showLargeToast(getApplicationContext(), response.getString("errMsg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(getClass().getSimpleName(), "Response error: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i(getClass().getSimpleName(), "Response: " + errorResponse.toString());
                    ToastUtil.showLargeToast(getApplicationContext(), getString(R.string.error_System), Toast.LENGTH_LONG).show();
                }
            });
        }
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
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
