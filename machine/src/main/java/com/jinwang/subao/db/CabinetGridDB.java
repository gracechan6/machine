package com.jinwang.subao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jinwang.subao.SubaoApplication;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.util.SharedPreferenceUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chenss on 2015/10/12.
 */
public class CabinetGridDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "CabinetGrid";
    
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static CabinetGridDB cabinetGridDB;

    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */
    private CabinetGridDB() {
        CabinetGridOpenHelper dbHelper = new CabinetGridOpenHelper(SubaoApplication.getContext(),
                DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CabinetGridDB的实例。
     */
    public synchronized static CabinetGridDB getInstance() {
        if (cabinetGridDB == null) {
            cabinetGridDB = new CabinetGridDB();
        }
        return cabinetGridDB;
    }

    /**
     * 向货柜表中添加初始化数据
     * @param cabinetGrids
     */
    public void saveCabintGrid(List<CabinetGrid> cabinetGrids){

        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < cabinetGrids.size() ; i++) {
            values=new ContentValues();
            values.put("CabinetId", cabinetGrids.get(i).getCabinetId());
            values.put("GridId",cabinetGrids.get(i).getGridId());
            values.put("Size",cabinetGrids.get(i).getSize());
            values.put("Status",cabinetGrids.get(i).getStatus());
            values.put("Uploaded",cabinetGrids.get(i).getUploaded());
            db.insert("tbCabinetGrid", null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 向货柜表中更新数据--箱格状态(Status~Uploaded)
     * @param cabinetGrids
     */
    public void updateCG(List<CabinetGrid> cabinetGrids){

        ContentValues values=null;

        db.beginTransaction();
        for (int i = 0; i < cabinetGrids.size() ; i++) {
            values=new ContentValues();
            int cabinetId,gridId;
            cabinetId=cabinetGrids.get(i).getCabinetId();
            gridId=cabinetGrids.get(i).getGridId();
            values.put("Status", cabinetGrids.get(i).getStatus());
            values.put("Uploaded", cabinetGrids.get(i).getUploaded());
            db.update("tbCabinetGrid", values, "CabinetId=? and GridId = ?",
                    new String[]{String.valueOf(cabinetId), String.valueOf(gridId)});
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 判断是否存在本地数据未成功更新至服务器
     * 存在则直接返回数据，否则返回null
     * @return
     */
    public List<CabinetGrid> FailUpload(){
        List<CabinetGrid> cabinetGrids= new ArrayList<>();
        Cursor cursor=db.query("tbCabinetGrid",null,"Uploaded=?",
                new String[]{String.valueOf(0)},null,null,null);

        if (cursor.moveToFirst()){
            do {
                CabinetGrid cabinetGrid = new CabinetGrid(cursor.getInt(cursor.getColumnIndex("CabinetId")),
                        cursor.getInt(cursor.getColumnIndex("GridId")),
                        cursor.getInt(cursor.getColumnIndex("Size")),
                        cursor.getInt(cursor.getColumnIndex("Status")),
                        cursor.getInt(cursor.getColumnIndex("Uploaded")));
                cabinetGrids.add(cabinetGrid);
            }while (cursor.moveToNext());
        }
        return cabinetGrids;
    }

    /**
     * 将本地数据同步至服务器
     * @param cabinetGrids
     * add by chenss
     */
    public void upLoadLocalData(AsyncHttpClient client,RequestParams params,final List<CabinetGrid> cabinetGrids) {
        client.post(SystemConfig.URL_SYNC_SERVERDATA,new RequestParams(params),new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                //本地记录更新 Uploaded改为1

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);


            }
        });
    }

    /**
     * 从服务器端获取数据同步到本地数据库
     * @param client
     * @param params
     * add by chenss
     */
    public void syncLocalDBData(AsyncHttpClient client, RequestParams params) {

        client.get(SystemConfig.URL_SYNC_LOCALDATA, params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i(getClass().getSimpleName(), "Response: " + response.toString());

                try {
                    if (response.getBoolean("success")) {


                        //同步数据到本地数据库
                        List<CabinetGrid> cabinetGrids = new ArrayList<>();


                        cabinetGridDB.saveCabintGrid(cabinetGrids);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Log.i(getClass().getSimpleName(), "Response: " + statusCode);
            }
        });
    }
}
