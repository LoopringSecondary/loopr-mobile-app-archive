package leaf.prod.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import leaf.prod.walletsdk.model.Language;
import leaf.prod.app.R;
import leaf.prod.app.utils.LanguageUtil;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.walletsdk.model.Language;

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
        if ((int) SPUtils.get(this, "language", 0) == 1) {
            ivEnglishCheck.setVisibility(View.VISIBLE);
            ivChineseCheck.setVisibility(View.GONE);
        } else if ((int) SPUtils.get(this, "language", 0) == 2) {
            ivEnglishCheck.setVisibility(View.GONE);
            ivChineseCheck.setVisibility(View.VISIBLE);
        } else {
            if (LanguageUtil.getLanguage(this) == Language.en_US) {
                ivEnglishCheck.setVisibility(View.VISIBLE);
                ivChineseCheck.setVisibility(View.GONE);
            } else {
                ivEnglishCheck.setVisibility(View.GONE);
                ivChineseCheck.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void initData() {
    }

    @OnClick({R.id.ll_english, R.id.ll_chinese})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_english:
                SPUtils.put(this, "isRecreate", true);//通知mainactivity更改语言设置标志
                SPUtils.put(this, "language", 1);  //语言显示，1为英语显示，2为汉语显示
                ivEnglishCheck.setVisibility(View.VISIBLE);
                ivChineseCheck.setVisibility(View.GONE);
                /**
                 * 显示英文
                 * */
                LanguageUtil.changeLanguage(this, "en");
                recreate();
                break;
            case R.id.ll_chinese:
                SPUtils.put(this, "isRecreate", true);//mainactivity更改语言设置标志
                SPUtils.put(this, "language", 2);//语言显示，1为英语显示，2为汉语显示
                ivEnglishCheck.setVisibility(View.GONE);
                ivChineseCheck.setVisibility(View.VISIBLE);
                //显示中文
                LanguageUtil.changeLanguage(this, "zh");
                recreate();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
