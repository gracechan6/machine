package com.jinwang.subao.activity.delivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.activity.SubaoBaseActivity;
import com.jinwang.subao.adapters.NumberKeyboardAdapter;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.util.KeyboardUtils;
import com.jinwang.subao.util.ToastUtil;

import java.lang.reflect.Method;


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

    //8/11/15 add by michael, 新增自定义键盘
    private NumberKeyboardAdapter mAdapter;
    //add --

    //9/28/15 add by chenss
    KeyboardUtils keyboardUtils;
    private Context context;
    private Activity activity;
    //add end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_put_good);
        activity=this;
        context=this;

        initToolBar();
        this.setTitle(getString(R.string.delivery_put));

        edt_expId= (EditText) findViewById(R.id.edt_expId);
        edt_tel= (EditText) findViewById(R.id.edt_tel);
        btnPut_good= (Button) findViewById(R.id.btnPut_good);
        btnPut_good.setOnClickListener(new put_goodListener());

        //8/11/15 add by michael, 新增自定义键盘

        ///不显示键盘
        /*edt_expId.setShowSoftInputOnFocus(false);
        edt_tel.setShowSoftInputOnFocus(false);

        GridView keyBoard = (GridView) findViewById(R.id.keyboard);
        mAdapter = new NumberKeyboardAdapter(this);
        keyBoard.setAdapter(mAdapter);
        keyBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText active = null;
                if (edt_expId.isFocused())
                {
                    active = edt_expId;
                }
                else if (edt_tel.isFocused())
                {
                    active = edt_tel;
                }
                else
                {
                    active = edt_expId;
                    edt_expId.requestFocus();
                }

                //删除键
                if (11 == position)
                {
                    String text = active.getText().toString().trim();
                    if (text.length() > 0) {
                        active.getText().delete(active.getSelectionStart() - 1, active.getSelectionStart());
                    }
                }
                else
                {
                    active.append(mAdapter.getItem(position).toString());
                }
            }
        });*/
        //add --

        //自定义键盘 chenss  Modified
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            edt_expId.setInputType(InputType.TYPE_NULL);
            edt_tel.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
//	setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);

//4.0的是setShowSoftInputOnFocus,4.2的是setSoftInputOnFocus
                setShowSoftInputOnFocus.setAccessible(false);
                setShowSoftInputOnFocus.invoke(edt_expId, false);
                setShowSoftInputOnFocus.invoke(edt_tel, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        keyboardUtils=new KeyboardUtils(activity,context,edt_expId);
        edt_expId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //keyboardUtils=new KeyboardUtils(activity,context,last4Tel);
                keyboardUtils.turnEdit(edt_expId);
                return false;
            }
        });

        edt_tel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //keyboardUtils=new KeyboardUtils(activity,context,code);
                keyboardUtils.turnEdit(edt_tel);
                return false;
            }
        });
        //modified  end
    }
    /*输入完快递单号以及收件人手机号后点击完成产生的事件*/
    public class put_goodListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String expId=edt_expId.getText().toString();
            String tel=edt_tel.getText().toString();

            if(expId==null || expId.length()==0) {
                ToastUtil.showLargeToast(DeliveryPutGoodActivity.this, getString(R.string.input_Right_expressSingle), Toast.LENGTH_SHORT).show();
                return;
            }
            if(tel==null || tel.length()!=11) {
                ToastUtil.showLargeToast(DeliveryPutGoodActivity.this, getString(R.string.input_Right_tel), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent=new Intent(DeliveryPutGoodActivity.this,DeliveryPutSizeActivity.class);

            ///快递员mUUID
            String uuid = getIntent().getStringExtra(SystemConfig.KEY_Muuid);
            if (null == uuid)
            {
                Log.i(getClass().getSimpleName(), "mUUID is null, please check");

                return;
            }

            /// 8/12/15 add by michael, 清除输入内容
            edt_expId.setText("");
            edt_tel.setText("");
            // add --

            //传递快件单号和电话号码给下一界面
            intent.putExtra(GOOD_NUM, expId);
            intent.putExtra(USER_TEL, tel);
            intent.putExtra(SystemConfig.KEY_Muuid, uuid);
            startActivity(intent);
        }
    }
}
