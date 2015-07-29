package com.jinwang.subao.activity.user;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import com.jinwang.subao.R;


public class UserMainActivity extends ActionBarActivity {


//    private LinearLayout llyUserPut,llyUserGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        this.setTitle(getString(R.string.app_name));
//        llyUserPut= (LinearLayout) findViewById(R.id.lly_put);
//        llyUserPut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserMainActivity.this, UserPutGoodActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        llyUserGet= (LinearLayout) findViewById(R.id.lly_get);
//        llyUserGet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserMainActivity.this, UserGetGoodActivity.class);
//                startActivity(intent);
//            }
//        });
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
