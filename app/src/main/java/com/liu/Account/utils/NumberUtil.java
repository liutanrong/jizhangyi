package com.liu.Account.utils;

import com.liu.Account.commonUtils.LogUtil;

import java.math.BigDecimal;

/**
 * Created by deonte on 16-1-25.
 */
public class NumberUtil {
    public static float roundHalfUp(float f){
        float count;
        BigDecimal b = new BigDecimal(f);
        count = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return count;
    }
    public static float roundHalfUp(String f){
        float count;
        if (f==null) {
            LogUtil.e("请确定传入值不为空");
            return -1;
        }
        BigDecimal b = new BigDecimal(f);
        count = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return count;
    }
}
