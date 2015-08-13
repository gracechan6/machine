package com.jinwang.subao.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.jinwang.subao.R;
import com.jinwang.subao.config.SystemConfig;
import com.jinwang.subao.entity.LockGrid;
import com.jinwang.subao.util.DeviceUtil;
import com.jinwang.subao.util.SharedPreferenceUtil;
import com.jinwang.yongbao.device.Device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by michael, 15/30/7
 * <p/>
 * 管理员界面，主要功能：配置箱格
 */
public class ManagerActivity extends AppCompatActivity {
    //根布局
    private ViewGroup rootView;
    private View progressBar;

    private int boardCount = 0;

    //板子数量选择器
    private Spinner boardSpinner;

    ///查看箱格状态任务
    private AsyncTask scanTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        rootView = (ViewGroup) findViewById(R.id.container);

        boardSpinner = (Spinner) findViewById(R.id.boardCount);
        boardSpinner.setAdapter(new SimpleAdapter(this, getBoardCountData(), R.layout.textview, new String[]{"num"}, new int[]{R.id.textView}));

        boardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(getClass().getSimpleName(), "On item selected: " + position);

                boardCount = position;

                if (0 == position)
                {
                    return;
                }

                scanTask = new AsyncTask<Integer, Integer, Map<String, Integer>>() {
                    @Override
                    protected Map<String, Integer> doInBackground(Integer... params) {
                        return DeviceUtil.getAllGridState(params[0]);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        progressBar = getLayoutInflater().inflate(R.layout.progress_view, null);

                        //        rootView.addView(progressBar);
                        rootView.addView(progressBar, 900, 600);
                    }

                    @Override
                    protected void onPostExecute(Map<String, Integer> stringIntegerMap) {
                        super.onPostExecute(stringIntegerMap);

                        gridList = new ArrayList<>();
                        //设置
                        for (String key : stringIntegerMap.keySet()) {
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
                                        lhs.getBoardID() == rhs.getBoardID() && lhs.getGridID() > rhs.getGridID()) {
                                    return 1;
                                }
                                if (lhs.getBoardID() < rhs.getBoardID() ||
                                        lhs.getBoardID() == rhs.getBoardID() && lhs.getGridID() < rhs.getGridID()) {
                                    return -1;
                                }

                                return 0;
                            }
                        });

                        adapter.notifyDataSetChanged();


                        rootView.removeView(progressBar);
                    }
                }.execute(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(getClass().getSimpleName(), "On nothing selected");
            }
        });
        GridView gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(adapter);

//        getLockStateTask.execute(1);
    }

    /**
     * 板子个数选择数据
     * 目前最多10块板子
     *
     * @return
     */
    private List<Map<String, Object>> getBoardCountData()
    {
        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> item = new HashMap<>();
        item.put("num", "请选择板子数量");
        result.add(item);

        for (int i = 1; i <= 10; i++)
        {
            item = new HashMap<>();
            item.put("num", i + "块板子");

            result.add(item);
        }

        return result;
    }



    @Override
    protected void onStart() {
        super.onStart();

        //如果板子数量为0，提示选择板子数
        if (0 == boardCount)
        {
            Toast.makeText(this, "请选择板子数量", Toast.LENGTH_LONG).show();
        }
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
            if (null != gridList) {
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
            if (null == convertView) {
                convertView = getLayoutInflater().inflate(R.layout.grid_table_row, null);
            }
            final LockGrid grid = (LockGrid) getItem(position);

            //如果箱格状态为不可用，设置不可操作
            if (DeviceUtil.GRID_STATUS_UNKOWN == grid.getGridState()) {
                convertView.findViewById(R.id.gridItem).setBackgroundColor(Color.GRAY);
            } else {
                convertView.findViewById(R.id.gridItem).setBackgroundColor(Color.BLUE);
            }

            //开箱按钮
            Button open = (Button) convertView.findViewById(R.id.open);
            open.setText(grid.getBoardID() + "_" + grid.getGridID());
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
                    if (isChecked) {
                        if (size0.isChecked()) size0.setChecked(false);
                        if (size1.isChecked()) size1.setChecked(false);

                        grid.setGridSize(DeviceUtil.GRID_SIZE_LARGE);
                    }
                }
            });

            size1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (size0.isChecked()) size0.setChecked(false);
                        if (size2.isChecked()) size2.setChecked(false);

                        grid.setGridSize(DeviceUtil.GRID_SIZE_MID);
                    }
                }
            });

            size0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (size1.isChecked()) size1.setChecked(false);
                        if (size2.isChecked()) size2.setChecked(false);

                        grid.setGridSize(DeviceUtil.GRID_SIZE_SMALL);
                    }
                }
            });

            return convertView;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

        if (null != scanTask && !scanTask.isCancelled())
        {
            scanTask.cancel(true);
        }
    }

    /**
     * 提交箱格配置
     * @param view
     */
    public void commitConfig(View view)
    {
        for (LockGrid grid : gridList)
        {
            Log.i(getClass().getSimpleName(), "Grid: " + grid.getBoardID()
                    + "_" + grid.getGridID() + " Size: " + grid.getGridSize());

            //更新本地
            DeviceUtil.setGridSize(this, grid.getBoardID(), grid.getGridID(), grid.getGridSize());
            DeviceUtil.updateGridState(this, grid.getBoardID(), grid.getGridID(), grid.getGridState());
        }

            //如果板子数量为0，提示选择板子数
            if (0 == boardCount)
            {
                Toast.makeText(this, "请选择板子数量", Toast.LENGTH_LONG).show();
            }
        else
            {
                SharedPreferenceUtil.saveData(this, SystemConfig.KEY_BOARD_COUNT, boardCount);
            }

        finish();
    }

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
