/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-19 5:45 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.transaction;

import org.web3j.crypto.Sign;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignedBody {

    private Sign.SignatureData sig;

    private String hash;
}
