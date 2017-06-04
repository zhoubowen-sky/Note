package com.lingdongkuaichuan.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.bean.Folder;
import com.lingdongkuaichuan.note.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 周博文 on 2017/6/4.
 */

public class FolderAdapter extends BaseAdapter {

    private List<Folder> data = new ArrayList<Folder>();

    private Context context;

    private LayoutInflater layoutInflater;

    public FolderAdapter(List<Folder> data, Context context){
        this.data = data;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder{
        public TextView tv_folder_item_name;
        public TextView tv_folder_item_date;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (viewHolder == null){
            viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.folder_item_layout, null);
            viewHolder.tv_folder_item_name = (TextView) convertView.findViewById(R.id.tv_folder_item_name);
            viewHolder.tv_folder_item_date = (TextView) convertView.findViewById(R.id.tv_folder_item_date);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 绑定数据
        viewHolder.tv_folder_item_name.setText(data.get(position).getName());
        // 此处将时间戳转换为标准时间格式
        String date_str = DateUtil.dateLineToString(data.get(position).getDate());
        viewHolder.tv_folder_item_date.setText(date_str);


        return convertView;
    }
}
