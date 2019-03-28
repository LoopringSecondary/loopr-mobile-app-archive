package leaf.prod.walletsdk.model.response.relay;

import java.util.List;

import leaf.prod.walletsdk.model.order.Fill;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 4:33 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class FillsResult {

    private Long total;

    private List<Fill> fills;
}
