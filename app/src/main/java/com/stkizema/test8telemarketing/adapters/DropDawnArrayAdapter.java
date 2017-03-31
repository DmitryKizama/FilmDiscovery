package com.stkizema.test8telemarketing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stkizema.test8telemarketing.R;

import java.util.List;

public class DropDawnArrayAdapter extends ArrayAdapter<String> {

    private List<String> generalList;

    public DropDawnArrayAdapter(Context context) {
        super(context, 0);
    }

    public void updateList(List<String> list) {
        generalList = list;
        clear();
        addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (generalList == null){
            return 0;
        }

        return generalList.size();
    }

    static class ViewHolder {
        TextView tvName;
    }

    @Override
    public String getItem(int position) {
        return generalList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_drop_dawn, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_title_drop_dawn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(getItem(position));

        return convertView;
    }

}
