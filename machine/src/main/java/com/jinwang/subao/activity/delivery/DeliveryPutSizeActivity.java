package com.jinwang.subao.activity.delivery;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.asyncHttpClient.SubaoHttpClient;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.util.DeviceUtil;
import com.jinwang.subao.util.SharedPreferenceUtil;
import com.jinwang.yongbao.device.Device;
import com.loopj.android.http.RequestParams;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class DeliveryPutSizeActivity extends SubaoBaseActivity {


    private LinearLayout lly_large,lly_medium,lly_small;
    private TextView tv_large,tv_medium,tv_small;

    //隐藏的文本框
    private TextView updateCabStatus;
    private ProgressBar progress_horizontal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_size);
        initToolBar();
        this.setTitle(getString(R.string.delivery_put));
        updateCabStatus= (TextView) findViewById(R.id.TD_code);
        updateCabStatus.addTextChangedListener(new textChanges());
        progress_horizontal= (ProgressBar) findViewById(R.id.progress_horizontal);


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

            tv_large.setText(large.size());
            tv_medium.setText(mid.size());
            tv_small.setText(small.size());
            /*界面展示各个箱格目前可用情况end*/

            //显示在界面使用情况
        } catch (Exception e) {
            e.printStackTrace();
            //不会出现此异常
        }
        //--end

        lly_large= (LinearLayout) findViewById(R.id.lly_large);
        lly_large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取大尺寸快件柜*/
                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                OpenCabinet(DeviceUtil.GRID_SIZE_LARGE);
            }
        });

        lly_medium= (LinearLayout) findViewById(R.id.lly_medium);
        lly_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取中尺寸快件柜*/
                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                OpenCabinet(DeviceUtil.GRID_SIZE_MID);
            }
        });

        lly_small= (LinearLayout) findViewById(R.id.lly_small);
        lly_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*选取小尺寸快件柜*/
                /*选好快件柜尺寸后，传送给服务器，服务器随机打开相应尺寸的柜子*/
                OpenCabinet(DeviceUtil.GRID_SIZE_SMALL);

            }
        });

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
        Iterator iter=null;
        int randomNum=-1;
        int useable=-1;
        TextView textView=null;

        switch(size)
        {
            case DeviceUtil.GRID_SIZE_LARGE:{
                try {
                    Map<Integer, Integer> large = DeviceUtil.getLargeUnusedGridsList(DeliveryPutSizeActivity.this);
                    if(large.size()==0)
                        Toast.makeText(DeliveryPutSizeActivity.this,getString(R.string.error_NoSuitableSize),Toast.LENGTH_SHORT).show();
                    else
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
                    Map<Integer, Integer> mid = DeviceUtil.getMidUnusedGridsList(DeliveryPutSizeActivity.this);
                    if(mid.size()==0)
                        Toast.makeText(DeliveryPutSizeActivity.this,getString(R.string.error_NoSuitableSize),Toast.LENGTH_SHORT).show();
                    else
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
                    Map<Integer, Integer> small = DeviceUtil.getSmallUnusedGridsList(DeliveryPutSizeActivity.this);
                    if(small.size()==0)
                        Toast.makeText(DeliveryPutSizeActivity.this,getString(R.string.error_NoSuitableSize),Toast.LENGTH_SHORT).show();
                    else
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
                DeviceUtil.updateGridState(this, bid, cid, 0);//更新箱格状态
                textView.setText(useable - 1);
                //去服务器更新数据
                updateServerData(bid, cid);
                //finish();
            } else
                Toast.makeText(DeliveryPutSizeActivity.this, getString(R.string.error_OpenCabinet), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 服务端验证取件码，验证通过后打开取件
     * @param bid cid
     */
    private void updateServerData(int bid,int cid)
    {
        //首先去服务端验证取件码，验证通过后得到对应的板子号和箱子编号，然后打开对应的箱子
        //服务端验证。。。
        progress_horizontal.setVisibility(View.VISIBLE);
        String url= SystemConfig.URL_PUT_DELIVERYCABINET;
        RequestParams param = new RequestParams();
        param.put(SystemConfig.KEY_Muuid,SystemConfig.VALUE_MuuidValue);
        param.put(SystemConfig.KEY_EquipmentNo, SharedPreferenceUtil.getStringData(this,SystemConfig.KEY_DEVICE_ID));
        param.put(SystemConfig.KEY_TerminalMuuid,SharedPreferenceUtil.getStringData(this,SystemConfig.KEY_DEVICE_MUUID));

        param.put(SystemConfig.KEY_BoardId,bid);
        param.put(SystemConfig.KEY_CabinetNo,cid);
        //手机+取件码
        param.put(SystemConfig.KEY_ReceivePhone,getIntent().getStringExtra(DeliveryPutGoodActivity.USER_TEL));
        param.put(SystemConfig.KEY_PackageNumber,getIntent().getStringExtra(DeliveryPutGoodActivity.GOOD_NUM));

        //param.put("TerminalMuuid", SharedPreferenceUtil.getStringData(this,SystemConfig.KEY_DEVICE_MUUID));
        //param.put("TerminalMuuid","A2AF397F-F35F-0392-4B7F-9DD1663B109C");//test
        new SubaoHttpClient(url,param).connect( updateCabStatus,
                progress_horizontal,
                getString(R.string.server_link_fail),
                "updateCabStatus");

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
            if (updateCabStatus.getText().toString() == null || updateCabStatus.getText().toString().length() ==
                    0) {
                Toast.makeText(DeliveryPutSizeActivity.this,getString(R.string.error_noReturn),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String[] result = updateCabStatus.getText().toString().trim().split(";");
            String success[] = result[0].split(":");
            if (success[0].equals("success") && success[1].equals("false")) {
                String errMsg[] = result[1].split(":");
                Toast.makeText(DeliveryPutSizeActivity.this, errMsg[1], Toast.LENGTH_SHORT).show();
            } else {
                progress_horizontal.setProgress(progress_horizontal.getProgress() -
                        progress_horizontal.getProgress());
                progress_horizontal.setVisibility(View.INVISIBLE);
                Toast.makeText(DeliveryPutSizeActivity.this, getString(R.string.succ_operate), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
