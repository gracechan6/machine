package com.jinwang.subao.activity.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.R;
import com.jinwang.subao.thread.CheckSoftInputThread;

public class DeliveryLoginActivity extends SubaoBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_login);

        initToolBar();
        this.setTitle(getString(R.string.login));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delivery_login, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (null == checkSoftInputThread){
            checkSoftInputThread = new CheckSoftInputThread(this);
        }

        checkSoftInputThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        checkSoftInputThread.interrupt();
        checkSoftInputThread = null;
    }


    /**
     * 登录
     * @param view
     */
    public void login(View view)
    {
        Intent intent=new Intent();
        intent.setClass(this,DeliveryMainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
