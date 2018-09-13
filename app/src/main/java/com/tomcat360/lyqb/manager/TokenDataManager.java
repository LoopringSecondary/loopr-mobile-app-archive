package com.tomcat360.lyqb.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lyqb.walletsdk.model.response.data.Token;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-13 下午1:55
 * Cooperation: loopring.org 路印协议基金会
 */
public class TokenDataManager {

    private List<Token> whiteList;

    private Context context;

    private static TokenDataManager tokenDataManager;

    private TokenDataManager(Context context) {
        this.context = context;
        this.parseJsonFile();
    }

    public static TokenDataManager getInstance(Context context) {
        return tokenDataManager;
    }

    private void parseJsonFile() {
        try {
            InputStream is = context.getAssets().open("json/tokens.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            parseJsonString(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonString(String jsonData) {
        Gson gson = new Gson();
        List<Token> tokens = gson.fromJson(jsonData, new TypeToken<List<Token>>() {
        }.getType());
        whiteList = tokens;
    }

    private void combine() {

    }
}
