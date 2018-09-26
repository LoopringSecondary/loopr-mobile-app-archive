package leaf.prod.walletsdk.model.request.param;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class BalanceParam {

    @NonNull
    private String owner;

    @NonNull
    private String delegateAddress;
}
