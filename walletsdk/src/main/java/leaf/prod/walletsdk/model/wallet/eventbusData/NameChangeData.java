package leaf.prod.walletsdk.model.wallet.eventbusData;

/**
 * Created by niedengqiang on 2018/8/21.
 */

public class NameChangeData {

    private String name;

    public NameChangeData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NameChangeData{" + "name='" + name + '\'' + '}';
    }
}
