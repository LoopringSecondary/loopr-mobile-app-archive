package leaf.prod.app.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.presenter.ConvertPresenter;
import leaf.prod.app.utils.WalletUtil;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.util.StringUtils;

public class ConvertActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.switch_img)
    ImageView switchImg;

    @BindView(R.id.first_val)
    TextView firstVal;

    @BindView(R.id.second_val)
    TextView secondVal;

    @BindView(R.id.hint_text)
    TextView hint;

    private ConvertPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_convert);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
        presenter = new ConvertPresenter(this, this);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.convert));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        switchImg.setOnClickListener(view -> {
            presenter.switchToken();
        });
        firstVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                double value = (editable != null && !editable.toString()
                        .isEmpty()) ? Double.parseDouble(editable.toString()
                        .equals(".") ? "0" : editable.toString()) : 0;
                if (value > presenter.getMaxValue()) {
                    presenter.setHint(1);
                } else {
                    presenter.setHint(value == 0 ? 0 : 3);
                    secondVal.setText(firstVal.getText());
                }
            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @OnClick({R.id.ll_show_fee, R.id.max, R.id.btn_convert})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_show_fee:
                presenter.showFeeDialog();
                break;
            case R.id.max:
                presenter.setMax();
                break;
            case R.id.btn_convert:
                double value = !StringUtils.isEmpty(firstVal.getText()
                        .toString()) ? Double.parseDouble(firstVal.getText().toString()
                        .equals(".") ? "0" : firstVal.getText().toString()) : 0;
                if (value == 0) {
                    presenter.setHint(2);
                } else if (value > presenter.getMaxValue()) {
                    presenter.setHint(1);
                } else {
                    if (WalletUtil.needPassword(this)) {
                        presenter.showPasswordDialog();
                    } else {
                        presenter.convert("");
                    }
                }
                break;
        }
    }
}
