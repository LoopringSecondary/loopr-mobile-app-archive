package leaf.prod.walletsdk.model.response.relay;

import java.util.List;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 5:34 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class MarketHistoryResult {

    private List<Data> data;

    @lombok.Data
    public static class Data {

        private List<Double> data;
    }
}
