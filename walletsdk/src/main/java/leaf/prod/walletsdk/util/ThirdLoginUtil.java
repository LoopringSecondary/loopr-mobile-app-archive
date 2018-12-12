package leaf.prod.walletsdk.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import leaf.prod.walletsdk.model.Currency;
import leaf.prod.walletsdk.model.Language;
import leaf.prod.walletsdk.model.LoginUser;
import leaf.prod.walletsdk.model.UserConfig;
import leaf.prod.walletsdk.model.response.AppResponseWrapper;
import leaf.prod.walletsdk.service.AppService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 上午11:33
 * Cooperation: Loopring
 */
public class ThirdLoginUtil {

    private static String THIRD_LOGIN = "third_login";

    private static AppService appService = new AppService();

    private static Gson gson = new Gson();

    public static String getUserId(Context context) {
        return (String) SPUtils.get(context, THIRD_LOGIN, "");
    }

    public static boolean isThirdLogin(Context context) {
        String uid = getUserId(context);
        return !StringUtils.isEmpty(uid) && !uid.equals("-") && SPUtils.getBean(context, THIRD_LOGIN + "_" + uid, LoginUser.class) != null;
    }

    public static boolean isSkip(Context context) {
        String uid = getUserId(context);
        return !StringUtils.isEmpty(uid) && uid.equals("-");
    }

    public static void skip(Context context) {
        SPUtils.remove(context, THIRD_LOGIN + "_" + getUserId(context));
        SPUtils.put(context, THIRD_LOGIN, "-");
    }

    public static void clearLocal(Context context, String uid) {
        SPUtils.remove(context, THIRD_LOGIN);
        SPUtils.remove(context, THIRD_LOGIN + "_" + uid);
    }

    public static UserConfig getLocalUser(Context context) {
        return SPUtils.getBean(context, THIRD_LOGIN + "_" + getUserId(context), UserConfig.class);
    }

    /**
     * 点击第三方微信登录
     *
     * @param userConfig
     */
    public static void initThirdLogin(Context context, UserConfig userConfig, Callback<AppResponseWrapper<LoginUser>> callback) {
        SPUtils.put(context, THIRD_LOGIN, userConfig.getUserId());
        appService.getUser(userConfig.getUserId(), callback);
    }

    /**
     * 初始化本地配置
     *
     * @return
     */
    public static void initLocalConf(Context context) {
        UserConfig userConfig = SPUtils.getBean(context, THIRD_LOGIN + "_" + getUserId(context), UserConfig.class);
        if (userConfig != null) {
            LanguageUtil.changeLanguage(context, Language.getLanguage(userConfig.getLanguage()));
            if (CurrencyUtil.getCurrency(context) != Currency.valueOf(userConfig.getCurrency())) {
                CurrencyUtil.setCurrency(context, Currency.valueOf(userConfig.getCurrency()));
            }
        } else {
            if (LanguageUtil.getLanguage(context) != LanguageUtil.getSettingLanguage(context)) {
                LanguageUtil.changeLanguage(context, LanguageUtil.getSettingLanguage(context));
            }
        }
    }

    /**
     * 初始化本地和线上
     *
     * @param context
     * @param loginUser
     * @param callback
     */
    public static void initLocalAndRemote(Context context, LoginUser loginUser, Callback<AppResponseWrapper<String>> callback) {
        appService.addUser(loginUser, callback);
        SPUtils.put(context, THIRD_LOGIN + "_" + getUserId(context), loginUser.getUserConfig());
    }

    /**
     * 初始化线上
     *
     * @param context
     * @param userConfig
     * @param callback
     */
    public static void initRemote(Context context, UserConfig userConfig, Callback<AppResponseWrapper<String>> callback) {
        LoginUser loginUser = LoginUser.builder()
                .accountToken(getUserId(context))
                .config(gson.toJson(userConfig))
                .build();
        appService.addUser(loginUser, callback);
    }

    /**
     * 配置更改后保存本地
     *
     * @param context
     * @param config
     */
    public static void updateLocal(Context context, UserConfig config) {
        if (isThirdLogin(context)) {
            String uid = getUserId(context);
            UserConfig loginUserConfig = SPUtils.getBean(context, THIRD_LOGIN + "_" + uid, UserConfig.class);
            if (loginUserConfig != null) {
                loginUserConfig.setLanguage(config.getLanguage() != null ? config.getLanguage() : loginUserConfig.getLanguage());
                loginUserConfig.setCurrency(config.getCurrency() != null ? config.getCurrency() : loginUserConfig.getCurrency());
                SPUtils.put(context, THIRD_LOGIN + "_" + uid, loginUserConfig);
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
            UserConfig loginUserConfig = SPUtils.getBean(context, THIRD_LOGIN + "_" + uid, UserConfig.class);
            if (loginUserConfig != null) {
                LoginUser loginUser = LoginUser.builder()
                        .accountToken(loginUserConfig.getUserId())
                        .config(gson.toJson(loginUserConfig))
                        .build();
                appService.addUser(loginUser, new Callback<AppResponseWrapper<String>>() {
                    @Override
                    public void onResponse(Call<AppResponseWrapper<String>> call, Response<AppResponseWrapper<String>> response) {
                        Log.d("[update remote]: ", "同步成功......" + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<AppResponseWrapper<String>> call, Throwable t) {
                        Log.e("[update remote]: ", "同步失败......" + t.getMessage());
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
    public static void deleteThirdLogin(Context context, Callback<AppResponseWrapper<String>> callback) {
        String uid = getUserId(context);
        if (!uid.isEmpty()) {
            appService.deleteUser(uid, callback);
        }
    }

    /**
     * 清空脏数据
     *
     * @param context
     */
    public static void clearDirtData(Context context) {
        if (((String) SPUtils.get(context, "dirt_data", "")).isEmpty()) {
            String uid = (String) SPUtils.get(context, THIRD_LOGIN, "");
            SPUtils.remove(context, THIRD_LOGIN + "_" + uid);
            SPUtils.remove(context, THIRD_LOGIN);
            SPUtils.put(context, "dirt_data", "true");
        }
    }
}
