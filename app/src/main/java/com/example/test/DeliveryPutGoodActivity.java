package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class DeliveryPutGoodActivity extends ActionBarActivity {


    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView mback,mexit;

    private Button btnPut_good;
    private EditText edt_expId,edt_tel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_put_good);

        initToolBar();

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
            if(expId==null || expId.length()==0) {
                Toast.makeText(DeliveryPutGoodActivity.this, "请输入正确的快件单号", Toast.LENGTH_SHORT).show();
                return;
            }
            if(tel==null || tel.length()==0) {
                Toast.makeText(DeliveryPutGoodActivity.this, "请输入收件人手机号", Toast.LENGTH_SHORT).show();
                return;
            }

            /*把数据传入服务器核实获取货物信息的正确性，验证正确后进入下一个界面*/





            Intent intent=new Intent(DeliveryPutGoodActivity.this,DeliveryPutSizeActivity.class);
            startActivity(intent);
        }
    }

    protected void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setBackgroundColor(Color.parseColor("#00000000"));

        //设置标题
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        mTitle = new TextView(this);
        mTitle.setTextColor(Color.GRAY);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        mTitle.setText("我要投件");
        mToolBar.addView(mTitle, lp);
        lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.LEFT;
        mback = new TextView(this);
        mback.setTextColor(Color.GRAY);
        mback.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_back);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mback.setCompoundDrawables(drawable, null, null, null);

        mback.setText(getString(R.string.app_back));
        mToolBar.addView(mback, lp);


        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryPutGoodActivity.this, DeliveryMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        mexit = new TextView(this);
        mexit.setTextColor(Color.GRAY);
        mexit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        drawable = getResources().getDrawable(R.drawable.icon_close);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mexit.setCompoundDrawables(drawable, null, null, null);

        mexit.setText(getString(R.string.app_exit));
        mToolBar.addView(mexit, lp);


        mexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
