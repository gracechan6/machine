package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BroadcastActivity extends Activity {

    private Button send = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        send = (Button)findViewById(R.id.sned);
        send.setOnClickListener(new BroadcastListener());


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_EDIT);
        BroadcastActivity.this.sendBroadcast(intent);
    }

    class BroadcastListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            System.out.println("------------");
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_EDIT);
            BroadcastActivity.this.sendBroadcast(intent);
        }

    }

}

