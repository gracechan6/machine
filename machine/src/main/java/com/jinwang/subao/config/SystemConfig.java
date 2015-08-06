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
    public static final String URL_MUUID_VALIDATE = URL_BASE + "/ybpt/web/ybmobile/TerminalDevice_checkTerminalMuuid.action";//接口二十三: 终端验证登录UUID是否存在在线 (其他角色账号验证通用)
    public static final String URL_GET_ALLCABINETS = URL_BASE + "/ybpt/web/ybmobile/ExpressPerson_updateStatus.action";//接口二十四: 获取快递员所属快件柜列表
    public static final String URL_GET_USERCABINET = URL_BASE + "/ybpt/web/ybmobile/MobilePackageInfo_takePackage.action";//接口二十八: 普通用户取件
    public static final String URL_PUT_USERCABINET = URL_BASE + "/ybpt/web/ybmobile/PackageStatus_checkWaitSend.action";//接口二十七: 根据寄件码获取寄件信息
    public static final String URL_PUT_DELIVERYCABINET = URL_BASE + "/ybpt/web/ybmobile/ExpressPerson_courierDelivery.action";//接口二十五: 快递员投递快件
    public static final String URL_PUT_USERCABSIZE = URL_BASE + "/ybpt/web/ybmobile/MobilePackageInfo_setPackageState.action";//接口二十六—->接口十二：快递寄件选完箱子点确定(也叫普通用户投件接口)
    public static final String URL_UPDATE_TERMINALVERSION = URL_BASE + "/ybpt/web/ybmobile/EquipmentAndrod_updateSysVersion.action";//接口二十九: 更新设备终端版本

    //================================

    //=================Constant config
    public static final String KEY_DEVICE_ID = "DEY_DEVICE_ID";
    public static final String KEY_DEVICE_PASSWORD = "KEY_DEVICE_PASSWORD";

    public static final String KEY_DEVICE_MUUID = "KEY_DEVICE_MUUID";
    //================================

    //=================System Role config
    public static final String SYSTEM_ROLE = "Group0006";
    public static final String SYSTEM_MANAGER_MUUID="root";

    //================================

    //=================Data encoding
    public static final String SERVER_CHAR_SET = "GBK";
    //================================

    //=================URL Parameters
    public static final String KEY_Muuid = "Muuid";
    public static final String KEY_TerminalMuuid= "TerminalMuuid";
    public static final String KEY_EquipmentNo = "EquipmentNo";
    public static final String KEY_BoardId = "BoardId";
    public static final String KEY_CabinetNo = "CabinetNo";
    public static final String KEY_PackageNumber = "PackageNumber";
    public static final String KEY_ReceivePhone = "ReceivePhone";
    public static final String KEY_BoxUuid = "BoxUuid";
    public static final String KEY_BoxType = "BoxType";
    public static final String KEY_SysVersion= "SysVersion";
    public static final String KEY_EquipmentMuuid = "EquipmentMuuid";

    public static  String VALUE_MuuidValue=null ;

    //public static final String KEY_ = "";

    //================================

    //=================System Version
    public static String SYSTEM_VERSION ;

    //================================
}
