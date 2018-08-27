package com.lyqb.walletsdk;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.web3j.crypto.WalletFile;

@RunWith(AndroidJUnit4.class)
public class Hello {

    @Test
    public void test() {
        String json = "{\"address\":\"963c0c589f3fc2467d862a5f7faed65f9b44e9f3\",\"id\":\"84fb42e5-c7f4-44ef-818c-a5f51064ac46\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"a9ae656844560976bc4c710500153df1\"},\"ciphertext\":\"353f9e8a6c45d00fbe4d6d4b18bfcba1da6c3a888f8d344dd31038fb7e263a2c\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"d828579878db764ae25574d27b823480cedeebf199af2e64f570499b6f451eb3\"},\"mac\":\"635708e6fd5e5c1cce0c3a360dceecc1ad9fe003e966a7c823d87c9092710102\"}}";
        WalletFile walletFile = KeystoreHelper.loadFromJsonString(json);
        System.out.println(walletFile);
    }
}
