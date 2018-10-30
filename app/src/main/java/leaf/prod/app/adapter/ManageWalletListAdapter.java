package leaf.prod.app.adapter;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;
import leaf.prod.app.model.WalletEntity;
import leaf.prod.app.utils.WalletUtil;

/**
 *
 */
public class ManageWalletListAdapter extends BaseQuickAdapter<WalletEntity, BaseViewHolder> {

    public ManageWalletListAdapter(int layoutResId, @Nullable List<WalletEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletEntity item) {
        helper.setText(R.id.wallet_name, item.getWalletname());
        helper.setText(R.id.wallet_count, item.getAmountShow());
        helper.setText(R.id.wallet_address, item.getAddress());
        if (item.getAddress().equals(WalletUtil.getCurrentAddress(mContext))) {
            helper.setVisible(R.id.icon_status, true);
        } else {
            helper.setVisible(R.id.icon_status, false);
        }
    }
}
