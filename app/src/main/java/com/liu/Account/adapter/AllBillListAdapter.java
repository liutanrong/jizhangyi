package com.liu.Account.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.fragment.AllBillFragment;
import com.liu.Account.model.AllBillListGroupData;
import com.liu.Account.model.HomeListViewData;

import java.util.List;

/**
 *
 * Created by deonte on 15-11-23.
 */
public class AllBillListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater mChildInflater;  //用于加载listitem的布局xml
    private LayoutInflater mGroupInflater;  //用于加载group的布局xml
    private List<AllBillListGroupData> groups;
    private ViewHolderChild holderChild =null;
    private ViewHolderGroup holderGroup = null;
    private ViewHolderChild0 holderChild0 = null;
    private ViewHolderChild1 holderChild1 = null;
    private AllBillFragment accountAnalysis=new AllBillFragment();

    public AllBillListAdapter(Context c, List<AllBillListGroupData> g){
        this.context=c;
        this.groups=g;
    }
    @Override
    public int getGroupCount() {
        if (groups==null)
            return 0;
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getChild().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).child.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_fragment_allbill_group,null);
            holderGroup =new ViewHolderGroup();
            holderGroup.moneyType= (TextView) convertView.findViewById(R.id.money_type);
            holderGroup.totalMoney=(TextView)convertView.findViewById(R.id.total_money);
            holderGroup.allTime= (TextView) convertView.findViewById(R.id.allTime);
            convertView.setTag(holderGroup);
        }else {
            holderGroup = (ViewHolderGroup) convertView.getTag();
        }
        AllBillListGroupData entity=this.groups.get(groupPosition);
        if (entity!=null){
            holderGroup.moneyType.setText(entity.getMoneyType());
            holderGroup.totalMoney.setText(entity.getTotalMoney());
            holderGroup.allTime.setText(entity.getAllTime());
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        switch (childPosition){
            case 0:
                convertView=View.inflate(context, R.layout.item_fragment_allbill_count,null);
                holderChild0=new ViewHolderChild0();
                holderChild0.allInMoney= (TextView) convertView.findViewById(R.id.AllInMoney);
                holderChild0.allOutMoney= (TextView) convertView.findViewById(R.id.AllOutMoney);

                HomeListViewData data=groups.get(groupPosition).child.get(childPosition);
                holderChild0.allInMoney.setText(data.getAllInMoney());
                holderChild0.allOutMoney.setText(data.getAllOutMoney());
                break;
      /**      case 1:
                holderChild1=new ViewHolderChild1();
                convertView=View.inflate(context,R.layout.analysis_group_collate,null);
                holderChild1.spendTime= (Button) convertView.findViewById(R.id.spendTime);
                holderChild1.inMoney= (Button) convertView.findViewById(R.id.inMoney);
                holderChild1.outMoney= (Button) convertView.findViewById(R.id.outMoney);
                holderChild1.outMoney.animate().translationY(180);
                holderChild1.spendTime.setOnClickListener(new buttonListener(childPosition));
                holderChild1.inMoney.setOnClickListener(new buttonListener(childPosition));
                holderChild1.outMoney.setOnClickListener(new buttonListener(childPosition));
                break;**/
            default:

                convertView=View.inflate(context, R.layout.item_home_list,null);
                holderChild =new ViewHolderChild();
                holderChild.unixTime= (TextView) convertView.findViewById(R.id.unixTime);
                holderChild.remarkInList=(TextView) convertView.findViewById(R.id.remarkInList);
                holderChild.dateInList=(TextView)convertView.findViewById(R.id.dateInList);
                holderChild.spendMoneyInList=(TextView)convertView.findViewById(R.id.spendMoneyInList);
                holderChild.moneyType= (TextView) convertView.findViewById(R.id.money_type);
                holderChild.creatTime= (TextView) convertView.findViewById(R.id.creatTime);
                holderChild.tag= (ImageView) convertView.findViewById(R.id.tag);
                holderChild.tagText= (TextView) convertView.findViewById(R.id.tagText);
                convertView.setTag(holderChild);

                HomeListViewData entity = groups.get(groupPosition).child.get(childPosition);
                if (entity!=null) {
                    holderChild.unixTime.setText(entity.getUnixTime());
                    holderChild.remarkInList.setText(entity.getRemark());
                    holderChild.dateInList.setText(entity.getDate());
                    holderChild.spendMoneyInList.setText(entity.getMoney());
                    holderChild.moneyType.setText(entity.getMoneyType());
                    holderChild.creatTime.setText(entity.getCreatTime());
                    holderChild.tag.setImageResource(entity.get_tagID());
                    holderChild.tagText.setText(entity.getTag());

                }
        }
        return convertView;
    }
    class buttonListener implements View.OnClickListener{
        private int position;
        buttonListener(int pos){
            position=pos;
        }
        @Override
        public void onClick(View v) {
          /**  int vid=v.getId();
            if (vid==holderChild1.inMoney.getId()){
                accountAnalysis.handler.sendEmptyMessage(Constants.INMONEY);
            }else if (vid==holderChild1.outMoney.getId()){


                accountAnalysis.handler.sendEmptyMessage(Constants.OUTMONEY);
            }else if (vid==holderChild1.spendTime.getId()){

                accountAnalysis.handler.sendEmptyMessage(Constants.SPENDTIME);

            }**/

        }
    }
    static class ViewHolderChild {
        TextView unixTime,remarkInList,dateInList,spendMoneyInList,moneyType,creatTime;
        ImageView tag;
        TextView tagText;
    }
    static class ViewHolderGroup {
        TextView totalMoney,allTime,moneyType;
    }
    static class ViewHolderChild0{
        TextView allInMoney,allOutMoney,totalMoney,moneyType;
    }
    static class ViewHolderChild1{
        Button spendTime,inMoney,outMoney;
    }
}
