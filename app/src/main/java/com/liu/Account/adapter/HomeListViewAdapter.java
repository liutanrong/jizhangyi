package com.liu.Account.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.Account.R;
import com.liu.Account.model.HomeListViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 15-10-11.
 * */
public class HomeListViewAdapter extends BaseAdapter {
    private Context context;
    private List<HomeListViewData> datas = new ArrayList<>();
    private ViewHolder holder = null;


    public HomeListViewAdapter(Context context, List<HomeListViewData> datas) {
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
            convertView = View.inflate(context, R.layout.item_home_list,null);
            holder=new ViewHolder();
            holder.unixTime= (TextView) convertView.findViewById(R.id.unixTime);
            holder.remarkInList=(TextView) convertView.findViewById(R.id.remarkInList);
            holder.dateInList=(TextView)convertView.findViewById(R.id.dateInList);
            holder.spendMoneyInList=(TextView)convertView.findViewById(R.id.spendMoneyInList);
            holder.moneyType= (TextView) convertView.findViewById(R.id.money_type);
            holder.creatTime= (TextView) convertView.findViewById(R.id.creatTime);
            holder.tag= (ImageView) convertView.findViewById(R.id.tag);
            holder.tagText= (TextView) convertView.findViewById(R.id.tagText);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeListViewData entity = datas.get(position);
        holder.unixTime.setText(entity.getUnixTime());
        holder.remarkInList.setText(entity.getRemark());
        holder.dateInList.setText(entity.getDate());
        holder.spendMoneyInList.setText(entity.getMoney());
        holder.moneyType.setText(entity.getMoneyType());
        holder.creatTime.setText(entity.getCreatTime());
        holder.tagText.setText(entity.getTag());
        holder.tag.setImageResource(entity.get_tagID());

        return convertView;
    }
    static class ViewHolder {
        TextView unixTime,remarkInList,dateInList,spendMoneyInList,moneyType,creatTime;
        ImageView tag;
        TextView tagText;
    }

}
