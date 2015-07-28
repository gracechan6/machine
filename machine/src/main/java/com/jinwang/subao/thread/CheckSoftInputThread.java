package com.jinwang.subao.thread;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by michael on 15/7/24.
 */
public class CheckSoftInputThread extends Thread
{
    private Activity mContext;

    public CheckSoftInputThread(Activity context)
    {
        this.mContext = context;
    }

    @Override
    public void run()
    {
        InputMethodManager m=(InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        while (true)
        {
            try {
                IBinder token = mContext.getCurrentFocus().getWindowToken();

                if (null != token)
                {
                    m.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
                }

                sleep(300);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
