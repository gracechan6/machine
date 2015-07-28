package com.jinwang.yongbao.device;

public class Device {
	static {
		System.loadLibrary("YongBaoDevice");
	}

	public static native int uartInit();
	public static native int uartDestroy();

	/**
	 * 打开箱格
	 * @param cardID	板子编号
	 * @param doorID	箱格编号
	 * @param retInfo	结果
	 * @return 0:打开成功， -1：失败
	 */
	public static native int openGrid(int cardID, int doorID, int[] retInfo);

	/**
	 * 获取板子地址
	 * @param retInfo	返回结果
	 * @return
	 */
	public static native int getBoardAddress(int[] retInfo);

	/**
	 * 获取箱格门的状态
	 * @param boardID
	 * @param LockID
	 * @param retInfo
	 * @return
	 */
	public static native int getDoorState(int boardID, int LockID, int[] retInfo);

	/**
	 * 获取协议ID
	 * @param retInfo
	 * @return
	 */
	public static native int getProtocalID(int[] retInfo);
}