package leaf.prod.app.adapter;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;
import leaf.prod.walletsdk.model.Contact;
import leaf.prod.walletsdk.util.ChineseCharUtil;

public class ContactListAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {

    public ContactListAdapter(int layoutResId, @Nullable List<Contact> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Contact item) {
        if (item == null)
            return;
        helper.setText(R.id.tv_wallet_symbol, ChineseCharUtil.getFirstLetter(item.getName()).toUpperCase());
        helper.setText(R.id.tv_wallet_name, item.getName());
        helper.setText(R.id.tv_wallet_address, item.getAddress());
        helper.setText(R.id.tv_wallet_note, item.getNote());
    }
}
