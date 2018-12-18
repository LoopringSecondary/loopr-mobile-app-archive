package leaf.prod.app.adapter;

import java.util.List;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtool.RxDataTool;

import leaf.prod.app.R;
import leaf.prod.app.activity.AddContactActivity;
import leaf.prod.app.activity.ContactListActivity;
import leaf.prod.walletsdk.model.Contact;
import leaf.prod.walletsdk.util.ChineseCharUtil;

public class ContactListAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {

    private ContactListActivity activity;

    public ContactListAdapter(ContactListActivity activity, int layoutResId, @Nullable List<Contact> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, Contact item) {
        if (item == null)
            return;
        helper.setText(R.id.tv_wallet_symbol, ChineseCharUtil.getFirstLetter(item.getName()).toUpperCase());
        helper.setText(R.id.tv_wallet_name, item.getName());
        helper.setText(R.id.tv_wallet_address, item.getAddress());
        helper.setText(R.id.tv_wallet_note, item.getNote());
        if (RxDataTool.isEmpty(item.getNote())) {
            helper.setGone(R.id.tv_wallet_note, false);
        } else {
            helper.setVisible(R.id.tv_wallet_note, true);
        }
        helper.setOnClickListener(R.id.iv_edit, view -> {
            activity.getOperation()
                    .addParameter("address", ((TextView) helper.getView(R.id.tv_wallet_address)).getText().toString());
            activity.getOperation().forward(AddContactActivity.class);
        });
    }
}
