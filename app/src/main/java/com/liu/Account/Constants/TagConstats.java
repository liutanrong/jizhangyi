package com.liu.Account.Constants;

import com.liu.Account.R;

/**
 * Created by deonte on 16-1-25.
 */
public class TagConstats {
    //tagLIst和tagImage必须一一对应
    public static String[] tagList={"无分类","餐饮","娱乐","购物","交通","工资","其他"};
    public static int[] tagImage={R.drawable.tag_none,R.drawable.tag_food,
            R.drawable.tag_play,R.drawable.tag_shop,R.drawable.tag_traffic,
            R.drawable.tag_wage,R.drawable.tag_others};
    //上面数组中的一个
    public static  int defaultTag=1;

    //改这里的时候记得改adapter
    public static String TagTypeShow[]={"全部","餐饮","娱乐","购物","交通","工资","其他","无分类"};
    public static String TagTypeSelect[]={"全部","餐饮","娱乐","购物","交通","工资","其他","无分类"};
    public static int  TagTypePosition=0;


    //搜索时用到都的
    public static String InOrOutShow[]={"全部","支出","收入"};
    public static String InOrOutSelect[]={"全部","支出","收入"};
    public static final int InOrOutPosition=0;

    public static String OrderByShow[]={"金额大小","创建时间","支出时间"};
    public static String OrderBySelet[]={"spendMoney","creatTime","unixTime"};


    public static String OrderWayShow[]={"降序","升序"};
    public static String OrderWaySelect[]={"desc","asc"};
}
