package leaf.prod.app.adapter;

import java.util.List;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;
import leaf.prod.walletsdk.model.Contact;

public class ContactSearchAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {


    public ContactSearchAdapter(int layoutResId, @Nullable List<Contact> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Contact item) {
        if (item == null)
            return;
        if (getData().indexOf(item) == 0) {
            helper.setGone(R.id.top_line, false);
        }
        helper.setText(R.id.tv_wallet_name, item.getName());
        helper.setText(R.id.tv_wallet_address, item.getAddress());
    }
}
