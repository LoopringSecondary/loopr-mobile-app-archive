package leaf.prod.app.adapter.setupwallet;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;

/**
 *
 */
public class MnemonicWordAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MnemonicWordAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.mnemonic_word, item);
    }
}
