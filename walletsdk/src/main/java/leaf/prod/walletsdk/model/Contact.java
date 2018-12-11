/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-11 7:46 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import java.util.Objects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact {
    private String name;
    private String address;
    private String note;
    private String tag;

    public void setName(String name) {
        this.name = name;
        this.tag = name.substring(0, 1).toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Contact))
            return false;
        if (!super.equals(o))
            return false;
        Contact contact = (Contact) o;
        return Objects.equals(getName(), contact.getName()) ||
                Objects.equals(getAddress(), contact.getAddress());
    }
}
