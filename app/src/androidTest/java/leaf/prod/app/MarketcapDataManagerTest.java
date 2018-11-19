package leaf.prod.app;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import leaf.prod.app.manager.MarketcapDataManager;

import static org.junit.Assert.assertTrue;

public class MarketcapDataManagerTest {

    @Test
    public void testLRCPrice() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MarketcapDataManager marketcapDataManager = MarketcapDataManager.getInstance(appContext);
        Double price = marketcapDataManager.getPriceBySymbol("LRC");
        assertTrue(price > 0);
    }

}
