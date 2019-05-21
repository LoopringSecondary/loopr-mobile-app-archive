/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-24 4:10 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.news;

public enum IndexType {

    BULLINDEX("bull_index"),

    BEARINDEX("bear_index"),

    FORWARDNUM("forward_num"),

    READNUM("read_num"),

    UNKNOWN("unknown");

    private String description;

    IndexType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
