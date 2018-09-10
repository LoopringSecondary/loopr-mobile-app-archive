package com.tomcat360.lyqb.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.model.WalletEntity;
import com.tomcat360.lyqb.model.eventbusData.NameChangeData;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletSafeActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.ll_wallet_name)
    LinearLayout llWalletName;

    @BindView(R.id.ll_backup_mnemonic)
    LinearLayout llBackupMnemonic;

    @BindView(R.id.ll_export_private_key)
    LinearLayout llExportPrivateKey;

    @BindView(R.id.ll_export_keystore)
    LinearLayout llExportKeystore;

    @BindView(R.id.ll_clear_records)
    LinearLayout llClearRecords;

    @BindView(R.id.btn_switch)
    Button btnSwitch;

    private int position;

    private String filename;

    private String address;

    private String walletname;

    private String mnemonic;

    private String pas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_safe);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        position = getIntent().getIntExtra("position", 0);
        filename = getIntent().getStringExtra("filename");
        address = getIntent().getStringExtra("address");
        walletname = getIntent().getStringExtra("walletname");
        mnemonic = getIntent().getStringExtra("mnemonic");
        pas = getIntent().getStringExtra("pas");
        title.setBTitle(walletname);
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
    }

    @OnClick({R.id.ll_wallet_name, R.id.ll_backup_mnemonic, R.id.ll_export_private_key, R.id.ll_export_keystore, R.id.ll_clear_records, R.id.btn_switch})
    public void onViewClicked(View view) {
        getOperation().addParameter("position", position);
        getOperation().addParameter("filename", filename);
        getOperation().addParameter("address", address);
        getOperation().addParameter("walletname", walletname);
        getOperation().addParameter("mnemonic", mnemonic);
        getOperation().addParameter("pas", pas);
        switch (view.getId()) {
            case R.id.ll_wallet_name:
                getOperation().forward(ReviseWalletNameActivity.class);
                break;
            case R.id.ll_backup_mnemonic:
                getOperation().addParameter("type", 1);
                getOperation().forward(ExportMatchPasSafeActivity.class);
                break;
            case R.id.ll_export_private_key:
                getOperation().addParameter("type", 2);
                getOperation().forward(ExportMatchPasSafeActivity.class);
                break;
            case R.id.ll_export_keystore:
                getOperation().addParameter("type", 3);
                getOperation().forward(ExportMatchPasSafeActivity.class);
                break;
            case R.id.ll_clear_records:
                String addressUsed = (String) SPUtils.get(this, "address", "");//当前使用钱包的地址
                List<WalletEntity> list = SPUtils.getWalletDataList(this, "walletlist", WalletEntity.class);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getAddress().equals(address)) {
                        if (list.size() < 2) {
                            ToastUtils.toast("当前只有一个钱包，不可以删除");
                        } else {
                            list.remove(i);
                            SPUtils.setDataList(this, "walletlist", list);
                            if (addressUsed.equals(address)) {
                                SPUtils.put(this, "address", list.get(0).getAddress());
                                SPUtils.put(this, "filename", list.get(0).getFilename());
                                startActivity(new Intent(this, MainActivity.class));
                            } else {
                                finish();
                            }
                            return;
                        }
                    }
                }
                break;
            case R.id.btn_switch:
                SPUtils.put(this, "address", address);
                SPUtils.put(this, "filename", filename);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NameChangeData event) {
        walletname = event.getName();
        title.setBTitle(walletname);
    }
}
