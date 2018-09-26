package leaf.prod.app.adapter;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import leaf.prod.app.R;
import leaf.prod.app.model.WalletEntity;
import leaf.prod.app.utils.SPUtils;

/**
 *
 */
public class ManageWalletListAdapter extends BaseQuickAdapter<WalletEntity, BaseViewHolder> {

    public ManageWalletListAdapter(int layoutResId, @Nullable List<WalletEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletEntity item) {
        //        helper.setText(R.id.wallet_name, (String) SPUtils.get(mContext,"walletname","name"));
        //        helper.setText(R.id.wallet_count, (String) SPUtils.get(mContext,"amount","$392.27"));
        //        helper.setText(R.id.wallet_address, (String) SPUtils.get(mContext,"address","address"));
        helper.setText(R.id.wallet_name, item.getWalletname());
        helper.setText(R.id.wallet_count, item.getAmount() + " ETH");
        helper.setText(R.id.wallet_address, item.getAddress());
        if (item.getAddress().equals((String) SPUtils.get(mContext, "address", ""))) {
            helper.setVisible(R.id.icon_status, true);
        } else {
            helper.setVisible(R.id.icon_status, false);
        }
    }
}
