package leaf.prod.walletsdk.model.response.relay;

import java.io.Serializable;

import leaf.prod.walletsdk.model.transaction.TxStatus;
import leaf.prod.walletsdk.model.transaction.TxType;
import lombok.Data;

@Data
public class Transaction implements Serializable {

    private String from;

    private String to;

    private String owner;

    private long createTime;

    private long updateTime;

    private String txHash;

    private String symbol;

    private long blockNumber;

    private String value;

    private TxType type;

    private TxStatus status;

    private String gas_price;

    private String gas_limit;

    private String gas_used;
}
