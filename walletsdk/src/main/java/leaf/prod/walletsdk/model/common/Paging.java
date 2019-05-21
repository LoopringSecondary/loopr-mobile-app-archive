/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:34 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.common;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Paging implements Serializable {

    private Integer cursor;

    private Integer size;
}
