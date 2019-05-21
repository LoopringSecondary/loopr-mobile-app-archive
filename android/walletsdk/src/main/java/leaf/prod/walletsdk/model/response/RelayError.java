/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-04 7:15 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelayError {

    private Integer code;
    private String message;
}
