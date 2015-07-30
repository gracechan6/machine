package com.jinwang.subao.entity;

/**
 * Created by michael on 15/7/30.
 * 箱格锁
 */
public class LockGrid {
    /**
     * 板子编号
     */
    private int boardID;

    /**
     * 箱格编号
     */
    private int gridID;

    /**
     * 箱格状态
     */
    private int gridState;

    /**
     * 箱格大小
     */
    private int gridSize;

    public int getBoardID() {
        return boardID;
    }

    public void setBoardID(int boardID) {
        this.boardID = boardID;
    }

    public int getGridID() {
        return gridID;
    }

    public void setGridID(int gridID) {
        this.gridID = gridID;
    }

    public int getGridState() {
        return gridState;
    }

    public void setGridState(int gridState) {
        this.gridState = gridState;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }
}
