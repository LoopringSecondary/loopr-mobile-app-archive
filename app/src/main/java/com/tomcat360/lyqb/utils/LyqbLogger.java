package com.tomcat360.lyqb.utils;


import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by  on 2017/7/11.
 *
 */

public class LyqbLogger {

    private static PrettyFormatStrategy formatStrategy;

    private static void startLog(String i, String name, String info) {
        initLogger(name);
        switch (i) {
            case "i":
                Logger.i(info);
                break;
            case "v":
                Logger.v(info);
                break;
            case "d":
                Logger.d(info);
                break;
            case "w":
                Logger.w(info);
                break;
            case "e":
                Logger.e(info);
                break;
            case "json":
                Logger.json(info);
                break;
            default:
                Logger.e(info);
                break;
        }
    }

    private static void initLogger(String name) {
//        Logger.init(name)                       // default PRETTYLOGGER or use just init()
//                .methodCount(3)                 // default 2
//                .hideThreadInfo()               // default shown
//                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
//                .methodOffset(2)                // default 0
//                .logTool(new AndroidLogTool());// custom log tool, optional
        if(formatStrategy == null) {
            formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                    .methodCount(1)         // (Optional) How many method line to show. Default 2
                    .methodOffset(2)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                    .tag(name)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                    .build();

            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        }
    }

    public static void log(String info){
        startLog("i", "DENG_QIANG", info);
    }

    public static void log2Json(String info){
        startLog("json", "DENG_QIANG", info);
    }


}
