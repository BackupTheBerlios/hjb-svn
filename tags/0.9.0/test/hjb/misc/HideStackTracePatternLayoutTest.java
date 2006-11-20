package hjb.misc;

import junit.framework.TestCase;

public class HideStackTracePatternLayoutTest extends TestCase {

    public void testShouldIgnoreThrowable() {
        HideStackTracePatternLayout l = new HideStackTracePatternLayout();
        assertFalse(l.ignoresThrowable());
    }
}
