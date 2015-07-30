package com.jinwang.subao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;
import android.widget.Toast;

import com.jinwang.subao.config.PushConfig;
import com.jinwang.subao.service.OnlineService;
import com.jinwang.subao.util.Util;

import org.ddpush.im.util.StringUtil;
import org.ddpush.im.v1.client.appserver.Pusher;


public class TickAlarmReceiver extends BroadcastReceiver {

	WakeLock wakeLock;
	
	public TickAlarmReceiver() {
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(!Util.hasNetwork(context)){
			return;
		}

		// 推送服务端能用消息，确保连接，服务端处理该请求
		try {
			Pusher pusher = new Pusher(PushConfig.SERVER_IP,PushConfig.PUSH_PORT, 1000*5);
			boolean result = pusher.push0x10Message(StringUtil.md5Byte(PushConfig.SERVER_NAME));

			if (!result)
			{
				Toast.makeText(context, "Connect server error!", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra("CMD", "TICK");
		context.startService(startSrv);
	}

}
