package leaf.prod.walletsdk.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
    public static String getFileFromSD(Context context, String filename) throws IOException, JSONException {
        FileInputStream isr = null;
        //            isr = new FileInputStream(context.getDir("myFile", Context.MODE_PRIVATE).getAbsolutePath() + "/keystore/" + WalletUtil.getCurrentFileName(context));
        isr = new FileInputStream(context.getFilesDir()
                .getAbsolutePath() + "/keystore/" + filename);
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
        return testjson.getString("address");
    }

    /**
     * 获取keystore文件
     */
    public static File getKeystoreFile(Context context) {
        File file = new File(context.getFilesDir()
                .getAbsolutePath() + "/keystore/" + WalletUtil.getCurrentFileName(context));
        return file;
    }

    /**
     * 获取keystore文件内容
     */
    public static String getKeystoreFromSD(Context context) throws IOException, JSONException {
        FileInputStream isr = null;
        //            isr = new FileInputStream(context.getDir("myFile", Context.MODE_PRIVATE).getAbsolutePath() + "/keystore/" + WalletUtil.getCurrentFileName(context));
        isr = new FileInputStream(context.getFilesDir()
                .getAbsolutePath() + "/keystore/" + WalletUtil.getCurrentFileName(context));
        BufferedReader br = new BufferedReader(new InputStreamReader(isr));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }
        br.close();
        isr.close();
        JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
        return testjson.toString();
    }

    /**
     * 获取keystore文件内容
     */
    public static String getKeystoreFromSD(Context context, String filename) throws IOException, JSONException {
        FileInputStream isr = null;
        //            isr = new FileInputStream(context.getDir("myFile", Context.MODE_PRIVATE).getAbsolutePath() + "/keystore/" + WalletUtil.getCurrentFileName(context));
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
        //            isr = new FileInputStream(context.getDir("myFile", Context.MODE_PRIVATE).getAbsolutePath() + "/keystore/" + WalletUtil.getCurrentFileName(context));
        isr = new FileInputStream(context.getFilesDir().getAbsolutePath() + "/keystore/" + fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(isr));
        String text = br.readLine();
        String[] s = text.split(" ");
        br.close();
        isr.close();
        return s;
    }

    /**
     * 读取本地文件
     */
    @SuppressLint("ResourceType")
    public static String getFile(Context context, int resourceId) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resourceId)));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            Log.e("getFile: ", e.getMessage());
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 删除文件
     *
     * @param context
     * @param fileName
     */
    public static void removeFile(Context context, String fileName) {
        boolean delete = (boolean) SPUtils.get(context, "delete_mnemonic", false);
        if (!delete) {
            String dir = context.getFilesDir().getAbsolutePath() + "/keystore/" + fileName;
            File mFile = new File(dir);
            if (mFile.exists()) {
                mFile.delete();
            }
            SPUtils.put(context, "delete_mnemonic", true);
        }
    }
}
