package com.jinwang.subao.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.SubaoApplication;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.sysconf.SysConfig;
import com.jinwang.subao.thread.CheckSoftInputThread;
import com.jinwang.subao.util.DeviceUtil;
import com.jinwang.subao.util.SharedPreferenceUtil;
import com.jinwang.yongbao.device.Device;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class UserGetGoodActivity extends SubaoBaseActivity {

    //隐藏的输入框
    private EditText inputArea;

    private ProgressBar progress_horizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_get_good);
        initToolBar();
        this.setTitle(getString(R.string.title_user_get));

        progress_horizontal= (ProgressBar) findViewById(R.id.progress_horizontal);

        // 15/7/27 add by michael, 输入取件码（从扫码器读入，扫码器就是一个输入设备）
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

                    //验证取件码packageUuid=text
                    verifyCode(text);
                }

      //          Log.i(getClass().getSimpleName(), "End text: " + inputArea.getText());
            }
        });
        // add end
    }

    /**
     * 服务端验证取件码，验证通过后打开取件->接口28普通用户取件（扫码）
     * @param code
     */
    private void verifyCode(String code)
    {
        //首先去服务端验证取件码，验证通过后得到对应的板子号和箱子编号，然后打开对应的箱子
        //服务端验证。。。
        progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_GET_USERCABINET;
        RequestParams param = new RequestParams();
        param.put(SystemConfig.KEY_PackageUuid,code);
        param.put(SystemConfig.KEY_TerminalMuuid, SharedPreferenceUtil.getStringData(this, SystemConfig.KEY_DEVICE_MUUID));

        // 8/7/15 add by michael, 处理搞乱了，这里有界面的处理
        AsyncHttpClient client = ((SubaoApplication)getApplication()).getSharedHttpClient();
        client.post(url, param, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            /**
             * Returns when request succeeds
             *
             * @param statusCode http response status line
             * @param headers    response headers if any
             * @param response   parsed response if any
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(getClass().getSimpleName(), "Response: " + response.toString());
                try {
                    boolean success = response.getBoolean("success");
                    //成功，打开箱格
                    if (success) {
                        JSONObject gridInfo = response.getJSONArray("returnData").getJSONObject(0);
                        int boardID = gridInfo.getInt("boardId");
                        int cabinetNo = gridInfo.getInt("cabinetNo");
                        String packageEquipment = gridInfo.getString("packageEquipment");

                        String terminalID = SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_ID);

                        if (!packageEquipment.equals(terminalID)) {
                            Log.e(getClass().getSimpleName(), "System return error data: terminal id: " + terminalID + ", but return: " + packageEquipment);
                            return;
                        }

                        int ret = Device.openGrid(boardID, cabinetNo, new int[10]);//打开对应箱格

                        if (0 == ret) {
                            //箱子打开后，修改箱子状态为可用，如果有必要，去服务端更新箱子状态
                            DeviceUtil.updateGridState(getApplicationContext(), boardID, cabinetNo, DeviceUtil.GRID_STATUS_USEABLE);
                            //进入扫码成功界面
                            Intent intent=new Intent(getApplicationContext(),UserGetGoodByCodeOkActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_System), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), response.getString("errMsg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Response error: " + e.getMessage());
                }
            }

            /**
             * Returns when request failed
             *
             * @param statusCode    http response status line
             * @param headers       response headers if any
             * @param throwable     throwable describing the way request failed
             * @param errorResponse parsed response if any
             */
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


    // 15/7/27 add by michael, 显示按取件码取件界面
    /**
     * 取件按取件码
     * @param view
     */
    public void showGetGoodByCode( View view )
    {
        Intent intent = new Intent(this, UserGetGoodByCode.class);
        startActivity(intent);
    }
    // add end
}
