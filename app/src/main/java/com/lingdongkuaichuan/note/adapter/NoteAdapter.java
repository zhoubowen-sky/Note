package com.lingdongkuaichuan.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.bean.Note;

import java.util.List;

/**
 * Created by 周博文 on 2017/6/1.
 */

public class NoteAdapter extends BaseAdapter {

    private List<Note> data;

    private LayoutInflater layoutInflater;

    private Context context;

    public NoteAdapter(Context context, List<Note> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder{
        public TextView tv_note_item_title;
        public TextView tv_note_item_content;
        public TextView tv_note_item_date;

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
            // 实例化 item 中的组件
            convertView = layoutInflater.inflate(R.layout.note_item_layout,null);
            viewHolder.tv_note_item_title = (TextView) convertView.findViewById(R.id.tv_note_item_title);
            viewHolder.tv_note_item_content = (TextView) convertView.findViewById(R.id.tv_note_item_content);
            viewHolder.tv_note_item_date = (TextView) convertView.findViewById(R.id.tv_note_item_date);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 将 data 数据绑定
        viewHolder.tv_note_item_title.setText(data.get(position).getTittle());
        viewHolder.tv_note_item_content.setText(data.get(position).getContent());
        viewHolder.tv_note_item_date.setText(data.get(position).getDate());


        return convertView;
    }
}
