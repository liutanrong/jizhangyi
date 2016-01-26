package com.liu.Account.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.Account.R;
import com.liu.Account.model.AddBillTagData;
import com.liu.Account.model.HomeListViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 15-10-11.
 * */
public class AddBillTagAdapter extends BaseAdapter {
    private Context context;
    private List<AddBillTagData> datas = new ArrayList<>();
    private ViewHolder holder = null;


    public AddBillTagAdapter(Context context, List<AddBillTagData> datas) {
        this.context = context;
        this.datas=datas;
    }

    @Override
    public int getCount() {
        if (datas == null)
            return 0;
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_add_bill_tag_list,null);
            holder=new ViewHolder();
            holder.tag= (ImageView) convertView.findViewById(R.id.item_add_bill_tag_list_image);
            holder.tagText= (TextView) convertView.findViewById(R.id.item_add_bill_tag_list_text);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        AddBillTagData entity = datas.get(position);
        holder.tag.setImageResource(entity.get_id());
        holder.tagText.setText(entity.getText());
        return convertView;
    }
    static class ViewHolder {
        ImageView tag;
        TextView tagText;
    }

}
