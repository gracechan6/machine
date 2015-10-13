package com.jinwang.subao.activity.delivery;


import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.db.CabinetGrid;
import com.jinwang.subao.db.CabinetGridDB;
import com.jinwang.subao.util.DeviceUtil;
import com.jinwang.subao.util.ToastUtil;
import com.jinwang.yongbao.device.Device;

import java.util.ArrayList;
import java.util.List;


public class DeliveryGetGoodActivity extends SubaoBaseActivity {

    private ProgressBar progress_horizontal;
    private CabinetGridDB cabinetGridDB;

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

        cabinetGridDB=CabinetGridDB.getInstance();
        List<CabinetGrid> cgs=new ArrayList<>();

        for(i=0;i<result.length;i+=2) {
            String cabintNo[]=result[i].split(":");
            String boardId[]=result[i+1].split(":");
            int bid,cid;
            bid = Integer.parseInt(boardId[1]);
            cid = Integer.parseInt(cabintNo[1]);
            if(Device.openGrid(bid, cid, new int[10])==0) {//如果成功打开箱格
                DeviceUtil.updateGridState(this, bid, cid, DeviceUtil.GRID_STATUS_USEABLE);//更新箱格状态
                //更新本地数据库
                CabinetGrid cabinetGrid=new CabinetGrid(bid,cid,0,0);
                cgs.add(cabinetGrid);
            }
            else
                ToastUtil.showLargeToast(DeliveryGetGoodActivity.this, getString(R.string.error_OpenCabinet), Toast.LENGTH_SHORT).show();

            progress_horizontal.setProgress(progress_horizontal.getProgress() + rate);
        }
        progress_horizontal.setProgress(100);
        progress_horizontal.setVisibility(View.INVISIBLE);

        if (cgs!=null && cgs.size()>0) {
            cabinetGridDB.updateCG(cgs);
            //cabinetGridDB.upLoadLocalData();
        }
    }
}
