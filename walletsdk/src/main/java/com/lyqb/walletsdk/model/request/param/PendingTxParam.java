package com.lyqb.walletsdk.model.request.param;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class PendingTxParam {
    @NonNull
    private String owner;
}
