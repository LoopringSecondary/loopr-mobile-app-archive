/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-25 上午10:43
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Partner {

    @SerializedName("walletAddress")
    private String walletAddress;

    @SerializedName("cityPartner")
    private String partner;
}
