package com.tomcat360.lyqb.utils;

/**
 * 避免重复点击工具类  判断是否短时间内多次点击
 *
 * @author wang
 * @className ButtonClickUtil
 * @description
 * @Date 16/11/30
 */
public class ButtonClickUtil {

    /**
     * 当前点击的时间
     */
    private static long mLastClickTime = 0;

    private static long staticid = -1;

    /**
     * 判断是否短时间内多次点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    /**
     * 判断是否短时间内多次点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(long id) {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        if (0 < timeD && timeD < 1000 && staticid == id) {
            return true;
        }
        staticid = id;
        mLastClickTime = time;
        return false;
    }
}
