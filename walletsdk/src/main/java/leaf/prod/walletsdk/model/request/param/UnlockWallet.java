package leaf.prod.walletsdk.model.request.param;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UnlockWallet {

    private String owner;
}
