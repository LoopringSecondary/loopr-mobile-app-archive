/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:18 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderFill implements Serializable {

    // long e.g. 1544761544
    private Long createTime;

    // price e.g. 0.00036995
    private Double price;

    // amount e.g. 2918.029729
    private Double amount;

    private String side;

    private String ringHash;

    // big integer hex string e.g. 0x34f07768a92a83d00000
    private String lrcFee;

    // big integer integer string e.g. 1760000000000000000
    private String splitS;

    // big integer integer string e.g. 1760000000000000000
    private String splitB;

    private String orderHash;

    private String preOrderHash;
}
