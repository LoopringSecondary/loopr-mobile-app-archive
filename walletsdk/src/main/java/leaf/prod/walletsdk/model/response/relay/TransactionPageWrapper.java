package leaf.prod.walletsdk.model.response.relay;

import java.util.List;

import lombok.Data;

@Data
public class TransactionPageWrapper {

    private List<Transaction> data;

    private int pageIndex;

    private int pageSize;

    private int total;
}
