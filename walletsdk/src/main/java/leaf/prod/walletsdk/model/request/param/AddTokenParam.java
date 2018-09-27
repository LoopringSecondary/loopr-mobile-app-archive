/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-26 下午5:39
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.request.param;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class AddTokenParam {

    @NonNull
    private String owner;

    @NonNull
    @SerializedName("tokenContractAddress")
    private String address;

    @NonNull
    private String symbol;

    @NonNull
    private String decimals;
}
