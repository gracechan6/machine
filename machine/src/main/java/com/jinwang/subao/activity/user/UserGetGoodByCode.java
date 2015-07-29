package com.jinwang.subao.activity.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.yongbao.device.Device;

public class UserGetGoodByCode extends SubaoBaseActivity {

    private EditText last4Tel;//取件手机号码后四位
    private EditText code;  //取件码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_get_good_by_code);

        last4Tel = (EditText) findViewById(R.id.last4_tel);
        code = (EditText) findViewById(R.id.code);

        initToolBar();
        this.setTitle(getString(R.string.title_user_get));
    }

    /**
     * 检查取件码，成功后打开箱柜取件
     * @param view
     */
    public void checkCode(View view)
    {
        String phoneNum = last4Tel.getText().toString().trim();
        String codeS = code.getText().toString().trim();

        //其它判断

        //首先去服务端验证取件码，验证通过后得到对应的板子号和箱子编号，然后打开对应的箱子
        //服务端验证



        //箱子打开后，修改箱子状态为可用，如果有必要，去服务端更新箱子状态
    }

    private void openGrid()
    {
        int[] ret = new int[4];
        Device.openGrid(1, 15, ret);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_get_good_by_code, menu);
        return true;
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
