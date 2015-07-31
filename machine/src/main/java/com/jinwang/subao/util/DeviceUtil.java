package com.jinwang.subao.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jinwang.yongbao.device.Device;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 15/7/28.
 */
public class DeviceUtil {

    /**
     * 箱格状态未知，不能使用
     */
    public static final int GRID_STATUS_UNKOWN = -1;

    /**
     * 箱格状态，可使用
     */
    public static final int GRID_STATUS_USEABLE = 0;

    /**
     * 箱格状态，已使用
     */
    public static final int GRID_STATUS_USED = 1;

    /**
     * 箱格大小, 大
     */
    public static final int GRID_SIZE_LARGE = 2;

    /**
     * 箱格大小, 中
     */
    public static final int GRID_SIZE_MID = 1;

    /**
     * 箱格大小, 小
     */
    public static final int GRID_SIZE_SMALL = 0;

    /**
     * 设备箱格编号状态Sharedpreference 名称
     */
    public static final String DEVICE_SP_NAME = "DEVICE_SP_NAME";

    /**
     * 每一块板子的箱格数量
     */
    public static final int PER_BOARD_GRID_NUM = 24;

    /**
     * 更新箱格使用状态\n
     * 箱格编号按 板子号_箱格号 组成，获取时再进行分割\n
     * 如2号板子， 3号箱子， 2_3
     *
     * @param context   应用上下文
     * @param boardNum  板子编号
     * @param gridNum   箱格编号
     * @param status    状态  -1不能使用，0可用， 1已经使用
     */
    public static void updateGridState(Context context, int boardNum, int gridNum, int status)
    {
        SharedPreferences sp = context.getSharedPreferences(DEVICE_SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //箱格编号按 板子号_箱格号 组成，获取时再进行分割
        String gridName = boardNum + "_" + gridNum;
        editor.putInt(gridName, status);

        editor.apply();
    }

    /**
     * 获取箱格的状态
     *
     * @param context   应用上下文
     * @param boardNum  板子编号
     * @param gridNum   箱格编号
     *
     * @return  箱格的状态, 默认返回-1， 不可用
     */
    public static int getGridState(Context context, int boardNum, int gridNum)
    {
        SharedPreferences sp = context.getSharedPreferences(DEVICE_SP_NAME, Context.MODE_PRIVATE);

        String gridName = boardNum + "_" + gridNum;

        return sp.getInt(gridName, -1);
    }

    /**
     * 设置箱格的大小
     * 箱格编号按 板子号_箱格号_size 组成，获取时再进行分割\n
     * 如2号板子， 3号箱子， 2_3_size
     *
     * @param context   应用上下文
     * @param boardNum  板子编号
     * @param gridNum   箱格编号
     * @param size      大小  0：小， 1：中， 2：大
     */
    public static void setGridSize(Context context, int boardNum, int gridNum, int size)
    {
        SharedPreferences sp = context.getSharedPreferences(DEVICE_SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //箱格编号按 板子号_箱格号 组成，获取时再进行分割
        String gridName = boardNum + "_" + gridNum + "size";
        editor.putInt(gridName, size);

        editor.apply();
    }

    /**
     * 获取箱格的大小
     *
     * @param context   应用上下文
     * @param boardNum  板子编号
     * @param gridNum   箱格编号
     *
     * @return  箱格的大小， 默认返回-1，未知
     */
    public static int getGridSize(Context context, int boardNum, int gridNum)
    {
        SharedPreferences sp = context.getSharedPreferences(DEVICE_SP_NAME, Context.MODE_PRIVATE);

        String gridName = boardNum + "_" + gridNum + "size";

        return sp.getInt(gridName, -1);
    }

    /**
     * 获取大的未使用的箱格列表
     *
     * @param context   应用上下文
     * @return  <板子号， 箱格编号>
     *
     * @throws Exception    未知箱格大小
     */
    public static Map<Integer, Integer> getLargeUnusedGridsList(Context context) throws Exception
    {
        return getUnusedGridList(context, GRID_SIZE_LARGE);
    }

    /**
     * 获取中的未使用的箱格列表
     *
     * @param context   应用上下文
     * @return  <板子号， 箱格编号>
     *
     * @throws Exception    未知箱格大小
     */
    public static Map<Integer, Integer> getMidUnusedGridsList(Context context) throws Exception
    {
        return getUnusedGridList(context, GRID_SIZE_MID);
    }

    /**
     * 获取小的未使用的箱格列表
     *
     * @param context   应用上下文
     * @return  <板子号， 箱格编号>
     *
     * @throws Exception    未知箱格大小
     */
    public static Map<Integer, Integer> getSmallUnusedGridsList(Context context) throws Exception
    {
        return getUnusedGridList(context, GRID_SIZE_SMALL);
    }

    /**
     * 获取未使用的箱格列表
     *
     * @param context   应用上下文
     * @param gridSize  箱格大小
     * @return  未使用箱格列表
     *
     * @throws Exception    未知箱格大小
     */
    public static Map<Integer, Integer> getUnusedGridList(Context context, int gridSize) throws Exception
    {
        if (GRID_SIZE_LARGE != gridSize
                || GRID_SIZE_MID != gridSize
                || GRID_SIZE_SMALL != gridSize)
        {
            throw new Exception("Unknown Grid Size");
        }

        Map<Integer, Integer> result = new HashMap<>();
        //根据配置板子的多少，单个获取所有可用大箱格编号

        /**
         * 板子的配置信息在配置中读取， 这里只做测试
         */
        int boardCount = 2;

        for (int board = 1; board <= boardCount; board++)
        {
            for (int grid = 1; grid <= PER_BOARD_GRID_NUM; grid++)
            {
                //不是大箱格，不计算
                if (gridSize != getGridSize(context, board, grid))
                {
                    continue;
                }

                int gridStatus = getGridState(context, board, grid);

                //首先判断该锁是否可用
                if (GRID_STATUS_UNKOWN == gridStatus || GRID_STATUS_USED == gridStatus)
                {
                    continue;
                }

                int[] ret = new int[7];
                Device.getDoorState(board, grid, ret);

                //如果箱格已经打开，可能出现问题
                if (0 ==ret[3]) {
                    result.put(board, grid);
                }
            }
        }

        return result;
    }

    /**
     * 获取所胡箱格的状态
     *
     * @param boardCount 板子总数
     *
     * @return  板子_箱号，状态
     */
    public static Map<String, Integer> getAllGridState(int boardCount)
    {
        Map<String, Integer> result = new HashMap<>();

        //首先打开所有可用箱格
//        for (int board = 1; board <= boardCount; board++)
//        {
//            for (int grid = 1; grid <= PER_BOARD_GRID_NUM; grid++)
//            {
//                Log.i("DeviceUtil", "Open grid: " + board + "_" + grid);
//                Device.openGrid(board, grid, new int[5]);
//            }
//        }

        //打开完成后，获取锁的状态如果还为“关”，则锁不能使用
        for (int board = 1; board <= boardCount; board++)
        {
            for (int grid = 1; grid <= PER_BOARD_GRID_NUM; grid++)
            {
                String key = board + "_" + grid;

                int[] rets = new int[5];

                Device.getDoorState(board, grid, rets);
                //可用，锁状态为打开
                if (0 != rets[3])
                {
                    result.put(key, GRID_STATUS_USEABLE);
                }
                //锁未连接
                else
                {
                    result.put(key, GRID_STATUS_UNKOWN);
                }

                Log.i("DeviceUtil", "Grid state: " + board + "_" + grid + " state: " + result.get(key));
            }
        }

        return result;
    }
}
