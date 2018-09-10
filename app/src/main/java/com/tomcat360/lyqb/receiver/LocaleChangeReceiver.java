package com.tomcat360.lyqb.receiver;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.tomcat360.lyqb.utils.LyqbLogger;

/**
 * Created by niedengqiang on 2018/8/22.
 */

public class LocaleChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LyqbLogger.log("mReceiver  onReceive  intent.getAction(): " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
            Log.e("LocaleChangeReceiver", "Language change");
            //            SystemUtil.initAppLanguage(context);
            Locale.setDefault(Locale.CHINA);
            Configuration config = context.getResources().getConfiguration();
            config.locale = Locale.CHINA;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }
}
