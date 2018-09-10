package com.tomcat360.lyqb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class FileUtils {

    /**
     * 在根目录下建文件夹
     */
    public static File getKeyStoreLocation(Context context) {
        //
        //        String dir = Environment.getExternalStorageDirectory().getPath() + "/keystore";
        String dir = context.getFilesDir()
                .getAbsolutePath() + "/keystore";  //getDir("myFile", Context.MODE_PRIVATE).getAbsolutePath()
        File mFile = new File(dir);
        if (!mFile.exists()) {
            boolean mkdirs = mFile.mkdirs();
        }
        return mFile;
    }

    /**
     * 获取keystore文件中的address
     */
    public static String getFileFromSD(Context context) throws IOException, JSONException {

        FileInputStream isr = null;
        //            isr = new FileInputStream(context.getDir("myFile", Context.MODE_PRIVATE).getAbsolutePath() + "/keystore/" + (String) SPUtils.get(context, "filename", ""));
        isr = new FileInputStream(context.getFilesDir()
                .getAbsolutePath() + "/keystore/" + (String) SPUtils.get(context, "filename", ""));
        BufferedReader br = new BufferedReader(new InputStreamReader(isr));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }
        br.close();
        isr.close();
        JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
        //            SPUtils.put(context, "address", "0x"+ testjson.getString("address"));
        LyqbLogger.log(testjson.toString());
        LyqbLogger.log(testjson.getString("address"));
        return testjson.getString("address");

    }

    /**
     * 获取keystore文件
     */
    public static File getKeystoreFile(Context context) {

        File file = new File(context.getFilesDir()
                .getAbsolutePath() + "/keystore/" + (String) SPUtils.get(context, "filename", ""));
        return file;
    }

    /**
     * 获取keystore文件内容
     */
    public static String getKeystoreFromSD(Context context) throws IOException, JSONException {

        FileInputStream isr = null;
        //            isr = new FileInputStream(context.getDir("myFile", Context.MODE_PRIVATE).getAbsolutePath() + "/keystore/" + (String) SPUtils.get(context, "filename", ""));
        isr = new FileInputStream(context.getFilesDir()
                .getAbsolutePath() + "/keystore/" + (String) SPUtils.get(context, "filename", ""));
        BufferedReader br = new BufferedReader(new InputStreamReader(isr));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }
        br.close();
        isr.close();
        JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
        LyqbLogger.log(testjson.toString());
        LyqbLogger.log(testjson.getString("address"));
        return testjson.toString();

    }

    /**
     * 获取keystore文件内容
     */
    public static String getKeystoreFromSD(Context context, String filename) throws IOException, JSONException {

        FileInputStream isr = null;
        //            isr = new FileInputStream(context.getDir("myFile", Context.MODE_PRIVATE).getAbsolutePath() + "/keystore/" + (String) SPUtils.get(context, "filename", ""));
        isr = new FileInputStream(context.getFilesDir().getAbsolutePath() + "/keystore/" + filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(isr));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }
        br.close();
        isr.close();
        JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
        LyqbLogger.log(testjson.toString());
        LyqbLogger.log(testjson.getString("address"));
        return testjson.toString();

    }

    /**
     * 保存文件到本地
     */
    public static void keepFile(Context context, String fileName, String fileInfo) {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(context.getFilesDir().getAbsolutePath() + "/keystore/" + fileName);
            fos.write(fileInfo.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取本地文件
     */
    public static String[] getFile(Context context, String fileName) throws IOException, JSONException {

        FileInputStream isr = null;
        //            isr = new FileInputStream(context.getDir("myFile", Context.MODE_PRIVATE).getAbsolutePath() + "/keystore/" + (String) SPUtils.get(context, "filename", ""));
        isr = new FileInputStream(context.getFilesDir().getAbsolutePath() + "/keystore/" + fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(isr));
        String text = br.readLine();
        String[] s = text.split(" ");
        br.close();
        isr.close();
        return s;

    }

}
