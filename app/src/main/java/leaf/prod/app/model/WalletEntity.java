package leaf.prod.app.model;

import java.io.Serializable;

import leaf.prod.walletsdk.model.Currency;

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

    private String pas;

    public WalletEntity(String walletname, String filename, String address, String mnemonic) {
        this.walletname = walletname;
        this.filename = filename;
        this.address = address;
        this.mnemonic = mnemonic;
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
                '}';
    }
}
