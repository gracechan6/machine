package com.jinwang.subao.asyncHttpClient;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.jinwang.subao.config.SystemConfig;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
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

}
