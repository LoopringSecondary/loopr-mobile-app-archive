package leaf.prod.walletsdk.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import leaf.prod.walletsdk.model.Currency;
import leaf.prod.walletsdk.model.Language;
import leaf.prod.walletsdk.model.LoginUser;
import leaf.prod.walletsdk.model.LoginUserConfig;
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

    public static LoginUserConfig getLocalUser(Context context) {
        return SPUtils.getBean(context, THIRD_LOGIN + "_" + getUserId(context), LoginUserConfig.class);
    }

    /**
     * 点击第三方微信登录
     *
     * @param loginUserConfig
     */
    public static void initThirdLogin(Context context, LoginUserConfig loginUserConfig, Callback<AppResponseWrapper<LoginUser>> callback) {
        SPUtils.put(context, THIRD_LOGIN, loginUserConfig.getUserId());
        appService.getUser(loginUserConfig.getUserId(), callback);
    }

    /**
     * 初始化本地配置
     *
     * @return
     */
    public static void initLocalConf(Context context) {
        LoginUserConfig loginUserConfig = SPUtils.getBean(context, THIRD_LOGIN + "_" + getUserId(context), LoginUserConfig.class);
        if (loginUserConfig != null) {
            LanguageUtil.changeLanguage(context, Language.getLanguage(loginUserConfig.getLanguage()));
            if (CurrencyUtil.getCurrency(context) != Currency.valueOf(loginUserConfig.getCurrency())) {
                CurrencyUtil.setCurrency(context, Currency.valueOf(loginUserConfig.getCurrency()));
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
     * @param loginUserConfig
     * @param callback
     */
    public static void initRemote(Context context, LoginUserConfig loginUserConfig, Callback<AppResponseWrapper<String>> callback) {
        LoginUser loginUser = LoginUser.builder()
                .accountToken(getUserId(context))
                .config(gson.toJson(loginUserConfig))
                .build();
        appService.addUser(loginUser, callback);
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
            LoginUserConfig loginUserConfig = SPUtils.getBean(context, THIRD_LOGIN + "_" + uid, LoginUserConfig.class);
            if (loginUserConfig != null) {
                loginUserConfig.setLanguage(language != null ? language.getText() : loginUserConfig.getLanguage());
                loginUserConfig.setCurrency(currency != null ? currency.name() : loginUserConfig.getCurrency());
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
            LoginUserConfig loginUserConfig = SPUtils.getBean(context, THIRD_LOGIN + "_" + uid, LoginUserConfig.class);
            if (loginUserConfig != null) {
                appService.getUser(uid, new Callback<AppResponseWrapper<LoginUser>>() {
                    @Override
                    public void onResponse(Call<AppResponseWrapper<LoginUser>> call, Response<AppResponseWrapper<LoginUser>> response) {
                        LoginUserConfig remoteLoginUserConfig = null;
                        LoginUser remoteLoginUser = null;
                        try {
                            remoteLoginUser = response.body().getMessage();
                            remoteLoginUserConfig = remoteLoginUser != null ? remoteLoginUser.getUserConfig() : null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (loginUserConfig.equals(remoteLoginUserConfig))
                            return;
                        if (remoteLoginUserConfig == null) {
                            remoteLoginUser = LoginUser.builder()
                                    .accountToken(loginUserConfig.getUserId())
                                    .config(gson.toJson(loginUserConfig)).build();
                        } else {
                            remoteLoginUser.setConfig(gson.toJson(loginUserConfig));
                        }
                        appService.addUser(remoteLoginUser, new Callback<AppResponseWrapper<String>>() {
                            @Override
                            public void onResponse(Call<AppResponseWrapper<String>> call, Response<AppResponseWrapper<String>> response) {
                                Log.d("[update remote]: ", "同步成功......");
                            }

                            @Override
                            public void onFailure(Call<AppResponseWrapper<String>> call, Throwable t) {
                                Log.e("[update remote]: ", "同步失败......");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<AppResponseWrapper<LoginUser>> call, Throwable t) {
                        Log.e("[update remote]: ", "获得云端信息失败......");
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
