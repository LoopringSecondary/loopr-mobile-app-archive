package leaf.prod.app;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppInfoTest {

    @Test
    public void testPackageName() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(appContext.getPackageName(), "leaf.prod.app");
    }
}
