package com.tomcat360.lyqb.view;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.lyqb.walletsdk.Loopring;
import com.lyqb.walletsdk.model.response.BalanceResult;
import com.lyqb.walletsdk.service.EthHttpService;
import com.lyqb.walletsdk.service.LooprHttpService;
import com.lyqb.walletsdk.service.LooprSocketService;
import com.tomcat360.lyqb.utils.AndroidUtils;
import com.tomcat360.lyqb.utils.LanguagesUtil;
import com.tomcat360.lyqb.utils.SPUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtool.RxTool;

import java.util.ArrayList;
import java.util.List;

public class APP extends Application {
    private static APP mInstance;
    String appVersion;
//    public static LooprSocketService looprSocketService; // 创建全局的socket服务
    private static Loopring loopring;

    private static List<BalanceResult.Token> listToken = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
        appVersion = AndroidUtils.getVersionName(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        /**
         * Loopring.
         */
        loopring = new Loopring();

        mInstance = this;

        /**
         * 通过language的状态来判断是否设置了显示英文还是中文，1为英文，2为中文,0为未设置，显示系统默认
         * */
        if ((int)SPUtils.get(this, "language", 0) == 1) {
            LanguagesUtil.changeLanguage(this,"en");

        } else if((int)SPUtils.get(this, "language", 0) == 2) {
            LanguagesUtil.changeLanguage(this,"zh");
        }

        /**
         * umeng分享
         * */
        UMShareAPI.get(this);

        Config.DEBUG = true;

    }

    {

        PlatformConfig.setWeixin("wx8303b66b243b4aa3","c96e252e26a7f62f833d7c545ab3d098");
        PlatformConfig.setQQZone("1106526050", "ldwhPFfveaNiOVUb");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("799587641", "09dfbfdc9cada2e8db0813bd298078c6","http://sns.whalecloud.com");
    }

    public static APP getInstance() {
        return mInstance;
    }

    public static LooprSocketService getLooprSocketService() {
        return loopring.getSocketService();
    }

    public static LooprHttpService getLooprHttpService() {
        return loopring.getHttpService();
    }

    public static EthHttpService getEthHttpService() {

        return loopring.getEthService();
    }
    public static Loopring getLoopring() {
        return loopring;
    }

    public static List<BalanceResult.Token> getListToken() {
        return listToken;
    }

    public static void setListToken(List<BalanceResult.Token> listToken) {
        APP.listToken.clear();
        APP.listToken = listToken;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean hasLogin() {
        boolean flag = "1".equals(SPUtils.get(getApplicationContext(), "isLogin", "0"));
        return flag;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        /**
         * 设置 当手机字体大小变化时，APP重新设置为默认字体大小，防止不适配
         */
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }
}
