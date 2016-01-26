package com.liu.Account.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by deonte on 16-1-26.
 */
public class LocationUtils {
    /**
     * 获取位置，优先返回GPS定位的位置信息，其次返回网络定位的位置信息
     *
     * @param context
     * @return
     */
    public static Location getLocation(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location1 = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return location1 == null ? location2 : location1;
        }catch (Exception e){

        }
        return null;
    }
}
