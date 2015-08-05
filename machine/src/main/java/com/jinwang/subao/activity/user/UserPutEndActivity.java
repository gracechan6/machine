package com.jinwang.subao.activity.user;


import android.os.Bundle;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;


public class UserPutEndActivity extends SubaoBaseActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_put_end);
        initToolBar();
        this.setTitle(getString(R.string.title_user_put));
        /*打印面单*/
    }
}
