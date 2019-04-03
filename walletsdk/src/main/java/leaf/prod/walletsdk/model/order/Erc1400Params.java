/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:34 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.order;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Erc1400Params implements Serializable {

    private Integer tokenStandardS;

    private Integer tokenStandardB;

    private Integer tokenStandardFee;

    private String trancheS;

    private String trancheB;

    private String transferDataS;
}
