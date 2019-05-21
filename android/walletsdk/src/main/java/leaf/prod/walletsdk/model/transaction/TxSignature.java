package leaf.prod.walletsdk.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TxSignature {

    private String v;

    private String r;

    private String s;
}
