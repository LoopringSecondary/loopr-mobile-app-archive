/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-13 下午1:55
 * Cooperation: loopring.org 路印协议基金会
 */
package com.tomcat360.lyqb.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lyqb.walletsdk.model.response.data.Token;

import rx.Observable;

public class TokenDataManager {

    private static TokenDataManager tokenDataManager = null;

    private List<Token> tokens;

    private List<Token> whiteList;

    private Context context;

    private Observable<List<Token>> tokenObservable;

    private TokenDataManager(Context context) {
        this.context = context;
        this.loadTokensFromJson();
    }

    public static TokenDataManager getInstance(Context context) {
        if (tokenDataManager == null) {
            tokenDataManager = new TokenDataManager(context);
        }
        return tokenDataManager;
    }

    // Generate whitelist of tokens through tokens json file
    private void loadTokensFromJson() {
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
        this.tokens = tokens;
        this.whiteList = tokens;
    }

    public List<Token> getTokens() {
        return this.tokens;
    }

    public Observable<List<Token>> getTokenObservable() {
        return tokenObservable;
    }

    public Token getTokenBySymbol(String symbol) {
        Token result = null;
        for (Token token : this.tokens) {
            if (token.getSymbol().equalsIgnoreCase(symbol)) {
                result = token;
            }
        }
        return result;
    }

    public Token getTokenByProtocol(String protocol) {
        Token result = null;
        for (Token token : this.tokens) {
            if (token.getProtocol().equalsIgnoreCase(protocol)) {
                result = token;
            }
        }
        return result;
    }

    // support for main frament presenter
    public void mergeTokens(List<Token> tokens) {
        for (Token token : tokens) {
            String image = String.format("icon_token_%s", token.getSymbol().toLowerCase());
            int identifier = context.getResources().getIdentifier(image, "mipmap", context.getPackageName());
            token.setImageResId(identifier);
            if (!this.tokens.contains(token)) {
                this.tokens.add(token);
            }
        }
    }
}
