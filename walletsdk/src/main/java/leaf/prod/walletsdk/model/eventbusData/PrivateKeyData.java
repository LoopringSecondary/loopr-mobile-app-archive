package leaf.prod.walletsdk.model.eventbusData;

/**
 * Created by niedengqiang on 2018/8/21.
 */

public class PrivateKeyData {

    private String privateKey;

    public PrivateKeyData(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
