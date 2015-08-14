package com.jinwang.subao.activity.delivery;



import android.content.Intent;
import android.os.AsyncTask;
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
import com.jinwang.subao.util.ToastUtil;
import com.jinwang.yongbao.device.Device;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class DeliveryPutSizeActivity extends SubaoBaseActivity {


    private LinearLayout lly_large,lly_medium,lly_small;
    private TextView tv_large,tv_medium,tv_small;

    //隐藏的文本框
    private TextView updateCabStatus;
    private ProgressBar progress_horizontal;

    ///箱格的使用情况, 不再多次获取，一次获取，多次使用
    List<Map<Integer, Integer>> large;
    List<Map<Integer, Integer>> mid;
    List<Map<Integer, Integer>> small;

    ///12/8/15 add by michael, 使用异步线程获取数据
    /**
     * 获取箱格的使用情况
     */
    private void getGridUseInfo()
    {
        new AsyncTask<String, Intent, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = "";

                try {
                    large = DeviceUtil.getLargeUnusedGridsList(getApplicationContext());
                    mid = DeviceUtil.getMidUnusedGridsList(getApplicationContext());
                    small = DeviceUtil.getSmallUnusedGridsList(getApplicationContext());

                    result = "大箱格可用：" + large.size() + ", 中箱格可用：" + mid.size() + ", 小箱格可用：" + small.size();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mDialog.setCancelable(false);
                mDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mDialog.dismiss();

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
            }
        }.execute();
    }
    //add --


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_size);
        initToolBar();
        this.setTitle(getString(R.string.delivery_put));

        progress_horizontal= (ProgressBar) findViewById(R.id.progress_horizontal);

        lly_large= (LinearLayout) findViewById(R.id.lly_large);
        lly_large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取大尺寸快件柜*/
                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                OpenCabinet(DeviceUtil.GRID_SIZE_LARGE);
            }
        });
        lly_large.setClickable(false);

        lly_medium= (LinearLayout) findViewById(R.id.lly_medium);
        lly_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取中尺寸快件柜*/
                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                OpenCabinet(DeviceUtil.GRID_SIZE_MID);
            }
        });
        lly_medium.setClickable(false);
        lly_small= (LinearLayout) findViewById(R.id.lly_small);
        lly_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取小尺寸快件柜*/
                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                OpenCabinet(DeviceUtil.GRID_SIZE_SMALL);

            }
        });
        lly_small.setClickable(false);
        //7/28/15 add by michael
        // 箱格使用信息同步到客户端，客户端可以直接获取箱格使用情况
        // 获取箱格使用情况，然后显示有界面，有几个大的，几个小的，几个中的

        getGridUseInfo();

        // 15/7/28 add by michael, 获取快件柜使用情况

    }


    /*size代表尺寸 2大 1中 0小 服务器随机打开相应尺寸的柜子*/

    /**
     * 从服务端获取适当大小的空柜子编号
     * @param size
     */
    private void OpenCabinet(int size){

        //String expId = getIntent().getStringExtra(DeliveryPutGoodActivity.GOOD_NUM);
        //String tel = getIntent().getStringExtra(DeliveryPutGoodActivity.USER_TEL);
        //服务端提交数据

        ///8/11/15 modified by michael, 随机选取一个用户选取大小的箱格
        Map<Integer, Integer> item = null;

        int randomNum=-1;
        int useable=-1;
        TextView textView=null;

        switch(size)
        {
            case DeviceUtil.GRID_SIZE_LARGE:{
                try {
                    if(large.size()==0) {
                        ToastUtil.showLargeToast(DeliveryPutSizeActivity.this, getString(R.string.error_NoSuitableSize), Toast.LENGTH_SHORT).show();
                        lly_large.setClickable(false);
                    }else
                    {
                        item = large.get(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;}
            case DeviceUtil.GRID_SIZE_MID:{
                try {
                    if(mid.size()==0) {
                        ToastUtil.showLargeToast(DeliveryPutSizeActivity.this, getString(R.string.error_NoSuitableSize), Toast.LENGTH_SHORT).show();
                        lly_medium.setClickable(false);
                    }else
                    {
                        item = mid.get(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;}
            case DeviceUtil.GRID_SIZE_SMALL:{
                try {
                    if(small.size()==0) {
                        ToastUtil.showLargeToast(DeliveryPutSizeActivity.this, getString(R.string.error_NoSuitableSize), Toast.LENGTH_SHORT).show();
                        lly_small.setClickable(false);
                    }else
                    {
                        item = small.get(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;}
        }
        /*打开柜子成功后，退出当前页面*/
        int bid=0,cid=0;

        bid = item.keySet().iterator().next();
        cid = item.get(bid);

        if (Device.openGrid(bid, cid, new int[10]) == 0) {//如果成功打开箱格
            DeviceUtil.updateGridState(this, bid, cid, DeviceUtil.GRID_STATUS_USED);//更新箱格状态
//                textView.setText(useable - 1);
            //去服务器更新数据
            mDialog.show();
            updateServerData(bid, cid);
            //finish();
        } else {
            ToastUtil.showLargeToast(DeliveryPutSizeActivity.this, getString(R.string.error_OpenCabinet), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 服务端验证取件码，验证通过后打开取件-->接口25
     * @param bid cid
     */
    private void updateServerData(final int bid,final int cid)
    {
        //首先去服务端验证取件码，验证通过后得到对应的板子号和箱子编号，然后打开对应的箱子
        //服务端验证。。。
//        progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_PUT_DELIVERYCABINET;
        RequestParams param = new RequestParams();
        ///UUID界面间传递
        String uuid = getIntent().getStringExtra(SystemConfig.KEY_Muuid);
        if (null == uuid)
        {
            Log.i(getClass().getSimpleName(), "mUUID is null, please check");

            DeviceUtil.updateGridState(this, bid, cid, DeviceUtil.GRID_STATUS_USEABLE);

            return;
        }

        param.put(SystemConfig.KEY_Muuid, uuid);
        param.put(SystemConfig.KEY_EquipmentNo, SharedPreferenceUtil.getStringData(this,SystemConfig.KEY_DEVICE_ID));
        param.put(SystemConfig.KEY_TerminalMuuid,SharedPreferenceUtil.getStringData(this,SystemConfig.KEY_DEVICE_MUUID));

        param.put(SystemConfig.KEY_BoardId,bid);
        param.put(SystemConfig.KEY_CabinetNo,cid);
        //手机+取件码
        param.put(SystemConfig.KEY_ReceivePhone,getIntent().getStringExtra(DeliveryPutGoodActivity.USER_TEL));
        param.put(SystemConfig.KEY_PackageNumber,getIntent().getStringExtra(DeliveryPutGoodActivity.GOOD_NUM));

        ///打印请求参数
        Log.i(getClass().getSimpleName(), "Request url: " + url + "\nRequest params: " + param.toString());

        AsyncHttpClient client=((SubaoApplication)getApplication()).getSharedHttpClient();
        client.post(url, param, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(getClass().getSimpleName(), "Response: " + response.toString());
                try {
                    boolean success = response.getBoolean("success");
                    //成功，跳转至相应页面
                    if (success){
                        ToastUtil.showLargeToast(DeliveryPutSizeActivity.this, getString(R.string.succ_operate), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        ToastUtil.showLargeToast(getApplicationContext(), response.getString("errMsg"), Toast.LENGTH_LONG).show();
                        DeviceUtil.updateGridState(getApplicationContext(), bid, cid, DeviceUtil.GRID_STATUS_USEABLE);
                    }
                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Response error: " + e.getMessage());
                    DeviceUtil.updateGridState(getApplicationContext(), bid, cid, DeviceUtil.GRID_STATUS_USEABLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(getClass().getSimpleName(), "Response: " + errorResponse.toString());
                ToastUtil.showLargeToast(getApplicationContext(), getString(R.string.error_System), Toast.LENGTH_LONG).show();

                DeviceUtil.updateGridState(getApplicationContext(), bid, cid, DeviceUtil.GRID_STATUS_USEABLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();

                mDialog.dismiss();
            }
        });
    }
}
