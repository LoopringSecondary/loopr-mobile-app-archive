package leaf.prod.walletsdk.model.response.data;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class BalanceResult {

    private String delegateAddress;

    private String owner;

    private List<Asset> tokens;

    @Data
    public static class Asset {

        private String symbol;

        private BigDecimal balance;

        private BigDecimal allowance;

        private double value;

        private String valueShown = "--";

        private double legalValue;

        private String legalShown = "--";

        private int precision = 4;
    }
}
