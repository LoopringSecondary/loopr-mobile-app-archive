package leaf.prod.walletsdk.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Depth {

    private String pair;

    private String price;

    private Double amountA;

    private String amountAShown;

    private Double amountB;

    private String amountBShown;
}
