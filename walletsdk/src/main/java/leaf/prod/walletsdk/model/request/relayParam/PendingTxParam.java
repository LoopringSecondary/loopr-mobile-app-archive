package leaf.prod.walletsdk.model.request.relayParam;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class PendingTxParam {

    @NonNull
    private String owner;

    @NonNull
    private String[] trxHashes;
}
