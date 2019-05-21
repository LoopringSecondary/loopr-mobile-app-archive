package leaf.prod.walletsdk.model.request.relayParam;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UnlockWallet {

    private String owner;
}
