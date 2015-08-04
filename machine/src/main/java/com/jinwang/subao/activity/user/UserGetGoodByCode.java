package com.jinwang.subao.activity.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.asyncHttpClient.SubaoHttpClient;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.util.DeviceUtil;
import com.jinwang.yongbao.device.Device;
import com.loopj.android.http.RequestParams;

public class UserGetGoodByCode extends SubaoBaseActivity {

    private EditText last4Tel;//取件手机号码后四位
    private EditText code;  //取件码


    //隐藏的文本框
    private TextView td_code;
    private ProgressBar progress_horizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_get_good_by_code);

        last4Tel = (EditText) findViewById(R.id.last4_tel);
        code = (EditText) findViewById(R.id.code);

        initToolBar();
        this.setTitle(getString(R.string.title_user_get));

        td_code= (TextView) findViewById(R.id.TD_code);
        td_code.addTextChangedListener(new textChanges());
        progress_horizontal= (ProgressBar) findViewById(R.id.progress_horizontal);
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


        //箱子打开后，修改箱子状态为可用，如果有必要，去服务端更新箱子状态
    }

    /**
     * 服务端验证取件码，验证通过后打开取件
     * @param code
     */
    private void verifyCode(String code,String last4Tel)
    {
        //首先去服务端验证取件码，验证通过后得到对应的板子号和箱子编号，然后打开对应的箱子
        //服务端验证。。。
        progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_GET_USERCABINET;
        RequestParams param = new RequestParams();
        param.put("LastFour",last4Tel);
        param.put("PackageNumber",code);
        //param.put("TerminalMuuid", SharedPreferenceUtil.getStringData(this,SystemConfig.KEY_DEVICE_MUUID));
        param.put("TerminalMuuid","A2AF397F-F35F-0392-4B7F-9DD1663B109C");//test
        new SubaoHttpClient(url,param).connect(td_code,
                progress_horizontal,
                getString(R.string.server_link_fail),
                "getMyPackageByTelCode");
        //箱子打开后，修改箱子状态为可用，如果有必要，去服务端更新箱子状态

        //最后关闭该页面，回到首页面
//        Intent intent = new Intent(this, MainActivity.class);
//
//        startActivity(intent);
    }

    /*监控隐藏(TextView)的内容变化情况*/
    protected class textChanges implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {// getString(R.string.error_noReturn)
            if (td_code.getText().toString() == null || td_code.getText().toString().length() == 0) {
                Toast.makeText(UserGetGoodByCode.this,getString(R.string.error_noReturn), Toast.LENGTH_SHORT).show();
                return;
            }
            String[] result = td_code.getText().toString().trim().split(";");
            String success[] = result[0].split(":");
            if (success[0].equals("success") && success[1].equals("false")) {
                String errMsg[] = result[1].split(":");
                Toast.makeText(UserGetGoodByCode.this, errMsg[1], Toast.LENGTH_SHORT).show();
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
                        //Device.openGrid(bid, cid, new int[10]);//打开对应箱格
                        //箱子打开后，修改箱子状态为可用，如果有必要，去服务端更新箱子状态
                        //DeviceUtil.updateGridState(UserGetGoodByCode.this, bid, cid, 0);
                        finish();
                    }
                }
            }
        }
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
