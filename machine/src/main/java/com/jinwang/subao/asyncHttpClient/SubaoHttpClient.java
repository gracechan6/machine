package com.jinwang.subao.asyncHttpClient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jinwang.subao.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


public class SubaoHttpClient{

    String url;
    RequestParams param;

    public SubaoHttpClient() {
    }

    public SubaoHttpClient(String url,RequestParams param) {
        this.url=url;
        this.param=param;
    }
    public SubaoHttpClient(String url) {
        this.url=url;
    }

    public void connect(final TextView textView,final ProgressBar progress_horizontal, final String errorMsg, final String incident) {

        RequstClient.post(
                url,
                param,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        progress_horizontal.setProgress(progress_horizontal.getProgress() + 20);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        JsonObjectShift jsonObjectShift = new JsonObjectShift(response);
                        progress_horizontal.setProgress(progress_horizontal.getProgress() + 80);
                        System.out.println(incident);
                        switch (incident){
                            case "mUUIDvalidate":{
                                textView.setText(new JsonObjectShift(response).MUUIDValidate());
                                break;
                            }
                        }

                        //System.out.println(response.toString()+"grace success");
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject errorResponse) {
                        super.onFailure(e, errorResponse);
                        textView.setText("errMsg" + errorMsg);
                        return;
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

}
