package com.jinwang.subao.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.jinwang.subao.R;
import com.jinwang.subao.entity.LockGrid;
import com.jinwang.subao.util.DeviceUtil;
import com.jinwang.yongbao.device.Device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * created by michael, 15/30/7
 *
 * 管理员界面，主要功能：配置箱格
 */
public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        GridView gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(adapter);

        getLockStateTask.execute(2);
    }

    /**
     * 所有箱格列表
     */
    private List<LockGrid> gridList;

    /**
     * 箱格列表适配器
     */
    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            if (null != gridList)
            {
                return gridList.size();
            }

            return 0;
        }

        @Override
        public Object getItem(int position) {
            return gridList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView)
            {
                convertView = getLayoutInflater().inflate(R.layout.grid_table_row, null);
            }
            final LockGrid grid = (LockGrid) getItem(position);

            //如果箱格状态为不可用，设置不可操作
            if (DeviceUtil.GRID_STATUS_UNKOWN == grid.getGridState())
            {
                convertView.findViewById(R.id.gridItem).setBackgroundColor(Color.GRAY);
            }
            else
            {
                convertView.findViewById(R.id.gridItem).setBackgroundColor(Color.BLUE);
            }

            //开箱按钮
            Button open = (Button) convertView.findViewById(R.id.open);
            open.setText(grid.getBoardID()+ "_" + grid.getGridID());
            open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Device.openGrid(grid.getBoardID(), grid.getGridID(), new int[5]);
                }
            });

            //箱格大小选择框，大
            final CheckBox size2 = (CheckBox) convertView.findViewById(R.id.checkboxBig);

            //箱格大小选择框，大
            final CheckBox size1 = (CheckBox) convertView.findViewById(R.id.checkboxMid);

            //箱格大小选择框，大
            final CheckBox size0 = (CheckBox) convertView.findViewById(R.id.checkBoxSmall);

            size2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        if (size0.isChecked()) size0.setChecked(false);
                        if (size1.isChecked()) size1.setChecked(false);
                    }
                }
            });

            size1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        if (size0.isChecked()) size0.setChecked(false);
                        if (size2.isChecked()) size2.setChecked(false);
                    }
                }
            });

            size0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        if (size1.isChecked()) size1.setChecked(false);
                        if (size2.isChecked()) size2.setChecked(false);
                    }
                }
            });

            return convertView;
        }
    };

    /**
     * 获取锁状态异步任务
     */
    private AsyncTask<Integer, Integer, Map<String, Integer>> getLockStateTask = new AsyncTask<Integer, Integer, Map<String, Integer>>() {
        @Override
        protected Map<String, Integer> doInBackground(Integer... params) {
            return DeviceUtil.getAllGridState(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressBar progressBar = new ProgressBar(ManagerActivity.this);

            /*测试屏幕属性*/
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;//宽度
//        int height = dm.heightPixels ;//高度
//
//        Log.i(getClass().getSimpleName(), "Height: " + height + "Width: " + width);
            progressBar.setMinimumHeight();
        }

        @Override
        protected void onPostExecute(Map<String, Integer> stringIntegerMap) {
            super.onPostExecute(stringIntegerMap);

            gridList = new ArrayList<>();
            //设置
            for (String key : stringIntegerMap.keySet())
            {
                LockGrid grid = new LockGrid();
                String[] ss = key.split("_");
                grid.setBoardID(Integer.valueOf(ss[0]));
                grid.setGridID(Integer.valueOf(ss[1]));
                grid.setGridState(stringIntegerMap.get(key));

                gridList.add(grid);
            }

            //按板址址排序
            Collections.sort(gridList, new Comparator<LockGrid>() {
                @Override
                public int compare(LockGrid lhs, LockGrid rhs) {
                    if (lhs.getBoardID() > rhs.getBoardID() ||
                            lhs.getBoardID() == rhs.getBoardID() && lhs.getGridID() > rhs.getGridID())
                    {
                        return 1;
                    }
                    if (lhs.getBoardID() < rhs.getBoardID() ||
                            lhs.getBoardID() == rhs.getBoardID() && lhs.getGridID() < rhs.getGridID())
                    {
                        return -1;
                    }

                    return 0;
                }
            });

            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
