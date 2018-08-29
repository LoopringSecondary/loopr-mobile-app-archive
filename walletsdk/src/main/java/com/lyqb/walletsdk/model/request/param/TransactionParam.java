package com.lyqb.walletsdk.model.request.param;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class TransactionParam {
    @NonNull
    private String owner;
    private String txHash;
    @NonNull
    private String symbol;
    private String status;
    private String txType;

    private int pageIndex;
    private int pageSize;

}
