/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-11 下午6:35
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.common;

public enum Sort {

    ASC("ASC"), DESC("DESC");

    private final String description;

    Sort(final String text) {
        this.description = text;
    }

    public String getDescription() {
        return description;
    }
}
