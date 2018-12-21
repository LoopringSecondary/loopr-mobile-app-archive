package leaf.prod.walletsdk.model.request.relayParam;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class NonceParam {

    @NonNull
    private String owner;
}
