package leaf.prod.app.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.adapter.ManageWalletListAdapter;
import leaf.prod.walletsdk.model.WalletEntity;
import leaf.prod.walletsdk.util.WalletUtil;
import leaf.prod.app.views.TitleView;

public class ManageWalletActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.btn_import)
    Button btnImport;

    @BindView(R.id.btn_generate)
    Button btnGenerate;

    private ManageWalletListAdapter mAdapter;

    private List<WalletEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_manage_wallet);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.set_manage_wallet));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        list = WalletUtil.getWalletList(ManageWalletActivity.this);
//        String amount = (String) SPUtils.get(this, "amount", "");
//        String amountValue = (String) SPUtils.get(this, "amountValue", "");
//        String pas = (String) SPUtils.get(this, "pas", "");
//        String address = (String) SPUtils.get(this, "address", "");
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getAddress().equals(address)) {
//                list.get(i).setAmountShow(amount);  //设置当前地址所拥有的币值
//                list.get(i).setAmount(Double.parseDouble(amountValue != null ? amountValue : "0"));  //设置当前地址所拥有的币值
//                list.get(i).setPas(pas);  //设置当前地址所拥有的pas
//            }
//        }
//        WalletUtil.setWalletList(this, list);
        mAdapter = new ManageWalletListAdapter(R.layout.adapter_item_manage_wallet, list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //            getOperation().addParameter("position", position);
            //            getOperation().addParameter("filename", list.get(position).getFilename());
            //            getOperation().addParameter("address", list.get(position).getAddress());
            //            getOperation().addParameter("walletname", list.get(position).getWalletname());
            //            getOperation().addParameter("mnemonic", list.get(position).getMnemonic());
            //            getOperation().addParameter("pas", list.get(position).getPas());
            getOperation().addParameter("selectedWallet", list.get(position));
            getOperation().forward(WalletSafeActivity.class);
        });
    }

    @OnClick({R.id.btn_import, R.id.btn_generate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_import:
                getOperation().forward(ImportWalletActivity.class);
                break;
            case R.id.btn_generate:
                getOperation().forward(GenerateWalletActivity.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = WalletUtil.getWalletList(ManageWalletActivity.this);
        mAdapter.setNewData(list);
    }

    @Override
    protected void initPresenter() {
    }
}
