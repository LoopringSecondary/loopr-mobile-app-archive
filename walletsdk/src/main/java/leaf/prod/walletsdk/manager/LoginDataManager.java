package leaf.prod.walletsdk.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;

import leaf.prod.walletsdk.model.Contact;
import leaf.prod.walletsdk.model.Currency;
import leaf.prod.walletsdk.model.Language;
import leaf.prod.walletsdk.model.LoginUser;
import leaf.prod.walletsdk.model.UserConfig;
import leaf.prod.walletsdk.model.response.AppResponseWrapper;
import leaf.prod.walletsdk.service.AppService;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.LanguageUtil;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.walletsdk.util.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 上午11:33
 * Cooperation: Loopring
 */
public class LoginDataManager {

    private static LoginDataManager loginDataManager;

    private static AppService appService = new AppService();

    private static Gson gson = new Gson();

    private static String ACCOUNT_TOKEN = "account_token";

    private static String LOGIN_TYPE = "login_type";

    private Context context;

    // 用户ID，登录时使用第三方id，未登录时使用androidID
    private String accountToken;

    private String androidId;

    // 用户配置信息
    private UserConfig userConfig;

    public static LoginDataManager getInstance(Context context) {
        if (loginDataManager == null) {
            loginDataManager = new LoginDataManager(context);
        }
        return loginDataManager;
    }

    @SuppressLint("HardwareIds")
    private LoginDataManager(Context context) {
        this.context = context;
        androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        accountToken = (String) SPUtils.get(context, ACCOUNT_TOKEN, "");
        if (accountToken.isEmpty()) {
            SPUtils.put(context, ACCOUNT_TOKEN, androidId);
            accountToken = androidId;
        }
        appService.getUser(accountToken, new Callback<AppResponseWrapper<LoginUser>>() {
            @Override
            public void onResponse(Call<AppResponseWrapper<LoginUser>> call, Response<AppResponseWrapper<LoginUser>> response) {
                AppResponseWrapper<LoginUser> responseWrapper = response.body();
                if (responseWrapper.getSuccess() && responseWrapper.getMessage() != null) {
                    LoginUser loginUser = responseWrapper.getMessage();
                    accountToken = loginUser.getAccountToken();
                    userConfig = gson.fromJson(loginUser.getConfig(), UserConfig.class);
                    initLocalConf();
                }
                if (userConfig == null) {
                    genNewUserConfig();
                }
                Log.d("[AndroidId]: ", androidId);
                Log.d("[AccountToken]: ", accountToken);
                Log.d("[UserConfig]: ", userConfig.toString());
            }

            @Override
            public void onFailure(Call<AppResponseWrapper<LoginUser>> call, Throwable t) {
                genNewUserConfig();
                Log.d("[AndroidId]: ", androidId);
                Log.d("[AccountToken]: ", accountToken);
                Log.d("[UserConfig]: ", userConfig.toString());
            }
        });
    }

