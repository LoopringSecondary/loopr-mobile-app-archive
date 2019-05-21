/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-24 4:10 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.news;

public enum IndexAction {

    CONFIRM(1), CANCEL(-1);

    private final int value;

    IndexAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
