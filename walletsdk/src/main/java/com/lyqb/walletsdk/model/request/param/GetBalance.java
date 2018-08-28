package com.lyqb.walletsdk.model.request.param;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetBalance {
    private String owner;
    private String delegateAddress;
}
