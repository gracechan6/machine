package com.jinwang.subao.asyncHttpClient;

import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.config.SystemConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class SubaoHttpClient{

    String url;
    RequestParams param;

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
                new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        progress_horizontal.setProgress(progress_horizontal.getProgress() + 20);
                        //System.out.println("进入json-start");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progress_horizontal.setProgress(progress_horizontal.getProgress() + 80);
                        //System.out.println(incident);
                        //System.out.println("进入json-onsuccess");
                        switch (incident){
                            case "mUUIDvalidate":
                            case "updateCabStatus":{
                                textView.setText(new JsonObjectShift(response).MUUIDValidate());
                                break;
                            }
                            case "getAllCabinets":
                            case "getMyPackageBytdcode":
                            case "getMyPackageByTelCode":{
                                textView.setText(new JsonObjectShift(response).getAllCabinets());
                                break;
                            }
                            case "PutMyPackage":{
                                textView.setText(new JsonObjectShift(response).PackageUuidVal());
                                break;
                            }
                            case "updateCabStatusUser":{
                                textView.setText(new JsonObjectShift(response).updateCabStatus());
                                break;
                            }


                        }
                        //Log.i("test",response.toString()+"success_g");
                        //System.out.println(response.toString()+"grace success");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        textView.setText("errMsg" + errorMsg);
                        //System.out.println("进入json-onfailure");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        //System.out.println("进入json-onfinish");
                    }
                });
    }

    String[] allowedContentTypes = new String[] { ".*" };
    public void getNewVersion(String filename)
    {
        RequstClient.get(url, new FileAsyncHttpResponseHandler(new File(filename)) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                file.delete();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                //file.getAbsolutePath();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }
        });
    }
    private Boolean result = false;
    public Boolean updateServerVersion() {

        RequstClient.post(
                url,
                param,
                new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (response.getBoolean("result")) result = true;
                            else result = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
        return result;
    }

}
