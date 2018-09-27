package leaf.prod.app.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;

public class SendResultActivity extends BaseActivity {

    @BindView(R.id.token_amount)
    TextView tokenAmount;

    @BindView(R.id.to_address)
    TextView toAddress;

    @BindView(R.id.time)
    TextView time;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_result);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
    }

    @Override
    public void initView() {
        tokenAmount.setText(getIntent().getStringExtra("tokenAmount"));
        toAddress.setText(getIntent().getStringExtra("address"));
        time.setText(sdf.format(new Date()));
    }

    @Override
    public void initData() {
    }

    @OnClick({R.id.btn_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_return:
                getOperation().forward(MainActivity.class);
                break;
            default:
                break;
        }
    }
}
