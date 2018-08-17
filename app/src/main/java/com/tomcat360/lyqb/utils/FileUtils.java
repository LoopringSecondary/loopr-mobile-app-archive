package com.tomcat360.lyqb.utils;

import android.os.Environment;

import java.io.File;

public class FileUtils {

    public static File getKeyStoreLocation() {
        //在SD卡下建目录
        String dir = Environment.getExternalStorageDirectory().getPath() + "/keystore";
        File mFile = new File(dir);
        if (!mFile.exists()) {
            boolean mkdirs = mFile.mkdirs();
        }
        return mFile;
    }

}
