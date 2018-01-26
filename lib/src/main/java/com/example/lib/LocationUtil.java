package com.example.lib;

import android.view.View;

/**
 * 描述：获取View绝对坐标工具
 *
 * @author yuanyang
 * @date 2018/1/26 11:12
 */

public class LocationUtil {


    public static Location getRawLocation(View view){
        if (view == null) {
            throw new NullPointerException("view is null,please check param");
        }
        int [] loc = new int[2];
        view.getLocationOnScreen(loc);
        return new Location(loc[0],loc[1]);
    }
}
