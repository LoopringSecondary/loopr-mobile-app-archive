/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:35 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.order;

import java.io.Serializable;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import leaf.prod.walletsdk.R;

public enum OrderStatus implements Serializable {

    @SerializedName(value = "STATUS_PENDING_ACTIVE")
    PENDING(R.string.order_pending),

    @SerializedName(value = "STATUS_NEW", alternate = {"STATUS_PENDING", "STATUS_PARTIALLY_FILLED"})
    OPENED(R.string.order_open),

    @SerializedName("STATUS_COMPLETELY_FILLED")
    FINISHED(R.string.order_completed),

    @SerializedName(value = "STATUS_SOFT_CANCELLED_BY_USER", alternate = {"STATUS_ONCHAIN_CANCELLED_BY_USER", "STATUS_ONCHAIN_CANCELLED_BY_USER_TRADING_PAIR", "STATUS_SOFT_CANCELLED_BY_USER_TRADING_PAIR"})
    CANCELLED(R.string.order_cancelled),

    @SerializedName("STATUS_EXPIRED")
    EXPIRED(R.string.order_expired),

    @SerializedName("ORDER_UNKNOWN")
    UNKNOWN(R.string.order_unknown);

    private int resourceId;

    OrderStatus(int id) {
        this.resourceId = id;
    }

    public String getDescription(Context context) {
        return context.getString(resourceId);
    }
}
