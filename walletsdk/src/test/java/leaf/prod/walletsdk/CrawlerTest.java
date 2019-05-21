/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-21 7:22 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk;

import org.junit.Before;
import org.junit.Test;

import leaf.prod.walletsdk.service.CrawlerService;

public class CrawlerTest {

    private CrawlerService crawlerService;

    @Before
    public void setUp() {
        SDK.initSDK();
        crawlerService = new CrawlerService();
    }

    @Test
    public void testGetNews() {

    }

}
