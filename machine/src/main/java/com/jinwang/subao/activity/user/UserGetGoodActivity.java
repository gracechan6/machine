package com.jinwang.subao.activity.user;

import android.content.Intent;
import android.content.IntentFilter;
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
import com.jinwang.subao.activity.MainActivity;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.activity.delivery.DeliveryGetGoodActivity;
import com.jinwang.subao.asyncHttpClient.SubaoHttpClient;
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

//    private Button get_good;
//    private EditText edt_getGoodCode;

    //隐藏的输入框
    private EditText inputArea;
    //隐藏的文本框
    private TextView td_code;
    private ProgressBar progress_horizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_get_good);
        initToolBar();
        this.setTitle(getString(R.string.title_user_get));

        td_code= (TextView) findViewById(R.id.TD_code);
        td_code.addTextChangedListener(new textChanges());
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

        //--15/7/27 modified by michael，不使用广播
        /*静态广播,针对直接扫描二维码从服务器获取取件信息*/
        /*
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_EDIT);
        sendBroadcast(intent);


//        edt_getGoodCode= (EditText) findViewById(R.id.edt_getGoodCode);
//        get_good= (Button) findViewById(R.id.get_good);
//        get_good.setOnClickListener(new get_goodListener());
*/
        //--modify end
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
        param.put("PackageUuid",code);
        param.put("TerminalMuuid", SharedPreferenceUtil.getStringData(this, SystemConfig.KEY_DEVICE_MUUID));
        //param.put("TerminalMuuid","A2AF397F-F35F-0392-4B7F-9DD1663B109C");//test

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
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "系统错误", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "系统错误", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

        // add--

//        new SubaoHttpClient(url,param).connect(td_code,
//                progress_horizontal,
//                getString(R.string.server_link_fail),
//                "getMyPackageBytdcode");
        //箱子打开后，修改箱子状态为可用，如果有必要，去服务端更新箱子状态

        //最后关闭该页面，回到首页面
//        Intent intent = new Intent(this, MainActivity.class);
//
//        startActivity(intent);
    }

    /*监控隐藏(TextView)的内容变化情况*/
    protected class textChanges implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (td_code.getText().toString() == null || td_code.getText().toString().length() == 0) {
                Toast.makeText(UserGetGoodActivity.this, getString(R.string.error_noReturn), Toast.LENGTH_SHORT).show();
                return;
            }
            String[] result = td_code.getText().toString().trim().split(";");
            String success[] = result[0].split(":");
            if (success[0].equals("success") && success[1].equals("false")) {
                String errMsg[] = result[1].split(":");
                Toast.makeText(UserGetGoodActivity.this, errMsg[1], Toast.LENGTH_SHORT).show();
            } else {
                progress_horizontal.setProgress(progress_horizontal.getProgress() - progress_horizontal.getProgress());
                progress_horizontal.setVisibility(View.INVISIBLE);
                if (result.length > 3) {
                    //打开用户箱格
                    for(int i=1;i<result.length;i+=3) {
                        String boardId[]=result[i].split(":");
                        String cabintNo[]=result[i+1].split(":");
                        int bid,cid;
                        bid = Integer.parseInt(boardId[1]);
                        cid = Integer.parseInt(cabintNo[1]);
                        Device.openGrid(bid, cid, new int[10]);//打开对应箱格
                        //箱子打开后，修改箱子状态为可用，如果有必要，去服务端更新箱子状态
                        DeviceUtil.updateGridState(UserGetGoodActivity.this, bid, cid, 0);
                    }
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


    // 15/7/27 modified by michael，不再使用
    class get_goodListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String code=null;//edt_getGoodCode.getText().toString();
            if(code==null || code.length()==0) {
                Toast.makeText(UserGetGoodActivity.this, "请正确填写取件码", Toast.LENGTH_SHORT).show();
                return;
            }

            /*从服务器后台查找是否存在该取件码，存在则打开相应柜子，返回true
                                            否则提示不存在该取件码。*/



            /*成功之后操作跳转至成功页面*/
            Intent intent = new Intent(UserGetGoodActivity.this, UserGetGoodByCodeOkActivity.class);
            startActivity(intent);

        }
    }
    // modified end
}
