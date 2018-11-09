package leaf.prod.app.utils;

import java.io.IOException;

import android.content.Context;

import com.google.gson.Gson;
import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.model.ThirdLogin;
import leaf.prod.app.model.ThirdLoginResp;
import leaf.prod.app.model.ThirdLoginUser;
import leaf.prod.walletsdk.model.Currency;
import leaf.prod.walletsdk.model.Language;
import leaf.prod.walletsdk.service.ThirdLoginService;
import leaf.prod.walletsdk.util.StringUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 上午11:33
 * Cooperation: Loopring
 */
public class ThirdLoginUtil {

    private static String THIRD_LOGIN = "third_login";

    private static ThirdLoginService thirdLoginService = new ThirdLoginService();

    private static Gson gson = new Gson();

    public static String getUserId(Context context) {
        return (String) SPUtils.get(context, THIRD_LOGIN, "");
    }

    public static boolean isThirdLogin(Context context) {
        String uid = getUserId(context);
        return !StringUtils.isEmpty(uid) && !uid.equals("-");
    }

    public static boolean isSkip(Context context) {
        String uid = getUserId(context);
        return !StringUtils.isEmpty(uid) && uid.equals("-");
    }

    public static void skip(Context context) {
        SPUtils.put(context, THIRD_LOGIN, "-");
    }

    /**
     * 点击第三方微信登录
     *
     * @param context
     * @param thirdLoginUser
     */
    public static void initThirdLogin(Context context, ThirdLoginUser thirdLoginUser) {
        if (thirdLoginUser == null)
            return;
        ThirdLogin newThirdLogin = new ThirdLogin(thirdLoginUser.getUserId(), gson.toJson(thirdLoginUser));
        ThirdLoginUser localThirdLoginUser = SPUtils.getBean(context, THIRD_LOGIN + "_" + thirdLoginUser.getUserId(), ThirdLoginUser.class);
        thirdLoginService.getUser(thirdLoginUser.getUserId()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RxToast.error("登录失败，请稍后重试");
            }

            @Override
            public void onResponse(Call call, Response response) {
                ThirdLoginUser remoteThirdLoginUser = null;
                try {
                    String re = response.body().string();
                    ThirdLoginResp thirdLoginResp = gson.fromJson(re, ThirdLoginResp.class);
                    if (thirdLoginResp.getSuccess() != null && thirdLoginResp.getSuccess()) {
                        ThirdLogin remoteThirdLogin = gson.fromJson(thirdLoginResp.getMessage(), ThirdLogin.class);
                        remoteThirdLoginUser = remoteThirdLogin != null ? remoteThirdLogin.getThirdLoginUser() : null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (localThirdLoginUser == null) {
                    if (remoteThirdLoginUser != null) {
                        // 初始化本地数据
                        SPUtils.put(context, THIRD_LOGIN + "_" + thirdLoginUser.getUserId(), remoteThirdLoginUser);
                        LanguageUtil.changeLanguage(context, Language.valueOf(remoteThirdLoginUser.getLanguage()));
                        CurrencyUtil.setCurrency(context, Currency.valueOf(remoteThirdLoginUser.getCurrency()));
                    } else {
                        // 初始化本地和线上
                        thirdLoginService.addUser(gson.toJson(newThirdLogin));
                        SPUtils.put(context, THIRD_LOGIN + "_" + thirdLoginUser.getUserId(), thirdLoginUser);
                    }
                } else {
                    // 更新线上数据
                    if (!localThirdLoginUser.equals(remoteThirdLoginUser)) {
                        thirdLoginService.addUser(gson.toJson(new ThirdLogin(localThirdLoginUser.getUserId(), gson.toJson(localThirdLoginUser))));
                    }
                }
                SPUtils.put(context, THIRD_LOGIN, thirdLoginUser.getUserId());
            }
        });
    }

    /**
     * 配置更改后保存本地
     *
     * @param context
     * @param language
     * @param currency
     */
    public static void updateLocal(Context context, Language language, Currency currency) {
        if (isThirdLogin(context)) {
            String uid = getUserId(context);
            ThirdLoginUser thirdLoginUser = SPUtils.getBean(context, THIRD_LOGIN + "_" + uid, ThirdLoginUser.class);
            if (thirdLoginUser != null) {
                thirdLoginUser.setLanguage(language != null ? language.name() : thirdLoginUser.getLanguage());
                thirdLoginUser.setCurrency(currency != null ? currency.name() : thirdLoginUser.getCurrency());
                SPUtils.put(context, THIRD_LOGIN + "_" + uid, thirdLoginUser);
            }
        }
    }

    /**
     * 打开app时同步云端
     *
     * @param context
     */
    public static void updateRemote(Context context) {
        if (isThirdLogin(context)) {
            String uid = getUserId(context);
            ThirdLoginUser thirdLoginUser = SPUtils.getBean(context, THIRD_LOGIN + "_" + uid, ThirdLoginUser.class);
            if (thirdLoginUser != null) {
                thirdLoginService.getUser(uid).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        ThirdLoginUser remoteThirdLoginUser = null;
                        ThirdLogin remoteThirdLogin = null;
                        try {
                            String re = response.body().string();
                            LyqbLogger.log(re);
                            ThirdLoginResp thirdLoginResp = gson.fromJson(re, ThirdLoginResp.class);
                            if (thirdLoginResp.getSuccess() != null && thirdLoginResp.getSuccess()) {
                                remoteThirdLogin = gson.fromJson(thirdLoginResp.getMessage(), ThirdLogin.class);
                                remoteThirdLoginUser = remoteThirdLogin != null ? remoteThirdLogin.getThirdLoginUser() : null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!thirdLoginUser.equals(remoteThirdLoginUser)) {
                            remoteThirdLogin.setConfig(gson.toJson(thirdLoginUser));
                            thirdLoginService.addUser(gson.toJson(remoteThirdLogin));
                        }
                    }
                });
            }
        }
    }

    /**
     * 删除所有第三方登录相关
     *
     * @param context
     */
    public static void deleteThirdLogin(Context context) {
        String uid = (String) SPUtils.get(context, THIRD_LOGIN, "");
        SPUtils.remove(context, THIRD_LOGIN + "_" + uid);
        SPUtils.remove(context, THIRD_LOGIN);
        thirdLoginService.deleteUser(uid);
    }
}
