package com.jinwang.subao;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/**
 * Created by Administrator on 2015/8/6.
 */


public class AutoInstall {
    private static String mUrl;
    private static Context mContext;

    /**
     * 外部传进来的url以便定位需要安装的APK
     *
     * @param url
     */
    public static void setUrl(String url) {
        mUrl = url;
    }

    /**
     * 安装
     *
     * @param context
     *            接收外部传进来的context
     */
    public static void install(Context context) {
        mContext = context;
        // 核心是下面几句代码
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//安装完成后提示完成 ，打开
        intent.setDataAndType(Uri.fromFile(new File(mUrl)),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());//安装完毕后，点打开可以打开新版本应用
    }


    /*方法二
    *//**
     * 安装下载完成的APK
     * @param savedFile
     *//*
    private void installAPK(Context context,File savedFile) {
        //调用系统的安装方法
        Intent intent=new Intent();
        intent.setAction(intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(savedFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
        //finish();
    }
    */

}