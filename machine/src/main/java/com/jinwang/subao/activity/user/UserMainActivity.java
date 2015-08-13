package com.jinwang.subao.activity.user;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;


public class UserMainActivity extends SubaoBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        initToolBar();
        this.setTitle(getString(R.string.app_name));

    }

    /**
     * 显示用户取件界面
     * @param view 被点击的View
     */
    public void showUserGetGoodActivity(View view)
    {
        Intent intent = new Intent(UserMainActivity.this, UserGetGoodActivity.class);
        startActivity(intent);
    }

    /**
     * 显示用户寄件界面
     * @param view 被点击的View
     */
    public void showUserPutGoodActivity(View view)
    {
        Intent intent = new Intent(UserMainActivity.this, UserPutGoodActivity.class);
        startActivity(intent);
    }

}
