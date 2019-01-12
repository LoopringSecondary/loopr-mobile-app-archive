/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-01-12 2:42 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Trend {

    private String intervals;

    private String market;

    private Double vol;

    private Double amount;

    private Double createTime;

    private Double open;

    private Double close;

    private Double high;

    private Double low;

    private Long start;

    private Long end;

    private String change;

    private String range;
}
