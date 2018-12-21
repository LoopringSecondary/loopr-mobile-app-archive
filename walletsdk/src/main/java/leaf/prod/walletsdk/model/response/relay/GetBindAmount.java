/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-18 5:29 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.response.relay;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GetBindAmount {

    private String script;

    private String state;

    @SerializedName("gas_consumed")
    private String gasConsumed;

    private List<Stack> stack;

    @Data
    public static class Stack {

        private String type;

        private String value;
    }
}
