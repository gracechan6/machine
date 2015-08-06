package com.jinwang.subao.asyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2015/7/24.
 */
public class JsonObjectShift {
    private JSONObject obj;
    public JsonObjectShift(JSONObject obj)
    {
        this.obj=obj;
    }

    public String toString() {
        String str="";
        try {
            if(obj.has("success")) str+="success,"+obj.getString("success")+";";
            if(obj.has("returnData"))
            {
                JSONArray array=obj.getJSONArray("returnData");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject oj = array.getJSONObject(i);
                    str+="returnFlag,"+oj.getString("returnFlag")+";";
                    if(oj.has("errMsg"))
                        str+="errMsg,"+oj.getString("errMsg")+";";
                    else {
                        str += "userName," + oj.getString("userName") + ";";
                        str += "userName," + oj.getString("userName") + ";";
                        str += "mUuid," + oj.getString("mUuid") + ";";
                        str += "role," + oj.getString("role") + ";";
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return str;
    }

    public String MUUIDValidate() {
        String str="";
        try {
            if(obj.has("success")) str+="success:"+obj.getString("success");

            if(obj.has("errMsg"))
                str+="errMsg:"+obj.getString("errMsg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return str;
    }

    public String getAllCabinets() {
        String str="";
        try {
            if(obj.has("success")) str+="success:"+obj.getString("success")+";";
            /*if(obj.has("errMsg")){
                String utf="";
                try {
                    utf= URLEncoder.encode(new String(obj.getString("errMsg").getBytes("UTF-8")),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                str+="errMsg:"+utf+";";
            }*/
            if(obj.has("errMsg"))
                str+="errMsg:"+obj.getString("errMsg")+";";
            if(obj.has("returnData"))
            {
                JSONArray array=obj.getJSONArray("returnData");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject oj = array.getJSONObject(i);
                    if(oj.has("boardId")&&oj.getString("boardId").length()==0||oj.getString("boardId").trim().length()==0 ){
                        if(oj.has("cabinetNo")&&oj.getString("cabinetNo").length()==0||oj.getString("cabinetNo").trim().length()==0){
                            str+="cabinetNo:"+oj.getString("cabinetNo")+";";
                            str+="boardId:"+oj.getString("boardId")+";packageEquipment:";
                            if(oj.has("packageEquipment"))
                                str+="packageEquipment"+oj.getString("packageEquipment");
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String PackageUuidVal() {
        String str="";
        try {
            if(obj.has("success")) str+="success:"+obj.getString("success")+";";

            if(obj.has("errMsg"))
                str+="errMsg:"+obj.getString("errMsg")+";";
            if(obj.has("packStage"))
                str+="packStage:"+obj.getString("packStage")+";";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return str;
    }

    public String updateCabStatus() {
        String str="";
        try {
            if(obj.has("success")) str+="success:"+obj.getString("success")+";";

            if(obj.has("errMsg"))
                str+="errMsg:"+obj.getString("errMsg")+";";

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return str;
    }



}
