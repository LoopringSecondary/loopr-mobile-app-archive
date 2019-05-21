package leaf.prod.walletsdk;

import java.io.IOException;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@RunWith(AndroidJUnit4.class)
public class CommonTest {

    @Test
    public void test1() throws IOException {
        SDK.initSDK();
    }

    @Test
    public void create() throws CipherException {
        Credentials credentials = Credentials.create("c34b6b238327f46db73d0be38cd283734aea6014a41f7b937d0ab720e82de61f");
        WalletFile walletFile = Wallet.createStandard("qqqqqq", credentials.getEcKeyPair());
        System.out.println(walletFile.toString());
    }
}
