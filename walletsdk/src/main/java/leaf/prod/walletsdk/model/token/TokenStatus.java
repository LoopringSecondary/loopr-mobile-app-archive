package leaf.prod.walletsdk.model.token;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 2:37 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public enum TokenStatus {

    VALID("VALID"), INVALID("INVALID"), UNKNOWN("UNKNOWN");

    private final String description;

    TokenStatus(final String text) {
        this.description = text;
    }

    public String getDescription() {
        return description;
    }
}
