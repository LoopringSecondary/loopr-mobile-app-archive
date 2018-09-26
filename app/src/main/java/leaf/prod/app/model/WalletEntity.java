package leaf.prod.app.model;

/**
 * Created by niedengqiang on 2018/9/7.
 */

public class WalletEntity {

    private String walletname;

    private String filename;

    private String address;

    private String mnemonic;

    private String amount;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPas() {
        return pas;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }

    @Override
    public String toString() {
        return "WalletEntity{" + "walletname='" + walletname + '\'' + ", filename='" + filename + '\'' + ", address='" + address + '\'' + ", mnemonic='" + mnemonic + '\'' + ", amount='" + amount + '\'' + ", pas='" + pas + '\'' + '}';
    }
}
