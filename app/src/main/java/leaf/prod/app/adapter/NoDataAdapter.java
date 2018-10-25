package leaf.prod.app.adapter;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import leaf.prod.app.R;
import leaf.prod.walletsdk.model.NoDataType;

public class NoDataAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private NoDataType type;

    public NoDataAdapter(int layoutResId, @Nullable List<Object> data, NoDataType type) {
        super(layoutResId, data);
        this.type = type;
    }

    public void refresh() {
        List<Object> list = new ArrayList<>();
        list.add(new Object());
        this.setNewData(list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        switch (type) {
            case asset:
                helper.setImageResource(R.id.wallet_image, R.mipmap.icon_empty_asset);
                helper.setText(R.id.wallet_title, R.string.no_data_asset);
                break;
            case transation:
                helper.setImageResource(R.id.wallet_image, R.mipmap.icon_empty_transaction);
                helper.setText(R.id.wallet_title, R.string.no_data_transaction);
                break;
        }
    }
}
