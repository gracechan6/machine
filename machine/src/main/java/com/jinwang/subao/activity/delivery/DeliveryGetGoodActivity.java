package com.jinwang.subao.activity.delivery;


import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.util.DeviceUtil;
import com.jinwang.yongbao.device.Device;


public class DeliveryGetGoodActivity extends SubaoBaseActivity {

    private ProgressBar progress_horizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_get_good);
        initToolBar();
        this.setTitle(getString(R.string.delivery_get));
        progress_horizontal= (ProgressBar) findViewById(R.id.progress_horizontal);

        Bundle bundle=this.getIntent().getExtras();
        String[] result=bundle.getString("result").split(";");
        //String[] result={"sucfs","boardId:1","cabintNo:2","cs:33","boardId:1","cabintNo:1","cs:33","boardId:1","cabintNo:3","cs,33"};
        progress_horizontal.setVisibility(View.VISIBLE);
        /*
        服务端获取所有该快递员的待取快件，打开所有箱格，更新箱格状态
        注意：网络获取时应该有提示信息：ProgressDialog或是ProgressBar这种
         */
        int i;
        int count=result.length;
        int rate=100/count;
        for(i=0;i<result.length;i+=2) {
            String cabintNo[]=result[i].split(":");
            String boardId[]=result[i+1].split(":");
            int bid,cid;
            bid = Integer.parseInt(boardId[1]);
            cid = Integer.parseInt(cabintNo[1]);
            if(Device.openGrid(bid, cid, new int[10])==0) {//如果成功打开箱格
                DeviceUtil.updateGridState(this, bid, cid, DeviceUtil.GRID_STATUS_USEABLE);//更新箱格状态
            }
            else
                Toast.makeText(DeliveryGetGoodActivity.this, getString(R.string.error_OpenCabinet), Toast.LENGTH_SHORT).show();

            progress_horizontal.setProgress(progress_horizontal.getProgress() + rate);
        }
        progress_horizontal.setProgress(100);
        progress_horizontal.setVisibility(View.INVISIBLE);
    }

}
