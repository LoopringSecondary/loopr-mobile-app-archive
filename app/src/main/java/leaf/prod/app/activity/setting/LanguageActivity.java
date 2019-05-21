package leaf.prod.app.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.LoginDataManager;
import leaf.prod.walletsdk.model.setting.Language;
import leaf.prod.walletsdk.model.setting.UserConfig;
import leaf.prod.walletsdk.util.LanguageUtil;
import leaf.prod.walletsdk.util.SPUtils;

public class LanguageActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.iv_english_check)
    ImageView ivEnglishCheck;

    @BindView(R.id.ll_english)
    LinearLayout llEnglish;

    @BindView(R.id.iv_chinese_check)
    ImageView ivChineseCheck;

    @BindView(R.id.ll_chinese)
    LinearLayout llChinese;

    @BindView(R.id.iv_chinese_traditional_check)
    ImageView ivChineseTranditionalCheck;

    @BindView(R.id.ll_chinese_traditional)
    LinearLayout ll_chinese_traditional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.set_language));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        Language language = LanguageUtil.getSettingLanguage(this);
        switch (language) {
            case en_US:
                ivEnglishCheck.setVisibility(View.VISIBLE);
                ivChineseCheck.setVisibility(View.GONE);
                ivChineseTranditionalCheck.setVisibility(View.GONE);
                break;
            case zh_CN:
                ivEnglishCheck.setVisibility(View.GONE);
                ivChineseCheck.setVisibility(View.VISIBLE);
                ivChineseTranditionalCheck.setVisibility(View.GONE);
                break;
            case zh_Hant:
                ivEnglishCheck.setVisibility(View.GONE);
                ivChineseCheck.setVisibility(View.GONE);
                ivChineseTranditionalCheck.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void initData() {
    }

    @OnClick({R.id.ll_english, R.id.ll_chinese, R.id.ll_chinese_traditional})
    public void onViewClicked(View view) {
        UserConfig userConfig = LoginDataManager.getInstance(this).getLocalUser();
        switch (view.getId()) {
            case R.id.ll_english:
                SPUtils.put(this, "isRecreate", true);//通知mainactivity更改语言设置标志
                ivEnglishCheck.setVisibility(View.VISIBLE);
                ivChineseCheck.setVisibility(View.GONE);
                ivChineseTranditionalCheck.setVisibility(View.GONE);
                userConfig.setLanguage(Language.en_US.getText());
                break;
            case R.id.ll_chinese:
                SPUtils.put(this, "isRecreate", true);//mainactivity更改语言设置标志
                ivEnglishCheck.setVisibility(View.GONE);
                ivChineseCheck.setVisibility(View.VISIBLE);
                ivChineseTranditionalCheck.setVisibility(View.GONE);
                userConfig.setLanguage(Language.zh_CN.getText());
                break;
            case R.id.ll_chinese_traditional:
                SPUtils.put(this, "isRecreate", true);//mainactivity更改语言设置标志
                ivEnglishCheck.setVisibility(View.GONE);
                ivChineseCheck.setVisibility(View.GONE);
                ivChineseTranditionalCheck.setVisibility(View.VISIBLE);
                userConfig.setLanguage(Language.zh_Hant.getText());
                break;
        }
        LoginDataManager.getInstance(this).updateRemote(userConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
