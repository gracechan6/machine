package com.jinwang.subao.activity.user;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.SubaoApplication;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.adapters.NumberKeyboardAdapter;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.util.DeviceUtil;
import com.jinwang.subao.util.SharedPreferenceUtil;
import com.jinwang.yongbao.device.Device;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.ddpush.im.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class UserGetGoodByCode extends SubaoBaseActivity {

    private EditText last4Tel;//取件手机号码后四位
    private EditText code;  //取件码

    private ProgressBar progress_horizontal;

    private NumberKeyboardAdapter mAdatper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_get_good_by_code);
        last4Tel = (EditText) findViewById(R.id.last4_tel);
        code = (EditText) findViewById(R.id.code);
        initToolBar();
        this.setTitle(getString(R.string.title_user_get));
        progress_horizontal= (ProgressBar) findViewById(R.id.progress_horizontal);

        // 10/8/15 add by michael, 添加自定议键盘

        GridView keyBoard = (GridView) findViewById(R.id.keyboard);
        mAdatper = new NumberKeyboardAdapter(this);
        keyBoard.setAdapter(mAdatper);
        keyBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText active = null;
                if (last4Tel.isFocused())
                {
                    active = last4Tel;
                }
                else if (code.isFocused())
                {
                    active = code;
                }
                else
                {
                    active = last4Tel;
                    last4Tel.requestFocus();
                }

                //删除键
                if (11 == position)
                {
                    String text = active.getText().toString().trim();
                    if (text.length() > 0) {
                        active.getText().delete(active.getSelectionStart() - 1, active.getSelectionStart());
                    }
                }
                else
                {
                    active.append(mAdatper.getItem(position).toString());
                }
            }
        });

        //不显示系统键盘
        last4Tel.setShowSoftInputOnFocus(false);
        code.setShowSoftInputOnFocus(false);
        // add --
    }


    /**
     * 检查取件码，成功后打开箱柜取件
     * @param view
     */
    public void checkCode(View view)
    {
        String phoneNum = last4Tel.getText().toString().trim();
        String codeS = code.getText().toString().trim();

        //其它判断
        if(phoneNum==null || phoneNum.length()==0 ||phoneNum.length()!=4){
            Toast.makeText(this,getString(R.string.error_last4Tel),Toast.LENGTH_SHORT).show();
            return;
        }

        if(codeS==null || codeS.length()==0){
            Toast.makeText(this,getString(R.string.error_rightPackageNo),Toast.LENGTH_SHORT).show();
            return;
        }
        //首先去服务端验证取件码，验证通过后得到对应的板子号和箱子编号，然后打开对应的箱子
        //服务端验证
        verifyCode(codeS,phoneNum);
    }

    /**
     * 服务端验证取件码，验证通过后打开取件->接口28普通用户取件（手机号后4位+取件码）
     * @param code
     */
    private void verifyCode(String code,String last4Tel)
    {
        //首先去服务端验证取件码，验证通过后得到对应的板子号和箱子编号，然后打开对应的箱子
        //服务端验证。。。
        progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_GET_USERCABINET;
        RequestParams param = new RequestParams();
        param.put(SystemConfig.KEY_LastFour,last4Tel);
        param.put(SystemConfig.KEY_PackageNumber,code);
        param.put(SystemConfig.KEY_TerminalMuuid, SharedPreferenceUtil.getStringData(this, SystemConfig.KEY_DEVICE_MUUID));

        // 8/7/15 add by michael, 处理搞乱了，这里有界面的处理
        AsyncHttpClient client = ((SubaoApplication)getApplication()).getSharedHttpClient();
        client.post(url, param, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(getClass().getSimpleName(), "Response: " + response.toString());
                try {
                    boolean success = response.getBoolean("success");
                    //成功，打开箱格
                    if (success)
                    {
                        JSONObject gridInfo = response.getJSONArray("returnData").getJSONObject(0);
                        int boardID = gridInfo.getInt("boardId");
                        int cabinetNo = gridInfo.getInt("cabinetNo");
                        String packageEquipment = gridInfo.getString("packageEquipment");

                        String terminalID = SharedPreferenceUtil.getStringData(getApplicationContext(), SystemConfig.KEY_DEVICE_ID);

                        if (!packageEquipment.equals(terminalID))
                        {
                            Log.e(getClass().getSimpleName(), "System return error data: terminal id: " + terminalID + ", but return: " + packageEquipment);
                            return;
                        }

                        int ret = Device.openGrid(boardID, cabinetNo, new int[10]);//打开对应箱格

                        if (0 == ret)
                        {
                            //箱子打开后，修改箱子状态为可用，如果有必要，去服务端更新箱子状态
                            DeviceUtil.updateGridState(getApplicationContext(), boardID, cabinetNo, DeviceUtil.GRID_STATUS_USEABLE);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),getString(R.string.error_System), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),response.getString("errMsg"), Toast.LENGTH_LONG).show();
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
        // add--
    }
    private void openGrid()
    {
        int[] ret = new int[4];
        Device.openGrid(1, 15, ret);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_get_good_by_code, menu);
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
