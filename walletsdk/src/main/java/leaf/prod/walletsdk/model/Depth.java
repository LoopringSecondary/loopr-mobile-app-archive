package leaf.prod.walletsdk.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Depth implements Serializable {

    private String market;

    private Datum depth;

    @Data
    @Builder
    public static class Datum {

        private String[][] buy;

        private String[][] sell;
    }
}
