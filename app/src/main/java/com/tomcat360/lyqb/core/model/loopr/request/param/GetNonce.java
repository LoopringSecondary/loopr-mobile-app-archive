package com.tomcat360.lyqb.core.model.loopr.request.param;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetNonce {
    private String owner;
}
