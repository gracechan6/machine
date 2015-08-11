package com.jinwang.subao.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.SubaoApplication;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.util.DeviceUtil;
import com.jinwang.subao.util.SharedPreferenceUtil;
import com.jinwang.yongbao.device.Device;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class UserPutSizeActivity extends SubaoBaseActivity {

    private LinearLayout lly_large, lly_medium, lly_small;
    private TextView tv_large,tv_medium,tv_small;


    private ProgressBar progress_horizontal;
    private int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_size);
        initToolBar();
        this.setTitle(getString(R.string.title_user_put));


        lly_large= (LinearLayout) findViewById(R.id.lly_large);
        lly_large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取大尺寸快件柜*/
                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                getCodetoPrint(DeviceUtil.GRID_SIZE_LARGE);
            }
        });
        lly_large.setClickable(false);

        lly_medium= (LinearLayout) findViewById(R.id.lly_medium);
        lly_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取中尺寸快件柜*/
                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                getCodetoPrint(DeviceUtil.GRID_SIZE_MID);
            }
        });
        lly_medium.setClickable(false);

        lly_small= (LinearLayout) findViewById(R.id.lly_small);
        lly_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取小尺寸快件柜*/
                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                getCodetoPrint(DeviceUtil.GRID_SIZE_SMALL);

            }
        });
        lly_small.setClickable(false);

        //7/28/15 add by michael
        // 箱格使用信息同步到客户端，客户端可以直接获取箱格使用情况
        // 获取箱格使用情况，然后显示有界面，有几个大的，几个小的，几个中的

        try {
            Map<Integer, Integer> large = DeviceUtil.getLargeUnusedGridsList(this);
            Map<Integer, Integer> mid = DeviceUtil.getMidUnusedGridsList(this);
            Map<Integer, Integer> small = DeviceUtil.getSmallUnusedGridsList(this);

            /*界面展示各个箱格目前可用情况*/
            tv_large= (TextView) findViewById(R.id.tv_large);
            tv_medium= (TextView) findViewById(R.id.tv_medium);
            tv_small= (TextView) findViewById(R.id.tv_small);
            if(large.size()>0){
                tv_large.setText("" + large.size());
                lly_large.setClickable(true);
            }
            if(mid.size()>0){
                tv_medium.setText("" + mid.size());
                lly_medium.setClickable(true);
            }
            if(small.size()>0){
                tv_small.setText("" + small.size());
                lly_small.setClickable(true);
            }
            /*界面展示各个箱格目前可用情况end*/

            //显示在界面使用情况
        } catch (Exception e) {
            e.printStackTrace();
            //不会出现此异常
        }

        ///测试更新服务端
