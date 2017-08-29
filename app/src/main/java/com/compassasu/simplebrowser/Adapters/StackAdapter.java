package com.compassasu.simplebrowser.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.compassasu.simplebrowser.R;
import com.compassasu.simplebrowser.Stack_Items;

import java.util.ArrayList;

/**
 * Created by Kagune on 5/23/2017.
 */
public class StackAdapter extends BaseAdapter {
    private ArrayList<Stack_Items> arrayList;
    private LayoutInflater inflater;
    private ViewHolder holder = null;

    public StackAdapter(Context context, ArrayList<Stack_Items> arrayList) {
        this.arrayList = arrayList;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayList.size();
    }

    @Override
    public Stack_Items getItem(int pos) {
        // TODO Auto-generated method stub
        return arrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.display_item, parent, false);
            holder = new ViewHolder();

            holder.text = (TextView) view.findViewById(R.id.textView1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.text.setText(arrayList.get(pos).getName());

        return view;
    }

    public class ViewHolder {
        TextView text;
    }

}
