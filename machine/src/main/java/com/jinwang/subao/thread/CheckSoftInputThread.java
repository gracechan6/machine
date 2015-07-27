package com.jinwang.subao.thread;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by michael on 15/7/24.
 */
public class CheckSoftInputThread extends Thread
{
    private Context mContext;

    public CheckSoftInputThread(Context context)
    {
        this.mContext = context;
    }

    @Override
    public void run()
    {
        InputMethodManager m=(InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        while (true)
        if (m.isActive())
        {
            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
