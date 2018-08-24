package com.lyqb.walletsdk.model.loopr.request.param;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetNonce {
    private String owner;
}
