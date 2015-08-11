package com.jinwang.subao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinwang.subao.R;

/**
 * Created by michael on 15/8/10.
 */
public class NumberKeyboardAdapter extends BaseAdapter {

    private static final String[] items = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "10"};

    private Context mContext;

    public NumberKeyboardAdapter(Context context)
    {
        mContext = context;
    }
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return 12;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {

        return items[position];
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {

            convertView = inflater.inflate(R.layout.number_keyboard_item, null);
        }
        TextView key = (TextView) convertView;//.findViewById(R.id.textView);

        key.setText(getItem(position).toString());

        ///删除键
        if (11 == position)
        {
            convertView = inflater.inflate(R.layout.delete_key_item, null);
       //     key.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.delect_icon, 0);
       //     key.setText("");
       //     key.setCompoundDrawablePadding(0);
        }

        return convertView;
    }
}
