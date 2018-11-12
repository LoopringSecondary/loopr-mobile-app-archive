/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-12 11:16 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import lombok.Data;

@Data
public class ScanWebQRCode {

    private String type;

    private String value;
}