    private void genNewUserConfig() {
        accountToken = androidId;
        SPUtils.put(context, ACCOUNT_TOKEN, androidId);
        if (isLogin()) {
            SPUtils.put(context, LOGIN_TYPE, LoginType.SKIP.name());
        }
        userConfig = UserConfig.builder()
                .userId(androidId)
                .language(LanguageUtil.getLanguage(context).getText())
                .currency(CurrencyUtil.getCurrency(context).getText())
                .build();
        appService.addUser(LoginUser.builder()
                .accountToken(androidId)
                .config(gson.toJson(userConfig))
                .build(), new Callback<AppResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<AppResponseWrapper<String>> call, Response<AppResponseWrapper<String>> response) {
                Log.d("[update remote " + androidId + "]: ", "同步成功......" + response.body().toString());
            }

            @Override
            public void onFailure(Call<AppResponseWrapper<String>> call, Throwable t) {
                Log.d("[update remote " + androidId + "]: ", "同步失败......" + t.getMessage());
            }
        });
    }

    public LoginType getLoginType() {
        LoginType loginType = LoginType.getByName((String) SPUtils.get(context, LOGIN_TYPE, ""));
        if (loginType == null) {
            SPUtils.remove(context, LOGIN_TYPE);
        }
        return loginType;
    }

    public boolean isLogin() {
        LoginType loginType = getLoginType();
        return !androidId.equals(accountToken) && loginType != null && loginType == LoginType.LOGIN;
    }

    public boolean isSkip() {
        LoginType loginType = getLoginType();
        return loginType != null && loginType == LoginType.SKIP;
    }

    public void skip() {
        SPUtils.put(context, LOGIN_TYPE, LoginType.SKIP.name());
    }

    /**
     * 获得本地配置
     *
     * @return
     */
    public UserConfig getLocalUser() {
        return userConfig;
    }

    /**
     * 获得云端配置
     * 登录时使用
     *
     * @param uid
     * @param callback
     */
    public void getRemoteUser(String uid, Callback<AppResponseWrapper<LoginUser>> callback) {
        appService.getUser(uid, callback);
    }

    /**
     * 登录成功
     */
    public void loginSuccess(String uid) {
        SPUtils.put(context, LOGIN_TYPE, LoginType.LOGIN.name());
        SPUtils.put(context, ACCOUNT_TOKEN, uid);
        accountToken = uid;
        userConfig.setUserId(uid);
        appService.deleteUser(androidId, new Callback<AppResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<AppResponseWrapper<String>> call, Response<AppResponseWrapper<String>> response) {
                Log.d("删除androidId[" + androidId + "]成功: ", response.body().toString());
            }

            @Override
            public void onFailure(Call<AppResponseWrapper<String>> call, Throwable t) {
                Log.d("删除androidId[" + androidId + "]失败: ", t.getMessage());
            }
        });
    }

    /**
     * 同步本地配置
     *
     * @param loginUser
     */
    public void updateLocal(LoginUser loginUser) {
        userConfig = gson.fromJson(loginUser.getConfig(), UserConfig.class);
        initLocalConf();
    }

    /**
     * 同步云端
     */
    public void updateRemote(UserConfig config) {
        this.userConfig = config;
        LoginUser loginUser = LoginUser.builder()
                .accountToken(config.getUserId())
                .config(gson.toJson(config))
                .build();
        appService.addUser(loginUser, new Callback<AppResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<AppResponseWrapper<String>> call, Response<AppResponseWrapper<String>> response) {
                Log.d("[update remote " + userConfig.getUserId() + "]: ", "同步成功......" + response.body().toString());
            }

            @Override
            public void onFailure(Call<AppResponseWrapper<String>> call, Throwable t) {
                Log.e("[update remote " + userConfig.getUserId() + "]: ", "同步失败......" + t.getMessage());
            }
        });
    }

    /**
     * 初始化本地配置
     *
     * @return
     */
    public void initLocalConf() {
        UserConfig userConfig = getLocalUser();
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
     * 删除所有第三方登录相关
     */
    public void logout(Callback<AppResponseWrapper<String>> callback) {
        appService.deleteUser(accountToken, callback);
    }

    public void logoutSuccess() {
        accountToken = androidId;
        userConfig.setUserId(androidId);
        SPUtils.put(context, ACCOUNT_TOKEN, androidId);
        skip();
        appService.addUser(LoginUser.builder()
                .accountToken(androidId)
                .config(gson.toJson(userConfig))
                .build(), new Callback<AppResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<AppResponseWrapper<String>> call, Response<AppResponseWrapper<String>> response) {
                Log.d("登录androidId[" + androidId + "]成功: ", response.body().toString());
            }

            @Override
            public void onFailure(Call<AppResponseWrapper<String>> call, Throwable t) {
                Log.d("登录androidId[" + androidId + "]失败: ", t.getMessage());
            }
        });
    }

    public Contact getContact(String address) {
        List<Contact> contacts = userConfig.getContacts();
        if (contacts != null) {
            for (Contact contact : contacts) {
                if (contact.getAddress().equalsIgnoreCase(address))
                    return contact;
            }
        }
        return null;
    }

    public void deleteContact(String address) {
        List<Contact> contacts = new ArrayList<>();
        if (userConfig.getContacts() != null) {
            for (Contact contact : userConfig.getContacts()) {
                if (!contact.getAddress().equalsIgnoreCase(address)) {
                    contacts.add(contact);
                }
            }
        }
        userConfig.setContacts(contacts);
        updateRemote(userConfig);
    }

    public boolean addContact(Contact newContact) {
        List<Contact> contacts = userConfig.getContacts() == null ? new ArrayList<>() : userConfig.getContacts();
        if (contacts.contains(newContact))
            return false;
        contacts.add(newContact);
        userConfig.setContacts(contacts);
        Collections.sort(contacts, (contact, t1) -> contact.getTag().compareTo(t1.getTag()));
        updateRemote(userConfig);
        return true;
    }

    public List<Contact> searchContacts(String content) {
        List<Contact> result = new ArrayList<>();
        if (userConfig.getContacts() == null || StringUtils.isEmpty(content))
            return result;
        for (Contact contact : userConfig.getContacts()) {
            if (contact.getName().toLowerCase().contains(content.toLowerCase()) ||
                    contact.getNote().toLowerCase().contains(content.toLowerCase())) {
                result.add(contact);
            }
        }
        return result;
    }

    public boolean updateContact(Contact newContact, String address) {
        List<Contact> contacts = userConfig.getContacts() == null ? new ArrayList<>() : userConfig.getContacts();
        Contact oldContact = getContact(address);
        for (Contact contact : contacts) {
            if (!oldContact.equals(newContact) && contact.equals(newContact))
                return false;
        }
        contacts.remove(oldContact);
        contacts.add(newContact);
        Collections.sort(contacts, (contact, t1) -> contact.getTag().compareTo(t1.getTag()));
        userConfig.setContacts(contacts);
        updateRemote(userConfig);
        return true;
    }

    public enum LoginType {
        LOGIN,
        LOGOUT,
        SKIP;

        public static LoginType getByName(String name) {
            for (LoginType loginType : LoginType.values()) {
                if (loginType.name().equals(name))
                    return loginType;
            }
            return null;
        }
    }
}
