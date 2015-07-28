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
			String uuid = StringUtil.md5(PushConfig.SERVER_NAME);
			System.out.println(uuid);
			new Thread(new send0x10Task(context, PushConfig.SERVER_IP, PushConfig.PUSH_PORT, StringUtil.hexStringToByteArray(uuid))).start();
		} catch (Exception e) {
			e.printStackTrace();
		}


		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra("CMD", "TICK");
		context.startService(startSrv);
	}

	class send0x10Task implements Runnable{
		private Context context;
		private String serverIp;
		private int port;
		private byte[] uuid;

		public send0x10Task(Context context, String serverIp, int port, byte[] uuid){
			this.context = context;
			this.serverIp = serverIp;
			this.port = port;
			this.uuid = uuid;
		}

		public void run(){
			//调用服务端同步在线接口，告诉服务端在线状态
		}
	}

}
