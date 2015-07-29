package com.jinwang.subao.activity.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;


public class DeliveryPutGoodActivity extends SubaoBaseActivity {

    /**
     * 快件编号
     */
    public static final String GOOD_NUM = "GOOD_NUM";
    /**
     * 用户电话号码
     */
    public static final String USER_TEL = "USER_TEL";


    private Button btnPut_good;

    private EditText edt_expId,edt_tel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_put_good);

        initToolBar();
        this.setTitle(getString(R.string.delivery_put));

        edt_expId= (EditText) findViewById(R.id.edt_expId);
        edt_tel= (EditText) findViewById(R.id.edt_tel);
        btnPut_good= (Button) findViewById(R.id.btnPut_good);
        btnPut_good.setOnClickListener(new put_goodListener());
    }
    /*输入完快递单号以及收件人手机号后点击完成产生的事件*/
    public class put_goodListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String expId=edt_expId.getText().toString();
            String tel=edt_tel.getText().toString();

            if(expId.length()==0) {
                Toast.makeText(DeliveryPutGoodActivity.this, getString(R.string.input_Right_expressSingle), Toast.LENGTH_SHORT).show();
                return;
            }
            if(tel.length()==0) {
                Toast.makeText(DeliveryPutGoodActivity.this, getString(R.string.input_Right_tel), Toast.LENGTH_SHORT).show();
                return;
            }

            /*把数据传入服务器核实获取货物信息的正确性，验证正确后进入下一个界面*/


            Intent intent=new Intent(DeliveryPutGoodActivity.this,DeliveryPutSizeActivity.class);

            //传递快件单号和电话号码给下一界面
            //传递快件单号和电话号码给下一界面
            intent.putExtra(GOOD_NUM, expId);
            intent.putExtra(USER_TEL, tel);
            startActivity(intent);
        }
    }
}
