package com.lingdongkuaichuan.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lingdongkuaichuan.note.R;
import com.lingdongkuaichuan.note.bean.Note;
import com.lingdongkuaichuan.note.fragment.HomeFragment;
import com.lingdongkuaichuan.note.utils.DateUtil;

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
        public CheckBox cb_note_item_check;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (viewHolder == null){
            viewHolder = new ViewHolder();
            // 实例化 item 中的组件
            convertView = layoutInflater.inflate(R.layout.note_item_layout,null);
            viewHolder.tv_note_item_title   = (TextView) convertView.findViewById(R.id.tv_note_item_title);
            viewHolder.tv_note_item_content = (TextView) convertView.findViewById(R.id.tv_note_item_content);
            viewHolder.tv_note_item_date    = (TextView) convertView.findViewById(R.id.tv_note_item_date);
            viewHolder.cb_note_item_check   = (CheckBox) convertView.findViewById(R.id.cb_note_item_check);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 将 data 数据绑定
        viewHolder.tv_note_item_title.setText(data.get(position).getTittle());
        viewHolder.tv_note_item_content.setText(data.get(position).getContent());
        // 此处将时间戳转换为标准时间格式
        String date_str = DateUtil.dateLineToString(data.get(position).getDate());
        viewHolder.tv_note_item_date.setText(date_str);
        // 默认的选中状态 false
        viewHolder.cb_note_item_check.setChecked(data.get(position).isChecked());

        if (HomeFragment.isShowCheckBox){
            viewHolder.cb_note_item_check.setVisibility(View.VISIBLE);
        }else {
            viewHolder.cb_note_item_check.setVisibility(View.INVISIBLE);
        }

        viewHolder.cb_note_item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    data.get(position).setChecked(true);
                }else {
                    data.get(position).setChecked(false);
                }
            }
        });

        return convertView;
    }
}
