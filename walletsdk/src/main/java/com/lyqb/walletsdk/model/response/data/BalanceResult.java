package com.lyqb.walletsdk.model.response.data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            if (!super.equals(o))
                return false;
            Asset asset = (Asset) o;
            return Objects.equals(symbol, asset.symbol);
        }
    }
}
