/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-11 7:46 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class Contact {

    private String name;

    private String address;

    private String note;

    private String tag;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) ||
                Objects.equals(address.toUpperCase(), contact.address.toUpperCase());
    }
}

