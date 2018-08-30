package com.lyqb.walletsdk;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class CommonTest {

    @Test
    public void test1() throws IOException {
        SDK.initSDK();

    }

}
