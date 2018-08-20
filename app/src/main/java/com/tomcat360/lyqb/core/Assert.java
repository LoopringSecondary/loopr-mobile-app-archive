package com.tomcat360.lyqb.core;

import com.google.common.base.Strings;

import java.io.File;

public class Assert {

    public static void hasText(String str) {
        hasText(str, "this string can not be null");
    }

    public static void hasText(String str, String errMsg) {
        if (Strings.isNullOrEmpty(str)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkDirectory(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file path can not be null");
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(file.getPath() + " is not a directory");
        }
    }

    public static void checkKeystoreFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("parameter can not be null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("file does not exist.");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("not a file!");
        }
    }

    public static void validateMnemonic(String mnemonic) {
        hasText(mnemonic, "mnemonic can not be null");
        if (mnemonic.split(" ").length % 12 != 0) {
            throw new IllegalArgumentException("illegal mnemonic");
        }
    }
}
