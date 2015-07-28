package com.jinwang.subao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jinwang.subao.service.OnlineService;
import com.jinwang.subao.util.Util;

/**
 * 重新设置系统
 */
public class ConnectivityAlarmReceiver extends BroadcastReceiver {

	public ConnectivityAlarmReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if(!Util.hasNetwork(context)){
			return;
		}
		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra("CMD", "RESET");
		context.startService(startSrv);
	}

}
