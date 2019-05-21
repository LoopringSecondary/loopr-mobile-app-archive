package leaf.prod.walletsdk.model.wallet;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import leaf.prod.walletsdk.model.common.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by niedengqiang on 2018/9/7.
 */
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class WalletEntity implements Serializable {

    private String walletname;

    private String filename;

    private String address;

    private String mnemonic;

    private double amount;

    private Currency currency;

    private String amountShow;

    // password
    private String pas;  // md5

    private String salt;

    private String iv;
    // add

    /**
     * 录入的钱包类型
     */
    private String dPath;

    private String walletFrom;

    /**
     * 录入的方式：助记词、keystore、privatekey
     */
    private ImportWalletType walletType;

    /**
     * 钱包的自选token
     */
    private List<String> chooseTokenList;
    //    public WalletEntity(String walletname, String filename, String address, String mnemonic, String pas, String salt, String iv, String dPath, String walletFrom, ImportWalletType walletType) {
    //        this.walletname = walletname;
    //        this.filename = filename;
    //        if (!StringUtils.isEmpty(address)) {
    //            this.address = address.toLowerCase().startsWith("0x") ? address : "0x" + address;
    //        }
    //        this.mnemonic = mnemonic;
    //        this.pas = pas;
    //        this.dPath = dPath;
    //        this.walletFrom = walletFrom;
    //        this.walletType = walletType;
    //        this.chooseTokenList = Arrays.asList("ETH", "WETH", "LRC");
    //    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WalletEntity that = (WalletEntity) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
