package leaf.prod.walletsdk.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ticker implements Serializable {

    private String market;

    private String exchange;

    private TradingPair tradingPair;

    private String currencyShown;

    private Double vol;

    private String change;

    @SerializedName("label")
    private TickerTag tag;

    private Integer decimals;

    private String balanceShown;

    private Double open;

    private Double close;

    private Double high;

    private Double low;

    private Double last;

    private Double buy;

    private Double sell;
}
