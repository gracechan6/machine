package com.jinwang.subao.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jinwang.subao.R;
import com.jinwang.subao.SubaoApplication;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.util.SharedPreferenceUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    //首次运行标志
    private static final String FIRST_RUN_FLAG = "FIRST_RUN_FLAG";
    //设备编号
    private static final String TERMINAL_NO = "terminalNo";
    //终端密码
    private static final String DEVICE_PASSWORD = "pwd";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (SharedPreferenceUtil.getBooleanData(this, FIRST_RUN_FLAG))
        {
            startMainActivity();

            return;
        }

        final AsyncHttpClient client = ((SubaoApplication)getApplication()).getSharedHttpClient();

        //首次启动服务端注册该设备
        //获取设备编号
        client.post(SystemConfig.URL_GET_CLIENT_ACOUNT, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.i(getClass().getSimpleName(), "Response: " + response.toString());

                try {
                    if (response.getBoolean("success"))
                    {
                        //保存设备账号和密码
                        SharedPreferenceUtil.saveData(getApplicationContext(), SystemConfig.KEY_DEVICE_ID, response.getString(TERMINAL_NO));
                        SharedPreferenceUtil.saveData(getApplicationContext(), SystemConfig.KEY_DEVICE_PASSWORD, response.getString(DEVICE_PASSWORD));

                        //注册
                        Map<String, String> params = new HashMap<>();
                        params.put("Mobilephone", SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_ID));
                        params.put("Password", SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_PASSWORD));
                        params.put("Role", "Group0006");

                        register(client, new RequestParams(params));
                    }
                    else
                    {
                        String error = response.getString("errMsg");
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
     * 服务端注册
     */
    private void register(final AsyncHttpClient client, final RequestParams params)
    {

        //注册
        client.post(SystemConfig.URL_REGISTER, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.i(getClass().getSimpleName(), "Response: " + response.toString());

                try {
                    if (response.getBoolean("success"))
                    {
                        //注册成功后登录
                        //保存首次运行标志
                        Map<String, String> params = new HashMap<>();
                        params.put("Mobilephone", SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_ID));
                        params.put("Password", SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_PASSWORD));
                        params.put("OperationType", "A");

                        login(client, new RequestParams(params));
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

    public void login(AsyncHttpClient client, RequestParams params)
    {
        //登录
        client.post(SystemConfig.URL_LOGIN, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.i(getClass().getSimpleName(), "Response: " + response.toString());

                try {
                    if (response.getBoolean("success"))
                    {
                        //保存登录UUID
                        SharedPreferenceUtil.saveData(getApplicationContext(), SystemConfig.KEY_DEVICE_MUUID, response.getString("mUuid"));
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
