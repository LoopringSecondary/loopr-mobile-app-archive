/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-12 5:27 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

public enum QRCodeType {

    KEY_STORE,
    TRANSFER(),
    P2P_ORDER(),
    LOGIN(),
    APPROVE(),
    CONVERT(),
    ORDER(),
    CANCEL_ORDER();
}
