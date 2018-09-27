/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-13 下午1:55
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.manager;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;

import org.web3j.utils.Convert;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import leaf.prod.app.utils.SPUtils;
import leaf.prod.walletsdk.model.response.data.Token;
import leaf.prod.walletsdk.service.LoopringService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TokenDataManager {

    private List<Token> tokens;

    private List<Token> whiteList;

    private Context context;

    private Observable<List<Token>> tokenObservable;

    private LoopringService loopringService = new LoopringService();

    private static TokenDataManager tokenDataManager = null;

    private TokenDataManager(Context context) {
        this.context = context;
        this.loadTokensFromJson();
        this.loadTokensFromRelay();
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

    private void loadTokensFromRelay() {
        String owner = (String) SPUtils.get(context, "address", "");
        if (this.tokenObservable == null) {
            this.tokenObservable = loopringService
                    .getCustomToken(owner)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public List<Token> getTokens() {
        return this.tokens;
    }

    public Observable<List<Token>> getTokenObservable() {
        return tokenObservable;
    }

    public void addToken(Token token) {
        if (!tokens.contains(token)) {
            this.tokens.add(token);
        }
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

    public Double getDoubleFromWei(String symbol, BigDecimal valueInWei) {
        Double result = null;
        Token token = getTokenBySymbol(symbol);
        if (token != null) {
            BigDecimal decimals = token.getDecimals();
            result = valueInWei.divide(decimals).doubleValue();
        }
        return result;
    }

    public Double getDoubleFromWei(String symbol, String valueInWei) {
        BigDecimal bigDecimal = Convert.toWei(valueInWei, Convert.Unit.WEI);
        return getDoubleFromWei(symbol, bigDecimal);
    }

    // support for main fragment presenter
    public void mergeTokens(List<Token> tokens) {
        synchronized (this) {
            for (Token token : tokens) {
                addToken(token);
            }
            for (Token token : this.tokens) {
                String image = String.format("icon_token_%s", token.getSymbol().toLowerCase());
                int identifier = context.getResources().getIdentifier(image, "mipmap", context.getPackageName());
                token.setImageResId(identifier);
            }
            Collections.sort(tokens, new Comparator<Token>() {
                @Override
                public int compare(Token t1, Token t2) {
                    return t1.getSymbol().compareTo(t2.getSymbol());
                }
            });
        }
    }
}
