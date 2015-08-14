package com.jinwang.subao.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.R;

/**
 * Created by jinwang on 15/8/14.
 *
 */
public class ToastUtil {
    public static final int LARGE_TEXT_SIZE = 40;
    public static final int MID_TEXT_SIZE = 30;
    public static final int SMALL_TEXT_SIZE = 15;

    public static Toast showLargeToast(Context context, String msg, int length)
    {
        Toast toast = Toast.makeText(context, msg, length);
        TextView msgView = new TextView(context);

        msgView.setBackgroundColor(Color.parseColor("#ff303030"));

        msgView.setTextColor(Color.WHITE);
        msgView.setText(msg);
        msgView.setTextSize(LARGE_TEXT_SIZE);
        toast.setView(msgView);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        return toast;
    }
}
