package com.jinwang.subao.activity.delivery;

import android.os.Bundle;
import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;


public class DeliveryGetGoodActivity extends SubaoBaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_get_good);
        initToolBar();
        this.setTitle(getString(R.string.delivery_get));

        /*
        服务端获取所有该快递员的待取快件，打开所有箱格，更新箱格状态
        注意：网络获取时应该有提示信息：ProgressDialog或是ProgressBar这种
         */
    }

}
