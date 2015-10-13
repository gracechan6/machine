package com.jinwang.subao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chenss on 2015/10/9.
 */
public class CabinetGridOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_BOOK_CABINETGRID = "create table tbCabinetGrid("
            + "CGId integer primary key autoincrement ,"
            + "CabinetId integer ,"
            + "GridId integer ,"
            + "Size integer ,"
            + "Status integer ,"
            + "Uploaded integer)";

    private Context context;

    public CabinetGridOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_CABINETGRID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
