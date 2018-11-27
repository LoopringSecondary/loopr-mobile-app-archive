package leaf.prod.walletsdk.model;

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

    private String language;

    private String currency;

    private List<WalletInfo> walletList;

    public ThirdLoginUser(String userId, String language, String currency, List<WalletInfo> walletList) {
        this.userId = userId;
        this.language = language;
        this.currency = currency;
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

    public List<WalletInfo> getWalletList() {
        return walletList;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setWalletList(List<WalletEntity> walletList) {
        if (walletList != null) {
            List<WalletInfo> walletInfos = new ArrayList<>();
            for (WalletEntity walletEntity : walletList) {
                walletInfos.add(new WalletInfo(walletEntity));
            }
            this.walletList = walletInfos;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ThirdLoginUser that = (ThirdLoginUser) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(language, that.language) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(walletList, that.walletList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, language, currency, walletList);
    }

    @Override
    public String toString() {
        return "ThirdLoginUser{" +
                "userId='" + userId + '\'' +
                ", language='" + language + '\'' +
                ", currency='" + currency + '\'' +
                ", walletList=" + walletList +
                '}';
    }

    private static class WalletInfo {

        private String walletname;

        private String address;

        private List<String> chooseTokenList;

        public WalletInfo(String walletname, String address, List<String> chooseTokenList) {
            this.walletname = walletname;
            this.address = address;
            this.chooseTokenList = chooseTokenList;
        }

        public WalletInfo(WalletEntity w) {
            this.walletname = w.getWalletname();
            this.address = w.getAddress();
            this.chooseTokenList = w.getChooseTokenList();
        }

        public String getWalletname() {
            return walletname;
        }

        public void setWalletname(String walletname) {
            this.walletname = walletname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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
                    Objects.equals(address, that.address) &&
                    Objects.equals(chooseTokenList, that.chooseTokenList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(walletname, address, chooseTokenList);
        }

        @Override
        public String toString() {
            return "WalletInfo{" +
                    "walletname='" + walletname + '\'' +
                    ", address='" + address + '\'' +
                    ", chooseTokenList=" + chooseTokenList +
                    '}';
        }
    }
}
