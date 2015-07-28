package com.jinwang.subao.activity.user;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.MainActivity;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.sysconf.SysConfig;
import com.jinwang.subao.thread.CheckSoftInputThread;


public class UserGetGoodActivity extends SubaoBaseActivity {

//    private Button get_good;
//    private EditText edt_getGoodCode;

    //隐藏的输入框
    private EditText inputArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_get_good);
        initToolBar();

        // 15/7/27 add by michael, 输入取件码（从扫码器读入，扫码器就是一个输入设备）
        inputArea = (EditText) findViewById(R.id.inputArea);

        inputArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = inputArea.getText().toString();
                text = text.trim();

                //因为键盘输入无法知道录入何时完成，设置^为结束符，读到^认为结束
                String lastOne = text.substring(text.length() - 1);
                if (lastOne.equals(SysConfig.LAST_CHAR))
                {
                    //去掉结束符
                    text = text.substring(0, text.length() - 1);
                    Log.i(getClass().getSimpleName(), "End text: " + text);

                    //验证取件码
                    verifyCode(text);
                }

      //          Log.i(getClass().getSimpleName(), "End text: " + inputArea.getText());
            }
        });
        // add end

        //--15/7/27 modified by michael，不使用广播
        /*静态广播,针对直接扫描二维码从服务器获取取件信息*/
        /*
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_EDIT);
        sendBroadcast(intent);


//        edt_getGoodCode= (EditText) findViewById(R.id.edt_getGoodCode);
//        get_good= (Button) findViewById(R.id.get_good);
//        get_good.setOnClickListener(new get_goodListener());
*/
        //--modify end
    }

    /**
     * 服务端验证取件码，验证通过后打开取件
     * @param code
     */
    private void verifyCode(String code)
    {
        //首先去服务端验证取件码，验证通过后得到对应的板子号和箱子编号，然后打开对应的箱子
        //服务端验证。。。



        //箱子打开后，修改箱子状态为可用，如果有必要，去服务端更新箱子状态

        //最后关闭该页面，回到首页面
//        Intent intent = new Intent(this, MainActivity.class);
//
//        startActivity(intent);
    }

    // 15/7/27 add by michael, 启动禁用系统软件盘线程
    @Override
    protected void onStart() {

        //输入框请求焦点
        inputArea.requestFocus();

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
    // add end


    // 15/7/27 add by michael, 显示按取件码取件界面
    /**
     * 取件按取件码
     * @param view
     */
    public void showGetGoodByCode( View view )
    {
        Intent intent = new Intent(this, UserGetGoodByCode.class);
        startActivity(intent);
    }
    // add end


    // 15/7/27 modified by michael，不再使用
    class get_goodListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String code=null;//edt_getGoodCode.getText().toString();
            if(code==null || code.length()==0) {
                Toast.makeText(UserGetGoodActivity.this, "请正确填写取件码", Toast.LENGTH_SHORT).show();
                return;
            }

            /*从服务器后台查找是否存在该取件码，存在则打开相应柜子，返回true
                                            否则提示不存在该取件码。*/



            /*成功之后操作跳转至成功页面*/
            Intent intent = new Intent(UserGetGoodActivity.this, UserGetGoodByCodeOkActivity.class);
            startActivity(intent);

        }
    }
    // modified end
}
