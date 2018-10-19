package leaf.prod.walletsdk.model.request.param;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class PendingTxParam {

    @NonNull
    private String owner;

    @NonNull
    private List<String> hashes;
}
