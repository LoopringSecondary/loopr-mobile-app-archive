package leaf.prod.walletsdk;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import leaf.prod.walletsdk.service.RelayService;

import leaf.prod.walletsdk.service.RelayService;

@RunWith(AndroidJUnit4.class)
public class HttpServiceTest {

    static {
        SDK.initSDK();
    }

    private RelayService httpService = new RelayService();

    @Test
    public void supportedTokenTest() {
        httpService.getSupportedToken().subscribe(new DebugSubscriber<>());
    }

    @Test
    public void getNonceTest() {
        httpService.getNonce("0x71c079107b5af8619d54537a93dbf16e5aab4900").subscribe(new DebugSubscriber<>());
    }

    @Test
    public void getEstimateGasPriceTest() {
        httpService.getEstimateGasPrice().subscribe(new DebugSubscriber<>());
    }

    @Test
    public void unlockWalletTest() {
        httpService.notifyCreateWallet("0xb94065482ad64d4c2b9252358d746b39e820a585").subscribe(new DebugSubscriber<>());
    }

    @Test
    public void getBalanceTest() {
        httpService.getBalance("0xb94065482ad64d4c2b9252358d746b39e820a585").subscribe(new DebugSubscriber<>());
    }
}
