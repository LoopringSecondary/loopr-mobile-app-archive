package com.tomcat360.lyqb.utils;

import android.content.Context;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

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


    //获取keystore文件中的address
    public static String getFileFromSD(Context context) throws IOException, JSONException {
        FileInputStream isr = null;
//        try {
//            isr = new FileInputStream(Environment.getExternalStorageDirectory().getPath() + "/keystore"+"/UTC--2018-08-20T17-52-51.231--8e1de49550b72f0dcc09b0c8344dac94248f066b.json");
            isr = new FileInputStream(Environment.getExternalStorageDirectory().getPath() + "/keystore/" + (String) SPUtils.get(context, "filename", ""));
            BufferedReader br = new BufferedReader(new InputStreamReader(isr));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
            SPUtils.put(context, "address", testjson.getString("address"));
            SPUtils.put(context, "private_key", testjson.toString());
            LyqbLogger.log(testjson.toString());
            LyqbLogger.log(testjson.getString("address"));
            return testjson.getString("address");
            //直接传入JSONObject来构造一个实例
//            JSONArray array = testjson.getJSONArray("role");         //从JSONObject中取出数组对象
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject role = array.getJSONObject(i);    //取出数组中的对象
//                text.append(role.getString("name") + ": ");  //取出数组中对象的各个值
//                text.append(role.getString("say") + "\n");
//            }//
//
//            text.append("now the " +testjson.getString("dog") + " is here");
//        } catch (FileNotFoundException e) {
////            hideProgress();
//            ToastUtils.toast("本地文件读取失败，请重试");
//            e.printStackTrace();
//        } catch (JSONException e) {
////            hideProgress();
//            ToastUtils.toast("本地文件读取失败，请重试");
//            e.printStackTrace();
//        } catch (IOException e) {
////            hideProgress();
//            ToastUtils.toast("本地文件读取失败，请重试");
//            e.printStackTrace();
//        }
//        return "";
    }

}
