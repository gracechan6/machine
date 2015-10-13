package com.jinwang.subao.db;

/**
 * Created by Chenss on 2015/10/12.
 */
public class CabinetGrid {

    private int CGId;
    private int CabinetId;
    private int GridId;
    private int Size;
    private int Status;
    private int Uploaded;

    public CabinetGrid(int cabinetId, int gridId, int status, int uploaded) {
        CabinetId = cabinetId;
        GridId = gridId;
        Status = status;
        Uploaded = uploaded;
    }

    public CabinetGrid() {
    }

    public CabinetGrid(int cabinetId, int gridId, int size, int status ,int uploaded) {
        CabinetId = cabinetId;
        GridId = gridId;
        Size = size;
        Status = status;
        Uploaded = uploaded;
    }

    public int getCabinetId() {
        return CabinetId;
    }

    public void setCabinetId(int cabinetId) {
        CabinetId = cabinetId;
    }

    public int getCGId() {
        return CGId;
    }

    public void setCGId(int CGId) {
        this.CGId = CGId;
    }

    public int getGridId() {
        return GridId;
    }

    public void setGridId(int gridId) {
        GridId = gridId;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getUploaded() {
        return Uploaded;
    }

    public void setUploaded(int uploaded) {
        Uploaded = uploaded;
    }
}
