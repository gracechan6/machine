package com.jinwang.subao.asyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            if(obj.has("success")) str+="success,"+obj.getString("success");

            if(obj.has("errMsg"))
                str+="errMsg,"+obj.getString("errMsg");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return str;
    }

}
