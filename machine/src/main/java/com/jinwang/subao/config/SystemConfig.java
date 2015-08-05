package com.jinwang.subao.config;

/**
 * Created by michael on 15/7/30.
 */
public class SystemConfig {

    //=======================URL config
    public static final String SERVER_IP = "192.168.0.50";
    public static final String SERVER_PORT = "8080";

    public static final String URL_BASE = "http://" + SERVER_IP + ":" + SERVER_PORT;


    public static final String URL_LOGIN = URL_BASE + "/ybpt/web/ybmobile/MobileLogin_Login.action";
    public static final String URL_GET_CLIENT_ACOUNT = URL_BASE + "/ybpt/web/ybmobile/TerminalDevice_produceTerminalNo.action";
    public static final String URL_REGISTER = URL_BASE + "/ybpt/web/ybmobile/MobileReg_Register.action";
    public static final String URL_MUUID_VALIDATE = URL_BASE + "/ybpt/web/ybmobile/TerminalDevice_checkTerminalMuuid.action";
    public static final String URL_GET_ALLCABINETS = URL_BASE + "/ybpt/web/ybmobile/ExpressPerson_updateStatus.action";
    public static final String URL_GET_USERCABINET = URL_BASE + "/ybpt/web/ybmobile/MobilePackageInfo_takePackage.action";
    public static final String URL_PUT_USERCABINET = URL_BASE + "/ybpt/web/ybmobile/PackageStatus_checkWaitSend.action";

    //================================

    //=================Constant config
    public static final String KEY_DEVICE_ID = "DEY_DEVICE_ID";
    public static final String KEY_DEVICE_PASSWORD = "KEY_DEVICE_PASSWORD";

    public static final String KEY_DEVICE_MUUID = "KEY_DEVICE_MUUID";
    //================================

    //=================System Role config
    public static final String SYSTEM_ROLE = "Group0006";
    //================================

    //=================Data encoding
    public static final String SERVER_CHAR_SET = "GBK";
    //================================
}
