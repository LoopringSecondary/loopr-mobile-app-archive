/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-26 上午11:31
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.presenter.AddCustomTokenPresenter;
import leaf.prod.app.views.TitleView;

public class AddCustomTokenActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.token_address)
    public MaterialEditText etTokenAddress;

    @BindView(R.id.token_symbol)
    public MaterialEditText etTokenSymbol;

    @BindView(R.id.token_decimal)
    public MaterialEditText etTokenDecimal;

    @BindView(R.id.add_btn)
    Button addButton;

    private AddCustomTokenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_custom_token);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化P层
     */
    @Override
    protected void initPresenter() {
        this.presenter = new AddCustomTokenPresenter(this, this);
    }

    /**
     * 初始化标题
     */
    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.token_add));
        title.clickLeftGoBack(getWContext());
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
    }

    @OnClick({R.id.add_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_btn:
                presenter.doAddCustomToken();
        }
    }

}
