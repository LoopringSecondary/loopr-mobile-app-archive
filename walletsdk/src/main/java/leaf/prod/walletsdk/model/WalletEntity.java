package leaf.prod.walletsdk.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import leaf.prod.walletsdk.util.StringUtils;

/**
 * Created by niedengqiang on 2018/9/7.
 */

public class WalletEntity implements Serializable {

    private String walletname;

    private String filename;

    private String address;

    private String mnemonic;

    private double amount;

    private Currency currency;

    private String amountShow;

    private String pas;  // md5
    // add

    /**
     * 录入的钱包类型
     */
    private String dPath;

    /**
     * 录入的方式：助记词、keystore、privatekey
     */
    private ImportWalletType walletType;

    /**
     * 钱包的自选token
     */
    private List<String> chooseTokenList;

    public WalletEntity(String walletname) {
        this.walletname = walletname;
    }

    public WalletEntity(String walletname, String filename, String address, String mnemonic, String pas, String dPath, ImportWalletType walletType) {
        this.walletname = walletname;
        this.filename = filename;
        if (!StringUtils.isEmpty(address)) {
            this.address = address.toLowerCase().startsWith("0x") ? address : "0x" + address;
        }
        this.mnemonic = mnemonic;
        this.pas = pas;
        this.dPath = dPath;
        this.walletType = walletType;
        this.chooseTokenList = Arrays.asList("ETH", "WETH", "LRC");
    }

    public String getWalletname() {
        return walletname;
    }

    public void setWalletname(String walletname) {
        this.walletname = walletname;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getAmountShow() {
        return amountShow;
    }

    public void setAmountShow(String amountShow) {
        this.amountShow = amountShow;
    }

    public String getPas() {
        return pas;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }

    public String getdPath() {
        return dPath;
    }

    public void setdPath(String dPath) {
        this.dPath = dPath;
    }

    public ImportWalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(ImportWalletType walletType) {
        this.walletType = walletType;
    }

    public List<String> getChooseTokenList() {
        return chooseTokenList;
    }

    public void setChooseTokenList(List<String> chooseTokenList) {
        this.chooseTokenList = chooseTokenList;
    }

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

    @Override
    public String toString() {
        return "WalletEntity{" +
                "walletname='" + walletname + '\'' +
                ", filename='" + filename + '\'' +
                ", address='" + address + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                ", amountShow='" + amountShow + '\'' +
                ", pas='" + pas + '\'' +
                ", dPath='" + dPath + '\'' +
                ", walletType=" + walletType +
                ", chooseTokenList=" + chooseTokenList +
                '}';
    }
}
