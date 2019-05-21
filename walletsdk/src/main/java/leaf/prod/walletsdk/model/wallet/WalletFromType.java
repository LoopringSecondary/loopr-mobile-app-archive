package leaf.prod.walletsdk.model.wallet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-10 3:21 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public enum WalletFromType {
    LOOPRING_WALLET("Loopring Wallet"),
    IMTOKEN("imToken"),
    METAMASK("MetaMask"),
    TREZOR_ETH("TREZOR (ETH)"),
    DIGITAL_BITBOX("Digital Bitbox"),
    EXODUS("Exodus"),
    JAXX("Jaxx"),
    LEDGER_ETH("Ledger (ETH)"),
    TREZOR_ETC("TREZOR (ETC)"),
    SINGULAR_DTV("SingularDTV"),
    NETWORK_TESTNETS("Network: Testnets"),
    NETWORK_EXPANSE("Network: Expanse"),
    NETWORK_UBIQ("Network: Ubiq"),
    NETWORK_ELLAISM("Network: Ellaism"),
    OTHER("other");

    private String name;

    WalletFromType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String[] getAllNames() {
        List<String> list = new ArrayList<>();
        for (WalletFromType walletFromType : WalletFromType.values()) {
            list.add(walletFromType.getName());
        }
        return list.toArray(new String[]{});
    }

    public static WalletFromType getByName(String name) {
        for (WalletFromType walletFromType : WalletFromType.values()) {
            if (walletFromType.getName().equals(name)) {
                return walletFromType;
            }
        }
        return null;
    }
}
