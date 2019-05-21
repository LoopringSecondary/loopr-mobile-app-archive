package leaf.prod.app.adapter.wallet;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.response.relay.AccountBalance;
import leaf.prod.walletsdk.model.token.Token;

public class MainWalletAdapter extends BaseQuickAdapter<AccountBalance, BaseViewHolder> {

	public MainWalletAdapter(int layoutResId, @Nullable List<AccountBalance> data) {
		super(layoutResId, data);
	}

	@Override
	protected void convert(BaseViewHolder helper, AccountBalance item) {
		Token token = TokenDataManager.getInstance(mContext).getTokenBySymbol(item.getTokenSymbol());
		helper.setText(R.id.wallet_title, item.getTokenSymbol());
		helper.setText(R.id.wallet_name, token != null ? token.getName() : "-");
		helper.setText(R.id.wallet_money, item.getValueShow());
		helper.setText(R.id.wallet_count, item.getLegalShown());
		if (token == null || token.getImageResId() == 0) {
			helper.setVisible(R.id.wallet_symbol, true);
			helper.setVisible(R.id.wallet_image, false);
			helper.setText(R.id.wallet_symbol, item.getTokenSymbol());
		} else {
			helper.setVisible(R.id.wallet_symbol, false);
			helper.setVisible(R.id.wallet_image, true);
			helper.setImageResource(R.id.wallet_image, token.getImageResId());
		}
	}
}
