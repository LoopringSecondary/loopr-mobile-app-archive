package com.tomcat360.lyqb.utils;

import java.io.File;

public class Assert {

    public static void hasText(String str) {
        hasText(str, "this string can not be null");
    }

    public static void hasText(String str, String errMsg) {
        if (str == null || 0 == str.trim().length()) {
            throw new RuntimeException(errMsg);
        }
    }

    public static void checkDirectory(File file) {
        if (file == null) {
            throw new RuntimeException("file path can not be null");
        }
        if (!file.isDirectory()) {
            throw new RuntimeException(file.getPath() + " is not a directory");
        }
    }

    public static void validateMnemonic(String mnemonic) {
        hasText(mnemonic, "mnemonic can not be null");
        if (mnemonic.split(" ").length % 12 != 0) {
            throw new RuntimeException("illegal mnemonic");
        }
    }
}
