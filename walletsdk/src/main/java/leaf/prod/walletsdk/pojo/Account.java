package leaf.prod.walletsdk.pojo;//package leaf.prod.walletsdk.pojo;
//
//import org.web3j.crypto.Credentials;
//import org.web3j.utils.Numeric;
//
//import lombok.Data;
//
//@Data
//public class Account {
//    private String address;
//    private String publicKey;
//    private String privateKey;
//
//    public Account(String privateKey) {
//        this(Credentials.create(privateKey));
//    }
//
//    public Account(Credentials credentials) {
//        this.address = credentials.getAddress();
//        this.publicKey = Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPublicKey());
//        this.privateKey = Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey());
//    }
//
//    public Account(String address, String publicKey, String privateKey) {
//        this.address = address;
//        this.publicKey = publicKey;
//        this.privateKey = privateKey;
//    }
//
//    public Credentials toCredentials() {
//        return Credentials.create(privateKey, publicKey);
//    }
//}