//        updateServerData(1, 13, DeviceUtil.GRID_SIZE_MID);
    }

    /*size代表尺寸 2大 1中 0小 服务器端获取信息生成二维码，之后打印*/
    protected void getCodetoPrint(int size) {
        String code = getIntent().getStringExtra(UserPutGoodActivity.USER_PUT_CODE);
        Iterator iter=null;
        int randomNum=-1;
        int useable=-1;
        TextView textView=null;

        switch(size)
        {
            case DeviceUtil.GRID_SIZE_LARGE:{
                try {
                    Map<Integer, Integer> large = DeviceUtil.getLargeUnusedGridsList(UserPutSizeActivity.this);
                    if(large.size()==0) {
                        Toast.makeText(UserPutSizeActivity.this, getString(R.string.error_NoSuitableSize), Toast.LENGTH_SHORT).show();
                        lly_large.setClickable(false);
                    }else
                    {
                        Random random = new Random();
                        randomNum=random.nextInt(large.size())+1;
                        iter = large.entrySet().iterator();
                        useable=large.size();
                        textView=tv_large;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;}
            case DeviceUtil.GRID_SIZE_MID:{
                try {
                    Map<Integer, Integer> mid = DeviceUtil.getMidUnusedGridsList(UserPutSizeActivity.this);
                    if(mid.size()==0) {
                        Toast.makeText(UserPutSizeActivity.this, getString(R.string.error_NoSuitableSize), Toast.LENGTH_SHORT).show();
                        lly_medium.setClickable(false);
                    }else
                    {
                        Random random = new Random();
                        randomNum=random.nextInt(mid.size())+1;
                        iter = mid.entrySet().iterator();
                        useable=mid.size();
                        textView=tv_medium;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;}
            case DeviceUtil.GRID_SIZE_SMALL:{
                try {
                    Map<Integer, Integer> small = DeviceUtil.getSmallUnusedGridsList(UserPutSizeActivity.this);
                    if(small.size()==0) {
                        Toast.makeText(UserPutSizeActivity.this, getString(R.string.error_NoSuitableSize), Toast.LENGTH_SHORT).show();
                        lly_small.setClickable(false);
                    }else
                    {
                        Random random = new Random();
                        randomNum=random.nextInt(small.size())+1;
                        iter = small.entrySet().iterator();
                        useable=small.size();
                        textView=tv_small;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;}
        }
        /*打开柜子成功后，退出当前页面*/
        int bid=0,cid=0;
        int i=0;
        if(iter!=null){
            while (iter.hasNext()) {
                i++;
                Map.Entry entry = (Map.Entry) iter.next();
                if(i==randomNum)
                {
                    bid=Integer.parseInt(String.valueOf(entry.getKey()));
                    cid=Integer.parseInt(String.valueOf(entry.getValue()));
                    break;
                }
            }
        }
        if(i==randomNum) {
            if (Device.openGrid(bid, cid, new int[10]) == 0) {//如果成功打开箱格
                DeviceUtil.updateGridState(this, bid, cid, DeviceUtil.GRID_STATUS_USED);//更新箱格状态
//                textView.setText(useable - 1);
                //打印面单

                //去服务器更新数据
                updateServerData(bid, cid,size);
                //finish();
            } else
                Toast.makeText(UserPutSizeActivity.this, getString(R.string.error_OpenCabinet), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 服务端验证取件码，验证通过后打开取件  ->接口12快递寄件选完箱子点确定(也叫普通用户投件接口)
     * @param bid cid
     */
    private void updateServerData(int bid,int cid,int size)
    {
        //首先去服务端验证取件码，验证通过后得到对应的板子号和箱子编号，然后打开对应的箱子
        //服务端验证。。。
//        progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_PUT_USERCABSIZE;
        RequestParams param = new RequestParams();
        param.put(SystemConfig.KEY_EquipmentNo, SharedPreferenceUtil.getStringData(this, SystemConfig.KEY_DEVICE_ID));
        param.put(SystemConfig.KEY_TerminalMuuid,SharedPreferenceUtil.getStringData(this,SystemConfig.KEY_DEVICE_MUUID));

        param.put(SystemConfig.KEY_BoardId,bid);
        param.put(SystemConfig.KEY_CabinetNo,cid);

        //测试UUID "FD3C0B8D-98B4-DC9E-4E15-758B8FDBDAC6"
        param.put(SystemConfig.KEY_BoxUuid, getIntent().getStringExtra(UserPutGoodActivity.USER_PUT_CODE));
        param.put(SystemConfig.KEY_BoxType,size);

        param.put(SystemConfig.KEY_TerminalMuuid, SharedPreferenceUtil.getStringData(this, SystemConfig.KEY_DEVICE_MUUID));

        AsyncHttpClient client=((SubaoApplication)getApplication()).getSharedHttpClient();
        client.post(url, param, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(getClass().getSimpleName(), "Response: " + response.toString());
                try {
                    boolean success = response.getBoolean("success");
                    //成功，跳转至相应页面
                    if (success)
                    {
                        Intent intent=new Intent(getApplicationContext(),UserPutEndActivity.class);
                        startActivity(intent);
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
    }
}
