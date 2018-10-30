package leaf.prod.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 上午10:06
 */
public class ThirdLoginUser {

    private String userId;

    private String userInfo;

    private List<WalletInfo> walletList;

    public ThirdLoginUser(String userId, String userInfo, List<WalletInfo> walletList) {
        this.userId = userId;
        this.userInfo = userInfo;
        this.walletList = walletList;
    }

    public ThirdLoginUser(List<WalletEntity> walletList) {
        setWalletList(walletList);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public List<WalletInfo> getWalletList() {
        return walletList;
    }

    public void setWalletList(List<WalletEntity> walletList) {
        List<WalletInfo> walletInfos = new ArrayList<>();
        for (WalletEntity walletEntity : walletList) {
            walletInfos.add(new WalletInfo(walletEntity));
        }
        this.walletList = walletInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ThirdLoginUser that = (ThirdLoginUser) o;
        return Objects.equals(walletList, that.walletList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletList);
    }

    @Override
    public String toString() {
        return "ThirdLoginUser{" +
                "userId='" + userId + '\'' +
                ", userInfo='" + userInfo + '\'' +
                ", walletList=" + walletList +
                '}';
    }

    private static class WalletInfo {

        private String walletname;

        private String filename;

        private String address;

        private String mnemonic;

        private String pas;  // md5

        private String dPath;

        private ImportWalletType walletType;

        private List<String> chooseTokenList;

        public WalletInfo(String walletname, String filename, String address, String mnemonic, String pas, String dPath, ImportWalletType walletType, List<String> chooseTokenList) {
            this.walletname = walletname;
            this.filename = filename;
            this.address = address;
            this.mnemonic = mnemonic;
            this.pas = pas;
            this.dPath = dPath;
            this.walletType = walletType;
            this.chooseTokenList = chooseTokenList;
        }

        public WalletInfo(WalletEntity w) {
            this.walletname = w.getWalletname();
            this.filename = w.getFilename();
            this.address = w.getAddress();
            this.mnemonic = w.getMnemonic();
            this.pas = w.getPas();
            this.dPath = w.getdPath();
            this.walletType = w.getWalletType();
            this.chooseTokenList = w.getChooseTokenList();
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
            WalletInfo that = (WalletInfo) o;
            return Objects.equals(walletname, that.walletname) &&
                    Objects.equals(filename, that.filename) &&
                    Objects.equals(address, that.address) &&
                    Objects.equals(mnemonic, that.mnemonic) &&
                    Objects.equals(pas, that.pas) &&
                    Objects.equals(dPath, that.dPath) &&
                    walletType == that.walletType &&
                    Objects.equals(chooseTokenList, that.chooseTokenList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(walletname, filename, address, mnemonic, pas, dPath, walletType, chooseTokenList);
        }

        @Override
        public String toString() {
            return "WalletInfo{" +
                    "walletname='" + walletname + '\'' +
                    ", filename='" + filename + '\'' +
                    ", address='" + address + '\'' +
                    ", mnemonic='" + mnemonic + '\'' +
                    ", pas='" + pas + '\'' +
                    ", dPath='" + dPath + '\'' +
                    ", walletType=" + walletType +
                    ", chooseTokenList=" + chooseTokenList +
                    '}';
        }
    }
}
