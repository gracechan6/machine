package com.jinwang.subao.activity.user;


import android.os.Bundle;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;


public class UserGetGoodByCodeOkActivity extends SubaoBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_get_good_by_code_ok);
        initToolBar();
        this.setTitle(getString(R.string.title_user_get));
    }
}
