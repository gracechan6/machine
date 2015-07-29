package com.jinwang.subao.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;


public class UserPutEndActivity extends SubaoBaseActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_put_end);
        initToolBar();
        this.setTitle(getString(R.string.title_user_put));

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        /*注意此时要根据客户选择的柜子，打印好码单后随机打开一个柜子*/
        openCabinet(bundle.getInt("size"));
    }

    /*注意此时要根据客户选择的柜子，打印好码单后随机打开一个柜子*/
    protected void openCabinet(int size){
        return;
    }

}
