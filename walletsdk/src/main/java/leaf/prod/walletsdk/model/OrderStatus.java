/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:35 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import java.io.Serializable;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import leaf.prod.walletsdk.R;

public enum OrderStatus implements Serializable {

    @SerializedName("ORDER_OPENED")
    OPENED(R.string.order_open),

    @SerializedName("ORDER_WAIT_SUBMIT_RING")
    WAITED(R.string.order_submitted),

    @SerializedName("ORDER_FINISHED")
    FINISHED(R.string.order_completed),

    @SerializedName("ORDER_CUTOFF")
    CUTOFF(R.string.order_cutoff),

    @SerializedName("ORDER_CANCELLED")
    CANCELLED(R.string.order_cancelled),

    @SerializedName("ORDER_EXPIRE")
    EXPIRED(R.string.order_expired),

    @SerializedName("ORDER_P2P_LOCKED")
    LOCKED(R.string.order_locked),

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
