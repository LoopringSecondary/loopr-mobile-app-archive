package leaf.prod.walletsdk.model.response.data;

import lombok.Data;

@Data
public class PendingTxResult {

    private String from;

    private String to;

    private String owner;

    private long createTime;

    private long updateTime;

    private String hash;

    private long blockNumber;

    private String value;

    private String type;

    private String status;
}
