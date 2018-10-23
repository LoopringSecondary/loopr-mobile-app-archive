/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-11 下午6:35
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

public enum H5ScanType {

    P2P_ORDER("p2p_order"),
    OTHER("other");

    private final String description;

    H5ScanType(final String text) {
        this.description = text;
    }

    public String getDescription() {
        return description;
    }
}
